package info.teksol.mindcode.compiler.generator;

import info.teksol.mindcode.InputPosition;
import info.teksol.mindcode.ast.AstNode;
import info.teksol.mindcode.ast.NoOp;
import info.teksol.mindcode.compiler.CompilerProfile;
import info.teksol.mindcode.compiler.instructions.LogicInstruction;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public final class AstContext {
    private static final AtomicInteger counter = new AtomicInteger();
    public final int id;

    private final CompilerProfile profile;
    private final LogicFunction function;
    private final int level;
    private final AstNode node;
    private final AstContextType contextType;
    private final AstSubcontextType subcontextType;
    private final AstContext parent;
    private double weight;
    private final List<AstContext> children;

    private AstContext(CompilerProfile profile, LogicFunction function, int level, AstNode node, AstContextType contextType,
            AstSubcontextType subcontextType, AstContext parent, double weight, List<AstContext> children) {
        this.id = counter.getAndIncrement();
        this.profile = profile;
        this.function = function;
        this.level = level;
        this.node = node;
        this.contextType = contextType;
        this.subcontextType = subcontextType;
        this.parent = parent;
        this.weight = weight;
        this.children = children;
    }

    public AstContext(CompilerProfile profile, LogicFunction function, int level, AstNode node, AstContextType contextType,
            AstSubcontextType subcontextType, AstContext parent, double weight) {
        this(profile, function, level, node, contextType, subcontextType, parent, weight, new ArrayList<>());
    }

    public static AstContext createRootNode(CompilerProfile profile) {
        return new AstContext(profile, null, 0, NoOp.NO_OP, AstContextType.ROOT,
                AstSubcontextType.BASIC, null, 1.0);
    }

    public static AstContext createStaticRootNode() {
        return createRootNode(CompilerProfile.standardOptimizations(false));
    }

    public AstContext createChild(CompilerProfile profile, AstNode node, AstContextType contextType) {
        AstContext child = new AstContext(profile, function, level + 1, node, contextType, node.getSubcontextType(),
                this, 1.0);
        children.add(child);

        return child;
    }

    public AstContext createFunctionDeclaration(CompilerProfile profile, LogicFunction function, AstNode node, AstContextType contextType, double weight) {
        AstContext child = new AstContext(profile, function, level + 1, node, contextType, node.getSubcontextType(),
                this, weight);
        children.add(child);
        return child;
    }

    public AstContext createSubcontext(AstSubcontextType subcontextType, double weight) {
        // Subcontext always inherits compiler profile from parent context
        AstContext child = new AstContext(profile, function, level, node, contextType, subcontextType, this, weight);
        children.add(child);
        return child;
    }

    public AstContext createSubcontext(LogicFunction function, AstSubcontextType subcontextType, double weight) {
        // Subcontext always inherits compiler profile from parent context
        AstContext child = new AstContext(profile, function, level, node, contextType, subcontextType, this, weight);
        children.add(child);
        return child;
    }

    public Map<AstContext, AstContext> createDeepCopy() {
        Map<AstContext, AstContext> map = new IdentityHashMap<>(16);
        createDeepCopy(map, parent);
        return map;
    }

    private AstContext createDeepCopy(Map<AstContext, AstContext> map, AstContext parent) {
        AstContext copy = new AstContext(profile, function, level, node, contextType, subcontextType, parent, weight);
        children.stream()
                .map(c -> c.createDeepCopy(map, copy))
                .forEachOrdered(copy.children::add);
        map.put(this, copy);
        return copy;
    }

    public Map<AstContext, AstContext> copyChildrenTo(AstContext newParent) {
        Map<AstContext, AstContext> map = new IdentityHashMap<>(16);
        children.stream()
                .map(c -> c.createDeepCopy(map, newParent))
                .forEachOrdered(newParent.children::add);
        map.put(this, newParent);
        return map;
    }

    public CompilerProfile getProfile() {
        return profile;
    }

    /**
     * This context belongs to another context when they're the same instance, or when this context is
     * a descendant (direct or indirect child) of the other context.
     *
     * @param other context to match
     * @return true if this context belongs to the other context
     */
    public boolean belongsTo(AstContext other) {
        return this == other || (parent != null && parent.belongsTo(other));
    }

    public boolean matches(AstContextType contextType) {
        return this.contextType == contextType;
    }

    public boolean matches(AstContextType contextType, AstSubcontextType subcontextType) {
        return this.contextType == contextType && this.subcontextType == subcontextType;
    }

    public boolean matches(AstSubcontextType subcontextType) {
        return this.subcontextType == subcontextType;
    }

    public boolean matches(AstSubcontextType... subcontextTypes) {
        for (AstSubcontextType subcontextType : subcontextTypes) {
            if (this.subcontextType == subcontextType) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesRecursively(AstSubcontextType... subcontextTypes) {
        if (matches(subcontextTypes)) {
            return true;
        }
        return parent != null && parent.matchesRecursively(subcontextTypes);
    }

    public boolean matchesRecursively(AstContextType... contextTypes) {
        for (AstContextType contextType : contextTypes) {
            if (this.contextType == contextType) {
                return true;
            }
        }
        return parent != null && parent.matchesRecursively(contextTypes);
    }

    public AstContext findContextOfType(AstContextType contextType) {
        AstContext current = this;
        while (current != null) {
            if (current.contextType == contextType) {
                return current;
            }
            current = current.parent;
        }

        return null;
    }

    public AstContext findTopContextOfType(AstContextType contextType) {
        AstContext current = this;
        AstContext found = null;
        while (current != null) {
            if (current.contextType == contextType) {
                found = current;
            }
            current = current.parent;
        }

        return found;
    }

    /**
     * For a given descendant, finds the direct child of this context that contains the descendant.
     *
     * @param descendant descendant to this context
     * @return direct child containing the descendant, or null if it doesn't exist
     */
    public AstContext findDirectChild(AstContext descendant) {
        AstContext current = descendant;
        while (current != null) {
            if (current.parent == this) {
                return current;
            }
            current = current.parent;
        }

        return null;
    }

    public AstContext findSubcontext(AstSubcontextType type) {
        for (AstContext child : children) {
            if (child.subcontextType == type) {
                return child;
            }
        }
        return null;
    }

    public AstContext findLastSubcontext(AstSubcontextType type) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (child(i).subcontextType == type) {
                return child(i);
            }
        }
        return null;
    }

    public List<AstContext> findSubcontexts(AstSubcontextType type) {
        return children.stream().filter(c -> c.subcontextType == type).toList();
    }

    public boolean containsChildContext(Predicate<AstContext> matcher) {
        for (AstContext child : children) {
            if (matcher.test(child) || child.containsChildContext(matcher)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the instruction is executed exactly once within a context - i.e. isn't part o any embedded
     * control flow structure, such as if, loop or case statements/expressions.
     *
     * @param instruction instruction to evaluate
     * @return true if the instruction is simply contained in this context
     */
    public boolean executesOnce(LogicInstruction instruction) {
        for (AstContext ctx = instruction.getAstContext(); ctx != null; ctx = ctx.parent()) {
            if (!ctx.isLinear()) {
                return ctx == this || ctx.parent() == this;
            }
        }
        return false;
    }

    private boolean isLinear() {
        if (contextType == AstContextType.CALL) {
            return subcontextType == AstSubcontextType.SYSTEM_CALL
                    || subcontextType == AstSubcontextType.ARGUMENTS
                    || subcontextType == AstSubcontextType.BASIC;
        } else {
            return subcontextType == AstSubcontextType.INIT || subcontextType == AstSubcontextType.BASIC || !contextType.flowControl;
        }
    }

    public String hierarchy() {
        String text = subcontextType == AstSubcontextType.BASIC
            ? contextType.text : contextType.text + "[" +  subcontextType.text + "]";

        return parent == null ? text : parent.hierarchy() + "/" + text;
    }

    @Override
    public String toString() {
        return "AstContext{" +
                "id=" + id +
                ", level=" + level +
                ", contextType=" + contextType +
                ", subcontextType=" + subcontextType +
                ", totalWeight=" + totalWeight() +
                ", node=" + node +
                '}';
    }

    public int level() {
        return level;
    }

    public AstNode node() {
        return node;
    }

    public InputPosition inputPosition() {
        return node != null ? node.inputPosition() : null;
    }

    public String functionPrefix() {
        return function == null ? null : function.getPrefix();
    }

    public LogicFunction function() {
        return function;
    }

    public boolean isFunction() {
        return function != null;
    }

    public AstContextType contextType() {
        return contextType;
    }

    public AstSubcontextType subcontextType() {
        return subcontextType;
    }

    public AstContext parent() {
        return parent;
    }

    public void updateWeight(double weight) {
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public double totalWeight() {
        return parent == null ? weight : weight * parent.totalWeight();
    }

    public List<AstContext> children() {
        return children;
    }

    public AstContext child(int index) {
        return children.get(index);
    }

    public AstContext firstChild() {
        return children.get(0);
    }

    public AstContext lastChild() {
        return children.get(children.size() - 1);
    }

    public AstContext nextChild(AstContext child) {
        for (int i = 0; i < children.size() - 1; i++) {
            if (children.get(i) == child) {
                return children.get(i + 1);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AstContext) obj;
        return this.level == that.level &&
                Objects.equals(this.node, that.node) &&
                Objects.equals(this.contextType, that.contextType) &&
                Objects.equals(this.subcontextType, that.subcontextType) &&
                this.parent == that.parent &&
                Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(that.weight) &&
                Objects.equals(this.children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, node, contextType, subcontextType, weight);
    }

}

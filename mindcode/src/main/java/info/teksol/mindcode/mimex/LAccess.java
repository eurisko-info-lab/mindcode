package info.teksol.mindcode.mimex;

public record LAccess(
        String name,
        String varName,
        boolean senseable,
        boolean controllable,
        boolean settable,
        String parameters
) implements MindustryContent {

    @Override
    public int id() {
        return -1;
    }

    public static LAccess forName(String name) {
        return MindustryContents.LACCESS_MAP.get(name);
    }
}

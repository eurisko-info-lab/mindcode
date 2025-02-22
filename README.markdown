# Mindcode

**Mindcode** is a high-level programming language for [Mindustry Logic](https://github.com/Anuken/Mindustry). Mindcode generates fairly well optimized mlog code, utilizing available instruction space to make the resulting code faster. It comes with a [web app](http://mindcode.herokuapp.com/) and a [command-line compiler](doc/syntax/TOOLS-IDE-INTEGRATION.markdown#setting-up-the-command-line-compiler), provides means for integration both with various IDEs and Mindustry itself. The project uses lots of automated tests to make sure the produced code is correct.   

**Schemacode**, an extension built over Mindcode, is a specialized definition language designed for creating a complete Mindustry schematic from a text file. [Schematics builder](doc/syntax/SCHEMACODE.markdown) compiles these definition files directly into Mindustry schematics, either into binary `.msch` file, or into the text representation. Processors can be included in these schematics, complete with the code (specified either in Mindcode or mlog) and linked blocks.

> [!TIP]
> Mindcode currently undergoes substantial changes to the syntax. You might want to have a look at the [upcoming changes](https://github.com/cardillan/mindcode/discussions/142) that are still being deliberated. Comments and suggestions are welcome.

## Mindcode Syntax

Please refer to the [documentation](doc/syntax/SYNTAX.markdown) for a complete description of Mindcode syntax. You can also use the code examples in the web application to get familiar with Mindcode.  

> [!IMPORTANT]
> In the [2.3.0 release](CHANGELOG.markdown#230---2024-10-16), Mindcode introduces a stricter form of the syntax where semicolons and the `do`/`then` keywords are no longer optional. The original syntax is deprecated, but will be available for some time through the `#relaxed;` directive, which can be put at the beginning of your code. Other changes to the syntax include compulsory `out` keywords for output function call arguments and using the `@` prefix for Mindustry built-in variables used as properties. The old syntax is still supported, but generates a warning. See [Strict and Relaxed syntax](doc/syntax/SYNTAX-STRICT-RELAXED.markdown) for a description of all changes to the syntax.
> 
> The relaxed variant of the syntax will be eventually removed.    

## Latest development

The most important recent changes to Mindcode include:

* Mindustry Logic 8
  * Full support for the Mindustry Logic in the upcoming [Mindustry version 8](/doc/syntax/MINDUSTRY-8.markdown).
* Basic functionality
  * Significant improvement in reporting error messages generated during compilation.
  * Ability to inject the mlog code to Mindustry processors via the [Mlog Watcher mod](doc/syntax/TOOLS-MLOG-WATCHER.markdown).
  * Support for running the compiled mlog code in an emulated processor.
  * Tool for [partial decompilation](doc/syntax/TOOLS-MLOG-DECOMPILER.markdown) of an existing mlog code into Mindcode.
* Mindcode syntax
  * User-defined functions enhancements: user-defined functions may provide additional return values through [output parameters](doc/syntax/SYNTAX-4-FUNCTIONS.markdown#function-parameters), can accept [variable number of arguments](doc/syntax/SYNTAX-4-FUNCTIONS.markdown#vararg-functions) and can be [overloaded](doc/syntax/SYNTAX-4-FUNCTIONS.markdown#function-overloading).
  * A built-in [library of system functions](doc/syntax/SYSTEM-LIBRARY.markdown).
  * New mechanism for [program parametrization](doc/syntax/SYNTAX-1-VARIABLES.markdown#program-parameters).
  * Improved [List iteration loops](doc/syntax/SYNTAX-3-STATEMENTS.markdown#list-iteration-loops) supporting more than one loop variable and modification of the list elements while iterating.   

See [changelog](CHANGELOG.markdown) for a comprehensive list of changes.

## Using Mindcode 

### Online

Mindcode is available at http://mindcode.herokuapp.com/. Write some Mindcode in the _Mindcode Source Code_ text area, then press the **Compile** button. The _Mindustry Logic_ text area will contain the mlog version of your Mindcode. Copy the mlog code into the clipboard. Back in Mindustry, edit your processor, then use the **Edit** button in the Logic UI. Select **Import from Clipboard**. Mindustry is now ready to execute your code.

You can also use the **Compile and Run** button to execute the compiled code right away on an emulated processor. The output produced by `print` instructions in your code will be displayed. Very limited interaction with the Mindustry World is supported.

### Offline

Alternatively, you can download the command-line compiler and use Mindcode [from within an IDE](doc/syntax/TOOLS-IDE-INTEGRATION.markdown).

## Mindustry Logic References

If you don't know much about Mindustry Logic, you can read more information about it here:

* [Mlog Documentation](https://yrueii.github.io/Mlog%20Documentation/) <small>Sep 2024</small>

Unfortunately, other sources are rather dated at this moment:

* [Logic in 6.0](https://www.reddit.com/r/Mindustry/comments/ic9wrm/logic_in_60/) <small>Aug 2020</small>
* [How To Use Processors in 6.0](https://steamcommunity.com/sharedfiles/filedetails/?id=2268059244) <small>Nov 2020</small>
* [An Overly In-Depth Logic Guide](https://www.reddit.com/r/Mindustry/comments/kfea1e/an_overly_indepth_logic_guide/) <small>Dec 2020</small>

## Contributing

See [CONTRIBUTING](CONTRIBUTING.markdown).

# License

MIT. See [LICENSE](LICENSE) for the full text of the license.

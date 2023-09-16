/*
BNF grammar for expressions, Sec 5.1.3
expression     → literal
               | unary
               | binary
               | grouping ;

literal        → NUMBER | STRING | "true" | "false" | "nil" ;
grouping       → "(" expression ")" ;
unary          → ( "-" | "!" ) expression ;
binary         → expression operator expression ;
operator       → "==" | "!=" | "<" | "<=" | ">" | ">="
               | "+"  | "-"  | "*" | "/" ;

Note: we CAPITALIZE terminals that are a single lexeme whose text representation may vary. 
NUMBER is any number literal, and STRING is any string literal. 
Later, we’ll do the same for IDENTIFIER
*/
package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                    "Binary   : Expr left, Token operator, Expr right",
                    "Grouping : Expr expression",
                    "Literal  : Object value",
                    "Unary    : Token operator, Expr right"
                    ));
    }

    private static void defineAst( String outputDir, String baseName, List<String> types)
            throws IOException {
            String path = outputDir + "/" + baseName + ".java";
            PrintWriter writer = new PrintWriter(path, "UTF-8");

            writer.println("package com.craftinginterpreters.lox;");
            writer.println();
            writer.println("import java.util.List;");
            writer.println();
            writer.println("abstract class " + baseName + " {");

            // The AST classes.
            for (String type : types) {
                String className = type.split(":")[0].trim();
                String fields = type.split(":")[1].trim(); 
                defineType(writer, baseName, className, fields);
            }
            writer.println("}");
            writer.close();
    }

    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println("  static class " + className + " extends " +
                baseName + " {");

        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
}

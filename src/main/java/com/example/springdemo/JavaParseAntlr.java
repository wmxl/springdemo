package com.example.springdemo;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class JavaParseAntlr {
    private static Map<String, String> constantsMap = new HashMap<>();

    public static void main(String[] args) {
        List<Endpoint> endpoints = new ArrayList<>();
        try {
            parseConstants("src/main/java/com/example/springdemo/web/config/MomentUrlConfig.java");
            
            // Parse the controller file
            FileInputStream controllerFile = new FileInputStream("src/main/java/com/example/springdemo/MomentTestController2.java");
            ANTLRInputStream input = new ANTLRInputStream(controllerFile);
            Java8Lexer lexer = new Java8Lexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            ParseTree tree = parser.compilationUnit();

            // Create a custom listener
            ParseTreeWalker walker = new ParseTreeWalker();
            EndpointListener listener = new EndpointListener(endpoints, constantsMap);
            walker.walk(listener, tree);

            // Print results
            System.out.println("Found " + endpoints.size() + " endpoints:");
            endpoints.forEach(endpoint -> {
                System.out.println("Method: " + endpoint.method);
                System.out.println("Type: " + endpoint.type);
                System.out.println("Path: " + endpoint.path);
                System.out.println("-------------------");
            });

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void parseConstants(String configFilePath) throws IOException {
        FileInputStream configFile = new FileInputStream(configFilePath);
        ANTLRInputStream input = new ANTLRInputStream(configFile);
        Java8Lexer lexer = new Java8Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        ParseTree tree = parser.compilationUnit();

        ParseTreeWalker walker = new ParseTreeWalker();
        ConstantsListener listener = new ConstantsListener(constantsMap);
        walker.walk(listener, tree);
    }

    // Keep these two methods from the original JavaParse class
    private static String resolveConstantExpression(String expression) {
        expression = expression.replaceAll("^\"(.*)\"$", "$1");
        
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                part = part.trim();
                part = part.replaceAll("^\"(.*)\"$", "$1");
                if (constantsMap.containsKey(part)) {
                    result.append(constantsMap.get(part));
                } else {
                    result.append(part);
                }
                result.append("+");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                part = part.trim();
                part = part.replaceAll("^\"(.*)\"$", "$1");
                if (constantsMap.containsKey(part)) {
                    result.append(constantsMap.get(part));
                } else {
                    result.append(part);
                }
                result.append("-");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                part = part.trim();
                part = part.replaceAll("^\"(.*)\"$", "$1");
                if (constantsMap.containsKey(part)) {
                    result.append(constantsMap.get(part));
                } else {
                    result.append(part);
                }
                result.append("*");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                part = part.trim();
                part = part.replaceAll("^\"(.*)\"$", "$1");
                if (constantsMap.containsKey(part)) {
                    result.append(constantsMap.get(part));
                } else {
                    result.append(part);
                }
                result.append("/");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } else {
            if (constantsMap.containsKey(expression)) {
                return constantsMap.get(expression);
            } else {
                return expression;
            }
        }
    }
} 
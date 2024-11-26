package com.example.springdemo;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class JavaParseMain {
    public static void main(String[] args) {
        List<Endpoint> endpoints = new ArrayList<>();
        Map<String, String> constantsMap = new HashMap<>();

        try {
            // First parse the constants file using simple file reading
            System.out.println("Parsing constants file...");
            parseConstantsFile("src/main/java/com/example/springdemo/web/config/MomentUrlConfig.java", constantsMap);
            
            // Print parsed constants for verification
            System.out.println("\nParsed constants:");
            constantsMap.forEach((key, value) -> 
                System.out.println(key + " = " + value));

            // Then parse the controller file
            System.out.println("\nParsing controller file...");
            parseFile("src/main/java/com/example/springdemo/MomentTestController2.java", 
                     new EndpointListener(endpoints, constantsMap));

            // Print results
            System.out.println("\nFound " + endpoints.size() + " endpoints:");
            for (Endpoint endpoint : endpoints) {
                System.out.println("Method: " + endpoint.method);
                System.out.println("Type: " + endpoint.type);
                System.out.println("Path: " + endpoint.path);
                System.out.println("-------------------");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void parseConstantsFile(String filePath, Map<String, String> constantsMap) throws IOException {
        Pattern pattern = Pattern.compile("String\\s+(\\w+)\\s*=\\s*\"([^\"]+)\"");
        String interfaceName = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Find interface name
                if (line.contains("interface")) {
                    Pattern interfacePattern = Pattern.compile("interface\\s+(\\w+)");
                    Matcher m = interfacePattern.matcher(line);
                    if (m.find()) {
                        interfaceName = m.group(1);
                    }
                }

                // Find constant name and value
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    String constantName = m.group(1);
                    String constantValue = m.group(2);
                    constantsMap.put(constantName, constantValue);
                }
            }
        }
    }

    private static void parseFile(String filePath, ParseTreeListener listener) throws IOException {
        System.out.println("Processing file: " + filePath);
        
        CharStream input = CharStreams.fromFileName(filePath);
        Java8Lexer lexer = new Java8Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokens);
        
        // Add error handling
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, 
                                  int line, int charPositionInLine, String msg, RecognitionException e) {
                System.err.println("Warning: Parse error at line " + line + ":" + charPositionInLine + " " + msg);
            }
        });

        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, tree);
    }
} 
package com.example.springdemo;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaParse {
    private static Map<String, String> constantsMap = new HashMap<>();


    public static void main(String[] args) {
        List<Endpoint> endpoints = new ArrayList<>();
        try {
            parseConstants("src/main/java/com/example/springdemo/web/config/MomentUrlConfig.java");

            CompilationUnit compilationUnit = StaticJavaParser.parse(new File("src/main/java/com/example/springdemo/MomentTestController2.java"));
            
            compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(classDecl -> {
                boolean isController = classDecl.getAnnotations().stream()
                        .anyMatch(a -> a.getNameAsString().equals("Controller") || 
                                     a.getNameAsString().equals("RestController"));
                
                if (isController) {
                    String basePath = classDecl.getAnnotationByName("RequestMapping")
                        .map(a -> resolveConstantExpression(a.getChildNodes().get(1).toString()))
                        .orElse("");
                    
                    classDecl.getMethods().forEach(method -> {
                        method.getAnnotations().forEach(annotation -> {
                            String annotationName = annotation.getNameAsString();
                            if (annotationName.contains("Mapping")) {
                                endpoints.add(new Endpoint(
                                    method.getNameAsString(),
                                    annotationName,
                                    basePath + resolveConstantExpression(getPathFromAnnotation(annotation))
                                ));
                            }
                        });
                    });
                }
            });

            System.out.println("Found " + endpoints.size() + " endpoints:");
            endpoints.forEach(endpoint -> {
                System.out.println("Method: " + endpoint.method);
                System.out.println("Type: " + endpoint.type);
                System.out.println("Path: " + endpoint.path);
                System.out.println("-------------------");
            });

        } catch (FileNotFoundException e) {
            System.err.println("Could not find file: " + e.getMessage());
        }
    }

    private static void parseConstants(String configFilePath) throws FileNotFoundException {
        CompilationUnit configCU = StaticJavaParser.parse(new File(configFilePath));
        configCU.findAll(ClassOrInterfaceDeclaration.class).forEach(classDecl -> {
            String className = classDecl.getNameAsString();
            classDecl.getFields().forEach(field -> {
                field.getVariables().forEach(var -> {
                    String constantName = className + "." + var.getNameAsString();
                    var.getInitializer().ifPresent(init -> 
                        constantsMap.put(constantName, init.toString().replace("\"", ""))
                    );
                });
            });
        });
    }

    private static String resolveConstantExpression(String expression) {
        // Remove any surrounding quotes from the entire expression
        expression = expression.replaceAll("^\"(.*)\"$", "$1");
        
        // Handle expressions like 'MomentUrlConfig.APP_URL_PREFIX + "/moment"'
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                part = part.trim();
                // Remove quotes from string literals
                part = part.replaceAll("^\"(.*)\"$", "$1");
                if (constantsMap.containsKey(part)) {
                    // Constant reference
                    result.append(constantsMap.get(part));
                } else {
                    // String literal or unresolved reference
                    result.append(part);
                }
            }
            return result.toString();
        }
        
        // Handle single constant reference
        return constantsMap.getOrDefault(expression, expression);
    }

    private static String getPathFromAnnotation(AnnotationExpr annotation) {
        return annotation.getChildNodes().size() > 1 ? 
               annotation.getChildNodes().get(1).toString() : "";
    }
}

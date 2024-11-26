package com.example.springdemo;

import org.antlr.v4.runtime.tree.ParseTreeProperty;
import com.example.springdemo.Java8Parser;
import java.util.*;
import java.util.regex.*;

public class EndpointListener extends Java8BaseListener {
    private List<Endpoint> endpoints;
    private Map<String, String> constantsMap;
    private String basePath;

    public EndpointListener(List<Endpoint> endpoints, Map<String, String> constantsMap) {
        this.endpoints = endpoints;
        this.constantsMap = constantsMap;
        this.basePath = constantsMap.get("MomentUrlConfig.APP_URL_PREFIX");
    }

    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        String classText = ctx.getText();
        System.out.println("\nDEBUG: Class text:");
        System.out.println(classText);
        
        findMappings(classText);
    }

    private void findMappings(String text) {
        Map<String, Pattern> mappingPatterns = new HashMap<>();
        String methodPattern = "public(?:Object|String|void|\\w+)(\\w+)\\(";
        mappingPatterns.put("GetMapping", Pattern.compile("@GetMapping\\([\"']([^\"']*)[\"']\\)" + methodPattern));
        mappingPatterns.put("PostMapping", Pattern.compile("@PostMapping\\([\"']([^\"']*)[\"']\\)" + methodPattern));
        mappingPatterns.put("RequestMapping", Pattern.compile("@RequestMapping\\([\"']([^\"']*)[\"']\\)" + methodPattern));
        mappingPatterns.put("PutMapping", Pattern.compile("@PutMapping\\([\"']([^\"']*)[\"']\\)" + methodPattern));
        mappingPatterns.put("DeleteMapping", Pattern.compile("@DeleteMapping\\([\"']([^\"']*)[\"']\\)" + methodPattern));

        for (Map.Entry<String, Pattern> entry : mappingPatterns.entrySet()) {
            Matcher matcher = entry.getValue().matcher(text);
            while (matcher.find()) {
                String path = matcher.group(1);
                String methodName = matcher.group(2);
                System.out.println("DEBUG: Found mapping type: " + entry.getKey());
                System.out.println("DEBUG: Found path: " + path);
                System.out.println("DEBUG: Found method: " + methodName);
                
                // Construct the full path
                String fullPath = basePath + "/moment" + path;
                
                endpoints.add(new Endpoint(
                    methodName,
                    entry.getKey(),
                    fullPath
                ));
            }
        }
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        String methodText = ctx.getText();
        System.out.println("\nDEBUG: Method text:");
        System.out.println(methodText);
    }

    private String resolveConstantExpression(String expression) {
        if (expression == null) return "";
        
        System.out.println("DEBUG: Resolving expression: " + expression);
        expression = expression.replaceAll("^\"(.*)\"$", "$1");
        
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                part = part.trim();
                part = part.replaceAll("^\"(.*)\"$", "$1");
                if (constantsMap.containsKey(part)) {
                    result.append(constantsMap.get(part));
                    System.out.println("DEBUG: Resolved constant " + part + " to " + constantsMap.get(part));
                } else {
                    result.append(part);
                    System.out.println("DEBUG: Using literal value: " + part);
                }
            }
            return result.toString();
        }
        
        String result = constantsMap.getOrDefault(expression, expression);
        System.out.println("DEBUG: Final resolved value: " + result);
        return result;
    }
} 
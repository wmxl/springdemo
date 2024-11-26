package com.example.springdemo.antlr;

import com.example.springdemo.Java8BaseListener;
import com.example.springdemo.Java8Parser;

import java.util.Map;

public class ConstantsListener extends Java8BaseListener {
    private Map<String, String> constantsMap;
    private String currentInterfaceName;

    public ConstantsListener(Map<String, String> constantsMap) {
        this.constantsMap = constantsMap;
    }

    @Override
    public void enterInterfaceDeclaration(Java8Parser.InterfaceDeclarationContext ctx) {
        if (ctx != null && ctx.Identifier() != null) {
            currentInterfaceName = ctx.Identifier().getText();
        }
    }


} 
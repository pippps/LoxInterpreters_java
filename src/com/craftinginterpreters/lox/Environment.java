package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    final Environment enclosing;
    private final Map<String, Object> globalValues = new HashMap<>();
    private final Object[] localValues = new Object[100];
    int index = 0;

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing)  {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        if (globalValues.containsKey(name.lexeme)) {
            return globalValues.get(name.lexeme);
        }

        if(enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if (globalValues.containsKey(name.lexeme)) {
            globalValues.put(name.lexeme, value);
            return;
        }

        if (enclosing != null){
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void define(String name, Object value) {
        globalValues.put(name, value);
    }

    void define(Object value) {
        localValues[index++] = value;
    }

    Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }

        return environment;
    }

    Object getAt(int distance, int index) {
        return ancestor(distance).localValues[index];
    }

    void assignAt(int distance, int index, Object value) {
        ancestor(distance).localValues[index] = value;
    }

}

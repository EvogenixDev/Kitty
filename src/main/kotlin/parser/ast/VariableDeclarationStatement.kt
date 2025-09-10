package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler

class VariableDeclarationStatement(val name: String, val expression: Expression): Statement {

    override fun execute() {
        if (ScopeHandler.hasVariable(name)) {
            throw RuntimeException("Variable $name already declared in current scope")
        }

        ScopeHandler.declareVariableInCurrentScope(name, expression.eval())
    }
}
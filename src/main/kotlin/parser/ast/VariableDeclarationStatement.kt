package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler

class VariableDeclarationStatement(val name: String, val expression: Expression): Statement {

    override fun execute() {
        ScopeHandler.declareVariableInCurrentScope(name, expression.eval())
    }
}
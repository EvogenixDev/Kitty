package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class VariableDeclarationStatement(val name: String, val expression: Node): Statement {

    override fun eval(): Value {
        ScopeHandler.declareVariableInCurrentScope(name, expression.eval())
        return NumberValue.ZERO
    }
}
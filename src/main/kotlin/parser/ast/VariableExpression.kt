package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class VariableExpression(val variable: String): Expression {

    override fun eval(): Value {
        return ScopeHandler.getVariable(variable)
    }
}
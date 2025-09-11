package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class VariableExpression(val variable: String): Node, Accessible {

    override fun set(value: Value): Value {
        ScopeHandler.setVariable(variable, value)
        return get()
    }

    override fun get(): Value {
        return ScopeHandler.getVariable(variable)
    }

    override fun eval(): Value {
        return get()
    }
}
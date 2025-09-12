package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.UserDefinedFunction
import ru.kitty.lang.Value

class FunctionDefineStatement(
    val name: String,
    val argNames: List<String>,
    val statement: Statement
): Statement {

    override fun eval(): Value {
        ScopeHandler.setFunction(name, UserDefinedFunction(argNames, statement))
        return NumberValue.ZERO
    }
}
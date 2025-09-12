package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.UserDefinedFunction
import ru.kitty.lang.Value

class FunctionalExpression(
    val name: String,
    val arguments: List<Node>
): Node {

    override fun eval(): Value {
        val values = mutableListOf<Value>()
        arguments.forEach { values.add(it.eval()) }

        val function = ScopeHandler.getFunction(name)
        if (function is UserDefinedFunction) {
            ScopeHandler.push()

            for (i in 0..< function.argsCount()) {
                ScopeHandler.declareVariableInCurrentScope(function.argName(i), values[i])
            }

            val result = function.execute(*values.toTypedArray())
            ScopeHandler.pop()
            return result
        }

        return function.execute(*values.toTypedArray())
    }
}
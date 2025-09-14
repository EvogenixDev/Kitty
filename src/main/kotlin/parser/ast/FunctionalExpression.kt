package ru.kitty.parser.ast

import ru.kitty.lang.Function
import ru.kitty.lang.FunctionValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class FunctionalExpression(
    val functionExpr: Node,
    val arguments: List<Node>
): Node {

    override fun eval(): Value {
        val values = mutableListOf<Value>()
        arguments.forEach { values.add(it.eval()) }

        val function = consumeFunction(functionExpr.eval())
        return function.execute(*values.toTypedArray())
    }

    private fun consumeFunction(value: Value): Function {
        if (value is FunctionValue) {
            return value.value
        }

        return ScopeHandler.getFunction(value.asString())
    }
}
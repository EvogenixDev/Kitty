package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class UnaryExpression(
    val operation: Operator,
    val expression: Node
): Node {

    enum class Operator {
        NEGATE,
        NOT,
    }

    override fun eval(): Value {
        val value = expression.eval()

        return when (operation) {
            Operator.NEGATE -> negate(value)
            Operator.NOT -> NumberValue.fromBoolean(value.asInt() == 0)
        }
    }

    private fun negate(value: Value): Value {
        if (value is NumberValue) {
            val raw = value.raw()
            if (raw is Double) {
                return NumberValue(-raw)
            }

            return NumberValue(-raw.toInt())
        }

        if (value is StringValue) {
            return StringValue(value.asString().reversed())
        }

        throw RuntimeException("Unsupported operation for value")
    }
}
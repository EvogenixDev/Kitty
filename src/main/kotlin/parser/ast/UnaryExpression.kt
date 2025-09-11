package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class UnaryExpression(
    val operation: String,
    val expression: Node
): Node {

    override fun eval(): Value {
        val value = expression.eval()

        return when (operation) {
            "-" -> negate(value)
            else -> throw RuntimeException("Unsupported operation $operation")
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
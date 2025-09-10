package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.Value

class UnaryExpression(
    val operation: String,
    val expression: Expression
): Expression {

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

        throw RuntimeException("Unsupported operation for value")
    }
}
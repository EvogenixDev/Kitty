package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class UnaryExpression(
    val operation: Operator,
    val expression: Node
): Statement {

    enum class Operator {
        NEGATE,
        NOT,
        PREFIX_INCREMENT,
        PREFIX_DECREMENT,
        POSTFIX_INCREMENT,
        POSTFIX_DECREMENT
    }

    override fun eval(): Value {
        val value = expression.eval()

        return when (operation) {
            Operator.NEGATE -> negate(value)
            Operator.NOT -> NumberValue.fromBoolean(value.asInt() == 0)
            //++var
            Operator.PREFIX_INCREMENT -> {
                val accessible = expression as Accessible
                return accessible.set(increment(value))
            }
            //--var
            Operator.PREFIX_DECREMENT -> {
                val accessible = expression as Accessible
                return accessible.set(decrement(value))
            }
            //var++
            Operator.POSTFIX_INCREMENT -> {
                val accessible = expression as Accessible
                accessible.set(increment(accessible.get()))
                return value
            }
            //var--
            Operator.POSTFIX_DECREMENT -> {
                val accessible = expression as Accessible
                accessible.set(decrement(value))
                return value
            }
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

    private fun increment(value: Value): Value {
        val number = value as NumberValue
        if (number.raw() is Double) {
            return NumberValue.of(number.asDouble() + 1)
        }

        return NumberValue.of(number.asInt() + 1)
    }

    private fun decrement(value: Value): Value {
        val number = value as NumberValue
        if (number.raw() is Double) {
            return NumberValue.of(number.asDouble() - 1)
        }

        return NumberValue.of(number.asInt() - 1)
    }
}
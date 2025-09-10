package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class BinaryExpression(
    val operation: String,
    val leftExpression: Expression,
    val rightExpression: Expression
): Expression {

    override fun eval(): Value {
        val left = leftExpression.eval()
        val right = rightExpression.eval()

        return when (operation) {
            "+" -> add(left, right)
            "-" -> subtract(left, right)
            "*" -> multiply(left, right)
            "/" -> divide(left, right)
            else -> throw RuntimeException("Unsupported operation $operation")
        }
    }

    private fun add(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            val num1 = left.raw()
            val num2 = right.raw()

            if (num1 is Double || num2 is Double) {
                return NumberValue(num1.toDouble() + num2.toDouble())
            }

            return NumberValue(num1.toInt() + num2.toInt())
        }

        return StringValue(left.asString() + right.asString())
    }

    private fun subtract(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            val num1 = left.raw()
            val num2 = right.raw()

            if (num1 is Double || num2 is Double) {
                return NumberValue(num1.toDouble() - num2.toDouble())
            }

            return NumberValue(num1.toInt() - num2.toInt())
        }

        throw RuntimeException("Unsupported operation $operation")
    }

    private fun multiply(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            val num1 = left.raw()
            val num2 = right.raw()

            if (num1 is Double || num2 is Double) {
                return NumberValue(num1.toDouble() * num2.toDouble())
            }

            return NumberValue(num1.toInt() * num2.toInt())
        }

        throw RuntimeException("Unsupported operation $operation")
    }

    private fun divide(left: Value, right: Value): Value {
        if (left is NumberValue && right is NumberValue) {
            val num1 = left.raw()
            val num2 = right.raw()

            if (num1 is Double || num2 is Double) {
                return NumberValue(num1.toDouble() / num2.toDouble())
            }

            return NumberValue(num1.toInt() / num2.toInt())
        }

        throw RuntimeException("Unsupported operation $operation")
    }
}
package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class ConditionalExpression(
    val operation: String,
    val leftExpression: Node,
    val rightExpression: Node
): Node {

    override fun eval(): Value {
        val left = leftExpression.eval()
        val right = rightExpression.eval()

        return when (operation) {
            "and" -> NumberValue.fromBoolean(left.asInt() == 1 && right.asInt() == 1)
            "or" -> NumberValue.fromBoolean(left.asInt() == 1 || right.asInt() == 1)
            else -> NumberValue.fromBoolean(compare(left, right))
        }
    }

    private fun compare(left: Value, right: Value): Boolean {
        if (left is NumberValue && right is NumberValue) {
            return when (operation) {
                "<" -> left.asDouble() < right.asDouble()
                ">" -> left.asDouble() > right.asDouble()
                "<=" -> left.asDouble() <= right.asDouble()
                ">=" -> left.asDouble() >= right.asDouble()
                "!=" -> left.asDouble() != right.asDouble()
                else -> left.asDouble() == right.asDouble()
            }
        }

        if (left is StringValue && right is StringValue) {
            return left.asString() == right.asString()
        }

        throw RuntimeException("Cannot compare values")
    }
}
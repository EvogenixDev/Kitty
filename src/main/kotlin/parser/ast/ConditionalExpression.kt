package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class ConditionalExpression(
    val operation: Operator,
    val leftExpression: Node,
    val rightExpression: Node
): Node {

    enum class Operator {
        AND,
        OR,

        LT,
        GT,
        LT_EQ,
        GT_EQ,

        EQ,
        NOT_EQ,
    }

    override fun eval(): Value {
        val left = leftExpression.eval()
        val right = rightExpression.eval()

        return when (operation) {
            Operator.AND -> NumberValue.fromBoolean(left.asInt() == 1 && right.asInt() == 1)
            Operator.OR -> NumberValue.fromBoolean(left.asInt() == 1 || right.asInt() == 1)
            else -> NumberValue.fromBoolean(compare(left, right))
        }
    }

    private fun compare(left: Value, right: Value): Boolean {
        if (left is NumberValue && right is NumberValue) {
            return when (operation) {
                Operator.LT -> left.asDouble() < right.asDouble()
                Operator.GT -> left.asDouble() > right.asDouble()
                Operator.LT_EQ -> left.asDouble() <= right.asDouble()
                Operator.GT_EQ -> left.asDouble() >= right.asDouble()
                Operator.NOT_EQ -> left.asDouble() != right.asDouble()
                else -> left.asDouble() == right.asDouble()
            }
        }

        if (left is StringValue && right is StringValue) {
            return left.asString() == right.asString()
        }

        throw RuntimeException("Cannot compare values")
    }
}
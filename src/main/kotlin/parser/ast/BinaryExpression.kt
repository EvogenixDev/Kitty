package ru.kitty.parser.ast

class BinaryExpression(
    val operation: String,
    val leftExpression: Expression,
    val rightExpression: Expression
): Expression {

    override fun eval(): Int {
        val left = leftExpression.eval()
        val right = rightExpression.eval()

        return when (operation) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" -> left / right
            else -> throw RuntimeException("Unsupported operation $operation")
        }
    }
}
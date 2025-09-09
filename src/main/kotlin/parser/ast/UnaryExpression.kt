package ru.kitty.parser.ast

class UnaryExpression(
    val operation: String,
    val expression: Expression
): Expression {

    override fun eval(): Int {
        val number = expression.eval()

        return when (operation) {
            "-" -> -number
            else -> throw RuntimeException("Unsupported operation $operation")
        }
    }
}
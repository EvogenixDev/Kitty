package ru.kitty.parser.ast

import ru.kitty.lang.Value

class AssignmentStatement(
    val variable: Accessible,
    val expression: Node,
    val operation: String? = null
): Statement {

    override fun eval(): Value {
        if (operation == null) {
            return variable.set(expression.eval())
        }

        val value = BinaryExpression(operation, variable, expression)
        return variable.set(value.eval())
    }
}
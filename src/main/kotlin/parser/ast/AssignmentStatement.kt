package ru.kitty.parser.ast

import ru.kitty.lang.Value

class AssignmentStatement(
    val target: Accessible,
    val expression: Node,
    val operation: BinaryExpression.Operator? = null
): Statement {

    override fun eval(): Value {
        if (operation == null) {
            return target.set(expression.eval())
        }

        val value = BinaryExpression(operation, target, expression)
        return target.set(value.eval())
    }
}
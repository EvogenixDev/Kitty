package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler

class AssignmentStatement(
    val variable: Accessible,
    val expression: Expression,
    val operation: String? = null
): Statement {

    override fun execute() {
        if (operation == null) {
            variable.set(expression.eval())
            return
        }

        val value = BinaryExpression(operation, variable, expression)
        variable.set(value.eval())
    }
}
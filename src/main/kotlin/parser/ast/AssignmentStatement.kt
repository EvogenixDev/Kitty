package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler

class AssignmentStatement(
    val variable: String,
    val expression: Expression
): Statement {

    override fun execute() {
        ScopeHandler.setVariable(variable, expression.eval())
    }
}
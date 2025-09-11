package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler

class IfStatement(
    val condition: Expression,
    val ifStatement: Statement,
    val elseStatement: Statement? = null
): Statement {

    override fun execute() {
        if (condition.eval().asInt() == 1) {
            ScopeHandler.push()
            ifStatement.execute()
            ScopeHandler.pop()
            return
        }

        elseStatement?.let {
            ScopeHandler.push()
            it.execute()
            ScopeHandler.pop()
        }
    }
}
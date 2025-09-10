package ru.kitty.parser.ast

class IfStatement(
    val condition: Expression,
    val ifStatement: Statement,
    val elseStatement: Statement? = null
): Statement {

    override fun execute() {
        if (condition.eval().asInt() == 1) {
            ifStatement.execute()
            return
        }

        elseStatement?.execute()
    }
}
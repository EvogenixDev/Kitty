package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler

class WhileStatement(
    val condition: Expression,
    val statement: Statement
): Statement {

    override fun execute() {
        ScopeHandler.push()

        while (condition.eval().asInt() == 1) {
            try {
                statement.execute()
            }
            catch (e: BreakStatement) {
                break
            }
            catch (e: ContinueStatement) {
                continue
            }
        }

        ScopeHandler.pop()
    }
}
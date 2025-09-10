package ru.kitty.parser.ast

class WhileStatement(
    val condition: Expression,
    val statement: Statement
): Statement {

    override fun execute() {
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
    }
}
package ru.kitty.parser.ast

class ForStatement(
    val initialization: Statement,
    val condition: Expression,
    val increment: Statement,
    val statement: Statement
): Statement {

    override fun execute() {
        initialization.execute()

        while (condition.eval().asInt() == 1) {
            try {
                statement.execute()
            }
            catch (e: BreakStatement) {
                break
            }
            catch (e: ContinueStatement) {

            }

            increment.execute()
        }
    }
}
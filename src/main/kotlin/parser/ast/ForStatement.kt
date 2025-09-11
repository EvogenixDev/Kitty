package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class ForStatement(
    val initialization: Node,
    val condition: Node,
    val increment: Node,
    val statement: Node
): Statement {

    override fun eval(): Value {
        ScopeHandler.push()
        initialization.eval()

        while (condition.eval().asInt() == 1) {
            try {
                statement.eval()
            }
            catch (e: BreakStatement) {
                break
            }
            catch (e: ContinueStatement) {

            }

            increment.eval()
        }

        ScopeHandler.pop()
        return NumberValue.ZERO
    }
}
package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class WhileStatement(
    val condition: Node,
    val statement: Node
): Statement {

    override fun eval(): Value {
        ScopeHandler.push()

        while (condition.eval().asInt() == 1) {
            try {
                statement.eval()
            }
            catch (e: BreakStatement) {
                break
            }
            catch (e: ContinueStatement) {
                continue
            }
        }

        ScopeHandler.pop()
        return NumberValue.ZERO
    }
}
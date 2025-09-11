package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class IfStatement(
    val condition: Node,
    val ifStatement: Node,
    val elseStatement: Node? = null
): Statement {

    override fun eval(): Value {
        when (condition.eval().asInt() == 1) {
            true -> {
                ScopeHandler.push()
                ifStatement.eval()
                ScopeHandler.pop()
            }
            false -> {
                elseStatement?.let {
                    ScopeHandler.push()
                    it.eval()
                    ScopeHandler.pop()
                }
            }
        }

        return NumberValue.ZERO
    }
}
package ru.kitty.parser.ast

import ru.kitty.lang.Value

class TernaryExpression(
    val condition: Node,
    val trueExpression: Node,
    val falseExpression: Node
): Node {

    override fun eval(): Value {
        return when(condition.eval().asInt() == 1) {
            true -> trueExpression.eval()
            false -> falseExpression.eval()
        }
    }
}
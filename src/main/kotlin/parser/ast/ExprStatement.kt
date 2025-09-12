package ru.kitty.parser.ast

import ru.kitty.lang.Value

class ExprStatement(val expression: Node): Statement {

    override fun eval(): Value {
        return expression.eval()
    }
}
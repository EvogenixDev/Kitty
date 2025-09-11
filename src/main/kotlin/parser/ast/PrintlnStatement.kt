package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.Value

class PrintlnStatement(val expression: Node): Statement {

    override fun eval(): Value {
        println(expression.eval())
        return NumberValue.ZERO
    }
}
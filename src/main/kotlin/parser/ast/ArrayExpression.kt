package ru.kitty.parser.ast

import ru.kitty.lang.ArrayValue
import ru.kitty.lang.Value

class ArrayExpression: Expression {
    private val expressions =mutableListOf<Expression>()

    fun add(expression: Expression) {
        expressions.add(expression)
    }

    override fun eval(): Value {
        val array = ArrayValue()
        expressions.forEach {
            array.add(it.eval())
        }

        return array
    }
}
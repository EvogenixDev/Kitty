package ru.kitty.parser.ast

import ru.kitty.lang.ArrayValue
import ru.kitty.lang.Value

class ArrayExpression: Node {
    private val expressions = mutableListOf<Node>()

    fun add(expression: Node) {
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
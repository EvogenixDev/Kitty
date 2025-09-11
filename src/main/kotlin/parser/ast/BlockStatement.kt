package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.Value

class BlockStatement(): Statement {
    val statements = mutableListOf<Node>()

    fun add(statement: Node) {
        statements.add(statement)
    }

    override fun eval(): Value {
        for (i in 0..<statements.size) {
            statements[i].eval()
        }

        return NumberValue.ZERO
    }
}
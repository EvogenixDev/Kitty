package ru.kitty.parser.ast

class BlockStatement(): Statement {
    val statements = mutableListOf<Statement>()

    fun add(statement: Statement) {
        statements.add(statement)
    }

    override fun execute() {
        for (i in 0..<statements.size) {
            statements[i].execute()
        }
    }
}
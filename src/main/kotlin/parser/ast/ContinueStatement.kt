package ru.kitty.parser.ast

class ContinueStatement: Statement, RuntimeException() {

    override fun execute() {
        throw this
    }
}
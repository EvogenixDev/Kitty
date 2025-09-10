package ru.kitty.parser.ast

class BreakStatement: Statement, RuntimeException() {

    override fun execute() {
        throw this
    }
}
package ru.kitty.parser.ast

import ru.kitty.lang.Value

class BreakStatement: Statement, RuntimeException() {

    override fun eval(): Value {
        throw this
    }
}
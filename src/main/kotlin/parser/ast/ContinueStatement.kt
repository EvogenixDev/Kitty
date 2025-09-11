package ru.kitty.parser.ast

import ru.kitty.lang.Value

class ContinueStatement: Statement, RuntimeException() {

    override fun eval(): Value {
        throw this
    }
}
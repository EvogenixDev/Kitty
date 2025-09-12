package ru.kitty.parser.ast

import ru.kitty.lang.Value

class ReturnStatement(val result: Node?): Statement, RuntimeException() {

    override fun eval(): Value {
        throw this
    }
}
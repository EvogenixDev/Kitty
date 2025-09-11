package ru.kitty.parser.ast

import ru.kitty.lang.Value

interface Accessible : Expression {

    fun set(value: Value): Value

    fun get(): Value
}
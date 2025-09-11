package ru.kitty.parser.ast

import ru.kitty.lang.Value

interface Accessible : Node {

    fun set(value: Value): Value

    fun get(): Value
}
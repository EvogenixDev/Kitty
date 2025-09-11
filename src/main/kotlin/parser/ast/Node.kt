package ru.kitty.parser.ast

import ru.kitty.lang.Value

interface Node {
    fun eval(): Value
}
package ru.kitty.parser.ast

import ru.kitty.lang.Value

interface Expression {
    fun eval(): Value
}
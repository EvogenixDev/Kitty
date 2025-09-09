package ru.kitty.parser.ast

class NumberExpression(val value: Int): Expression {

    override fun eval(): Int {
        return value
    }
}
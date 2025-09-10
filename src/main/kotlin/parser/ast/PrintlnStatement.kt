package ru.kitty.parser.ast

class PrintlnStatement(val expression: Expression): Statement {

    override fun execute() {
        println(expression.eval())
    }
}
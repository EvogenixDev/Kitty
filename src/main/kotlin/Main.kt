package ru.kitty

import ru.kitty.parser.Lexer
import ru.kitty.parser.Parser

fun main() {
    val code = "10 == 10 or 5 < 2"
    val tokens = Lexer(code).tokenize()
    val expressions = Parser(tokens).parse()
    expressions.forEach { println(it.eval()) }
}
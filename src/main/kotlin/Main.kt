package ru.kitty

import ru.kitty.parser.KittyParser
import ru.kitty.parser.Lexer
import java.nio.file.Files
import kotlin.io.path.Path

fun main() {
    val code = Files.readString(Path("program.kitty"))
    val tokens = Lexer(code).tokenize()
//    val program = KittyParser.create(tokens).parse()
//    program.eval()
    tokens.forEach { println(it) }
}
package ru.kitty

import ru.kitty.parser.Lexer
import ru.kitty.parser.Parser
import java.nio.file.Files
import kotlin.io.path.Path

fun main() {
    val code = Files.readString(Path("program.kitty"))
    val tokens = Lexer(code).tokenize()
    val statement = Parser(tokens).parse()
    statement.eval()
}
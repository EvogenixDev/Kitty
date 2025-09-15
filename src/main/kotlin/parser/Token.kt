package ru.kitty.parser

import ru.kitty.parser.util.Pos

data class Token(
    val text: String,
    val type: Type,
    val pos: Pos
) {

    override fun toString(): String {
        return "$type $text, [$pos]"
    }
}
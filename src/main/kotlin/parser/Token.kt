package ru.kitty.parser

data class Token(
    val text: String,
    val type: Type,
    val row: Int,
    val col: Int
) {

    override fun toString(): String {
        return "$type $text, [$row, $col]"
    }
}
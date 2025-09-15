package ru.kitty.parser.util

data class Pos(
    val row: Int,
    val col: Int
) {
    override fun toString(): String {
        return "[$row, $col]"
    }
}
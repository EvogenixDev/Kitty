package ru.kitty.parser.util

data class Range(val start: Pos, val end: Pos) {
    override fun toString(): String {
        return "$start ... $end"
    }
}
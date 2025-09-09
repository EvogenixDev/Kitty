package ru.kitty.parser

data class Token(val text: String, val type: Type) {

    override fun toString(): String {
        return "$type $text"
    }
}
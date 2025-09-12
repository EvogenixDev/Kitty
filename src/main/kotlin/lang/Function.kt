package ru.kitty.lang

fun interface Function {
    fun execute(vararg args: Value): Value
}
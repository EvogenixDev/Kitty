package ru.kitty.lang

interface Value {

    fun asInt(): Int

    fun asDouble(): Double

    fun asString(): String
}
package ru.kitty.lang

class StringValue(private val value: String): Value {

    override fun asInt(): Int {
        return value.toInt()
    }

    override fun asDouble(): Double {
        return value.toDouble()
    }

    override fun asString(): String {
        return value
    }

    override fun toString(): String {
        return asString()
    }
}
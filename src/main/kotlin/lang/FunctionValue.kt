package ru.kitty.lang

class FunctionValue(val value: Function): Value {

    override fun asInt(): Int {
        throw RuntimeException("Cannot cast function to int")
    }

    override fun asDouble(): Double {
        throw RuntimeException("Cannot cast function to double")
    }

    override fun asString(): String {
        return value.toString()
    }
}
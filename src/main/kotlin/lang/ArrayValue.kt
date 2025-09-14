package ru.kitty.lang

class ArrayValue: Value {
    private val elements = mutableListOf<Value>()

    fun add(element: Value) {
        elements.add(element)
    }

    fun set(index: Int, value: Value) {
        elements[index] = value
    }

    fun get(index: Value): Value {
        return when(index.asString()) {
            "size" -> NumberValue.of(size())
            "isEmpty" -> NumberValue.fromBoolean(size() == 0)
            else -> elements[index.asInt()]
        }
    }

    fun size(): Int {
        return elements.size
    }

    override fun asInt(): Int {
        throw RuntimeException("Cannot convert array to int")
    }

    override fun asDouble(): Double {
        throw RuntimeException("Cannot convert array to double")
    }

    override fun asString(): String {
        return elements.toString()
    }

    override fun toString(): String {
        return asString()
    }
}
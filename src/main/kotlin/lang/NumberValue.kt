package ru.kitty.lang

class NumberValue(private val value: Number): Value {

    companion object {
        val ZERO = of(0)
        val ONE = of(1)

        fun of(num: Int): NumberValue {
            return NumberValue(num)
        }

        fun of(num: Float): NumberValue {
            return NumberValue(num)
        }

        fun fromBoolean(bool: Boolean): NumberValue {
            return if (bool) ONE else ZERO
        }
    }

    fun raw(): Number {
        return value
    }

    override fun asInt(): Int {
        return value.toInt()
    }

    override fun asDouble(): Double {
        return value.toDouble()
    }

    override fun asString(): String {
        return value.toString()
    }

    override fun toString(): String {
        return asString()
    }
}
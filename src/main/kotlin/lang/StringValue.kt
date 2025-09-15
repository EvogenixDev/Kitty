package ru.kitty.lang

class StringValue(private val value: String): Value {

    fun access(propertyValue: Value): Value {
        return when (val property = propertyValue.asString()) {
            //properties
            "size" -> NumberValue.of(value.length)

            //functions
            "upper" -> Converters.voidToString(value::uppercase)
            "lower" -> Converters.voidToString(value::lowercase)
            "reversed" -> Converters.voidToString(value::reversed)
            "trim" -> Converters.voidToString(value::trim)
            "substring" -> FunctionValue { args ->
                StringValue(value.substring(args[0].asInt(), args[1].asInt()))
            }

            "contains" -> Converters.stringToBoolean(value::contains)
            "isEmpty" -> Converters.voidToBoolean(value::isEmpty)
            "isNotEmpty" -> Converters.voidToBoolean(value::isNotEmpty)
            else -> throw RuntimeException("Unknown property $property")
        }
    }

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

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is StringValue) return false
        return asString() == other.asString()
    }
}
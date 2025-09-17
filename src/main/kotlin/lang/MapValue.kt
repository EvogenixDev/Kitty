package ru.kitty.lang

class MapValue: Value {
    private val map = mutableMapOf<Value, Value>()

    fun set(key: Value, value: Value) {
        map[key] = value
    }

    fun set(key: String, value: Value) {
        set(StringValue(key), value)
    }

    fun set(key: String, function: Function) {
        set(StringValue(key), FunctionValue(function))
    }

    fun get(index: Value): Value {
        return when(index.asString()) {
            "size" -> NumberValue.of(map.size)
            "isEmpty" -> NumberValue.fromBoolean(map.isEmpty())
            "set" -> FunctionValue { args ->
                map[args[0]] = args[1]
                NumberValue.ZERO
            }
            "get" -> FunctionValue { args ->
                map[args[0]]!!
            }
            "remove" -> FunctionValue { args ->
                map.remove(args[0])
                NumberValue.ZERO
            }
            "hasKey" -> FunctionValue { args ->
                NumberValue.fromBoolean(map.containsKey(args[0]))
            }
            else -> {
                map[index]!!
            }
        }
    }

    fun raw(): Map<Value, Value> {
        return map
    }

    override fun asInt(): Int {
        throw RuntimeException("Cannot cast map to int")
    }

    override fun asDouble(): Double {
        throw RuntimeException("Cannot cast map to double")
    }

    override fun asString(): String {
        return map.toString()
    }

    override fun toString(): String {
        return asString()
    }
}
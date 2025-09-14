package ru.kitty.lang

object Converters {

    fun stringToBoolean(str: (String) -> Boolean): FunctionValue {
        return FunctionValue { args ->
            NumberValue.fromBoolean(str(args[0].asString()))
        }
    }

    fun voidToString(void: () -> String): FunctionValue {
        return FunctionValue { args ->
            StringValue(void())
        }
    }

    fun voidToBoolean(void: () -> Boolean): FunctionValue {
        return FunctionValue { args ->
            NumberValue.fromBoolean(void())
        }
    }
}
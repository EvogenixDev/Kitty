package ru.kitty.lang

import kotlin.math.cos
import kotlin.math.sin

class RootScope(override val parent: Scope?): Scope(parent) {
    private val functions = mutableMapOf<String, Function>(
        "println" to Function { args ->
            args.forEach { print(it) }
            println()
            NumberValue.ZERO
        },

        "cos" to Function { args ->
            val num = (args[0] as NumberValue).raw()
            NumberValue.of(cos(num.toDouble()))
        },

        "sin" to Function { args ->
            val num = (args[0] as NumberValue).raw()
            NumberValue.of(sin(num.toDouble()))
        }
    )

    override fun hasParent(): Boolean {
        return false
    }

    override fun isRoot(): Boolean {
        return true
    }

    fun setFunction(name: String, function: Function) {
        functions[name] = function
    }

    fun getFunction(name: String): Function {
        return functions[name]!!
    }
}
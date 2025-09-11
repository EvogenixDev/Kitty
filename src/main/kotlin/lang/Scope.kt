package ru.kitty.lang

open class Scope(open val parent: Scope?) {
    private val variables = mutableMapOf<String, Value>()

    open fun hasParent(): Boolean {
        return parent != null
    }

    open fun isRoot(): Boolean {
        return !hasParent()
    }

    fun setVariable(name: String, value: Value) {
        variables[name] = value
    }

    fun getVariable(name: String): Value {
        return variables[name]!!
    }

    fun hasVariable(name: String): Boolean {
        return variables.containsKey(name)
    }

    fun variables(): Map<String, Value> {
        return variables
    }

    data class FindData(
        val scope: Scope,
        val isFound: Boolean
    )
}
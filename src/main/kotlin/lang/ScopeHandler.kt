package ru.kitty.lang

object ScopeHandler {
    private val rootScope = RootScope(null)
    private var scope: Scope = rootScope

    init {
        scope = rootScope
    }

    fun push() {
        scope = Scope(scope)
    }

    fun pop() {
        scope = scope.parent?: rootScope
    }

    fun hasVariable(name: String): Boolean {
        return rootScope.hasVariable(name)
    }

    fun declareVariableInCurrentScope(name: String, value: Value) {
        scope.setVariable(name, value)
    }

    fun setVariable(name: String, value: Value) {
        val data = findScope(name)
        if (data.isFound) {
            data.scope.setVariable(name, value)
            return
        }

        throw RuntimeException("Variable $name is not declared in current scope")
    }

    fun getVariable(name: String): Value {
        val data = findScope(name)
        if (data.isFound) {
            return data.scope.getVariable(name)
        }

        throw RuntimeException("Variable $name is not exists")
    }

    private fun findScope(name: String): Scope.FindData {
        var current: Scope = scope
        do {
            if (current.hasVariable(name)) {
                return Scope.FindData(current, true)
            }

            current = current.parent?: rootScope
        }
        while (current.parent != null)

        return Scope.FindData(scope, false)
    }
}
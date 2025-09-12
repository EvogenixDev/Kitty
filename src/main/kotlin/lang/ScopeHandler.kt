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

    fun setFunction(name: String, function: Function) {
        rootScope.setFunction(name, function)
    }

    fun getFunction(name: String): Function {
        return rootScope.getFunction(name)
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
        return findScope(name, scope)
    }

    private fun findScope(name: String, s: Scope): Scope.FindData {
        if (s.hasVariable(name)) {
            return Scope.FindData(s, true)
        }

        if (s.hasParent()) {
            return findScope(name, s.parent!!)
        }

        return Scope.FindData(s, false)
    }
}
package ru.kitty.lang

class ClassMethod(
    val name: String,
    val function: Function,
    val classInstance: ClassInstance?
): Function {

    override fun execute(vararg args: Value): Value {
        try {
            ScopeHandler.push()
            if (classInstance != null) {
                ScopeHandler.declareVariableInCurrentScope("self", classInstance.map)
                classInstance.map.raw().forEach {
                    ScopeHandler.declareVariableInCurrentScope(it.key.asString(), it.value)
                }
            }

            return function.execute(*args)
        }
        finally {
            ScopeHandler.pop()
        }
    }
}
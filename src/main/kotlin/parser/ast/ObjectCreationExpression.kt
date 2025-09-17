package ru.kitty.parser.ast

import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.Value

class ObjectCreationExpression(
    val className: String,
): Node {

    override fun eval(): Value {
        val clazz = ScopeHandler.getClassDeclaration(className)
        return clazz.newInstance()
    }
}
package ru.kitty.parser.ast

import ru.kitty.lang.ClassField
import ru.kitty.lang.ClassMethod
import ru.kitty.lang.NumberValue
import ru.kitty.lang.UserDefinedFunction
import ru.kitty.lang.Value

class ClassDeclarationStatement(val className: String): Statement {
    val fields = mutableListOf<VariableDeclarationStatement>()
    val methods = mutableListOf<FunctionDefineStatement>()

    fun addField(field: VariableDeclarationStatement) {
        fields.add(field)
    }

    fun addMethod(method: FunctionDefineStatement) {
        methods.add(method)
    }

    override fun eval(): Value {
        val classFields = fields.map {
            ClassField(it.name)
        }

        val classMethods = methods.map {
            ClassMethod(it.name, UserDefinedFunction(it.argNames, it.statement))
        }

        return NumberValue.ZERO
    }
}
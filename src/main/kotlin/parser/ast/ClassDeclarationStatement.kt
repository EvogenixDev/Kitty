package ru.kitty.parser.ast

import ru.kitty.lang.ClassDeclaration
import ru.kitty.lang.ClassField
import ru.kitty.lang.ClassMethod
import ru.kitty.lang.NumberValue
import ru.kitty.lang.ScopeHandler
import ru.kitty.lang.UserDefinedFunction
import ru.kitty.lang.Value

class ClassDeclarationStatement(
    val name: String,
    val fields: List<VariableDeclarationStatement>,
    val methods: List<FunctionDefineStatement>
): Statement {

    override fun eval(): Value {
        val classFields = mutableListOf<ClassField>()
        fields.forEach { classFields.add(ClassField(it.name, it.expression)) }

        val classMethods = mutableListOf<ClassMethod>()
        methods.forEach {
            classMethods.add(ClassMethod(it.name, UserDefinedFunction(it.argNames, it.statement), null))
        }

        val clazz = ClassDeclaration(name, classFields, classMethods)
        ScopeHandler.setClassDeclaration(clazz)
        return NumberValue.ZERO
    }
}
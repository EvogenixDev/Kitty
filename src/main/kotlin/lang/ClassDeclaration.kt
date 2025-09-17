package ru.kitty.lang

class ClassDeclaration(
    val name: String,
    val classFields: List<ClassField>,
    val classMethods: List<ClassMethod>
) {

    fun newInstance(vararg args: Value): ClassInstance {
        val instance = ClassInstance()
        classFields.forEach {
            instance.addField(it)
        }

        classMethods.forEach {
            instance.addMethod(it)
        }

        return instance
    }
}
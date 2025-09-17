package ru.kitty.lang

class ClassDeclaration(
    val name: String,
    val classFields: List<ClassField>
) {

    fun newInstance(vararg args: Value): ClassInstance {
        val instance = ClassInstance()
        classFields.forEach {
            instance.addField(it)
        }

        return instance
    }
}
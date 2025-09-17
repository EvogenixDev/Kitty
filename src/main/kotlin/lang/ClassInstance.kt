package ru.kitty.lang

import ru.kitty.parser.ast.Accessible

class ClassInstance: Value {
    val map = MapValue()

    fun addField(field: ClassField) {
        map.set(field.name, field.expr.eval())
    }

    fun addMethod(method: ClassMethod) {
        val m = ClassMethod(method.name, method.function, this)
        map.set(m.name, m)
    }

    fun access(property: Value): Value {
        return map.get(property)
    }

    fun set(key: Value, value: Value) {
        map.set(key, value)
    }
//    public void set(Value key, Value value) {
//        final Value v = thisMap.get(key);
//        if (v == null) {
//            throw new OwnLangRuntimeException(
//                    "Unable to add new field %s to class %s"
//                        .formatted(key.asString(), className));
//        }
//        thisMap.set(key, value);
//    }
    override fun asInt(): Int {
        TODO("Not yet implemented")
    }

    override fun asDouble(): Double {
        TODO("Not yet implemented")
    }

    override fun asString(): String {
        TODO("Not yet implemented")
    }
}
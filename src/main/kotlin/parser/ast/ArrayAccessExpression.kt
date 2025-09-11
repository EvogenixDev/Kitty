package ru.kitty.parser.ast

import ru.kitty.lang.ArrayValue
import ru.kitty.lang.Value

class ArrayAccessExpression(
    val target: Node,
    val indices: List<Node>
): Node, Accessible {

    override fun eval(): Value {
        return get()
    }

    override fun set(value: Value): Value {
        val array = getArray()
        array.set(lastIndex(), value)
        return value
    }

    override fun get(): Value {
        return getArray().get(lastIndex())
    }

    private fun getArray(): ArrayValue {
        var array = consumeArray(target.eval())
        val size = indices.size-1

        for (i in 0..<size) {
            array = consumeArray(array.get(index(i)))
        }

        return array
    }

    private fun consumeArray(value: Value): ArrayValue {
        if (value !is ArrayValue) {
            throw RuntimeException("Array expected")
        }

        return value
    }

    private fun index(index: Int): Int {
        return indices[index].eval().asInt()
    }

    private fun lastIndex(): Int {
        return index(indices.size-1)
    }
}
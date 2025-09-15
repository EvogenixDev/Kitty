package ru.kitty.parser.ast

import ru.kitty.lang.ArrayValue
import ru.kitty.lang.MapValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class ContainerAccessExpression(
    val target: Node,
    val indices: List<Node>
): Node, Accessible {

    override fun eval(): Value {
        return get()
    }

    override fun set(value: Value): Value {
        val container = target.eval()

        if (container is MapValue) {
            container.set(lastIndex(), value)
            return value
        }

        val array = getArray()
        array.set(lastIndex().asInt(), value)
        return value
    }

    override fun get(): Value {
        val container = target.eval()
        if (container is StringValue) {
            return container.access(lastIndex())
        }

        if (container is MapValue) {
            return container.get(lastIndex())
        }

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

    private fun index(index: Int): Value {
        return indices[index].eval()
    }

    private fun lastIndex(): Value {
        return index(indices.size-1)
    }
}
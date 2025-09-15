package ru.kitty.parser.ast

import ru.kitty.lang.MapValue
import ru.kitty.lang.Value

class MapExpression: Node {
    private val entries = mutableMapOf<Node, Node>()

    fun add(key: Node, value: Node) {
        entries[key] = value
    }

    override fun eval(): Value {
        val map = MapValue()
        entries.forEach { (key, value) ->
            map.set(key.eval(), value.eval())
        }

        return map
    }
}
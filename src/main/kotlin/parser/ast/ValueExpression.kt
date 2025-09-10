package ru.kitty.parser.ast

import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class ValueExpression(private val value: Value): Expression {

    constructor(num: Int) : this(NumberValue(num))

    constructor(num: Float) : this(NumberValue(num))

    constructor(str: String) : this(StringValue(str))

    override fun eval(): Value {
        return value
    }
}
package ru.kitty.parser.ast

import ru.kitty.lang.Function
import ru.kitty.lang.FunctionValue
import ru.kitty.lang.NumberValue
import ru.kitty.lang.StringValue
import ru.kitty.lang.Value

class ValueExpression(val value: Value): Node {

    constructor(num: Int) : this(NumberValue(num))

    constructor(num: Double) : this(NumberValue(num))

    constructor(str: String) : this(StringValue(str))

    constructor(func: Function) : this(FunctionValue(func))

    override fun eval(): Value {
        return value
    }
}
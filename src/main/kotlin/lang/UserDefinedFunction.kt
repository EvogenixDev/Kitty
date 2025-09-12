package ru.kitty.lang

import ru.kitty.parser.ast.ReturnStatement
import ru.kitty.parser.ast.Statement

class UserDefinedFunction(
    private val argNames: List<String>,
    private val statement: Statement
): Function {

    fun argsCount(): Int {
        return argNames.size
    }

    fun argName(index: Int): String {
        return argNames[index]
    }

    override fun execute(vararg args: Value): Value {
        try {
            statement.eval()
            return NumberValue.ZERO
        }
        catch (e: ReturnStatement) {
            return e.result?.eval()?: NumberValue.ZERO
        }
    }
}
package ru.kitty.parser

import ru.kitty.parser.ast.Expression
import ru.kitty.parser.ast.NumberExpression

class Parser(private val tokens: List<Token>) {
    private val size = tokens.size
    private var position = 0


    companion object {
        private val EOF = Token("", Type.EOF)
    }


    fun parse(): List<Expression> {
        val result = mutableListOf<Expression>()
        while (!match(Type.EOF)) {
            result.add(parseExpression())
        }

        return result
    }


    private fun parseExpression(): Expression {
        return parsePrimary()
    }


    private fun parsePrimary(): Expression {
        if (lookMatch(0, Type.NUMBER)) {
            return NumberExpression(consume(Type.NUMBER).text.toInt())
        }

        throw RuntimeException("Unknown expression ${get(0)}")
    }


    private fun lookMatch(offset: Int, type: Type): Boolean {
        return get(offset).type == type
    }


    private fun match(type: Type): Boolean {
        if (get(0).type == type) {
            position++
            return true
        }

        return false
    }


    private fun consume(type: Type): Token {
        val token = get(0)
        if (token.type == type) {
            position++
            return token
        }

        throw RuntimeException("Unexpected token $token")
    }


    private fun get(relativePos: Int): Token {
        val nextPos = position + relativePos
        if (nextPos >= size)
            return EOF

        return tokens[nextPos]
    }
}
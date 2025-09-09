package ru.kitty.parser

class Lexer(private val input: String) {
    private val length = input.length
    private val tokens = mutableListOf<Token>()
    private var position = 0


    companion object {
        private const val OPERATOR_CHARS = "+-*/()"
        private val OPERATORS = mapOf<String, Type>(
            "+" to Type.PLUS,
            "-" to Type.MINUS,
            "*" to Type.STAR,
            "/" to Type.SLASH,
            "(" to Type.L_PAREN,
            ")" to Type.R_PAREN,
        )
    }


    fun tokenize(): List<Token> {
        while (position < length) {
            val current = peekChar(0)
            when {
                isNumber(current) -> tokenizeNumber()
                isOperator(current) -> tokenizeOperator()
                else -> skipChar()
            }
        }

        return tokens
    }


    private fun tokenizeNumber() {
        var current = peekChar(0)
        val buffer = StringBuilder()

        while (true) {
            if (!isNumber(current)) {
                break
            }

            buffer.append(current)
            current = nextChar()
        }

        addToken(Type.NUMBER, buffer.toString())
    }


    private fun tokenizeOperator() {
        var current = peekChar(0)
        val buffer = StringBuilder()

        while (true) {
            val text = buffer.toString()
            if (text.isNotEmpty() && !OPERATORS.containsKey(text + current)) {
                addToken(OPERATORS[text]!!)
                break
            }

            buffer.append(current)
            current = nextChar()
        }
    }


    private fun isNumber(char: Char): Boolean {
        return char in '0'..'9'
    }


    private fun isOperator(char: Char): Boolean {
        return char in OPERATOR_CHARS
    }


    private fun nextChar(): Char {
        skipChar()
        return peekChar(0)
    }


    private fun skipChar() {
        position++
    }


    private fun peekChar(relativePos: Int): Char {
        val nextPos = position + relativePos
        if (nextPos >= length)
            return '\u0000'

        return input[nextPos]
    }


    private fun addToken(type: Type) {
        addToken(type, "")
    }


    private fun addToken(type: Type, text: String) {
        tokens.add(Token(text, type))
    }
}
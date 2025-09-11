package ru.kitty.parser

class Lexer(private val input: String) {
    private val length = input.length
    private val tokens = mutableListOf<Token>()
    private var position = 0


    companion object {
        private const val OPERATOR_CHARS = "+-*/<>!=(){},"
        private val OPERATORS = mapOf<String, Type>(
            "+" to Type.PLUS,
            "-" to Type.MINUS,
            "*" to Type.STAR,
            "/" to Type.SLASH,

            "=" to Type.EQ,
            "+=" to Type.PLUS_EQ,
            "-=" to Type.MINUS_EQ,
            "*=" to Type.STAR_EQ,
            "/=" to Type.SLASH_EQ,
            "++" to Type.PLUS_PLUS,
            "--" to Type.MINUS_MINUS,

            "(" to Type.L_PAREN,
            ")" to Type.R_PAREN,
            "{" to Type.L_BRACE,
            "}" to Type.R_BRACE,

            "<" to Type.LT,
            ">" to Type.GT,
            "<=" to Type.LT_EQ,
            ">=" to Type.GT_EQ,
            "==" to Type.EQ_EQ,

            "," to Type.COMMA,
        )

        private val KEYWORDS = mapOf<String, Type>(
            "and" to Type.AND,
            "or" to Type.OR,
            "if" to Type.IF,
            "else" to Type.ELSE,
            "echo" to Type.ECHO,
            "var" to Type.VAR,
            "for" to Type.FOR,
            "while" to Type.WHILE,
            "break" to Type.BREAK,
            "continue" to Type.CONTINUE,
        )
    }


    fun tokenize(): List<Token> {
        while (position < length) {
            val current = peekChar(0)
            when {
                isNumber(current) -> tokenizeNumber()
                isOperator(current) -> tokenizeOperator()
                current.isLetter() || current == '_' -> tokenizeWord()
                current == '"' -> tokenizeText()
                else -> skipChar()
            }
        }

        return tokens
    }


    private fun tokenizeNumber() {
        var current = peekChar(0)
        val buffer = StringBuilder()
        var hasDot = false

        while (true) {
            if (current == '.') {
                if (hasDot) {
                    throw RuntimeException("Invalid double number. Multiple dots")
                }

                hasDot = true
                buffer.append(current)
                current = nextChar()
                continue
            }

            if (!isNumber(current)) {
                break
            }

            buffer.append(current)
            current = nextChar()
        }

        val text = buffer.toString()
        if (text.endsWith(".")) {
            throw RuntimeException("Invalid double number. Ends with dot")
        }

        addToken(if (hasDot) Type.NUMBER_DECIMAL else Type.NUMBER, text)
    }


    private fun tokenizeWord() {
        var current = peekChar(0)
        val buffer = StringBuilder()

        while (current.isLetterOrDigit() || current == '_') {
            buffer.append(current)
            current = nextChar()
        }

        val text = buffer.toString()
        if (isKeyword(text)) {
            addToken(KEYWORDS[text]!!)
        } else {
            addToken(Type.WORD, text)
        }
    }


    private fun tokenizeText() {
        skipChar()

        var current = peekChar(0)
        val buffer = StringBuilder()

        while (true) {
            if (current == '\\') {
                current = nextChar()

                when {
                    current == '"' -> {
                        buffer.append('"')
                        current = nextChar()
                        continue
                    }

                    current == 'n' -> {
                        buffer.append('\n')
                        current = nextChar()
                        continue
                    }
                }
            }

            if (current == '"') {
                break
            }

            buffer.append(current)
            current = nextChar()
        }

        skipChar()
        addToken(Type.TEXT, buffer.toString())
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


    private fun isKeyword(text: String): Boolean {
        return text in KEYWORDS
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
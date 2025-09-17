package ru.kitty.parser

import ru.kitty.parser.exception.ParseException
import ru.kitty.parser.util.Pos
import kotlin.math.max

class Lexer(private val input: String) {
    private val length = input.length
    private val tokens = mutableListOf<Token>()

    //позиция символа
    private var position = 0
    //строка
    private var row = 1
    //столбец
    private var col = 1

    companion object {
        private const val OPERATOR_CHARS = "+-*/<>!=(){}[],.?:#"
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
            "[" to Type.L_BRACKET,
            "]" to Type.R_BRACKET,

            "<" to Type.LT,
            ">" to Type.GT,
            "<=" to Type.LT_EQ,
            ">=" to Type.GT_EQ,
            "==" to Type.EQ_EQ,
            "!" to Type.NOT,

            "," to Type.COMMA,
            "." to Type.DOT,
            "?" to Type.QUESTION,
            ":" to Type.COLON
        )

        private val KEYWORDS = mapOf<String, Type>(
            "and" to Type.AND,
            "or" to Type.OR,
            "not" to Type.NOT,
            "if" to Type.IF,
            "else" to Type.ELSE,
            "meow" to Type.ECHO,
            "var" to Type.VAR,
            "for" to Type.FOR,
            "while" to Type.WHILE,
            "break" to Type.BREAK,
            "continue" to Type.CONTINUE,
            "return" to Type.RETURN,
            "func" to Type.FUNC,
            "class" to Type.CLASS,
            "new" to Type.NEW,
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
        val startPos = markStartPos()

        while (true) {
            if (current == '.') {
                if (hasDot) {
                    throw error("Syntax error at line $row: Invalid number")
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
            throw error("Syntax error at line $row: Invalid number")
        }

        addToken(if (hasDot) Type.NUMBER_DECIMAL else Type.NUMBER, text, startPos)
    }


    private fun tokenizeWord() {
        var current = peekChar(0)
        val buffer = StringBuilder()
        val startPos = markStartPos()

        while (current.isLetterOrDigit() || current == '_') {
            buffer.append(current)
            current = nextChar()
        }

        val text = buffer.toString()
        if (isKeyword(text)) {
            addToken(KEYWORDS[text]!!, startPos)
        } else {
            addToken(Type.WORD, text, startPos)
        }
    }


    private fun tokenizeText() {
        val startPos = markStartPos()
        skipChar()

        var current = peekChar(0)
        val buffer = StringBuilder()

        while (true) {
            if (current == '\\') {
                current = nextChar()

                when (current) {
                    '"' -> {
                        buffer.append('"')
                        current = nextChar()
                        continue
                    }
                    'n' -> {
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
        addToken(Type.TEXT, buffer.toString(), startPos)
    }


    private fun tokenizeOperator() {
        var current = peekChar(0)

        if (current == '#') {
            tokenizeComment()
            return
        }

        val buffer = StringBuilder()
        val startPos = markStartPos()

        while (true) {
            val text = buffer.toString()
            if (text.isNotEmpty() && !OPERATORS.containsKey(text + current)) {
                addToken(OPERATORS[text]!!, startPos)
                break
            }

            buffer.append(current)
            current = nextChar()
        }
    }


    private fun tokenizeComment() {
        skipChar()//skip #

        var current = peekChar(0)

        while ("\r\n\u0000".indexOf(current) == -1) {
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
        col++

        if (peekChar(0) == '\n') {
            col = 1
            row++
        }

        position++
    }


    private fun peekChar(relativePos: Int): Char {
        val nextPos = position + relativePos
        if (nextPos >= length)
            return '\u0000'

        return input[nextPos]
    }


    private fun addToken(type: Type, pos: Pos) {
        addToken(type, "", pos)
    }


    private fun addToken(type: Type, text: String, pos: Pos) {
        tokens.add(Token(text, type, pos))
    }


    private fun markStartPos(): Pos {
        return Pos(row, col)
    }


    private fun markEndPos(): Pos {
        return Pos(row, max(0, col - 1))
    }


    private fun error(message: String): ParseException {
        return ParseException(message)
    }
}
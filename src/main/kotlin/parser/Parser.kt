package ru.kitty.parser

import ru.kitty.parser.ast.AssignmentStatement
import ru.kitty.parser.ast.BinaryExpression
import ru.kitty.parser.ast.BlockStatement
import ru.kitty.parser.ast.ConditionalExpression
import ru.kitty.parser.ast.Expression
import ru.kitty.parser.ast.IfStatement
import ru.kitty.parser.ast.PrintlnStatement
import ru.kitty.parser.ast.Statement
import ru.kitty.parser.ast.ValueExpression
import ru.kitty.parser.ast.UnaryExpression
import ru.kitty.parser.ast.VariableDeclarationStatement
import ru.kitty.parser.ast.VariableExpression

class Parser(private val tokens: List<Token>) {
    private val size = tokens.size
    private var position = 0


    companion object {
        private val EOF = Token("", Type.EOF)
    }


    fun parse(): Statement {
        val result = BlockStatement()

        while (!match(Type.EOF)) {
            result.add(parseStatement())
        }

        return result
    }


    private fun parseStatementOrBlock(): Statement {
        if (match(Type.L_BRACE)) {
            val block = BlockStatement()
            while (!lookMatch(0, Type.R_BRACE)) {
                block.add(parseStatement())
            }

            consume(Type.R_BRACE)
            return block
        }

        return parseStatement()
    }


    private fun parseStatement(): Statement {
        if (match(Type.IF)) {
            return parseIfStatement()
        }

        if (match(Type.ECHO)) {
            return PrintlnStatement(parseExpression())
        }

        if (match(Type.VAR)) {
            val name = consume(Type.WORD).text
            consume(Type.EQ)
            return VariableDeclarationStatement(name, parseExpression())
        }

        return parseAssignment()
    }


    private fun parseAssignment(): Statement {
        if (lookMatch(0, Type.WORD)) {
            val variable = consume(Type.WORD).text
            consume(Type.EQ)
            return AssignmentStatement(variable, parseExpression())
        }

        throw RuntimeException("Unknown statement")
    }


    private fun parseIfStatement(): Statement {
        val condition = parseExpression()
        val ifStatement = parseStatementOrBlock()
        val elseStatement = when (match(Type.ELSE)) {
            true -> parseStatementOrBlock()
            false -> null
        }

        return IfStatement(condition, ifStatement, elseStatement)
    }


    private fun parseExpression(): Expression {
        return parseLogicalOr()
    }


    private fun parseLogicalOr(): Expression {
        var result = parseLogicalAnd()

        while (true) {
            if (match(Type.OR)) {
                result = ConditionalExpression("or", result, parseLogicalAnd())
                continue
            }

            break
        }

        return result
    }


    private fun parseLogicalAnd(): Expression {
        var result = parseEquality()

        while (true) {
            if (match(Type.AND)) {
                result = ConditionalExpression("and", result, parseEquality())
                continue
            }

            break
        }

        return result
    }


    private fun parseEquality(): Expression {
        var result = parseCondition()

        while (true) {
            if (match(Type.EQ_EQ)) {
                result = ConditionalExpression("==", result, parseCondition())
                continue
            }

            if (match(Type.NOT_EQ)) {
                result = ConditionalExpression("!=", result, parseCondition())
                continue
            }

            break
        }

        return result
    }


    private fun parseCondition(): Expression {
        var result = parseAddition()

        while (true) {
            if (match(Type.LT)) {
                result = ConditionalExpression("<", result, parseAddition())
                continue
            }

            if (match(Type.GT)) {
                result = ConditionalExpression(">", result, parseAddition())
                continue
            }

            if (match(Type.LT_EQ)) {
                result = ConditionalExpression("<=", result, parseAddition())
                continue
            }

            if (match(Type.GT_EQ)) {
                result = ConditionalExpression(">=", result, parseAddition())
                continue
            }

            break
        }

        return result
    }


    private fun parseAddition(): Expression {
        var result = parseMultiplication()

        while (true) {
            if (match(Type.PLUS)) {
                result = BinaryExpression("+", result, parseMultiplication())
                continue
            }

            if (match(Type.MINUS)) {
                result = BinaryExpression("-", result, parseMultiplication())
                continue
            }

            break
        }

        return result
    }


    private fun parseMultiplication(): Expression {
        var result = parseUnary()

        while (true) {
            if (match(Type.STAR)) {
                result = BinaryExpression("*", result, parseUnary())
                continue
            }

            if (match(Type.SLASH)) {
                result = BinaryExpression("/", result, parseUnary())
                continue
            }

            break
        }

        return result
    }


    private fun parseUnary(): Expression {
        if (match(Type.MINUS)) {
            return UnaryExpression("-", parsePrimary())
        }

        if (match(Type.PLUS)) {
            return parsePrimary()
        }

        return parsePrimary()
    }


    private fun parsePrimary(): Expression {
        if (match(Type.L_PAREN)) {
            val result = parseExpression()
            consume(Type.R_PAREN)
            return result
        }

        if (lookMatch(0, Type.NUMBER)) {
            return ValueExpression(consume(Type.NUMBER).text.toInt())
        }

        if (lookMatch(0, Type.NUMBER_DECIMAL)) {
            return ValueExpression(consume(Type.NUMBER_DECIMAL).text.toDouble())
        }

        if (lookMatch(0, Type.TEXT)) {
            return ValueExpression(consume(Type.TEXT).text)
        }

        if (lookMatch(0, Type.WORD)) {
            return VariableExpression(consume(Type.WORD).text)
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
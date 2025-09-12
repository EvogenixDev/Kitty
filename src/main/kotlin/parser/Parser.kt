package ru.kitty.parser

import ru.kitty.parser.ast.Accessible
import ru.kitty.parser.ast.ArrayAccessExpression
import ru.kitty.parser.ast.ArrayExpression
import ru.kitty.parser.ast.AssignmentStatement
import ru.kitty.parser.ast.BinaryExpression
import ru.kitty.parser.ast.BlockStatement
import ru.kitty.parser.ast.BreakStatement
import ru.kitty.parser.ast.ConditionalExpression
import ru.kitty.parser.ast.ContinueStatement
import ru.kitty.parser.ast.ExprStatement
import ru.kitty.parser.ast.Node
import ru.kitty.parser.ast.ForStatement
import ru.kitty.parser.ast.FunctionDefineStatement
import ru.kitty.parser.ast.FunctionalExpression
import ru.kitty.parser.ast.IfStatement
import ru.kitty.parser.ast.PrintlnStatement
import ru.kitty.parser.ast.ReturnStatement
import ru.kitty.parser.ast.Statement
import ru.kitty.parser.ast.ValueExpression
import ru.kitty.parser.ast.UnaryExpression
import ru.kitty.parser.ast.VariableDeclarationStatement
import ru.kitty.parser.ast.VariableExpression
import ru.kitty.parser.ast.WhileStatement

class Parser(private val tokens: List<Token>) {
    private val size = tokens.size
    private var position = 0


    companion object {
        private val EOF = Token("", Type.EOF)

        private val ASSIGN_OPERATORS = mapOf<Type, BinaryExpression.Operator?>(
            Type.EQ to null,
            Type.PLUS_EQ to BinaryExpression.Operator.ADD,
            Type.MINUS_EQ to BinaryExpression.Operator.SUBTRACT,
            Type.STAR_EQ to BinaryExpression.Operator.MULTIPLY,
            Type.SLASH_EQ to BinaryExpression.Operator.DIVIDE
        )
    }


    fun parse(): Node {
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


//    private fun parseBlock(): Node {
//
//    }


    private fun parseStatement(): Statement {
        if (match(Type.IF)) {
            return parseIfStatement()
        }

        if (match(Type.ECHO)) {
            return PrintlnStatement(parseExpression())
        }

        if (match(Type.FOR)) {
            return parseForStatement()
        }

        if (match(Type.WHILE)) {
            return parseWhileStatement()
        }

        if (match(Type.VAR)) {
            return parseVariableDeclaration()
        }

        if (match(Type.BREAK)) {
            return BreakStatement()
        }

        if (match(Type.CONTINUE)) {
            return ContinueStatement()
        }

        if (match(Type.RETURN)) {
            val expression = try {
                parseExpression()
            }
            catch (e: Exception) {
                null
            }

            return ReturnStatement(expression)
        }

        if (match(Type.FUNC)) {
            return parseFunctionDefine()
        }

        if (lookMatch(0, Type.WORD) && lookMatch(1, Type.L_PAREN)) {
            return ExprStatement(parseFunction())
        }

        return parseAssignment()
    }


    private fun parseAssignment(): AssignmentStatement {
        val targetExpr = parseQualifiedName()
        val currentType = get(0).type

        if (ASSIGN_OPERATORS.containsKey(currentType)) {
            consume(currentType)
            return AssignmentStatement(targetExpr as Accessible, parseExpression(), ASSIGN_OPERATORS[currentType])
        }

        throw RuntimeException("Unknown statement")
    }


    private fun parseVariableDeclaration(): VariableDeclarationStatement {
        val name = consume(Type.WORD).text
        consume(Type.EQ)
        return VariableDeclarationStatement(name, parseExpression())
    }


    private fun parseIfStatement(): IfStatement {
        match(Type.L_PAREN)
        val condition = parseExpression()
        match(Type.R_PAREN)

        val ifStatement = parseStatementOrBlock()
        val elseStatement = when (match(Type.ELSE)) {
            true -> parseStatementOrBlock()
            false -> null
        }

        return IfStatement(condition, ifStatement, elseStatement)
    }


    private fun parseForStatement(): ForStatement {
        match(Type.L_PAREN)
        val initialization = when (match(Type.VAR)) {
            true -> parseVariableDeclaration()
            else -> parseAssignment()
        }

        consume(Type.COMMA)

        val condition = parseExpression()
        consume(Type.COMMA)

        val increment = parseAssignment()
        match(Type.R_PAREN)

        val statement = parseStatementOrBlock()
        return ForStatement(initialization, condition, increment, statement)
    }


    private fun parseWhileStatement(): WhileStatement {
        match(Type.L_PAREN)
        val condition = parseExpression()
        match(Type.R_PAREN)
        val statement = parseStatementOrBlock()
        return WhileStatement(condition, statement)
    }


    private fun parseFunctionDefine(): FunctionDefineStatement {
        val name = consume(Type.WORD).text
        val argNames = mutableListOf<String>()
        consume(Type.L_PAREN)

        while (!lookMatch(0, Type.R_PAREN)) {
            argNames.add(consume(Type.WORD).text)
            match(Type.COMMA)
        }

        consume(Type.R_PAREN)

        val statement = parseStatementOrBlock()
        return FunctionDefineStatement(name, argNames, statement)
    }


    private fun parseArrayExpression(): Node {
        val array = ArrayExpression()
        while (!lookMatch(0, Type.R_BRACKET)) {
            array.add(parseExpression())
            match(Type.COMMA)
        }

        consume(Type.R_BRACKET)
        return array
    }

    private fun parseArrayElement(): Node {
        val target = VariableExpression(consume(Type.WORD).text)
        val indices = mutableListOf<Node>()

        while (match(Type.L_BRACKET)) {
            indices.add(parseExpression())
            consume(Type.R_BRACKET)
        }

        return ArrayAccessExpression(target, indices)
    }


    private fun parseFunction(): FunctionalExpression {
        val name = consume(Type.WORD).text
        consume(Type.L_PAREN)

        val arguments = mutableListOf<Node>()
        while (!lookMatch(0, Type.R_PAREN)) {
            arguments.add(parseExpression())
            match(Type.COMMA)
        }

        consume(Type.R_PAREN)
        return FunctionalExpression(name, arguments)
    }


    private fun parseExpression(): Node {
        return parseLogicalOr()
    }


    private fun parseLogicalOr(): Node {
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


    private fun parseLogicalAnd(): Node {
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


    private fun parseEquality(): Node {
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


    private fun parseCondition(): Node {
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


    private fun parseAddition(): Node {
        var result = parseMultiplication()

        while (true) {
            if (match(Type.PLUS)) {
                result = BinaryExpression(BinaryExpression.Operator.ADD, result, parseMultiplication())
                continue
            }

            if (match(Type.MINUS)) {
                result = BinaryExpression(BinaryExpression.Operator.SUBTRACT, result, parseMultiplication())
                continue
            }

            break
        }

        return result
    }


    private fun parseMultiplication(): Node {
        var result = parseUnary()

        while (true) {
            if (match(Type.STAR)) {
                result = BinaryExpression(BinaryExpression.Operator.MULTIPLY, result, parseUnary())
                continue
            }

            if (match(Type.SLASH)) {
                result = BinaryExpression(BinaryExpression.Operator.DIVIDE, result, parseUnary())
                continue
            }

            break
        }

        return result
    }


    private fun parseUnary(): Node {
        if (match(Type.MINUS)) {
            return UnaryExpression("-", parsePrimary())
        }

        if (match(Type.PLUS)) {
            return parsePrimary()
        }

        return parsePrimary()
    }


    private fun parsePrimary(): Node {
        if (match(Type.L_PAREN)) {
            val result = parseExpression()
            consume(Type.R_PAREN)
            return result
        }

        if (match(Type.L_BRACKET)) {
            return parseArrayExpression()
        }

        return parseQualifiedName()
    }


    private fun parseQualifiedName(): Node {
        if (lookMatch(0, Type.WORD) && lookMatch(1, Type.L_PAREN)) {
            return parseFunction()
        }

        if (lookMatch(0, Type.WORD) && lookMatch(1, Type.L_BRACKET)) {
            return parseArrayElement()
        }

        if (lookMatch(0, Type.WORD)) {
            return VariableExpression(consume(Type.WORD).text)
        }

        return parseValue()
    }


    private fun parseValue(): ValueExpression {
        if (lookMatch(0, Type.NUMBER)) {
            return ValueExpression(consume(Type.NUMBER).text.toInt())
        }

        if (lookMatch(0, Type.NUMBER_DECIMAL)) {
            return ValueExpression(consume(Type.NUMBER_DECIMAL).text.toDouble())
        }

        if (lookMatch(0, Type.TEXT)) {
            return ValueExpression(consume(Type.TEXT).text)
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
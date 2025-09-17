package ru.kitty.parser

import ru.kitty.parser.ast.*
import ru.kitty.parser.util.Pos

class Parser private constructor(private val tokens: List<Token>){
    companion object {

        fun create(tokens: List<Token>): Parser {
            return Parser(tokens)
        }

        private val ASSIGN_OPERATORS = mapOf<Type, BinaryExpression.Operator?>(
            Type.EQ to null,
            Type.PLUS_EQ to BinaryExpression.Operator.ADD,
            Type.MINUS_EQ to BinaryExpression.Operator.SUBTRACT,
            Type.STAR_EQ to BinaryExpression.Operator.MULTIPLY,
            Type.SLASH_EQ to BinaryExpression.Operator.DIVIDE
        )
    }


    private var position = 0
    private val size = tokens.size


    fun parse(): Node {
        val result = BlockStatement()

        while (!match(Type.EOF)) {
            result.add(statement())
        }

        return result
    }


    private fun statementOrBlock(): Statement {
        if (lookMatch(0, Type.L_BRACE)) {
            return block()
        }

        return statement()
    }


    private fun block(): BlockStatement {
        val block = BlockStatement()
        consume(Type.L_BRACE)

        while (!lookMatch(0, Type.R_BRACE)) {
            block.add(statement())
        }

        consume(Type.R_BRACE)
        return block
    }


    private fun statement(): Statement {
        if (match(Type.IF)) {
            return ifElse()
        }

        if (match(Type.FOR)) {
            return forStatement()
        }

        if (match(Type.WHILE)) {
            return whileStatement()
        }

        if (match(Type.BREAK)) {
            return BreakStatement()
        }

        if (match(Type.CONTINUE)) {
            return ContinueStatement()
        }

        if (match(Type.RETURN)) {
            return returnStatement()
        }

        if (match(Type.ECHO)) {
            return PrintlnStatement(expression())
        }

        if (match(Type.VAR)) {
            return variableDeclaration()
        }

        if (match(Type.FUNC)) {
            return functionDefine()
        }

        if (match(Type.CLASS)) {
            return classDeclaration()
        }

        if (lookMatch(0, Type.WORD) && lookMatch(1, Type.L_PAREN)) {
            return functionCallStatement()
        }

        return assignmentStatement()
    }


    private fun ifElse(): IfStatement {
        val hasParen = match(Type.L_PAREN)
        val condition = expression()

        if (hasParen) {
            consume(Type.R_PAREN)
        }

        val ifStatement = statementOrBlock()
        val elseStatement = when (match(Type.ELSE)) {
            true -> statementOrBlock()
            false -> null
        }

        return IfStatement(condition, ifStatement, elseStatement)
    }


    private fun forStatement(): ForStatement {
        val hasParen = match(Type.L_PAREN)
        val init = when (match(Type.VAR)) {
            true -> variableDeclaration()
            false -> assignmentStatement()
        }

        consume(Type.COMMA)

        val condition = expression()
        consume(Type.COMMA)

        val increment = assignmentStatement()
        if (hasParen) {
            consume(Type.R_PAREN)
        }

        val statement = statementOrBlock()
        return ForStatement(init, condition, increment, statement)
    }


    private fun whileStatement(): WhileStatement {
        val hasParen = match(Type.L_PAREN)
        val condition = expression()

        if (hasParen) {
            consume(Type.R_PAREN)
        }

        val statement = statementOrBlock()
        return WhileStatement(condition, statement)
    }


    private fun returnStatement(): ReturnStatement {
        val result = try {
            expression()
        }
        catch (_: Exception) {
            null
        }

        return ReturnStatement(result)
    }


    private fun variableDeclaration(): VariableDeclarationStatement {
        val variable = consume(Type.WORD).text
        consume(Type.EQ)

        val expression = expression()
        return VariableDeclarationStatement(variable, expression)
    }


    private fun functionDefine(): FunctionDefineStatement {
        val name = consume(Type.WORD).text
        val argNames = mutableListOf<String>()
        consume(Type.L_PAREN)

        while (!lookMatch(0, Type.R_PAREN)) {
            argNames.add(consume(Type.WORD).text)
            match(Type.COMMA)
        }

        consume(Type.R_PAREN)

        val statement = block()
        return FunctionDefineStatement(name, argNames, statement)
    }


    private fun classDeclaration(): ClassDeclarationStatement {
        val name = consume(Type.WORD).text
        val variables = mutableListOf<VariableDeclarationStatement>()
        val methods = mutableListOf<FunctionDefineStatement>()
        consume(Type.L_BRACE)

        while (!lookMatch(0, Type.R_BRACE)) {
            if (match(Type.VAR)) {
                variables.add(variableDeclaration())
            }

            if (match(Type.FUNC)) {
                methods.add(functionDefine())
            }
        }

        consume(Type.R_BRACE)

        val clazz = ClassDeclarationStatement(name, variables, methods)
        return clazz
    }


    private fun assignmentStatement(): Statement {
        val expr = expression()
        if (expr is Statement) {
            return expr
        }

        throw RuntimeException("Unknown statement ${get(0)}")
    }


    private fun functionCallStatement(): ExprStatement {
        return ExprStatement(
            functionChain(ValueExpression(consume(Type.WORD).text))
        )
    }


    private fun functionChain(nameExpr: Node): Node {
        val expr = function(nameExpr)

        if (lookMatch(0, Type.DOT)) {
            val indices = variableSuffix()
            if (indices.isEmpty()) {
                return expr
            }

            if (lookMatch(0, Type.L_PAREN)) {
                return functionChain(ContainerAccessExpression(expr, indices))
            }

            return ContainerAccessExpression(expr, indices)
        }

        return expr
    }


    private fun function(nameExpr: Node): FunctionalExpression {
        val args = mutableListOf<Node>()
        consume(Type.L_PAREN)

        while (!lookMatch(0, Type.R_PAREN)) {
            args.add(expression())
            match(Type.COMMA)
        }

        consume(Type.R_PAREN)
        return FunctionalExpression(nameExpr, args)
    }


    private fun array(): ArrayExpression {
        val array = ArrayExpression()
        consume(Type.L_BRACKET)

        while (!lookMatch(0, Type.R_BRACKET)) {
            array.add(expression())
            match(Type.COMMA)
        }

        consume(Type.R_BRACKET)

        return array
    }


    private fun map(): MapExpression {
        val map = MapExpression()
        consume(Type.L_BRACE)

        while (!lookMatch(0, Type.R_BRACE)) {
            val key = expression()
            consume(Type.COLON)
            val value = expression()
            map.add(key, value)
            match(Type.COMMA)
        }

        consume(Type.R_BRACE)
        return map
    }


    private fun expression(): Node {
        return assignment()
    }


    private fun assignment(): Node {
        val assignment = assignmentStrict()
        if (assignment != null) {
            return assignment
        }

        return ternary()
    }


    private fun assignmentStrict(): AssignmentExpression? {
        val index = position
        val target = qualifiedName()

        if (target !is Accessible) {
            position = index
            return null
        }

        val currentType = get(0).type

        if (!ASSIGN_OPERATORS.containsKey(currentType)) {
            position = index
            return null
        }

        match(currentType)

        val operation = ASSIGN_OPERATORS[currentType]
        return AssignmentExpression(target, expression(), operation)
    }


    private fun ternary(): Node {
        val condition = logicalOr()

        if (match(Type.QUESTION)) {
            val trueExpression = expression()
            consume(Type.COLON)
            val falseExpression = expression()
            return TernaryExpression(condition, trueExpression, falseExpression)
        }

        return condition
    }


    private fun logicalOr(): Node {
        var result = logicalAnd()

        while (true) {
            if (match(Type.OR)) {
                result = ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd())
                continue
            }

            break
        }

        return result
    }


    private fun logicalAnd(): Node {
        var result = equality()

        while (true) {
            if (match(Type.AND)) {
                result = ConditionalExpression(ConditionalExpression.Operator.AND, result, equality())
                continue
            }

            break
        }

        return result
    }


    private fun equality(): Node {
        var result = condition()

        while (true) {
            if (match(Type.EQ_EQ)) {
                result = ConditionalExpression(ConditionalExpression.Operator.EQ, result, condition())
                continue
            }

            if (match(Type.NOT_EQ)) {
                result = ConditionalExpression(ConditionalExpression.Operator.NOT_EQ, result, condition())
                continue
            }

            break
        }

        return result
    }


    private fun condition(): Node {
        var result = addition()

        while (true) {
            if (match(Type.LT)) {
                result = ConditionalExpression(ConditionalExpression.Operator.LT, result, addition())
                continue
            }

            if (match(Type.GT)) {
                result = ConditionalExpression(ConditionalExpression.Operator.GT, result, addition())
                continue
            }

            if (match(Type.LT_EQ)) {
                result = ConditionalExpression(ConditionalExpression.Operator.LT_EQ, result, addition())
                continue
            }

            if (match(Type.GT_EQ)) {
                result = ConditionalExpression(ConditionalExpression.Operator.GT_EQ, result, addition())
                continue
            }

            break
        }

        return result
    }


    private fun addition(): Node {
        var result = multiplication()

        while (true) {
            if (match(Type.PLUS)) {
                result = BinaryExpression(BinaryExpression.Operator.ADD, result, multiplication())
                continue
            }

            if (match(Type.MINUS)) {
                result = BinaryExpression(BinaryExpression.Operator.SUBTRACT, result, multiplication())
                continue
            }

            break
        }

        return result
    }


    private fun multiplication(): Node {
        var result = objectCreation()

        while (true) {
            if (match(Type.STAR)) {
                result = BinaryExpression(BinaryExpression.Operator.MULTIPLY, result, objectCreation())
                continue
            }

            if (match(Type.SLASH)) {
                result = BinaryExpression(BinaryExpression.Operator.DIVIDE, result, objectCreation())
                continue
            }

            break
        }

        return result
    }


    private fun objectCreation(): Node {
        if (match(Type.NEW)) {
            val className = consume(Type.WORD).text
            consume(Type.L_PAREN)
            consume(Type.R_PAREN)
            return ObjectCreationExpression(className)
        }

        return unary()
    }


    private fun unary(): Node {
        if (match(Type.MINUS)) {
            return UnaryExpression(UnaryExpression.Operator.NEGATE, primary())
        }

        if (match(Type.NOT)) {
            return UnaryExpression(UnaryExpression.Operator.NOT, primary())
        }

        if (match(Type.PLUS_PLUS)) {
            return UnaryExpression(UnaryExpression.Operator.PREFIX_INCREMENT, primary())
        }

        if (match(Type.MINUS_MINUS)) {
            return UnaryExpression(UnaryExpression.Operator.PREFIX_DECREMENT, primary())
        }

        if (match(Type.PLUS)) {
            return primary()
        }

        return primary()
    }


    private fun primary(): Node {
        if (match(Type.L_PAREN)) {
            val result = expression()
            consume(Type.R_PAREN)
            return result
        }

        return variable()
    }


    //parse variables, arrays and function calls
    private fun variable(): Node {
        if (lookMatch(0, Type.WORD) && lookMatch(1, Type.L_PAREN)) {
            return functionChain(ValueExpression(consume(Type.WORD).text))
        }

        val qualifiedName = qualifiedName()
        if (qualifiedName != null) {
            if (lookMatch(0, Type.L_PAREN)) {
                return functionChain(qualifiedName)
            }

            //var++
            if (match(Type.PLUS_PLUS)) {
                return UnaryExpression( UnaryExpression.Operator.POSTFIX_INCREMENT, qualifiedName)
            }

            //var--
            if (match(Type.MINUS_MINUS)) {
                return UnaryExpression( UnaryExpression.Operator.POSTFIX_DECREMENT, qualifiedName)
            }

            return qualifiedName
        }

        if (lookMatch(0, Type.L_BRACKET)) {
            return array()
        }

        if (lookMatch(0, Type.L_BRACE)) {
            return map()
        }

        return value()
    }


    private fun qualifiedName(): Node? {
        val current = get(0)

        if (!match(Type.WORD)) {
            return null
        }

        val variable = VariableExpression(current.text)
        val indices = variableSuffix()

        if (indices.isEmpty()) {
            return variable
        }

        return ContainerAccessExpression(variable, indices)
     }


    private fun variableSuffix(): List<Node> {
        if (!lookMatch(0, Type.L_BRACKET) && !lookMatch(0, Type.DOT)) {
            return emptyList()
        }

        val indices = mutableListOf<Node>()

        while (lookMatch(0, Type.L_BRACKET) || lookMatch(0, Type.DOT)) {
            when {
                match(Type.L_BRACKET) -> {
                    indices.add(expression())
                    consume(Type.R_BRACKET)
                }

                match(Type.DOT) -> {
                    val property = consume(Type.WORD)
                    indices.add(ValueExpression(property.text))
                }
            }
        }

        return indices
    }


    //parse simple tokens like numbers and text
    private fun value(): Node {
        val current = get(0)

        if (match(Type.NUMBER)) {
            return ValueExpression(current.text.toInt())
        }

        if (match(Type.NUMBER_DECIMAL)) {
            return ValueExpression(current.text.toDouble())
        }

        if (match(Type.TEXT)) {
            val textValue = ValueExpression(current.text)
            if (lookMatch(0, Type.DOT)) {
                return stringProperty(textValue)
            }

            return textValue
        }

        throw RuntimeException("Unknown token $current")
    }


    private fun stringProperty(stringExpr: Node): Node {
        if (lookMatch(1, Type.WORD) && lookMatch(2, Type.L_PAREN)) {
            match(Type.DOT)
            return functionChain(
                ContainerAccessExpression(
                    stringExpr,
                    listOf(ValueExpression(consume(Type.WORD).text))
                )
            )
        }

        val indices = variableSuffix()
        if (indices.isEmpty()) {
            return stringExpr
        }

        return ContainerAccessExpression(stringExpr, indices)
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
            return Token("", Type.EOF, Pos(-1, -1))

        return tokens[nextPos]
    }
}
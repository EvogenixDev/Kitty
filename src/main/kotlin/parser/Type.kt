package ru.kitty.parser

enum class Type {
    NUMBER,
    NUMBER_DECIMAL,
    TEXT,
    WORD,

    IF,
    ELSE,
    ECHO,
    VAR,
    FOR,
    WHILE,
    BREAK,
    CONTINUE,
    RETURN,
    FUNC,
    CLASS,

    PLUS,
    MINUS,
    STAR,
    SLASH,
    EQ,

    PLUS_EQ,
    MINUS_EQ,
    STAR_EQ,
    SLASH_EQ,
    PLUS_PLUS,
    MINUS_MINUS,

    LT,
    LT_EQ,
    GT,
    GT_EQ,
    EQ_EQ,
    NOT_EQ,
    AND,
    OR,
    NOT,

    L_PAREN,
    R_PAREN,
    L_BRACE,
    R_BRACE,
    L_BRACKET,
    R_BRACKET,

    COMMA,
    DOT,
    QUESTION,
    COLON,

    EOF
}
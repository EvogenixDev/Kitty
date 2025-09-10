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

    PLUS,
    MINUS,
    STAR,
    SLASH,
    EQ,

    LT,
    LT_EQ,
    GT,
    GT_EQ,
    EQ_EQ,
    NOT_EQ,
    AND,
    OR,

    L_PAREN,
    R_PAREN,
    L_BRACE,
    R_BRACE,

    COMMA,

    EOF
}
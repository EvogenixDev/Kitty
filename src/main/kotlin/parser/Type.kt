package ru.kitty.parser

enum class Type {
    NUMBER,
    NUMBER_DECIMAL,
    WORD,

    PLUS,
    MINUS,
    STAR,
    SLASH,

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

    EOF
}
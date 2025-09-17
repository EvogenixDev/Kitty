package ru.kitty.lang

import ru.kitty.parser.ast.Node

class ClassField(
    val name: String,
    val expr: Node
)
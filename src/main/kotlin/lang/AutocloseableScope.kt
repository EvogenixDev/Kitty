package ru.kitty.lang

class AutocloseableScope(override val parent: Scope?): Scope(parent), AutoCloseable {
    override fun close() {
        ScopeHandler.pop()
    }
}
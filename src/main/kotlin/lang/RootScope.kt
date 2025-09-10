package ru.kitty.lang

class RootScope(override val parent: Scope?): Scope(parent) {

    override fun hasParent(): Boolean {
        return false
    }

    override fun isRoot(): Boolean {
        return true
    }
}
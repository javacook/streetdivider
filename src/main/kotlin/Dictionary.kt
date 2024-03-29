package de.kotlincook.textmining.streetdivider

import java.util.*

/**
 * A class to store and find words. The advantage in comparison with a
 * HashSet is that it can find prefixes and enlargement of the stored
 * words as well.
 */
class Dictionary(words: Collection<String>) {

    private var treeSet = TreeSet<String>()

    fun contains(word: String): Boolean {
        return treeSet.contains(word)
    }

    init {
        treeSet.addAll(words)
    }

}
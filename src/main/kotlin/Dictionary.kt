import Dictionary.Matching.*
import java.lang.Math.min
import java.util.TreeSet


class Dictionary {
    constructor(vararg words: String) {
        treeSet.addAll(words)
    }
    constructor(words: Collection<String>) {
        treeSet.addAll(words)
    }

    enum class Matching {
        PREFIX, EXACT, EMPTY, NULL
    }

    private var treeSet = TreeSet<String>()

    fun add(word: String) {
        treeSet.add(word)
    }

    fun addAll(words: Collection<String>) {
        treeSet.addAll(words)
    }

    fun addAll(vararg words: String) {
        for (word in words) treeSet.add(word)
    }

    fun contains(word: String): Boolean {
        return treeSet.contains(word)
    }

    /**
     * @param prefix
     * @return true, falls word ein Praefix eines der Woerter des Dictionarys ist.
     */
    fun isPrefix(prefix: String): Boolean {
        val ceiling = treeSet.ceiling(prefix)
        return ceiling?.startsWith(prefix) ?: false
    }


    /**
     * Sucht im Dictionary nach einem Prefix von enlargement und gibt
     * dies zurueck, falls vorhanden. Wenn nicht, wird null geliefert.
     * @param enlargement
     */
    fun prefixOf(enlargement: String): String? {
        var prefix = enlargement
        // Bemerkung: Es reicht nicht einfach, floor zu bestimmen und zu schauen, ob floor
        // ein Prefix von enlargement ist, wie man am Beipiel {"supa", "supakupa", "supalupa"} sieht,
        // falls enlargement == "supaluper" ist, da floor dann zunaechst "supalupa", dann "supakupa" waere
        // und beide kein Praefix von "supaluper" waeren.
        var floor = treeSet.floor(prefix)
        while (floor != null) {
            if (prefix.startsWith(floor)) return floor
            prefix = commonPrefix(prefix, floor)
            floor = treeSet.floor(prefix)
        }
        return null
    }


    /**
     * Berechnet das gemeinsame Praefix von <code>str1</code> und <code>str2</code>
     * @param str1 Eingabe1
     * @param str2 Eingabe2
     * @return gemeinsamens Praefix; "", falls kein gemeinsames Praefix existiert
     */
    private fun commonPrefix(str1: String, str2: String): String {
        var prefix = "";
        for (i in 0..min(str1.length, str2.length)) {
            if (str1[i] != str2[i]) break
            prefix += str1[i]
        }
        return prefix
    }

    /**
     * @param enlargement
     * @return true, falls ein Wort des Dictionarys Praefix von word ist.
     */
    fun hasPrefix(enlargement: String): Boolean {
        return prefixOf(enlargement) != null
    }


    fun size(): Int {
        return treeSet.size
    }


    /**
     * Beispiel:
     * @param word
     * @return
     */
    fun search(word: String): Matching {
        // Sonderfall: Der Leerstring ist stets PREFIX, ist aber oft nicht das Gewuenschte
        if (word.isEmpty()) return EMPTY

        val ceiling = treeSet.ceiling(word)

        return when {
            ceiling == null -> NULL
            ceiling.startsWith(word) -> if (ceiling.length == word.length) EXACT else PREFIX
            else -> NULL
        }
    }

}

fun main(args: Array<String>) {
    val dictionary = Dictionary()

    dictionary.addAll("supa", "supakupa", "supalupa")
    println(dictionary.prefixOf("supaluper"))
}
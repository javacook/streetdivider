import java.nio.charset.Charset

object StreetReader {
    val streets: List<String>

    init {
        val resource = this::class.java.classLoader.getResource("specialstreets.txt")
        val readText = resource.readText(Charset.defaultCharset())
        streets = readText.split(System.lineSeparator())
    }
}

fun main(args: Array<String>) {
    println(StreetReader.streets)
}
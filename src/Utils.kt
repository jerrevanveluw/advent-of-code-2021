import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("input", "$name.txt").readLines()

fun <R : Any, T : Any> Day.setup(block: T.() -> R, transform: Sequence<String>.() -> T) = { answer: R? ->
    readInput("Day${day}").asSequence()
        .run(transform)
        .block()
        .log(answer)
}

fun Any.log(correctAnswer: Any? = null) = println(this).also {
    correctAnswer?.let { check(this == it) }
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

interface Day {
    val day: String get() = javaClass.simpleName.substring(3)

    fun solve(parts: () -> Unit) {
        println("Day $day:")
        parts()
    }
}

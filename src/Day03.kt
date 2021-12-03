fun main() {

    val data = readInput("Day03").asSequence()

    part1(data).report(2743844)
    part2(data).report(6677951)

}

private fun part1(data: Sequence<String>) = data
    .fold(buildList { repeat(12) { add(Counter()) } }) { acc, cur ->
        acc.zip(cur.map(::Counter)).map { (a, b) -> a + b }
    }
    .let { it.most().toInt(2) * it.least().toInt(2) }

private fun part2(data: Sequence<String>) = data.toList().run {
    findMostRecursively().first().toInt(2) * findLeastRecursively().first().toInt(2)
}

data class Counter(
    val zero: Int = 0,
    val one: Int = 0
) {
    constructor(char: Char) : this(
        zero = if (char.digitToInt() == 0) 1 else 0,
        one = if (char.digitToInt() == 1) 1 else 0,
    )

    fun most() = if (zero > one) "0" else "1"

    fun least() = if (zero < one) "0" else "1"

    operator fun plus(that: Counter) = Counter(zero + that.zero, one + that.one)
}

fun List<Counter>.most() = joinToString("") { it.most() }
fun List<Counter>.least() = joinToString("") { it.least() }

fun List<String>.findMostRecursively(idx: Int = 0): List<String> =
    if (size > 1) filter { it[idx].digitToInt() == findMostUsedDigit(idx) }.findMostRecursively(idx + 1)
    else this

fun List<String>.findLeastRecursively(idx: Int = 0): List<String> =
    if (size > 1) filter { it[idx].digitToInt() == findLeastUsedDigit(idx) }.findLeastRecursively(idx + 1)
    else this

fun List<String>.findMostUsedDigit(idx: Int) = map { it[idx] }
    .fold(Counter()) { acc, cur -> acc + Counter(cur) }
    .run { if (zero > one) 0 else 1 }

fun List<String>.findLeastUsedDigit(digit: Int) = map { it[digit] }
    .fold(Counter()) { acc, cur -> acc + Counter(cur) }
    .run { if (zero <= one) 0 else 1 }
import kotlin.math.abs

fun main() {
    Day07.all()
}

object Day07 {

    fun all() {
        println("Day 07:")
        part1()
        part2()
    }

    private fun part1() = prepare().toList().sorted()
        .run {
            val median = this[size / 2]
            map { abs(median - it) }
        }
        .sum()
        .report(328318)

    private fun part2() = prepare()
        .run {
            val average = average().toInt()
            map { abs(average - it) }
        }
        .sumOf { calculateFuel(it) }
        .report(89791146)

    private fun prepare() = readInput("Day07").asSequence().flatMap { it.split(",") }.map { it.toInt() }

    private fun calculateFuel(int: Int): Int = if (int == 0) 0 else calculateFuel(int - 1) + int

}

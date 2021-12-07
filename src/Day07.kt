import kotlin.math.abs

fun main() {
    Day07.all()
}

object Day07 : Day {

    fun all() = solve {
        part1(328318)
        part2(89791146)
    }

    private val part1 = report {
        toList().sorted()
            .run {
                val median = this[size / 2]
                map { abs(median - it) }
            }
            .sum()
    }

    private val part2 = report {
        val average = average().toInt()
        map { abs(average - it) }
            .sumOf { calculateFuel(it) }
    }

    private fun calculateFuel(int: Int): Int = if (int == 0) 0 else calculateFuel(int - 1) + int

    private fun <R : Any> report(block: Sequence<Int>.() -> R) = setup(block) {
        flatMap { it.split(",") }.map { it.toInt() }
    }

}

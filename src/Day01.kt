fun main() {
    Day01.all()
}

object Day01 : Day {

    fun all() = solve {
        part1(1448)
        part2(1471)
    }

    private val part1 = report {
        zipWithNext { a, b -> a < b }
            .filter { it }
            .count()
    }

    private val part2 = report {
        windowed(4)
            .map { (a, _, _, d) -> a < d }
            .filter { it }
            .count()
    }

    private fun <R : Any> report(block: Sequence<Int>.() -> R) = setup(block) {
        map { it.toInt() }
    }

}

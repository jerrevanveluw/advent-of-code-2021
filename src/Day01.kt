fun main() {

    val data = readInput("Day01").asSequence()
        .map { it.toInt() }

    Day01.part1(data).report(1448)
    Day01.part2(data).report(1471)

}

object Day01 {

    fun part1(data: Sequence<Int>) = data
        .zipWithNext { a, b -> a < b }
        .filter { it }
        .count()

    fun part2(data: Sequence<Int>) = data
        .windowed(4)
        .map { (a, _, _, d) -> a < d }
        .filter { it }
        .count()

}

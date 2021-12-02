fun main() {

    val data = readInput("Day01").asSequence()
        .map { it.toInt() }

    part1(data).report(1448)
    part2(data).report(1471)

}

private fun part1(data: Sequence<Int>) = data
    .zipWithNext { a, b -> a < b }
    .filter { it }
    .count()

private fun part2(data: Sequence<Int>) = data
    .windowed(4)
    .map { (a, _, _, d) -> a < d }
    .filter { it }
    .count()

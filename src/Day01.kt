fun main() {

    val data = readInput("Day01").asSequence()
        .map { it.toInt() }

    part1(data).report(1448)
    part2(data).report(1471)

}

private fun part1(data: Sequence<Int>) = data
    .windowed(2)
    .map { (first, second) -> second - first }
    .filter { it > 0 }
    .count()

private fun part2(data: Sequence<Int>) = data
    .windowed(3)
    .map { (a, b, c) -> a + b + c }
    .windowed(2)
    .map { (first, second) -> second - first }
    .filter { it > 0 }
    .count()

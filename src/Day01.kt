fun main() {
    fun part1(input: Sequence<String>) = input
        .map { it.toInt() }
        .windowed(2)
        .map { (first, second) -> first - second }
        .filter { it < 0 }
        .count()

    fun part2(input: Sequence<String>) = input
        .map { it.toInt() }
        .windowed(3)
        .map { (a, b, c) -> a + b + c }
        .windowed(2)
        .map { (first, second) -> first - second }
        .filter { it < 0 }
        .count()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput.asSequence()) == 1)

    val input = readInput("Day01")
    println(part1(input.asSequence()))
    println(part2(input.asSequence()))
}

fun main() {
    Day01.all()
}

object Day01 {

    fun all() {
        println("Day 01:")
        part1()
        part2()
    }

    private fun part1() = prepare()
        .zipWithNext { a, b -> a < b }
        .filter { it }
        .count()
        .report(1448)

    private fun part2() = prepare()
        .windowed(4)
        .map { (a, _, _, d) -> a < d }
        .filter { it }
        .count()
        .report(1471)

    private fun prepare() = readInput("Day01").asSequence()
        .map { it.toInt() }

}

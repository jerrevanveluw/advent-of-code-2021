import Day13.Axis.X
import Day13.Axis.Y

fun main() {
    Day13.all()
}

object Day13 : Day {

    fun all() = solve {
        part1(null)
        part2(null)
    }

    private val part1 = report {
        foldAlong(X, 655)
            .count()
    }

    private val part2 = report {
        this
            .foldAlong(X, 655)
            .foldAlong(Y, 447)
            .foldAlong(X, 327)
            .foldAlong(Y, 223)
            .foldAlong(X, 163)
            .foldAlong(Y, 111)
            .foldAlong(X, 81)
            .foldAlong(Y, 55)
            .foldAlong(X, 40)
            .foldAlong(Y, 27)
            .foldAlong(Y, 13)
            .foldAlong(Y, 6)
            .let { "\n${it.toPrintableString(40)}" }
    }

    private fun List<Pair<Int, Int>>.toPrintableString(x: Int): String {
        val y = maxOf { (_, y) -> y } + 1
        val grid = buildList { repeat(x * y) { add('.') } }.chunked(x).map { it.toMutableList() }
        forEach { (x, y) -> grid[y][x] = '#' }
        return grid.joinToString("\n") { it.joinToString("") }
    }

    private enum class Axis { X, Y }

    private fun List<Pair<Int, Int>>.foldAlong(axis: Axis, int: Int) = when (axis) {
        X -> foldLeft(int)
        Y -> foldUp(int)
    }

    private fun List<Pair<Int, Int>>.foldUp(int: Int): List<Pair<Int, Int>> {
        val up = filter { (_, y) -> y < int }
        val down = filter { (_, y) -> y > int }
            .map { (x, y) -> x to y - (y - int) * 2 }
        return (up + down).distinct()

    }

    private fun List<Pair<Int, Int>>.foldLeft(int: Int): List<Pair<Int, Int>> {
        val left = filter { (x, _) -> x < int }
        val right = filter { (x, _) -> x > int }
            .map { (x, y) -> x - (x - int) * 2 to y }
        return (left + right).distinct()
    }

    private fun <R : Any> report(block: List<Pair<Int, Int>>.() -> R) = setup(block) {
        toList().dropLast(13)
            .map { it.split(",") }
            .map { (a, b) -> a.toInt() to b.toInt() }
    }

}

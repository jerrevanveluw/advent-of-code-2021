import Day09.Ignore.BOTTOM
import Day09.Ignore.LEFT
import Day09.Ignore.RIGHT
import Day09.Ignore.TOP

fun main() {
    Day09.all()
}

object Day09 : Day {

    fun all() = solve {
        part1(512)
        part2(1600104)
    }

    private val part1 = report {
        val grid = toGrid()
        toCoordinates()
            .mapNotNull { (value, coordinate) -> if (grid.isLowPoint(value, coordinate)) value to coordinate else null }
            .sumOf { it.first + 1 }
    }

    private val part2 = report {
        val grid = toGrid()
        toCoordinates()
            .mapNotNull { (value, coordinate) -> if (grid.isLowPoint(value, coordinate)) value to coordinate else null }
            .map { grid.findBasins(it) }
            .map { it.distinct().count() }
            .sorted()
            .takeLast(3)
            .reduce { a, b -> a * b }
    }

    private fun List<List<Int>>.findBasins(lowPoint: Pair<Int, Pair<Int, Int>>, ignore: Ignore? = null, coordinates: List<Pair<Int, Int>> = emptyList()): List<Pair<Int, Int>> =
        lowPoint.let { (value, coordinate) ->
            val (x, y) = coordinate
            val left = runCatching { this[y][x - 1] }.getOrNull()
            val right = runCatching { this[y][x + 1] }.getOrNull()
            val top = runCatching { this[y - 1][x] }.getOrNull()
            val bottom = runCatching { this[y + 1][x] }.getOrNull()

            val a = if (ignore == LEFT || left == null || left == 9) emptyList()
            else (bla(value, left, (x - 1 to y), RIGHT, coordinates + coordinate))

            val b = if (ignore == RIGHT || right == null || right == 9) emptyList()
            else (bla(value, right, (x + 1 to y), LEFT, coordinates + coordinate))

            val c = if (ignore == TOP || top == null || top == 9) emptyList()
            else (bla(value, top, (x to y - 1), BOTTOM, coordinates + coordinate))

            val d = if (ignore == BOTTOM || bottom == null || bottom == 9) emptyList()
            else (bla(value, bottom, (x to y + 1), TOP, coordinates + coordinate))
            a + b + c + d + coordinate
        }

    private fun List<List<Int>>.bla(value: Int, other: Int, coordinate: Pair<Int, Int>, ignore: Ignore, coordinates: List<Pair<Int, Int>>) =
        if (compare(value) { other }) findBasins(other to coordinate, ignore, coordinates) else emptyList()

    private enum class Ignore {
        LEFT, RIGHT, TOP, BOTTOM
    }

    private const val gridSizeX = 100
    private fun List<Int>.toGrid() = chunked(gridSizeX)
    private fun List<Int>.toCoordinates() = mapIndexed { idx, i -> Pair(i, idx % gridSizeX to idx / gridSizeX) }

    private fun List<List<Int>>.isLowPoint(value: Int, coordinate: Pair<Int, Int>): Boolean {
        val (x, y) = coordinate
        val a = compare(value) { this[y][x - 1] }
        val b = compare(value) { this[y][x + 1] }
        val c = compare(value) { this[y - 1][x] }
        val d = compare(value) { this[y + 1][x] }
        return (a && b && c && d)
    }

    private fun compare(int: Int, block: () -> Int) = int - (runCatching { block() }.getOrNull() ?: 9) < 0

    private fun <R : Any> report(block: List<Int>.() -> R) = setup(block) {
        map { it.split("").mapNotNull { s -> s.toIntOrNull() } }.toList().flatten()
    }

}

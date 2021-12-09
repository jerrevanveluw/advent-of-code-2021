import Day09.Ignore.BOTTOM
import Day09.Ignore.Companion.opposite
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
            .mapNotNull { if (it.isLowPointIn(grid)) it else null }
            .sumOf { (a, _) -> a + 1 }
    }

    private val part2 = report {
        val grid = toGrid()
        toCoordinates()
            .mapNotNull { if (it.isLowPointIn(grid)) it else null }
            .map { it.findBasin(grid) }
            .map { it.distinct().count() }
            .sortedDescending()
            .take(3)
            .reduce { a, b -> a * b }
    }

    private fun ValueAtCoordinate.findBasin(
        grid: List<List<Int>>,
        ignore: Ignore? = null,
        previousCoordinates: List<ValueAtCoordinate> = emptyList()
    ): List<Pair<Int, Int>> = let {
        val (x, y) = it.coordinate
        val coordinates = previousCoordinates + it

        val left = grid.getValueAt(x - 1 to y)?.run {
            if (ignore == LEFT || value >= 9) null
            else findAnother(it.value, grid, LEFT, coordinates)
        } ?: emptyList()

        val right = grid.getValueAt(x + 1 to y)?.run {
            if (ignore == RIGHT || value >= 9) null
            else findAnother(it.value, grid, RIGHT, coordinates)
        } ?: emptyList()

        val top = grid.getValueAt(x to y - 1)?.run {
            if (ignore == TOP || value >= 9) null
            else findAnother(it.value, grid, TOP, coordinates)
        } ?: emptyList()

        val bottom = grid.getValueAt(x to y + 1)?.run {
            if (ignore == BOTTOM || value >= 9) null
            else findAnother(it.value, grid, BOTTOM, coordinates)
        } ?: emptyList()

        left + right + top + bottom + it.coordinate
    }

    private fun List<List<Int>>.getValueAt(coordinate: Pair<Int, Int>) = coordinate
        .let { (x, y) -> runCatching { ValueAtCoordinate(this[y][x], coordinate) }.getOrNull() }

    private fun List<List<Int>>.getBy(coordinate: Pair<Int, Int>) = getValueAt(coordinate)?.value

    private fun ValueAtCoordinate.findAnother(
        current: Int, grid: List<List<Int>>,
        ignore: Ignore, coordinates: List<ValueAtCoordinate>
    ) = if (current - value < 0) findBasin(grid, ignore.opposite(), coordinates) else null

    private enum class Ignore {
        LEFT, RIGHT, TOP, BOTTOM;

        companion object {
            fun Ignore.opposite() = when (this) {
                LEFT -> RIGHT
                RIGHT -> LEFT
                TOP -> BOTTOM
                BOTTOM -> TOP
            }
        }
    }

    private const val gridSizeX = 100
    private fun Sequence<Int>.toGrid() = chunked(gridSizeX).toList()
    private fun Sequence<Int>.toCoordinates() = mapIndexed { idx, i -> Pair(i, idx % gridSizeX to idx / gridSizeX) }
        .map(::ValueAtCoordinate)

    private data class ValueAtCoordinate(val value: Int, val coordinate: Pair<Int, Int>) {
        constructor(v: Pair<Int, Pair<Int, Int>>) : this(v.first, v.second)
    }

    private fun ValueAtCoordinate.isLowPointIn(grid: List<List<Int>>): Boolean {
        val (x, y) = coordinate
        val l = value - (grid.getBy(x - 1 to y) ?: 9) < 0
        val r = value - (grid.getBy(x + 1 to y) ?: 9) < 0
        val t = value - (grid.getBy(x to y - 1) ?: 9) < 0
        val b = value - (grid.getBy(x to y + 1) ?: 9) < 0
        return (l && r && t && b)
    }

    private fun <R : Any> report(block: Sequence<Int>.() -> R) = setup(block) {
        map { it.split("").mapNotNull { s -> s.toIntOrNull() } }.flatten()
    }

}

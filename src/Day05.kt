fun main() {

    val data = readInput("Day05").asSequence()

    Day05.part1(data).report(4993)
    Day05.part2(data).report(21101)

}


object Day05 {

    fun part1(data: Sequence<String>) = Grid()
        .apply { data.prepare().filter { (a, b) -> a.x == b.x || a.y == b.y }.onEach { addLine(it) } }
        .countPointsWhereAtLeastTwoOverlap()

    fun part2(data: Sequence<String>) = Grid()
        .apply { data.prepare().onEach { addLine(it) } }
        .countPointsWhereAtLeastTwoOverlap()

    private fun Sequence<String>.prepare() = map { it.split(" -> ") }
        .map { (a, b) -> a.toCoordinate() to b.toCoordinate() }
        .toList()

    private data class Coordinate(val x: Int, val y: Int)

    private data class Counter(var count: Int = 0)

    private class Grid {
        private val rows: List<List<Counter>> =
            buildList { repeat(990) { add(buildList { repeat(990) { add(Counter()) } }) } }

        fun addLine(line: Pair<Coordinate, Coordinate>) = line.let { (a, b) ->
            when {
                a.x == b.x -> line.sortY().addVertical()
                a.y == b.y -> line.sortX().addHorizontal()
                else -> line.sortX().addDiagonal()
            }
        }

        fun countPointsWhereAtLeastTwoOverlap() = rows.flatten().count { it.count > 1 }

        override fun toString() = rows.joinToString("\n") { row -> row.joinToString(" ") { it.count.toString() } }

        private fun Pair<Coordinate, Coordinate>.sortY() = let { (a, b) -> if (a.y < b.y) a to b else b to a }
        private fun Pair<Coordinate, Coordinate>.sortX() = let { (a, b) -> if (a.x < b.x) a to b else b to a }

        private fun Pair<Coordinate, Coordinate>.addHorizontal() =
            let { (a, b) -> (a.x..b.x).forEach { rows[a.y][it].count++ } }

        private fun Pair<Coordinate, Coordinate>.addVertical() =
            let { (a, b) -> (a.y..b.y).forEach { rows[it][a.x].count++ } }

        private fun Pair<Coordinate, Coordinate>.addDiagonal() = let { (a, b) ->
            var x = a.x
            if (a.y < b.y) (a.y..b.y).forEach { rows[it][x++].count++ }
            else (a.y downTo b.y).forEach { rows[it][x++].count++ }
        }

    }

    private fun String.toCoordinate() = split(",").let { (x, y) -> Coordinate(x.toInt(), y.toInt()) }

}

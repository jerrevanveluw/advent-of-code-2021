fun main() {
    Day05.all()
}


object Day05 : Day {

    fun all() = solve {
        part1(4993)
        part2(21101)
    }

    private val part1 = report {
        Grid().also {
            filter { (a, b) -> a.x == b.x || a.y == b.y }.onEach { line -> it.addLine(line) }
        }.countPointsWhereAtLeastTwoOverlap()
    }

    private val part2 = report {
        Grid().also {
            onEach { line -> it.addLine(line) }
        }.countPointsWhereAtLeastTwoOverlap()
    }

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

    private fun <R : Any> report(block: List<Pair<Coordinate, Coordinate>>.() -> R) = setup(block) {
        map { it.split(" -> ") }
            .map { (a, b) -> a.toCoordinate() to b.toCoordinate() }
            .toList()
    }

}

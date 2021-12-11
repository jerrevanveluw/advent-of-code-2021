fun main() {
    Day11.all()
}

object Day11 : Day {

    fun all() = solve {
        part1(1681)
        part2(276)
    }

    private val part1 = report {
        repeat(100) { inc() }
        flashed
    }

    private val part2 = report {
        var step = 0
        while (!synchronized) {
            inc()
            step += 1
        }
        step
    }

    class Grid(private val digits: List<Integer>) {

        var flashed = 0

        var synchronized = false

        init {
            if (digits.size != 100) throw RuntimeException("Digits need to be ${digits.size}")
            digits.flash()
        }

        private fun List<Integer>.flash() {
            if (any { it.int > maxEnergy }) {
                mapIndexed { idx, digit -> if (digit.int > maxEnergy) idx else null }.filterNotNull()
                    .onEach {
                        this[it].int = 0
                        this[it].flashed = true
                        flashed += 1
                    }
                    .toCoordinates()
                    .flatMap { it.affectedCoordinates() }
                    .filter { it.first in 0 until gridSizeX && it.second in 0 until gridSizeY }
                    .toIndices()
                    .onEach {
                        if (!this[it].flashed) this[it].int += 1
                    }
                flash()
            }
            if (all { it.flashed }) synchronized = true
            forEach { it.flashed = false }
        }

        operator fun inc() = apply {
            digits.forEach { it.int += 1 }
            digits.flash()
        }

        override fun toString() = digits.toGrid().joinToString("\n") { it.joinToString(" ") { i -> i.toString() } }

        override fun equals(other: Any?) = when (other) {
            is Grid -> digits.zip(other.digits).all { (a, b) -> a == b }
            else -> false
        }

        override fun hashCode() = digits.hashCode()

        companion object {
            private const val gridSizeX = 10
            private const val gridSizeY = 10
            private const val maxEnergy = 9

            private fun List<Integer>.toGrid() = chunked(gridSizeX)
            private fun List<Int>.toCoordinates() = map { it % gridSizeX to it / gridSizeX }
            private fun List<Pair<Int, Int>>.toIndices() = map { (a, b) -> a + gridSizeX * b }
            private fun Pair<Int, Int>.affectedCoordinates() = listOf(
                first - 1 to second,
                first - 1 to second - 1,
                first - 1 to second + 1,
                first + 1 to second,
                first + 1 to second + 1,
                first + 1 to second - 1,
                first to second + 1,
                first to second - 1,
            )
        }

    }

    data class Integer(var int: Int, var flashed: Boolean = false) {
        override fun toString() = int.toString()
    }

    private fun <R : Any> report(block: Grid.() -> R) = setup(block) {
        map { s -> s.map { it.digitToInt() } }
            .flatten()
            .map { Integer(it) }
            .toList()
            .let(::Grid)
    }

}

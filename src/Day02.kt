fun main() {
    Day02.all()
}

object Day02 : Day {

    fun all() = solve {
        part1(2150351)
        part2(1842742223)
    }

    private val part1 = report {
        fold(Pair(0, 0)) { (horizontal, depth), (direction, value) ->
            when (direction) {
                Direction.FORWARD -> horizontal + value to depth
                Direction.UP -> horizontal to depth - value
                Direction.DOWN -> horizontal to depth + value
            }
        }.let { (horizontal, depth) -> horizontal * depth }
    }

    private val part2 = report {
        fold(Triple(0, 0, 0)) { (horizontal, depth, aim), (direction, value) ->
            when (direction) {
                Direction.FORWARD -> Triple(horizontal + value, depth + aim * value, aim)
                Direction.UP -> Triple(horizontal, depth, aim - value)
                Direction.DOWN -> Triple(horizontal, depth, aim + value)
            }
        }.let { (horizontal, depth) -> horizontal * depth }
    }

    private enum class Direction {
        UP, DOWN, FORWARD
    }

    private fun <R : Any> report(block: Sequence<Pair<Direction, Int>>.() -> R) = setup(block) {
        this.map { it.split(" ") }
            .map { (direction, value) -> Direction.valueOf(direction.uppercase()) to value.toInt() }
    }

}

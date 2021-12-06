fun main() {

    val data = readInput("Day02").asSequence()

    Day02.part1(data).report(2150351)
    Day02.part2(data).report(1842742223)

}

object Day02 {

    fun part1(data: Sequence<String>) = data.prepare()
        .fold(Pair(0, 0)) { (horizontal, depth), (direction, value) ->
            when (direction) {
                Direction.FORWARD -> horizontal + value to depth
                Direction.UP -> horizontal to depth - value
                Direction.DOWN -> horizontal to depth + value
            }
        }
        .let { (horizontal, depth) -> horizontal * depth }

    fun part2(data: Sequence<String>) = data.prepare()
        .fold(Triple(0, 0, 0)) { (horizontal, depth, aim), (direction, value) ->
            when (direction) {
                Direction.FORWARD -> Triple(horizontal + value, depth + aim * value, aim)
                Direction.UP -> Triple(horizontal, depth, aim - value)
                Direction.DOWN -> Triple(horizontal, depth, aim + value)
            }
        }
        .let { (horizontal, depth) -> horizontal * depth }

    private fun Sequence<String>.prepare() = map { it.split(" ") }
        .map { (direction, value) -> Direction.valueOf(direction.uppercase()) to value.toInt() }

    private enum class Direction {
        UP, DOWN, FORWARD
    }

}

fun main() {

    val data = readInput("Day02").asSequence()
        .map { it.split(" ") }
        .map { (direction, value) -> Direction.valueOf(direction.uppercase()) to value.toInt() }

    part1(data).report(2150351)
    part2(data).report(1842742223)

}

enum class Direction {
    UP, DOWN, FORWARD
}

private fun part1(data: Sequence<Pair<Direction, Int>>) = data
    .fold(Pair(0, 0)) { (horizontal, depth), (direction, value) ->
        when (direction) {
            Direction.FORWARD -> horizontal + value to depth
            Direction.UP -> horizontal to depth - value
            Direction.DOWN -> horizontal to depth + value
        }
    }
    .let { (horizontal, depth) -> horizontal * depth }

private fun part2(data: Sequence<Pair<Direction, Int>>) = data
    .fold(Triple(0, 0, 0)) { (horizontal, depth, aim), (direction, value) ->
        when (direction) {
            Direction.FORWARD -> Triple(horizontal + value, depth + aim * value, aim)
            Direction.UP -> Triple(horizontal, depth, aim - value)
            Direction.DOWN -> Triple(horizontal, depth, aim + value)
        }
    }
    .let { (horizontal, depth) -> horizontal * depth }

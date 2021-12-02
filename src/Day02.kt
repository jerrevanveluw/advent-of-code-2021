import Direction.DOWN
import Direction.FORWARD
import Direction.UP
import java.util.Locale

fun main() {

    readInput("Day02").asSequence()
        .map { it.split(" ") }
        .map { (direction, value) -> Direction.valueOf(direction.uppercase(Locale.getDefault())) to value.toInt() }
        .fold(Pair(0, 0)) { (horizontal, depth), (direction, value) ->
            when (direction) {
                FORWARD -> horizontal + value to depth
                UP -> horizontal to depth - value
                DOWN -> horizontal to depth + value
            }
        }
        .let { (horizontal, depth) -> horizontal * depth }
        .also { println(it) }

    readInput("Day02").asSequence()
        .map { it.split(" ") }
        .map { (direction, value) -> Direction.valueOf(direction.uppercase(Locale.getDefault())) to value.toInt() }
        .fold(Triple(0, 0, 0)) { (horizontal, depth, aim), (direction, value) ->
            when (direction) {
                FORWARD -> Triple(horizontal + value, depth + aim * value, aim)
                UP -> Triple(horizontal, depth, aim - value)
                DOWN -> Triple(horizontal, depth, aim + value)
            }
        }
        .let { (horizontal, depth) -> horizontal * depth }
        .also { println(it) }

}

enum class Direction {
    UP, DOWN, FORWARD
}

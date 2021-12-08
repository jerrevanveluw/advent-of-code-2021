import Day08.Number.EIGHT
import Day08.Number.FIVE
import Day08.Number.FOUR
import Day08.Number.NINE
import Day08.Number.ONE
import Day08.Number.SEVEN
import Day08.Number.SIX
import Day08.Number.THREE
import Day08.Number.TWO
import Day08.Number.ZERO
import Day08.Segment.BOTTOM
import Day08.Segment.BOTTOM_LEFT
import Day08.Segment.BOTTOM_RIGHT
import Day08.Segment.Companion.ALL
import Day08.Segment.Companion.LEFT
import Day08.Segment.Companion.RIGHT
import Day08.Segment.MIDDLE
import Day08.Segment.TOP
import Day08.Segment.TOP_LEFT
import Day08.Segment.TOP_RIGHT

fun main() {
    Day08.all()
}

object Day08 : Day {

    fun all() = solve {
        part1(342)
        part2(1068933)
    }

    private val part1 = report {
        flatMap { (_, b) -> b }
            .filter { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }
            .count()
    }

    private val part2 = report {
        map { (a, b) -> a.map { it.map(::encode).toSet() } to b.map { it.map(::encode).toSet() } }
            .map { (a, b) -> Puzzle(a.map(::Digit), b.map(::Digit)) }
            .map { it.solve() }
            .map { (output, solution) -> output.map { digit -> digit.value.map { solution[it]!! } } }
            .map { puzzle ->
                puzzle.map {
                    when (it.sorted()) {
                        ZERO.segments.sorted() -> 0
                        ONE.segments.sorted() -> 1
                        TWO.segments.sorted() -> 2
                        THREE.segments.sorted() -> 3
                        FOUR.segments.sorted() -> 4
                        FIVE.segments.sorted() -> 5
                        SIX.segments.sorted() -> 6
                        SEVEN.segments.sorted() -> 7
                        EIGHT.segments.sorted() -> 8
                        NINE.segments.sorted() -> 9
                        else -> throw RuntimeException()
                    }
                }.joinToString("").toInt()
            }.sum()
    }

    private fun Puzzle.solve() = input.run {
        val top = getTop()
        val bottom = getBottom(top)
        val middle = getMiddle(top, bottom)
        val topLeft = getTopLeft(middle)
        val bottomRight = getBottomRight(top, middle, bottom, topLeft)
        val topRight = getTopRight(bottomRight)
        val bottomLeft = getBottomLeft(top, middle, bottom, topRight, topLeft, bottomRight)
        output to mapOf(
            top to TOP,
            middle to MIDDLE,
            bottom to BOTTOM,
            topLeft to TOP_LEFT,
            topRight to TOP_RIGHT,
            bottomLeft to BOTTOM_LEFT,
            bottomRight to BOTTOM_RIGHT,
        )
    }

    private fun List<Digit>.getTopRight(bottomRight: EncodedSegment) =
        (first { it.number.contains(ONE) }.value - bottomRight).first()

    private fun List<Digit>.getBottomRight(vararg segments: EncodedSegment) = run {
        filter { it.number.contains(FIVE) }
            .map { it.value - segments.toSet() }
            .first { it.size == 1 }
            .first()
    }

    private fun List<Digit>.getBottomLeft(vararg segments: EncodedSegment) =
        filter { it.number.contains(EIGHT) }
            .map { it.value - segments.toSet() }
            .first { it.size == 1 }
            .first()

    private fun List<Digit>.getTop() = filter { it.number.contains(ONE) || it.number.contains(SEVEN) }
        .let { (a, b) -> a - b }.first()

    private fun List<Digit>.getTopLeft(middle: EncodedSegment) =
        filter { it.number.contains(ONE) || it.number.contains(FOUR) }
            .let { (a, b) -> a - b - middle }.first()

    private fun List<Digit>.getMiddle(top: EncodedSegment, bottom: EncodedSegment) = run {
        val one = first { it.number.contains(ONE) }
        filter { it.number.contains(THREE) }
            .map { it - one - top - bottom }
            .first { it.size == 1 }
            .first()
    }

    private fun List<Digit>.getBottom(top: EncodedSegment) = run {
        val four = first { it.number.contains(FOUR) }
        filter { it.number.contains(NINE) }
            .map { it - four - top }
            .first { it.size == 1 }
            .first()
    }

    private data class Digit(val value: Set<EncodedSegment>, val number: Set<Number>) {
        constructor(value: Set<EncodedSegment>) : this(
            value,
            when (value.size) {
                2 -> setOf(ONE)
                3 -> setOf(SEVEN)
                4 -> setOf(FOUR)
                5 -> setOf(TWO, THREE, FIVE)
                6 -> setOf(ZERO, SIX, NINE)
                7 -> setOf(EIGHT)
                else -> throw RuntimeException()
            }
        )

        operator fun minus(that: Digit) = value.union(that.value) - value.intersect(that.value)

        override fun toString() = "($value, $number)"
    }

    private data class Puzzle(
        val input: List<Digit>,
        val output: List<Digit>,
        val solution: Map<EncodedSegment, Segment>? = null
    )

    private enum class Segment {
        TOP, MIDDLE, BOTTOM, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

        companion object {
            val LEFT = setOf(TOP_LEFT, BOTTOM_LEFT)
            val RIGHT = setOf(TOP_RIGHT, BOTTOM_RIGHT)
            val ALL = values().toSet()
        }
    }

    private sealed class Number(val segments: Set<Segment>) {
        object ZERO : Number(ALL - MIDDLE)
        object ONE : Number(RIGHT)
        object TWO : Number(ALL - setOf(TOP_LEFT, BOTTOM_RIGHT))
        object THREE : Number(ALL - LEFT)
        object FOUR : Number(setOf(TOP_LEFT, MIDDLE) + RIGHT)
        object FIVE : Number(ALL - setOf(TOP_RIGHT, BOTTOM_LEFT))
        object SIX : Number(ALL - TOP_RIGHT)
        object SEVEN : Number(RIGHT + TOP)
        object EIGHT : Number(ALL)
        object NINE : Number(ALL - BOTTOM_LEFT)

        operator fun minus(that: Number) = segments - that.segments

        override fun toString() = when (this) {
            ZERO -> 0
            ONE -> 1
            TWO -> 2
            THREE -> 3
            FOUR -> 4
            FIVE -> 5
            SIX -> 6
            SEVEN -> 7
            EIGHT -> 8
            NINE -> 9
        }.toString()
    }

    private enum class EncodedSegment {
        A, B, C, D, E, F, G
    }

    private fun encode(c: Char) = EncodedSegment.valueOf(c.uppercaseChar().toString())

    private fun <R : Any> report(block: Sequence<Pair<List<String>, List<String>>>.() -> R) = setup(block) {
        map { it.split(" | ") }
            .map { (a, b) -> a to b }
            .map { (digits, output) -> digits.split(" ") to output.split(" ") }
    }

}

import Day10.Side.LEFT
import Day10.Side.RIGHT
import Day10.Type.Angled
import Day10.Type.Block
import Day10.Type.Curly
import Day10.Type.Paren

fun main() {
    Day10.all()
}

object Day10 : Day {

    fun all() = solve {
        part1(399153)
        part2(2995077699)
    }

    private val part1 = report {
        mapNotNull { it.parse() }
            .flatten()
            .map {
                when (it.type) {
                    is Angled -> 25137
                    is Block -> 57
                    is Curly -> 1197
                    is Paren -> 3
                }
            }
            .sum()
    }

    private val part2 = report {
        mapNotNull { it.parse(true) }
            .map { it.autocomplete() }
            .map { it.reversed() }
            .map { it.calculateSum() }
            .sorted()
            .run { elementAt(count() / 2) }
    }

    private fun List<Brace>.calculateSum() = fold(0L) { acc, brace ->
        (acc * 5) + when (brace.type) {
            is Curly -> 3L
            is Paren -> 1L
            is Block -> 2L
            is Angled -> 4L
        }
    }

    private fun List<Brace>.autocomplete() = mutableListOf<Brace>().let {
        forEach { brace ->
            when (brace.type.side) {
                LEFT -> it.add(brace)
                RIGHT -> it.removeLast().run {
                    when {
                        type is Curly && brace.type is Curly && type.side == LEFT -> true
                        type is Paren && brace.type is Paren && type.side == LEFT -> true
                        type is Block && brace.type is Block && type.side == LEFT -> true
                        type is Angled && brace.type is Angled && type.side == LEFT -> true
                        else -> false
                    }
                }
            }
        }
        it
    }

    private fun List<Brace>.parse(filterCorrupt: Boolean = false) = mutableListOf<Brace>().let {
        forEach { brace ->
            val valid = when (brace.type.side) {
                LEFT -> it.add(brace)
                RIGHT -> it.removeLast().run {
                    when {
                        type is Curly && brace.type is Curly && type.side == LEFT -> true
                        type is Paren && brace.type is Paren && type.side == LEFT -> true
                        type is Block && brace.type is Block && type.side == LEFT -> true
                        type is Angled && brace.type is Angled && type.side == LEFT -> true
                        else -> false
                    }
                }
            }
            if (!valid) {
                return@let if (filterCorrupt) null else listOf(brace)
            }
        }
        return@let if (filterCorrupt) this else null
    }

    private fun tokenize(string: String) = string.mapIndexed { idx, s ->
        when (s) {
            '{' -> Brace(idx, s, Curly(LEFT))
            '}' -> Brace(idx, s, Curly(RIGHT))
            '(' -> Brace(idx, s, Paren(LEFT))
            ')' -> Brace(idx, s, Paren(RIGHT))
            '[' -> Brace(idx, s, Block(LEFT))
            ']' -> Brace(idx, s, Block(RIGHT))
            '<' -> Brace(idx, s, Angled(LEFT))
            '>' -> Brace(idx, s, Angled(RIGHT))
            else -> throw RuntimeException("Parse error at index: $idx")
        }
    }

    private data class Brace(val index: Int, val char: Char, val type: Type)

    private enum class Side {
        LEFT, RIGHT
    }

    private sealed class Type(val side: Side) {
        class Curly(side: Side) : Type(side)
        class Paren(side: Side) : Type(side)
        class Angled(side: Side) : Type(side)
        class Block(side: Side) : Type(side)
    }


    private fun <R : Any> report(block: Sequence<List<Brace>>.() -> R) = setup(block) {
        map(::tokenize)
    }

}

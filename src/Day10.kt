import Day10.Brace.Angled
import Day10.Brace.Block
import Day10.Brace.Curly
import Day10.Brace.Paren
import Day10.Brace.Type.LEFT
import Day10.Brace.Type.RIGHT

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
                when (it) {
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
            .map(::autocomplete)
            .map(::reversed)
            .map(::calculateSum)
            .sorted()
            .getMiddle()
    }

    private fun <T> Sequence<T>.getMiddle() = elementAt(count() / 2)

    private fun <T> reversed(iterable: Iterable<T>) = iterable.reversed()

    private fun calculateSum(l: List<Brace>): Long = l.fold(0L) { acc, brace ->
        acc * 5 + when (brace) {
            is Curly -> 3
            is Paren -> 1
            is Block -> 2
            is Angled -> 4
        }
    }

    private fun autocomplete(braces: List<Brace>): List<Brace> {
        val mutableList = mutableListOf<Brace>()
        braces.forEach { mutableList.addUntilCorruptOrIncomplete(it) }
        return mutableList
    }

    private fun List<Brace>.parse(filterCorrupt: Boolean = false): List<Brace>? {
        val mutableList = mutableListOf<Brace>()
        forEach { if (!mutableList.addUntilCorruptOrIncomplete(it)) return if (filterCorrupt) null else listOf(it) }
        return if (filterCorrupt) this else null
    }

    private fun MutableList<Brace>.addUntilCorruptOrIncomplete(brace: Brace) = when (brace.type) {
        LEFT -> add(brace)
        RIGHT -> removeLast().let { it == brace && it.type == LEFT }
    }

    private sealed class Brace(val type: Type) {
        class Curly(type: Type) : Brace(type)
        class Paren(type: Type) : Brace(type)
        class Angled(type: Type) : Brace(type)
        class Block(type: Type) : Brace(type)

        enum class Type { LEFT, RIGHT }

        override fun equals(other: Any?): Boolean = when {
            this is Curly && other is Curly -> true
            this is Paren && other is Paren -> true
            this is Angled && other is Angled -> true
            this is Block && other is Block -> true
            else -> false
        }

        override fun hashCode() = type.hashCode()
    }

    private fun <R : Any> report(block: Sequence<List<Brace>>.() -> R) = setup(block) { map(::tokenize) }

    private fun tokenize(string: String) = string.map { c ->
        when (c) {
            '{' -> Curly(LEFT)
            '}' -> Curly(RIGHT)
            '(' -> Paren(LEFT)
            ')' -> Paren(RIGHT)
            '[' -> Block(LEFT)
            ']' -> Block(RIGHT)
            '<' -> Angled(LEFT)
            '>' -> Angled(RIGHT)
            else -> throw RuntimeException("Illegal brace: '$c'")
        }
    }

}

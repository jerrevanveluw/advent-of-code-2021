import Day03.Counter.Strategy
import Day03.Counter.Strategy.LEAST
import Day03.Counter.Strategy.MOST

fun main() {
    Day03.all()
}

object Day03 : Day {

    fun all() = solve {
        part1(2743844)
        part2(6677951)
    }

    private val part1 = report {
        fold(buildList { repeat(12) { add(Counter()) } }) { acc, cur ->
            acc.zip(cur.map { it.toCounter() }).map { (a, b) -> a + b }
        }.let { it.toInt(MOST) * it.toInt(LEAST) }
    }

    private val part2 = report {
        findRecursively(MOST) * findRecursively(LEAST)
    }

    private data class Counter(
        val zeros: Int = 0,
        val ones: Int = 0
    ) {

        fun toInt(strategy: Strategy) = when (strategy) {
            MOST -> if (zeros > ones) 0 else 1
            LEAST -> if (zeros <= ones) 0 else 1
        }

        operator fun plus(that: Counter) = Counter(zeros + that.zeros, ones + that.ones)

        enum class Strategy {
            MOST, LEAST
        }
    }

    private fun Char.toCounter() = when (digitToInt()) {
        0 -> Counter(1, 0)
        1 -> Counter(0, 1)
        else -> throw IllegalStateException()
    }

    private fun List<Counter>.toInt(strategy: Strategy) = joinToString("") { it.toInt(strategy).toString() }.toInt(2)

    private fun List<String>.findRecursively(strategy: Strategy, idx: Int = 0): Int =
        if (size == 1) first().toInt(2)
        else filter { it[idx].digitToInt() == findDigit(idx, strategy) }
            .findRecursively(strategy, idx + 1)

    private fun List<String>.findDigit(idx: Int, strategy: Strategy) = map { it[idx] }
        .fold(Counter()) { acc, cur -> acc + cur.toCounter() }
        .toInt(strategy)

    private fun <R : Any> report(block: List<String>.() -> R) = setup(block) { toList() }

}

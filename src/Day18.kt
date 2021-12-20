fun main() {
    Day18.all()
}

object Day18 : Day {

    fun all() = solve {
        part1(null)
        part2(null)
    }

    private val part1 = report { count() }

    private val part2 = report { count() }

    private fun <R : Any> report(block: Sequence<String>.() -> R) = setup(block) {
        this
    }

}

fun main() {
    Day08.all()
}

object Day08 : Day {

    fun all() = solve {
        part1(null)
        part2(null)
    }

    private val part1 = report { 0 }

    private val part2 = report { 0 }

    private fun <R : Any> report(block: Sequence<String>.() -> R) = setup(block) {
        this
    }

}

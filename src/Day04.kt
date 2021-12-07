fun main() {
    Day04.all()
}

object Day04 : Day {

    fun all() = solve {
        part1(16674)
        part2(7075)
    }

    private val part1 = report { first() }

    private val part2 = report { last() }

    private data class Board(val rows: List<List<Marked>>) {

        fun mark(int: Int) = rows.forEach { row -> row.forEach { if (it.number == int) it.marked = true } }

        fun bingo() = if (rows.isBingo() || rows.transpose().isBingo()) this else null

        fun unMarkedSum() = rows.flatMap { row -> row.filter { !it.marked } }.sumOf { it.number }

        private fun List<List<Marked>>.isBingo() = find { row -> row.all { it.marked } } != null

    }

    private data class Marked(val number: Int, var marked: Boolean = false)

    private fun <T : Any> List<List<T>>.transpose(): List<List<T>> =
        (0 until first().size).map { idx -> map { it[idx] } }

    private fun <R : Any> report(block: List<Int>.() -> R) = setup(block) {
        val input = take(1)
            .first()
            .split(",")
            .map { it.toInt() }
        val boards = drop(2)
            .chunked(6)
            .map { it.dropLast(1) }
            .map { board ->
                board.map { row ->
                    row.split(" ")
                        .mapNotNull { it.ifBlank { null } }
                        .map { it.toInt() }
                        .map(::Marked)
                }.let(::Board)
            }.toMutableList()

        input.flatMap { int ->
            boards.forEach { it.mark(int) }
            boards.mapNotNull { it.bingo() }
                .also { boards.removeAll(it) }
                .map { it.unMarkedSum() * int }
        }
    }

}

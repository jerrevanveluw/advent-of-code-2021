fun main() {

    val data = readInput("Day04").asSequence()

    Day04.part1(data).report(16674)
    Day04.part2(data).report(7075)

}

object Day04 {

    fun part1(data: Sequence<String>) = data.prepare().run {
        first.flatMap { int ->
            second.forEach { it.mark(int) }
            second.mapNotNull { it.bingo() }
                .also { second.removeAll(it) }
                .map { it.unMarkedSum() * int }
        }.first()
    }

    fun part2(data: Sequence<String>) = data.prepare().run {
        first.flatMap { int ->
            second.forEach { it.mark(int) }
            second.mapNotNull { it.bingo() }
                .also { second.removeAll(it) }
                .map { it.unMarkedSum() * int }
        }.last()
    }

    private fun Sequence<String>.prepare(): Pair<List<Int>, MutableList<Board>> = take(1)
        .first()
        .split(",")
        .map { it.toInt() } to drop(2)
        .chunked(6)
        .map { it.dropLast(1) }
        .map { board ->
            board.map { row ->
                row
                    .split(" ")
                    .mapNotNull { it.ifBlank { null } }
                    .map { it.toInt() }
                    .map(::Mark)
            }.let {
                Board(it)
            }
        }.toMutableList()

    private data class Board(
        val rows: List<List<Mark>>
    ) {
        fun mark(int: Int) = rows.forEach { row -> row.forEach { if (it.number == int) it.marked = true } }

        fun bingo() = if (rows.isBingo() || rows.transpose().isBingo()) this else null

        fun unMarkedSum() = rows.flatMap { row -> row.filter { !it.marked } }.sumOf { it.number }

        private fun List<List<Mark>>.isBingo() = find { it.all { mark -> mark.marked } } != null
    }

    private data class Mark(val number: Int, var marked: Boolean = false)

    private fun <T : Any> List<List<T>>.transpose(): List<List<T>> =
        (0 until first().size).map { idx -> map { it[idx] } }

}

fun main() {

    val data = readInput("Day04").asSequence()

    part1(data.prepare()).report(16674)
    part2(data.prepare()).report(7075)

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

private fun part1(data: Pair<List<Int>, MutableList<Board>>) = with(data.second) {
    data.first.flatMap { int ->
        forEach { it.mark(int) }
        mapNotNull { it.bingo() }
            .also { removeAll(it) }
            .map { it.unMarkedSum() * int }
    }.first()
}

private fun part2(data: Pair<List<Int>, MutableList<Board>>) = with(data.second) {
    data.first.flatMap { int ->
        forEach { it.mark(int) }
        mapNotNull { it.bingo() }
            .also { removeAll(it) }
            .map { it.unMarkedSum() * int }
    }.last()
}

private data class Board(
    val rows: List<List<Mark>>
) {
    fun mark(int: Int) = rows.forEach { row -> row.forEach { if (it.number == int) it.marked = true } }

    fun bingo() = if (rows.isBingo() || rows.transpose().isBingo()) this else null

    fun unMarkedSum() = rows.flatMap { row -> row.filter { !it.marked } }.sumOf { it.number }

    private fun List<List<Mark>>.isBingo() = find { it.all { mark -> mark.marked } } != null
}

private data class Mark(val number: Int, var marked: Boolean = false)

private fun <T : Any> List<List<T>>.transpose(): List<List<T>> = (0 until first().size).map { idx -> map { it[idx] } }

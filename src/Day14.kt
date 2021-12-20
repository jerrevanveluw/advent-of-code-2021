fun main() {
    Day14.all()
}

object Day14 : Day {

    fun all() = solve {
        part1(2915)
        part2(null)
    }

    private val part1 = report { (template, instructions) ->
        var modifiedTemplate = template
        repeat(10) {
            modifiedTemplate = modifiedTemplate.zipWithNext()
                .map { instructions.check(it) }
                .unzip()
                .let { (a, b) -> a + b.last() }
                .joinToString("")
        }
        val groupedByChar = modifiedTemplate.groupBy { it }.mapValues { (_, value) -> value.count() }
        val max = groupedByChar.maxByOrNull { (_, v) -> v }!!.value
        val min = groupedByChar.minByOrNull { (_, v) -> v }!!.value
        max - min
    }

    private val part2 = report { (template, instructions) ->
        var modifiedTemplate = template
        repeat(10) {
            modifiedTemplate = modifiedTemplate.zipWithNext()
                .map { instructions.check(it) }
                .unzip()
                .let { (a, b) -> a + b.last() }
                .joinToString("")
        }
        val groupedByChar = modifiedTemplate.groupBy { it }.mapValues { (_, value) -> value.count() }
        val max = groupedByChar.maxByOrNull { (_, v) -> v }!!.value
        val min = groupedByChar.minByOrNull { (_, v) -> v }!!.value
        max - min
    }

    private fun List<Instruction>.check(pair: Pair<Char, Char>) = find { pair == it.pattern }
        ?.let { "${pair.first}${it.insert}" to pair.second }
        ?: (pair.first.toString() to pair.second)

    private data class Instruction(val pattern: Pair<Char, Char>, val insert: Char) {
        override fun toString() = "${pattern.first}${pattern.second} -> $insert"
    }

    private fun <R : Any> report(block: (Pair<String, List<Instruction>>) -> R) = setup(block) {
        val template = first()
        val instructions = drop(2).map {
            it.split(" -> ").let { (pattern, insert) ->
                Instruction(
                    pattern.toCharArray().take(2).let { (a, b) -> a to b },
                    insert.first()
                )
            }
        }
        template to instructions.toList()
    }

}

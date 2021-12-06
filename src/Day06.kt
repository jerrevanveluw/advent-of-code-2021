fun main() {

    val data = readInput("Day06").asSequence()

    Day06.part1(data).report(365131)
    Day06.part2(data).report(1650309278600)

}

object Day06 {

    fun part1(data: Sequence<String>) = with(data.prepare().toMutableList()) {
        (1..80).forEach { _ ->
            val newFish = mutableListOf<Int>()
            forEachIndexed { idx, f ->
                if (f == 0) {
                    this[idx] = 6
                    newFish.add(8)
                } else this[idx] = this[idx] - 1
            }
            addAll(newFish)
        }
        this
    }.count()


    fun part2(rawData: Sequence<String>) = rawData.prepare().toList().let { data ->
        var fish = buildMap<Int, Long> { data.distinct().forEach { i -> put(i, data.count { it == i }.toLong()) } }
        (1..256).forEach { _ ->
            fish = fish
                .map { (k, v) -> k - 1 to v }
                .toMap().toMutableMap().apply {
                    entries
                        .filter { (k, _) -> k < 0 }
                        .forEach { (k, v) ->
                            put(8, (get(8) ?: 0) + v)
                            put(6, (get(6) ?: 0) + v)
                            remove(k)
                        }
                }
        }
        fish.map { it.value }.sum()
    }

    private fun Sequence<String>.prepare() = flatMap { it.split(",") }.map { it.toInt() }

}

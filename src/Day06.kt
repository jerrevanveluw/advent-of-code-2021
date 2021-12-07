fun main() {
    Day06.all()
}

object Day06 : Day {

    fun all() = solve {
        part1(365131)
        part2(1650309278600)
    }

    private val part1 = report {
        toMutableList().run {
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
    }


    private val part2 = report {
        toList().let { data ->
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
            fish
                .map { it.value }
                .sum()
        }
    }

    private fun <R : Any> report(block: Sequence<Int>.() -> R) = setup(block) {
        flatMap { it.split(",") }
            .map { it.toInt() }
    }

}

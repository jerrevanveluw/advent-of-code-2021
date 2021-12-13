fun main() {
    Day12.all()
}

object Day12 : Day {

    var count = 0

    fun all() = solve {
        part1(null)
//        part2(null)
    }

    private val part1 = report {
        toMutableSet().toTree(find { it.a == Point.Start }!!)
    }

    private val part2 = report {
        count()
    }

    private class Tree(val root: Point, val children: List<Tree>) {
        override fun toString(): String = if (children.isNotEmpty()) "$root -> $children" else "$root"
    }

    private fun MutableSet<Path>.toTree(path: Path): Tree {
        remove(path)
        val root = path.a
        val children = filter { it.a.name == path.b.name }
            .map { toTree(it) }
        return if (children.isEmpty()) Tree(root, listOf(Tree(Point.End, listOf()))) else Tree(root, children)
    }

    private sealed class Point(val name: String) {
        object Start : Point("start")
        object End : Point("end")
        sealed class Cave(name: String) : Point(name) {
            class Big(name: String) : Cave(name)
            class Small(name: String) : Cave(name) {
                var visited: Boolean = false
                    set(b: Boolean) {
                        if (b) field = b
                    }
            }
        }

        override fun toString() = when (this) {
            is Start -> "start"
            is End -> "end"
            is Cave -> name
        }
    }

    private data class Path(val a: Point, val b: Point) {
        override fun toString() = "$a -> $b"
    }

    private fun <R : Any> report(block: Set<Path>.() -> R) = setup(block) {
        map { it.split("-") }
            .map { (a, b) ->
                fun String.toPoint() = when (this) {
                    "start" -> Point.Start
                    "end" -> Point.End
                    else -> if (all { it.isUpperCase() }) Point.Cave.Big(this) else Point.Cave.Small(this)
                }
                a.toPoint() to b.toPoint()
            }
            .map { (a, b) -> if (a is Point.End) b to a else a to b }
            .map { (a, b) -> Path(a, b) }
            .toSet()
    }

}

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
        toTree(Point.Start)
            .flatten()
            .also { println(it) }
            .count()
    }

    private val part2 = report {
        count()
    }

    private class Tree(val root: Point, val children: List<Tree> = emptyList()) {
        fun flatten(): List<List<Point>> {
//            return if (children.isEmpty()) listOf(listOf(root)) else children.map { it.flatten().flatten() }
            return listOf(listOf(listOf( root)).flatten() +  children.map { it.root })
        }

        override fun toString(): String = if (children.isNotEmpty()) "$root -> $children" else "$root"
    }

    private fun Set<Path>.toTree(root: Point): Tree {
        if (root is Point.End) return Tree(root)
        val pathsToProcess = filter { (a, b) -> a.name == root.name || b.name == root.name }
            .map { (a, b) -> if (a.name == root.name) a to b else b to a }.toSet()
        val newPaths = filter { (a, b) -> a.name != root.name && b.name != root.name }.toSet()
        val children = pathsToProcess
            .map { (_, child) -> child }
            .map { newPaths.toTree(it) }

        return if (children.isEmpty()) Tree(root, listOf(Tree(Point.End))) else Tree(root, children)
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
            .flatMap { (a, b) -> listOf(a to b, b to a) }
            .map { (a, b) -> Path(a, b) }
            .toSet()
    }

}

import java.io.File

fun day7_1() {
    data class Edge(val bag: String, val num: Int)

    val graph = mutableMapOf<String, List<Edge>>()
    val revGraph = mutableMapOf<String, MutableList<Edge>>()
    File("7.txt").useLines { lines ->
        val lineRe = """(.+) bags contain (.+)\.""".toRegex()
        val partRe = """(\d+) (.+) bags?""".toRegex()
        for (line in lines) {
            val lineMatch = lineRe.matchEntire(line)!!
            val node = lineMatch.groupValues[1]
            val edgesText = lineMatch.groupValues[2]
            val edges = buildList<Edge> {
                if (edgesText == "no other bags") {
                    return@buildList
                }
                val edgeParts = edgesText.split(", ")
                for (part in edgeParts) {
                    val partMatch = partRe.matchEntire(part)!!
                    val num = partMatch.groupValues[1].toInt()
                    val bag = partMatch.groupValues[2]
                    add(Edge(bag, num))
                }
            }
            graph[node] = edges
            for ((bag, num) in edges) {
                revGraph.putIfAbsent(bag, mutableListOf())
                revGraph[bag]!!.add(Edge(node, num))
            }
        }
    }
    val startNode = "shiny gold"
    val visited = mutableSetOf(startNode)
    val queue = ArrayDeque<String>()
    queue.add(startNode)
    while (!queue.isEmpty()) {
        val cur = queue.removeFirst()
        for ((bag, _) in revGraph[cur] ?: emptyList()) {
            if (bag !in visited) {
                queue.add(bag)
                visited.add(bag)
            }
        }
    }
    println(visited.toList())
    println(visited.size - 1)
}

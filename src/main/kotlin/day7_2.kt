import java.io.File

fun day7_2() {
    data class Edge(val bag: String, val num: Int)

    fun countBags(graph: Map<String, List<Edge>>, node: String): Int {
        var result = 1
        for ((bag, num) in graph[node] ?: emptyList()) {
            result += num * countBags(graph, bag)
        }
        return result
    }

    val graph = mutableMapOf<String, List<Edge>>()
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
        }
    }
    val startNode = "shiny gold"
    println(countBags(graph, startNode) - 1)
}

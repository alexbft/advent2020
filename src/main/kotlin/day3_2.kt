import java.io.File

fun day3_2() {
    val lines = File("3.txt").readLines()
    fun solve(dx: Int, dy: Int): Int {
        var x = 0
        var y = 0
        var ctr = 0
        while (y < lines.size) {
            if (lines[y][x] == '#') {
                ctr += 1
            }
            x = (x + dx) % lines[0].length
            y += dy
        }
        return ctr
    }

    val allCases = listOf(Pair(1, 1), Pair(3, 1), Pair(5, 1), Pair(7, 1), Pair(1, 2))
    val allResults = allCases.map { (dx, dy) -> solve(dx, dy) }
    println(allResults)
    println(allResults.reduce { a, b -> a * b })
}

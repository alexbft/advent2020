import java.io.File

fun day6_2() {
    val input = File("6.txt").readText()
    val cases = input.replace("\r\n", "\n").trim().split("\n\n")
    var total = 0
    for (case in cases) {
        var conjunction: Set<Char>? = null
        val lines = case.split('\n')
        for (line in lines) {
            val chars = line.toSet()
            conjunction = conjunction?.intersect(chars) ?: chars
        }
        val num = conjunction?.size ?: 0
        println(num)
        total += num
    }
    println("Total: $total")
}

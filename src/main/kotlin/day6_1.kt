import java.io.File

fun day6_1() {
    val input = File("6.txt").readText()
    val cases = input.replace("\r\n", "\n").trim().split("\n\n")
    var total = 0
    for (case in cases) {
        val union = mutableSetOf<Char>()
        val lines = case.split('\n')
        for (line in lines) {
            union.addAll(line.asIterable())
        }
        println(union.size)
        total += union.size
    }
    println("Total: $total")
}

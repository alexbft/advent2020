import java.io.File

fun day1_1() {
    val nums = File("1.txt").readLines().map { it.toInt() }
    for ((i, n) in nums.withIndex()) {
        for (n2 in nums.subList(0, i)) {
            if (n + n2 == 2020) {
                println("$n $n2 ${n * n2}")
            }
        }
    }
}


import java.io.File

fun day1_2() {
    val nums = File("1.txt").readLines().map { it.toInt() }
    for ((i, n) in nums.withIndex()) {
        for ((j, n2) in nums.subList(0, i).withIndex()) {
            for (n3 in nums.subList(0, j)) {
                if (n + n2 + n3 == 2020) {
                    println("$n $n2 $n3 ${n * n2 * n3}")
                }
            }
        }
    }
}
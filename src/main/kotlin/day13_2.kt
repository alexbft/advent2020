package my.advent2020.day13part2

import java.io.File

private fun gcd(a: Long, b: Long): Long {
    var x = a
    var y = b
    while (y > 0) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}

private fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

fun day13_2(inputFile: String) {
    val lines = File(inputFile).readLines()
    for (line in lines.slice(1..lines.lastIndex)) {
        val buses = line.split(",")
        var shift = 0
        var result = 0L
        var mult = 1L
        for (bus in buses) {
            if (bus == "x") {
                shift += 1
                continue
            }
            val busInt = bus.toLong()
            var iters = 0
            while ((result + shift) % busInt != 0L && iters < 100000) {
                result += mult
                ++iters
            }
            if (iters >= 100000) {
                throw Exception("possible infinite loop")
            }
            shift += 1
            mult = lcm(mult, busInt)
        }
        println("$line $result")
    }
}

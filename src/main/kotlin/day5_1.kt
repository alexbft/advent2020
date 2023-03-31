import java.io.File

fun day5_1() {
    var max = 0
    File("5.txt").useLines { lines ->
        for (line in lines) {
            var x = 0
            for (c in line) {
                x *= 2
                if (c == 'B' || c == 'R') {
                    x += 1
                }
            }
            println("$line $x")
            if (x > max) {
                max = x
            }
        }
    }
    println(max)
}

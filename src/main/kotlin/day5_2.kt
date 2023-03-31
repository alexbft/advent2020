import java.io.File

fun day5_2() {
    val seats = MutableList(1024) { false }
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
            seats[x] = true
        }
    }
    var index = seats.indexOf(true)
    while (seats[index]) {
        index++
    }
    println(index)
}

import java.io.File

fun day3_1() {
    var ctr = 0
    File("3.txt").useLines { lines ->
        var x = 0
        for (line in lines) {
            if (line[x] == '#') {
                ctr += 1
            }
            x = (x + 3) % line.length
        }
    }
    println(ctr)
}
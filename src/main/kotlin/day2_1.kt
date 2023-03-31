import java.io.File

fun day2_1() {
    var ctr = 0
    val re = Regex("""(\d+)-(\d+) (\w): (\w+)""")
    File("2.txt").useLines { lines ->
        for (line in lines) {
            val match = re.matchEntire(line)!!
            val min = match.groups[1]!!.value.toInt()
            val max = match.groups[2]!!.value.toInt()
            val chr = match.groups[3]!!.value.single()
            val source = match.groups[4]!!.value
            val count = source.count { it == chr }
            if (count in min..max) {
                ctr += 1
            }
        }
    }
    println(ctr)
}

import java.io.File

fun day2_2() {
    var ctr = 0
    val re = Regex("""(\d+)-(\d+) (\w): (\w+)""")
    File("2.txt").useLines { lines ->
        for (line in lines) {
            val match = re.matchEntire(line)!!
            val a = match.groups[1]!!.value.toInt()
            val b = match.groups[2]!!.value.toInt()
            val chr = match.groups[3]!!.value.single()
            val source = match.groups[4]!!.value
            val matchA = if (source[a - 1] == chr) 1 else 0
            val matchB = if (source[b - 1] == chr) 1 else 0
            if (matchA + matchB == 1) {
                ctr += 1
            }
        }
    }
    println(ctr)
}
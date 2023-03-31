import java.io.File

fun day4_1() {
    val content = File("4.txt").readText()
    val inputs = content.trim().replace("\r\n", "\n").split("\n\n")
    var ctr = 0
    val reqFields = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    for (input in inputs) {
        val parts = input.split(' ', '\n')
        var fields = 0
        for (part in parts) {
            val (k, _) = part.split(':')
            if (k in reqFields) {
                fields += 1
            }
        }
        if (fields == reqFields.size) {
            ctr += 1
        }
    }
    println(ctr)
}

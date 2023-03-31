import java.io.File

//byr (Birth Year) - four digits; at least 1920 and at most 2002.
//iyr (Issue Year) - four digits; at least 2010 and at most 2020.
//eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
//hgt (Height) - a number followed by either cm or in:
//If cm, the number must be at least 150 and at most 193.
//If in, the number must be at least 59 and at most 76.
//hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
//ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
//pid (Passport ID) - a nine-digit number, including leading zeroes.
//cid (Country ID) - ignored, missing or not.

private fun parseYear(s: String): Int? {
    if (s.length != 4) {
        return null
    }
    return s.toIntOrNull()
}

private fun validateByr(s: String): Boolean {
    val x = parseYear(s)
    return x != null && x in 1920..2002
}

private fun validateIyr(s: String): Boolean {
    val x = parseYear(s)
    return x != null && x in 2010..2020
}

private fun validateEyr(s: String): Boolean {
    val x = parseYear(s)
    return x != null && x in 2020..2030
}

private fun validateHgt(s: String): Boolean {
    val re = """(\d+)(cm|in)""".toRegex()
    val match = re.matchEntire(s)
    if (match == null) {
        return false
    }
    val heightNum = match.groups[1]!!.value.toIntOrNull()
    if (heightNum == null) {
        return false
    }
    if (match.groups[2]!!.value == "cm") {
        return heightNum in 150..193
    }
    return heightNum in 59..76
}

private fun validateHcl(s: String): Boolean {
    return """#[0-9a-f]{6}""".toRegex().matches(s)
}

private fun validateEcl(s: String): Boolean {
    return s in setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
}

private fun validatePid(s: String): Boolean {
    return """\d{9}""".toRegex().matches(s)
}

fun day4_2() {
    val content = File("4.txt").readText()
    val inputs = content.trim().replace("\r\n", "\n").split("\n\n")
    var ctr = 0
    val validators = mapOf(
        "byr" to ::validateByr,
        "iyr" to ::validateIyr,
        "eyr" to ::validateEyr,
        "hgt" to ::validateHgt,
        "hcl" to ::validateHcl,
        "ecl" to ::validateEcl,
        "pid" to ::validatePid
    )
    for (input in inputs) {
        val parts = input.split(' ', '\n')
        var fields = 0
        for (part in parts) {
            val (k, v) = part.split(':')
            if (validators.containsKey(k) && validators[k]!!(v)) {
                fields += 1
            }
        }
        if (fields == validators.size) {
            ctr += 1
        }
    }
    println(ctr)
}

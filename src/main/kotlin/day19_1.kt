package my.advent2020.day19part1

import java.io.File

private data class Rule(
    val id: Int,
    val options: List<List<Int>>,
    val literal: Char?,
) {
    companion object {
        fun options(id: Int, options: List<List<Int>>) = Rule(id, options, null)
        fun literal(id: Int, literal: Char) = Rule(id, listOf(), literal)
    }

    fun matches(line: String, rules: Map<Int, Rule>): Boolean {
        val matcher = RuleMatcher(line, rules)
        val result = matcher.tryMatch(this, 0)
        return result.success && result.charCount == line.length
    }
}

private data class RuleMatchResult(val success: Boolean, val charCount: Int)

private class RuleMatcher(val line: String, val rules: Map<Int, Rule>) {
    fun tryMatch(rule: Rule, startPos: Int): RuleMatchResult {
        if (startPos >= line.length) {
            return RuleMatchResult(false, 0)
        }
        if (rule.literal != null) {
            if (line[startPos] == rule.literal) {
                return RuleMatchResult(true, 1)
            }
            return RuleMatchResult(false, 0)
        }
        for (option in rule.options) {
            val maybeResult = tryMatchList(option, startPos)
            if (maybeResult.success) {
                return maybeResult
            }
        }
        return RuleMatchResult(false, 0)
    }

    private fun tryMatchList(list: List<Int>, startPos: Int): RuleMatchResult {
        var pos = startPos
        for (ruleId in list) {
            val matchResult = tryMatch(rules[ruleId]!!, pos)
            if (!matchResult.success) {
                return matchResult
            }
            pos += matchResult.charCount
        }
        return RuleMatchResult(true, pos - startPos)
    }
}

fun day19_1(inputFile: String) {
    // This is to convert from \r\n -> \n
    val text = File(inputFile).readLines().joinToString("\n")
    val (ruleLines, dataLines) = text.split("\n\n").map { it.split("\n") }
    val rulesRe =
        """(?<id>\d+): ("(?<literal>\w)"|(?<list>\d+(?: \d+)*)|(?<options>\d+(?: \d+)* \| \d+(?: \d+)*))""".toRegex()
    val rules = buildMap<Int, Rule> {
        for (rule in ruleLines) {
            val match = rulesRe.matchEntire(rule) ?: throw Exception("Unparsed rule: '$rule'")
            val id = match.groups["id"]!!.value.toInt()
            val ruleObj: Rule
            if (match.groups["literal"] != null) {
                ruleObj = Rule.literal(id, match.groups["literal"]!!.value.single())
            } else if (match.groups["list"] != null) {
                val singleList = match.groups["list"]!!.value.split(" ").map { it.toInt() }
                ruleObj = Rule.options(id, listOf(singleList))
            } else if (match.groups["options"] != null) {
                val options = match.groups["options"]!!.value.split("|").map { option ->
                    option.trim().split(" ").map { it.toInt() }
                }
                ruleObj = Rule.options(id, options)
            } else {
                throw Exception("unreachable")
            }
            put(id, ruleObj)
        }
    }
    var result = 0
    val rule0 = rules[0]!!
    for (line in dataLines) {
        if (rule0.matches(line, rules)) {
            println("$line +")
            result += 1
        } else {
            println("$line -")
        }
    }
    println(result)
}

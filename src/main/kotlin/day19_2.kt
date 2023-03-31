package my.advent2020.day19part2

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
        return result.charCount.any { it == line.length }
    }
}

private data class RuleMatchResult(val charCount: Set<Int>)

private class RuleMatcher(val line: String, val rules: Map<Int, Rule>) {
    fun tryMatch(rule: Rule, startPos: Int): RuleMatchResult {
        if (startPos >= line.length) {
            return RuleMatchResult(emptySet())
        }
        if (rule.literal != null) {
            if (line[startPos] == rule.literal) {
                return RuleMatchResult(setOf(1))
            }
            return RuleMatchResult(emptySet())
        }
        val result = buildSet {
            for (option in rule.options) {
                val maybeResult = tryMatchList(option, startPos)
                addAll(maybeResult.charCount)
            }
        }
        return RuleMatchResult(result)
    }

    private fun tryMatchList(list: List<Int>, startPos: Int): RuleMatchResult {
        if (list.isEmpty()) {
            return RuleMatchResult(setOf(0))
        }
        val ruleId = list.first()
        val matches = tryMatch(rules[ruleId]!!, startPos)
        val result = buildSet {
            for (charCount in matches.charCount) {
                val recResult = tryMatchList(list.slice(1..list.lastIndex), startPos + charCount)
                addAll(recResult.charCount.map { it + charCount })
            }
        }
        return RuleMatchResult(result)
    }
}

fun day19_2(inputFile: String) {
    // This is to convert from \r\n -> \n
    val text = File(inputFile).readLines().joinToString("\n")
    val (ruleLines, dataLines) = text.split("\n\n").map { it.split("\n") }
    val rulesRe =
        """(?<id>\d+): ("(?<literal>\w)"|(?<list>\d+(?: \d+)*)|(?<options>\d+(?: \d+)* \| \d+(?: \d+)*))""".toRegex()
    val rules = buildMap<Int, Rule> {
        for (rule in ruleLines) {
            val patchedRule = if (rule.startsWith("8:")) {
                "8: 42 | 42 8"
            } else if (rule.startsWith("11:")) {
                "11: 42 31 | 42 11 31"
            } else {
                rule
            }
            val match = rulesRe.matchEntire(patchedRule) ?: throw Exception("Unparsed rule: '$patchedRule'")
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

package my.advent2020.day24part2

import java.io.File

private data class Point(val x: Int, val y: Int) {
    operator fun plus(v: Vector): Point {
        return Point(v.x + x, v.y + y)
    }
}
private typealias Vector = Point

private fun dirToVector(dir: String): Vector {
    return when (dir) {
        "w" -> Vector(-1, 0)
        "e" -> Vector(1, 0)
        "nw" -> Vector(-1, -1)
        "ne" -> Vector(0, -1)
        "sw" -> Vector(0, 1)
        "se" -> Vector(1, 1)
        else -> throw Exception("dir $dir")
    }
}

private val allDirections =
    listOf(Vector(-1, 0), Vector(1, 0), Vector(-1, -1), Vector(0, -1), Vector(0, 1), Vector(1, 1))

private fun neighbors(p: Point): List<Point> {
    return allDirections.map { p + it }
}

private fun countNeighbors(points: Set<Point>, p: Point): Int {
    return neighbors(p).count { it in points }
}

fun day24_2(inputFile: String) {
    val lines = File(inputFile).readLines()
    val dirRe = """w|e|nw|ne|sw|se""".toRegex()
    val grid = mutableMapOf<Point, Boolean>()
    for (line in lines) {
        val dirs = dirRe.findAll(line).map { dirToVector(it.value) }
        val coords = dirs.fold(Point(0, 0)) { acc, v -> acc + v }
        grid[coords] = !(grid[coords] ?: false)
    }
    var blackPoints = buildSet {
        for ((p, v) in grid) {
            if (v) add(p)
        }
    }
    for (turn in 1..100) {
        val possiblePoints = buildSet {
            for (p in blackPoints) {
                add(p)
                addAll(neighbors(p))
            }
        }
        val newPoints = buildSet {
            for (p in possiblePoints) {
                if (p in blackPoints) {
                    if (countNeighbors(blackPoints, p) in 1..2) {
                        add(p)
                    }
                } else {
                    if (countNeighbors(blackPoints, p) == 2) {
                        add(p)
                    }
                }
            }
        }
        blackPoints = newPoints
    }
    val result = blackPoints.size
    println(result)
}

package my.advent2020.day17part1

import java.io.File
import kotlin.math.abs

private val maxSize = 100

private data class Point3d(val x: Int, val y: Int, val z: Int)

private class Grid3d {
    private val cells = mutableListOf<Int>()
    private var minX = 0
    private var maxX = 0
    private var minY = 0
    private var maxY = 0
    private var minZ = 0
    private var maxZ = 0

    private fun toIndex(p: Point3d): Int {
        val (x, y, z) = p
        val sx = if (x < 0) 1 else 0
        val sy = if (y < 0) 1 else 0
        val sz = if (z < 0) 1 else 0
        return (abs(z) * maxSize * maxSize + abs(y) * maxSize + abs(x)) * 8 + (sz * 4 + sy * 2 + sx)
    }

    fun get(p: Point3d): Int {
        val index = toIndex(p)
        if (index >= cells.size) {
            return 0
        }
        return cells[index]
    }

    fun set(p: Point3d, v: Int) {
        if (v != 0) {
            if (p.x < minX) minX = p.x
            if (p.x > maxX) maxX = p.x
            if (p.y < minY) minY = p.y
            if (p.y > maxY) maxY = p.y
            if (p.z < minZ) minZ = p.z
            if (p.z > maxZ) maxZ = p.z
        }
        val index = toIndex(p)
        if (index > cells.size) {
            cells.addAll(IntArray(index - cells.size).asList())
            cells.add(v)
        } else {
            cells[index] = v
        }
    }

    fun sumNeighbors(p: Point3d): Int {
        var sum = 0
        for (z in p.z - 1..p.z + 1) {
            for (y in p.y - 1..p.y + 1) {
                for (x in p.x - 1..p.x + 1) {
                    sum += get(Point3d(x, y, z))
                }
            }
        }
        sum -= get(p)
        return sum
    }

    fun forEachWithBorder(fn: (p: Point3d, v: Int) -> Unit) {
        for (z in minZ - 1..maxZ + 1) {
            for (y in minY - 1..maxY + 1) {
                for (x in minX - 1..maxX + 1) {
                    val p = Point3d(x, y, z)
                    fn(p, get(p))
                }
            }
        }
    }
}

fun day17_1(inputFile: String) {
    val lines = File(inputFile).readLines()
    var grid = Grid3d()
    for ((y, line) in lines.withIndex()) {
        for ((x, c) in line.withIndex()) {
            if (c == '#') {
                grid.set(Point3d(x, y, 0), 1)
            }
        }
    }
    for (turn in 1..6) {
        val newGrid = Grid3d()
        grid.forEachWithBorder { p, v ->
            val neighborCount = grid.sumNeighbors(p)
            if (neighborCount == 3 || neighborCount == 2 && v == 1) {
                newGrid.set(p, 1)
            }
        }
        grid = newGrid
    }
    var total = 0
    grid.forEachWithBorder { _, v -> total += v }
    println("$total")
}

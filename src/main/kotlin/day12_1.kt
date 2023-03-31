package my.advent2020.day12part1

import java.io.File
import kotlin.math.abs

private data class Order(val dir: Char, val distance: Int)
private data class Vec2(val x: Int, val y: Int)

private val dirs = listOf(Vec2(1, 0), Vec2(0, 1), Vec2(-1, 0), Vec2(0, -1))

fun day12_1(inputFile: String) {
    val orders = File(inputFile).readLines().map { Order(it[0], it.substring(1).toInt()) }
    var x = 0
    var y = 0
    var angle = 0
    for (order in orders) {
        when (order.dir) {
            'E' -> x += order.distance
            'N' -> y += order.distance
            'W' -> x -= order.distance
            'S' -> y -= order.distance
            'L' -> angle = (angle + order.distance / 90) % 4
            'R' -> {
                angle = (angle - order.distance / 90) % 4
                if (angle < 0) {
                    angle += 4
                }
            }

            'F' -> {
                val (dx, dy) = dirs[angle]
                x += order.distance * dx
                y += order.distance * dy
            }
        }
    }
    println("$x $y ${abs(x) + abs(y)}")
}

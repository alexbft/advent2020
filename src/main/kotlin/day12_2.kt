package my.advent2020.day12part2

import java.io.File
import kotlin.math.*

private data class Order(val dir: Char, val distance: Int)
private data class Vec2(val x: Int, val y: Int)

private fun rotate(x: Int, y: Int, angleDeg: Int): Vec2 {
    val angle = angleDeg.toDouble() * PI / 180.0
    val cosAngle = cos(angle)
    val sinAngle = sin(angle)
    val nx = x * cosAngle - y * sinAngle
    val ny = x * sinAngle + y * cosAngle
    return Vec2(nx.roundToInt(), ny.roundToInt())
}

fun day12_2(inputFile: String) {
    val orders = File(inputFile).readLines().map { Order(it[0], it.substring(1).toInt()) }
    var x = 0
    var y = 0
    var wayX = 10
    var wayY = 1
    for (order in orders) {
        when (order.dir) {
            'E' -> wayX += order.distance
            'N' -> wayY += order.distance
            'W' -> wayX -= order.distance
            'S' -> wayY -= order.distance
            'L' -> {
                val (nX, nY) = rotate(wayX, wayY, order.distance)
                wayX = nX
                wayY = nY
            }

            'R' -> {
                val (nX, nY) = rotate(wayX, wayY, -order.distance)
                wayX = nX
                wayY = nY
            }

            'F' -> {
                x += wayX * order.distance
                y += wayY * order.distance
            }
        }
    }
    println("$x $y ${abs(x) + abs(y)}")
}

package my.advent2020.day21part1

import java.io.File

private data class Dish(val ingredients: List<String>, val allergens: List<String>)

fun day21_1(inputFile: String) {
    val lines = File(inputFile).readLines()
    val dishes = buildList {
        for (line in lines) {
            val (left, right) = line.split(" (contains ")
            val ingredients = left.split(" ")
            val allergens = right.substring(0..right.length - 2).split(", ")
            add(Dish(ingredients, allergens))
        }
    }
    val allAllergens = buildSet {
        for (dish in dishes) {
            addAll(dish.allergens)
        }
    }
    val allergicIngredients = mutableSetOf<String>()
    for (allergen in allAllergens) {
        val dishesWithAllergen = dishes.filter { dish -> dish.allergens.contains(allergen) }
        val possibleIngredients =
            dishesWithAllergen.map { it.ingredients.toSet() }.reduce { acc, x -> acc.intersect(x) }
        if (possibleIngredients.isEmpty()) {
            throw Exception("Can't determine possible ingredients for allergen: $allergen")
        }
        allergicIngredients.addAll(possibleIngredients)
    }
    var result = 0
    for (dish in dishes) {
        for (ingredient in dish.ingredients) {
            if (ingredient !in allergicIngredients) {
                ++result
            }
        }
    }
    println("$result")
}

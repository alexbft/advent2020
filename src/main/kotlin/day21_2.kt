package my.advent2020.day21part2

import java.io.File

private data class Dish(val ingredients: List<String>, val allergens: List<String>)

fun day21_2(inputFile: String) {
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
    val possibleIngredientsByAllergen = mutableMapOf<String, Set<String>>()
    for (allergen in allAllergens) {
        val dishesWithAllergen = dishes.filter { dish -> dish.allergens.contains(allergen) }
        val possibleIngredients =
            dishesWithAllergen.map { it.ingredients.toSet() }.reduce { acc, x -> acc.intersect(x) }
        if (possibleIngredients.isEmpty()) {
            throw Exception("Can't determine possible ingredients for allergen: $allergen")
        }
        possibleIngredientsByAllergen[allergen] = possibleIngredients
    }
    val undeterminedIngredients = mutableSetOf<String>()
    for ((_, possibleIngredients) in possibleIngredientsByAllergen) {
        undeterminedIngredients.addAll(possibleIngredients)
    }
    val ingredientByAllergen = mutableMapOf<String, String>()
    while (undeterminedIngredients.isNotEmpty()) {
        var foundNewMatches = false
        for ((allergen, possibleIngredients) in possibleIngredientsByAllergen) {
            if (allergen in ingredientByAllergen) {
                continue
            }
            val undeterminedPossibleIngredients = possibleIngredients.intersect(undeterminedIngredients)
            if (undeterminedPossibleIngredients.isEmpty()) {
                throw Exception("Can't determine ingredient for allergen: $allergen")
            }
            if (undeterminedPossibleIngredients.size == 1) {
                val ingredient = undeterminedPossibleIngredients.single()
                ingredientByAllergen[allergen] = ingredient
                undeterminedIngredients.remove(ingredient)
                foundNewMatches = true
            }
        }
        if (!foundNewMatches) {
            throw Exception("infinite loop")
        }
    }
    val allergensAlphabetically = allAllergens.sorted()
    val result = allergensAlphabetically.joinToString(",") { allergen -> ingredientByAllergen[allergen]!! }
    println(result)
}

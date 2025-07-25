package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common

class Day7 {

    data class Equation(val sum: Long, val parts: List<Long>)

    data class SolvedEquation(val permutations: List<Long>, val equation: Equation)

    enum class OPERATIONS() {
        SUM, MULTIPLY
    }

    private val input = Common.readInput(7).readLines()

    private val equations = input.map { line ->
        val split = line.split(":")
        val sum = split[0].trim().toLong()
        val parts = split[1].trim().split(" ").map { it.toLong() }
        Equation(sum, parts)
    }

    private fun calculateEquationPermutations(parts: List<Long>, sum: Long): List<Long> {
        if (parts.isEmpty()) return listOf(sum)

        val mul = sum * parts.first()
        val add = sum + parts.first()
        return calculateEquationPermutations(parts.drop(1), mul) + calculateEquationPermutations(parts.drop(1), add)
    }

    private fun calculateEquationPermutationsWithCatenation(parts: List<Long>, sum: Long): List<Long> {
        if (parts.isEmpty()) return listOf(sum)

        val mul = sum * parts.first()
        val add = sum + parts.first()
        val catenated = sum.toString() + parts.first().toString()
        return calculateEquationPermutationsWithCatenation(parts.drop(1), mul) +
                calculateEquationPermutationsWithCatenation(parts.drop(1), add) +
                calculateEquationPermutationsWithCatenation(parts.drop(1), catenated.toLong())
    }



    private fun part1(): Long {
        return equations.map { eq ->
            val permutations = calculateEquationPermutations(eq.parts, 0)
            SolvedEquation(permutations, eq)
        }.filter {
            it.permutations.contains(it.equation.sum)
        }.sumOf { it.equation.sum }
    }

    private fun part2(): Long {
        return equations.map { eq ->
            val permutations = calculateEquationPermutationsWithCatenation(eq.parts, 0)
            SolvedEquation(permutations, eq)
        }.filter {
            it.permutations.contains(it.equation.sum)
        }.sumOf { it.equation.sum }
    }

    fun run() {

        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")

    }
}

fun main() {
    Day7().run()
}
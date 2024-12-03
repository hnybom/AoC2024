package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common

class Day3 {

    private val input = Common.readInput(3).readText()

    private val multiplyRegx = """mul\((\d+),(\d+)\)""".toRegex()
    private val doRegx = """do\(\)""".toRegex()
    private val dontRegx = """don't\(\)""".toRegex()

    fun doMultiply(match : MatchResult) : Long {
        val (a, b) = match.destructured
        return a.toLong() * b.toLong()
    }

    fun executeStringMultiplication(input : String) : Long {
        val matches = multiplyRegx.findAll(input)
        return matches.sumOf { match ->
            doMultiply(match)
        }
    }

    fun part1() : Long {
        return executeStringMultiplication(input)
    }

    fun part2() : Long {
        val donts = input.split(dontRegx)
        val firstSum = executeStringMultiplication(donts[0])

        val doSums = donts.sumOf { dont ->
            dont.split(doRegx).drop(1).sumOf { match ->
                executeStringMultiplication(match)
            }
        }
        return firstSum + doSums

    }

    fun run() {
        println("Day 1")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }
}

fun main() {
    Day3().run()
}
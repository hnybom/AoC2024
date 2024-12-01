package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import kotlin.math.abs

class Day1 {

    private val input = Common.readInput(1).readLines()
        .map {
            val split = it.split("   ")
            Pair(split[0].trim().toLong(), split[1].trim().toLong())
        }

    fun part1() : Long {
        val firstList = input.map { it.first }.sorted()
        val secondList = input.map { it.second }.sorted()
        return firstList.zip(secondList).map { abs(it.first - it.second) }.sum()
    }

    fun part2() : Long {
        val firstList = input.map { it.first }
        val secondList = input.map { it.second }
        val countMap = secondList.associateWith { value ->
            secondList.count { it == value }
        }

        return firstList.sumOf { value ->
            countMap.getOrDefault(value, 0) * value
        }
    }


    fun run() {
        println("Day 1")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

}

fun main() {
    Day1().run()
}
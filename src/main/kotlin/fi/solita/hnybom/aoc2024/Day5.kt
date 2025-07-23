package fi.solita.hnybom.aoc2024

import fi.solita.hnybom.aoc2024.common.Common
import kotlin.math.abs

class Day5 {

    data class Rule(val before: Int, val after: Int)
    data class PageOrder(val order: List<Int>)

    private val input = Common.readInput(5).readLines()

    val rules = input.takeWhile { it.isNotBlank() }.map {
        val split = it.split("|")
        Rule(split[0].toInt(), split[1].toInt())
    }

    val orders = input.dropWhile { it.isNotBlank() }.drop(1).map {
        PageOrder(it.split(",").map { it.toInt() })
    }

    fun validateOrder(order: PageOrder) : Boolean {
        val all = rules.all { rule ->
            val before = order.order.indexOf(rule.before)
            val after = order.order.indexOf(rule.after)
            if (before == -1 || after == -1) true
            else before < after
        }
        return all
    }

    fun reOrder(order: PageOrder) : PageOrder {
        val newOrder = order.order.toMutableList()
        rules.forEach { rule ->
            val before = newOrder.indexOf(rule.before)
            val after = newOrder.indexOf(rule.after)
            if (before != -1 && after != -1 && before > after) {
                newOrder[before] = rule.after
                newOrder[after] = rule.before
            }
        }
        val reOrdered = PageOrder(newOrder)
        return if(!validateOrder(reOrdered)) reOrder(reOrdered)
        else reOrdered
    }

    fun part1() : Int {
        val filter = orders.filter { validateOrder(it) }
        return filter.sumOf { it.order[it.order.size / 2] }
    }

    fun part2() : Int {
        val filter = orders.filter { !validateOrder(it) }
        val reOrdered = filter.map { reOrder(it) }
        return reOrdered.sumOf {  it.order[it.order.size / 2] }
    }


    fun run() {
        println("Day 1")
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

}

fun main() {
    Day5().run()
}
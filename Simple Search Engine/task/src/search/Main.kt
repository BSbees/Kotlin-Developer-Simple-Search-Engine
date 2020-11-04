package search

import java.io.File
import java.util.*

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    val searchEngine = SearchEngine.initDb(findDataPath(args))

    while (true) {
        printBanner()
        when (scanner.nextLine()) {
            "1" -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val strategy = Strategy.valueOf(scanner.nextLine())
                println("Enter a name or email to search all suitable people.")
                val data = scanner.nextLine().toLowerCase().split(" ")
                searchEngine.findPerson(data, strategy)
            }
            "2" -> searchEngine.printDb()
            "0" -> break
            else -> println("Incorrect option! Try again.")
        }
    }
}

fun printBanner() {
    print("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit\n")
}

fun findDataPath(args: Array<String>): String {
    for (arg in args) {
        if (arg == "--data") {
            return args[arg.indexOf(arg) + 1]
        }
    }
    throw IllegalArgumentException("Cannot find --data in ${args.joinToString()}")
}

class SearchEngine(private val db: List<List<String>>) {

    private val invertedIndex = initIndex()

    private fun initIndex(): Map<String, List<Int>> {
        val map = mutableMapOf<String, MutableList<Int>>()
        db.forEachIndexed { index, list ->
            list.forEach {
                map.computeIfAbsent(it.toLowerCase()) {
                    mutableListOf()
                }.add(index)
            }
        }
        return map.toMap()
    }

    fun findPerson(query: List<String>, strategy: Strategy) {
        val results = query.map(::getIndexWithData)
                .toList()

        val searchIndexes = collectResults(results, strategy)
        if (searchIndexes.isEmpty()) {
            print("No matching people found.\n")
        } else {
            println()
            print("Found people:\n")
            searchIndexes.map(db::get)
                    .map { it.joinToString(" ") }
                    .forEach(::println)
        }
        println()
    }

    private fun collectResults(results: List<List<Int>>, strategy: Strategy): List<Int> {
        return when (strategy) {
            Strategy.ANY -> results.flatten().distinct()
            Strategy.ALL -> results.reduce(::bothPresent)
            Strategy.NONE -> {
                val indexList = db.indices.toMutableList()
                results.forEach {
                    indexList.removeAll(it)
                }
                return indexList
            }
        }
    }

    private fun bothPresent(list1: List<Int>, list2: List<Int>): List<Int> {
        return list1.intersect(list2).toList()
    }

    fun printDb() {
        print("=== List of people ===\n")
        db.map { it.joinToString(" ") }
                .forEach(::println)
    }

    private fun getIndexWithData(data: String): List<Int> =
            invertedIndex.entries
                    .filter { entry -> entry.key == data }
                    .flatMap { entry -> entry.value }
                    .distinct()

    companion object {
        fun initDb(path: String): SearchEngine {
            return SearchEngine(File(path).readLines()
                    .map { it.split(" ") })
        }
    }
}

enum class Strategy {
    ALL, ANY, NONE
}

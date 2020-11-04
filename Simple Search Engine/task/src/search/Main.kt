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
                println("Enter a name or email to search all suitable people.")
                val data = scanner.nextLine().toLowerCase()
                searchEngine.findPerson(data)
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

    fun findPerson(data: String) {
        val search = invertedIndex.entries
                .filter { entry -> entry.key == data }
                .flatMap { entry -> entry.value }
                .distinct()
                .map ( db::get )
                .map { it.joinToString(" ") }
                .toList()
        if (search.isEmpty()) {
            print("No matching people found.\n")
        } else {
            println()
            print("Found people:\n")
            search.forEach(::println)
        }
        println()
    }

    fun printDb() {
        print("=== List of people ===\n")
        db.map { it.joinToString(" ") }
                .forEach(::println)
    }

    companion object {
        fun initDb(path: String): SearchEngine {
            return SearchEngine(File(path).readLines()
                    .map { it.split(" ") })
        }
    }
}
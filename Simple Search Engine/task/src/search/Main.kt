package search

import java.io.File
import java.util.*
import java.util.stream.Collectors

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    val db = initDb(args)

    while (true) {
        printBanner()
        when (scanner.nextLine()) {
            "1" -> findPerson(db)
            "2" -> printDb(db)
            "0" -> break
            else -> println("Incorrect option! Try again.")
        }
    }
}

fun printBanner() {
    print("=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit\n")
}

fun findPerson(db: List<List<String>>) {
    println("Enter a name or email to search all suitable people.")
    val data = scanner.nextLine().toLowerCase()
    val search = db.stream()
            .filter { t -> t.any { it.toLowerCase().contains(data.toLowerCase()) } }
            .collect(Collectors.toList())
    if (search.isEmpty()) {
        print("No matching people found.\n")
    } else {
        println()
        print("Found people:\n")
        search.stream()
                .map { it.joinToString(" ") }
                .forEach(::println)
    }
    println()
}

fun printDb(db: List<List<String>>) {
    print("=== List of people ===\n")
    db.map { it.joinToString(" ") }
            .forEach(::println)
}

fun initDb(args: Array<String>): List<List<String>> {
    val path = findDataPath(args)
    return File(path).readLines()
            .map { it.split(" ") }
}

fun findDataPath(args: Array<String>): String {
    for (arg in args) {
        if (arg == "--data") {
            return args[arg.indexOf(arg) + 1]
        }
    }
    throw IllegalArgumentException("Cannot find --data in ${args.joinToString()}")
}

package search

import java.util.*
import java.util.stream.Collectors

val scanner = Scanner(System.`in`)

fun main() {
    val db = initDb()

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

fun findPerson(db: MutableList<List<String>>) {
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

fun printDb(db: MutableList<List<String>>) {
    print("=== List of people ===\n")
    db.map { it.joinToString(" ") }
            .forEach(::println)
}

fun initDb(): MutableList<List<String>> {
    print("Enter the number of people:\n")
    val noOfPeople = scanner.nextInt()
    print("Enter all people:\n")
    scanner.nextLine()
    val db = mutableListOf<List<String>>()
    repeat(noOfPeople) {
        db.add(scanner.nextLine().split(" "))
    }
    println()
    return db
}

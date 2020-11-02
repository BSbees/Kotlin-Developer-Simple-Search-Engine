package search

import java.util.*
import java.util.stream.Collectors

val scanner = Scanner(System.`in`)

fun main() {
    val db = initDb()

    print("Enter the number of search queries:\n")
    val noOfQueries = scanner.nextInt()
    println()
    repeat(noOfQueries) {
        println("Enter data to search people:")
        val data = scanner.next().toLowerCase()
        val search = db.stream()
                .filter { t -> t.any { it.toLowerCase().contains(data.toLowerCase())} }
                .collect(Collectors.toList())
        if (search.isEmpty()) {
            print("No matching people found.\n")
        } else {
            println()
            print("Found people:\n")
            search.stream()
                    .map { it.joinToString(" ")}
                    .forEach(::println)
        }
        println()
    }
}

private fun initDb(): MutableList<List<String>> {
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

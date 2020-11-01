package search

import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)
    val line = scanner.nextLine()
    val word = scanner.nextLine()

    var index = 1
    var result = 0

    for (s in line.split(" ")) {
        if (s == word) {
            result = index
            break
        }
        index++
    }

    if (result == 0) {
        print("Not found")
    } else {
        print(result)
    }
}

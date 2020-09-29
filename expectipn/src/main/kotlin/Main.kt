import arrow.core.Either

fun main() {
    runCatching { doSomethingButErrorHappen() }
            .onFailure { println(it.message) }
            .onSuccess { println("execution success") }

    doSomethingsCatching(listOf(1, 2, 3, 4, 5)).map { result ->
        result.map { println(it) }
    }
    when (val either = doSomethingButErrorHappenWithEither()) {
        is Either.Left -> println(either.a.message)
        is Either.Right -> println(either.b)
    }

    val either = doSomethingButErrorHappenWithEither()
    either.fold({ println(it.message) }, { println("eeee : $it") })

    listOf(1, 2, 3, 4, 5)
            .map {
                doSomethingsMightErrorHappenWithEither(it)
                        .fold(
                                { f -> println(f) },
                                { s -> println(s) }
                        )
            }

}

private fun doSomethingButErrorHappen(): String = throw RuntimeException("execute failed")

private fun doSomethingButErrorHappenWithEither(): Either<Exception, String> = Either.left(RuntimeException("execute failed"))

private fun doSomethingsCatching(numbers: List<Int>): List<Result<String>> = numbers.map {
    runCatching {
        doSomethingsMightErrorHappen(it)
    }
}

private fun doSomethingsMightErrorHappen(number: Int): String {
    if (number == 3) {
        throw RuntimeException(number.toString())
    }
    return number.toString()
}


private fun doSomethingsMightErrorHappenWithEither(number: Int): Either<Exception, String> {
    if (number == 3) {
        return Either.left(RuntimeException(number.toString()))
    }
    return Either.right(number.toString())
}

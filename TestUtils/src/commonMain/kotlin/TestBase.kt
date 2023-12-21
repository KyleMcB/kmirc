/*
 * Copyright 2024 Kyle McBurnett
 */

data class Test(val name: String, val testLambda: suspend () -> Unit) {
    override fun hashCode(): Int = name.hashCode()

    override fun equals(other: Any?): Boolean = name == other
    override fun toString(): String = name
}

abstract class TestBase {
    private val list = mutableSetOf<Test>()
    fun addTest(test: Test) {
        if (list.contains(test)) {
            throw IllegalArgumentException(
                """
                Test: $test
                has already been added
            """.trimIndent()
            )
        }
        list.add(test)
    }

    suspend fun runAllTest() {
        list.forEach {
            println("Running $it")
            try {
                it.testLambda()
                println("Test Passed")

            } catch (e: Throwable) {
                println(
                    """
                        Test Failed
                    """.trimIndent()
                )
                throw e
            }
        }
    }
}
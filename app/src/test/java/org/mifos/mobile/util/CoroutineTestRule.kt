import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutineTestRule(
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestRule, TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                Dispatchers.setMain(testDispatcher)
                base?.evaluate()
                cleanupTestCoroutines()
                Dispatchers.resetMain()
            }
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
        testDispatcher.runBlockingTest(block)
    }
}

package tasks

import contributors.MockGitLabService
import contributors.expectedResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class Request4SuspendKtTest {
    @Test
    fun testSuspend() = runBlockingTest {
        val startTime = currentTime
        val result = loadContributorsSuspend(MockGitLabService, testRequestData)
        Assert.assertEquals("Wrong result for 'loadContributorsSuspend'", expectedResults.users, result)
        val totalTime = currentTime - startTime
        Assert.assertEquals(
            "The calls run consequently," +
                    "so the total virtual time should be 4000 ms: ",
            expectedResults.timeFromStart, totalTime
        )
    }
}
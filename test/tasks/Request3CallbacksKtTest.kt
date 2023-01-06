package tasks

import contributors.MockGitLabService
import contributors.expectedResults
import contributors.testRequestData
import org.junit.Assert
import org.junit.Test

class Request3CallbacksKtTest {
    @Test
    fun testDataIsLoaded() {
        loadContributorsCallbacks(MockGitLabService, testRequestData) {
            Assert.assertEquals(
                "Wrong result for 'loadContributorsCallbacks'",
                expectedResults.users, it
            )
        }
    }
}
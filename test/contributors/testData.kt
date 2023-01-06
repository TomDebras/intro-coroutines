package contributors

val testRequestData = RequestData("token", "org")

data class TestRepo(val name: String, val delay: Long, val users: List<User>)

data class TestResults(val timeFromStart: Long, val users: List<User>)

const val reposDelay = 1000L

val testRepos = listOf(
    TestRepo(
        "repo-1", 1000, listOf(
            User("user-1", 10),
            User("user-2", 20)
        )
    ),
    TestRepo(
        "repo-2", 1200, listOf(
            User("user-2", 30),
            User("user-1", 40)
        )
    ),
    TestRepo(
        "repo-3", 800, listOf(
            User("user-2", 50),
            User("user-3", 60)
        )
    )
)


val repos = testRepos.mapIndexed { index, testRepo -> Repo(index.toLong(), testRepo.name) }

val reposMap = testRepos.associate { it.name to it }

val expectedResults = TestResults(
    4000, // 1000 + (1000 + 1200 + 800)
    listOf(
        User("user-2", 100),
        User("user-3", 60),
        User("user-1", 50)
    )
)

val expectedConcurrentResults = TestResults(
    2200, // 1000 + max(1000, 1200, 800)
    expectedResults.users
)

val progressResults = listOf(
    TestResults(
        2000, // 1000 + 1000
        listOf(User(name = "user-2", commits = 20), User(name = "user-1", commits = 10))
    ),
    TestResults(
        3200, // 2000 + 1200
        listOf(User(name = "user-2", commits = 50), User(name = "user-1", commits = 50))
    ),
    expectedResults
)

val concurrentProgressResults = listOf(
    TestResults(
        1800, // 1000 + 800
        listOf(User(name = "user-3", commits = 60), User(name = "user-2", commits = 50))
    ),
    TestResults(
        2000, // 1000 + max(800, 1000)
        listOf(User(name = "user-2", commits = 70), User(name = "user-3", commits = 60),
            User(name = "user-1", commits = 10))
    ),
    expectedConcurrentResults
)
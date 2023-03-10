package tasks

import contributors.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

@OptIn(DelicateCoroutinesApi::class)
suspend fun loadContributorsNotCancellable(service: GitLabService, req: RequestData): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val deferreds: List<Deferred<List<User>>> = repos.map { repo ->
        GlobalScope.async(Dispatchers.Default) {
            log("starting loading for ${repo.name}")
            delay(3000)
            service.getRepoContributors(repo.id.toString())
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }
    return deferreds.awaitAll().flatten().aggregate()
}
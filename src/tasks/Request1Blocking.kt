package tasks

import contributors.*
import retrofit2.Response

fun loadContributorsBlocking(service: GitLabService, req: RequestData) : List<User> {
    val allRepos = mutableListOf<Repo>()
    var page = 1
    var hasMore = true
    while (hasMore) {
        val response = service.getOrgReposCall(req.org, page, 20).execute().also {logRepos(req, it)}
        val repos = response.body()
        if (repos != null) {
            allRepos.addAll(repos)
        }
        page++
        hasMore = repos != null && repos.size == 20
    }

    return allRepos.flatMap { repo ->
        service
            .getRepoContributorsCall(repo.id.toString())
            .execute() // Executes request and blocks the current thread
            .also { logUsers(repo, it) }
            .bodyList()
    }.aggregate()
}

fun <T> Response<List<T>>.bodyList(): List<T> {
    return body() ?: emptyList()
}
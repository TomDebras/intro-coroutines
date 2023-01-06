package tasks

import contributors.*

suspend fun loadContributorsSuspend(service: GitLabService, req: RequestData): List<User> {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    return repos.flatMap { repo ->
        service.getRepoContributors(repo.id.toString())
            .also { logUsers(repo, it) }
            .bodyList()
    }.aggregate()
}
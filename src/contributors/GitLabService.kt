package contributors

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface GitLabService {
    @GET("groups/{org}/projects")
    fun getOrgReposCall(
        @Path("org") org: String
    ): Call<List<Repo>>

    @GET("projects/{repo}/repository/contributors")
    fun getRepoContributorsCall(
        @Path("repo") repo: String
    ): Call<List<User>>

    @GET("groups/{org}/projects")
    suspend fun getOrgRepos(
        @Path("org") org: String
    ): Response<List<Repo>>

    @GET("projects/{repo}/repository/contributors")
    suspend fun getRepoContributors(
        @Path("repo") repo: String
    ): Response<List<User>>
}

@Serializable
data class Repo(
    val id: Long,
    val name: String
)

@Serializable
data class User(
    val name: String,
    val commits: Int
)

@Serializable
data class RequestData(
    val token: String,
    val org: String
)

@OptIn(ExperimentalSerializationApi::class)
fun createGitLabService(AccessToken: String): GitLabService {
    val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .header("Authorization", "Bearer $AccessToken")
            val request = builder.build()
            chain.proceed(request)
        }
        .build()

    val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://gitlab.com/api/v4/")
        .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
        .client(httpClient)
        .build()

    return retrofit.create(GitLabService::class.java)
}

package com.example.baddit.data.remote

import com.example.baddit.data.dto.auth.ChangePasswordRequestBody
import com.example.baddit.data.dto.auth.EmailVerificationRequestBody
import com.example.baddit.data.dto.auth.LoginRequestBody
import com.example.baddit.data.dto.auth.RegisterRequestBody
import com.example.baddit.data.dto.comment.CommentCommentRequestBody
import com.example.baddit.data.dto.comment.EditCommentRequestBody
import com.example.baddit.data.dto.comment.PostCommentRequestBody
import com.example.baddit.data.dto.comment.VoteCommentRequestBody
import com.example.baddit.data.dto.community.CreateRequestBody
import com.example.baddit.data.dto.community.ModerateMemberRequestBody
import com.example.baddit.data.dto.posts.PostEditRequestBody
import com.example.baddit.data.dto.posts.VotePostRequestBody
import com.example.baddit.domain.model.auth.GetMeResponseDTO
import com.example.baddit.domain.model.auth.GetOtherResponseDTO
import com.example.baddit.domain.model.auth.LoginResponseDTO
import com.example.baddit.domain.model.comment.CommentResponseDTO
import com.example.baddit.domain.model.community.GetACommunityResponseDTO
import com.example.baddit.domain.model.community.GetCommunityListResponseDTO
import com.example.baddit.domain.model.community.Members
import com.example.baddit.domain.model.community.Moderators
import com.example.baddit.domain.model.posts.PostResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File

interface BadditAPI {
    @GET("/v1/posts")
    suspend fun getPosts(
        @Query("communityName") communityName: String? = null,
        @Query("authorName") authorName: String? = null,
        @Query("cursor") cursor: String? = null,
        @Query("postTitle") postTitle: String? = null
    ): Response<PostResponseDTO>

    @GET("/v1/posts")
    suspend fun getPost(@Query("postId") postId: String): Response<PostResponseDTO>

    @POST("/v1/posts/{postId}/votes")
    suspend fun votePost(
        @Path("postId") postId: String,
        @Body voteBody: VotePostRequestBody
    ): Response<Unit>

    @POST("/v1/auth/login")
    suspend fun login(@Body loginBody: LoginRequestBody): Response<LoginResponseDTO>

    @POST("/v1/auth/signup")
    suspend fun signup(@Body loginBody: RegisterRequestBody): Response<Unit>

    @POST("/v1/auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("/v1/auth/verification")
    suspend fun verify(@Body tokenBody: EmailVerificationRequestBody): Response<Unit>

    @GET("/v1/users/me")
    suspend fun getMe(): Response<GetMeResponseDTO>

    @GET("v1/users/{username}")
    suspend fun getOther(@Path("username") username: String): Response<GetOtherResponseDTO>


    @Multipart
    @POST("/v1/posts")
    suspend fun upLoadPost(
        @Part("title") title: RequestBody ,
        @Part("content") content: RequestBody,
        @Part("type") type: RequestBody,
        @Part("communityName") communityName: RequestBody?,
        @Part image: MultipartBody.Part?
    )
    :Response<Unit>

    @GET("/v1/comments")
    suspend fun getComments(
        @Query("postId") postId: String?=null,
        @Query("commentId") commentId: String? = null,
        @Query("authorName") authorName: String? = null,
        @Query("cursor") cursor: String? = null
    ): Response<CommentResponseDTO>

    // community
    @GET("v1/communities")
    suspend fun getCommunities(): Response<GetCommunityListResponseDTO>

    @GET("v1/communities/{communityName}")
    suspend fun getCommunity(@Path("communityName") communityName: String): Response<GetACommunityResponseDTO>

    @POST("v1/communities")
    suspend fun createCommunity(@Body createRequestBody: CreateRequestBody): Response<Unit>

    @POST("/v1/comments/votes")
    suspend fun voteComment(@Body voteBody: VoteCommentRequestBody): Response<Unit>

    @PATCH("/v1/auth/update-password")
    suspend fun changePassword(@Body changePasswordBody: ChangePasswordRequestBody): Response<Unit>

    @POST("/v1/comments")
    suspend fun replyPost(@Body replyBody: PostCommentRequestBody): Response<Unit>

    @POST("/v1/comments")
    suspend fun replyComment(@Body replyBody: CommentCommentRequestBody): Response<Unit>

    @PUT("/v1/posts/{postId}")
    suspend fun editPost(@Path("postId") postId: String, @Body content: PostEditRequestBody): Response<Unit>

    @DELETE("/v1/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: String): Response<Unit>

    @DELETE("/v1/comments/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: String): Response<Unit>

    @PUT("/v1/comments")
    suspend fun editComment(@Body editBody: EditCommentRequestBody): Response<Unit>

    @POST("v1/communities/{communityName}/members")
    suspend fun joinCommunity(@Path("communityName") communityName: String): Response<Unit>

    @DELETE("v1/communities/{communityName}/members")
    suspend fun leaveCommunity(@Path("communityName") communityName: String): Response<Unit>
    @Multipart
    @POST("v1/communities/{communityName}/banner")
    suspend fun uploadBanner(
        @Path("communityName") communityName: String,
        @Part banner: MultipartBody.Part
    ): Response<Unit>
    @Multipart
    @POST("v1/communities/{communityName}/logo")
    suspend fun uploadLogo(
        @Path("communityName") communityName: String,
        @Part logo: MultipartBody.Part
    ): Response<Unit>

    @DELETE("v1/communities/{communityName}")
    suspend fun deleteCommunity(@Path("communityName") communityName: String): Response<Unit>


    @Multipart
    @POST("/v1/users/avatar")
    suspend fun updateAvatar(
        @Part avatar: MultipartBody.Part
    ):Response<Unit>

    @GET("v1/communities/{communityName}/members")
    suspend fun getMembers(@Path("communityName") communityName: String): Response<Members>

    @GET("v1/communities/{communityName}/moderators")
    suspend fun getModerators(@Path("communityName") communityName: String): Response<Moderators>

    @POST("/v1/communities/{communityName}/moderators")
    suspend fun moderateMember(
        @Path("communityName") communityName: String,
        @Body moderateBody : ModerateMemberRequestBody
    ): Response<Unit>

    @DELETE("/v1/communities/{communityName}/moderators/{memberName}")
    suspend fun unModerateMember(
        @Path("communityName") communityName: String,
        @Path("memberName") memberName: String
    ): Response<Unit>
}
package com.nailmind.app.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Streaming

interface NailMindApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @GET("api/auth/me")
    suspend fun authMe(): AuthMeResponse

    @POST("api/auth/logout")
    suspend fun logout(): StatusResponse

    @GET("api/users/style-profile")
    suspend fun styleProfile(): StyleProfileDto

    @PUT("api/users/style-profile")
    suspend fun updateStyleProfile(@Body request: UpdateStyleProfileRequest): StyleProfileDto

    @POST("api/users/style-profile/skip")
    suspend fun skipStyleProfile(): StyleProfileDto

    @POST("api/events")
    suspend fun trackEvent(@Body request: TrackEventRequest): TrackEventResponse

    @GET("api/home")
    suspend fun home(): HomeResponse

    @GET("api/styles")
    suspend fun styles(@Query("tag") tag: String? = null): StylesResponse

    @GET("api/styles/search")
    suspend fun searchStyles(@Query("q") query: String): SearchResponse

    @POST("api/assistant/meimei/chat")
    suspend fun meimeiChat(@Body request: MeimeiChatRequest): MeimeiChatResponse

    @Streaming
    @Headers("Accept: text/event-stream")
    @POST("api/assistant/meimei/chat/stream")
    suspend fun meimeiChatStream(@Body request: MeimeiChatRequest): Response<ResponseBody>

    @GET("api/styles/{styleId}")
    suspend fun styleDetail(@Path("styleId") styleId: String): StyleDetailResponse

    @GET("api/favorites")
    suspend fun favorites(): StylesResponse

    @POST("api/favorites/{styleId}")
    suspend fun addFavorite(@Path("styleId") styleId: String): FavoriteToggleResponse

    @DELETE("api/favorites/{styleId}")
    suspend fun removeFavorite(@Path("styleId") styleId: String): FavoriteToggleResponse

    @Multipart
    @POST("api/tryon/upload-hand")
    suspend fun uploadHandImage(@Part file: MultipartBody.Part): TryOnUploadResponse

    @GET("api/tryon/hand-images")
    suspend fun handImages(): HandImagesResponse

    @POST("api/tryon/try-on")
    suspend fun syncTryOn(@Body request: SyncTryOnRequest): SyncTryOnResponse

    @GET("api/tryon/history")
    suspend fun tryOnHistory(): TryOnHistoryResponse

    @GET
    suspend fun resultImageByUrl(@Url url: String): Response<okhttp3.ResponseBody>

    @Multipart
    @POST("api/try-on/uploads")
    suspend fun uploadTryOnImage(@Part file: MultipartBody.Part): LegacyTryOnUploadResponse

    @GET("api/try-on/jobs")
    suspend fun tryOnJobs(): TryOnJobsResponse

    @POST("api/try-on/jobs")
    suspend fun createTryOnJob(@Body request: CreateTryOnJobRequest): TryOnJobDto

    @GET("api/try-on/jobs/{jobId}")
    suspend fun tryOnJob(@Path("jobId") jobId: String): TryOnJobDto

    @GET("api/try-on/jobs/{jobId}/result")
    suspend fun tryOnResult(@Path("jobId") jobId: String): TryOnJobDto

    @POST("api/try-on/jobs/{jobId}/rerender")
    suspend fun rerenderTryOn(@Path("jobId") jobId: String, @Body request: RerenderTryOnJobRequest): TryOnJobDto

    @GET("api/stores")
    suspend fun stores(@Query("styleId") styleId: String? = null): StoresResponse

    @GET("api/stores/{storeId}")
    suspend fun storeDetail(@Path("storeId") storeId: String): StoreDto

    @GET("api/stores/{storeId}/slots")
    suspend fun storeSlots(@Path("storeId") storeId: String): StoreSlotsResponse

    @GET("api/stores/{storeId}/restoration-performance")
    suspend fun restorationPerformance(@Path("storeId") storeId: String): RestorationPerformanceResponse

    @POST("api/chats")
    suspend fun startChat(@Body request: ChatStartRequest): ChatStartResponse

    @GET("api/chats/{conversationId}/messages")
    suspend fun chatMessages(@Path("conversationId") conversationId: String): ChatMessagesResponse

    @POST("api/chats/{conversationId}/messages")
    suspend fun sendChatMessage(@Path("conversationId") conversationId: String, @Body request: ChatMessageCreateRequest): ChatMessageResponse

    @GET("api/bookings")
    suspend fun bookings(): BookingsResponse

    @POST("api/bookings")
    suspend fun createBooking(@Body request: BookingRequest): BookingDto

    @GET("api/bookings/{bookingId}")
    suspend fun booking(@Path("bookingId") bookingId: String): BookingDto

    @POST("api/bookings/{bookingId}/confirm")
    suspend fun confirmBooking(@Path("bookingId") bookingId: String): BookingDto

    @POST("api/bookings/{bookingId}/review")
    suspend fun reviewBooking(@Path("bookingId") bookingId: String, @Body request: BookingReviewRequest): BookingReviewDto

    @Multipart
    @POST("api/bookings/{bookingId}/review-image")
    suspend fun uploadBookingReviewImage(
        @Path("bookingId") bookingId: String,
        @Part file: MultipartBody.Part
    ): BookingReviewImageUploadResponse

    @GET("api/profile")
    suspend fun profile(): ProfileResponse

    @GET("api/settings")
    suspend fun settings(): SettingsResponse

    @GET("api/try-on/jobs/{jobId}/result-image")
    suspend fun resultImage(@Path("jobId") jobId: String): Response<okhttp3.ResponseBody>
}

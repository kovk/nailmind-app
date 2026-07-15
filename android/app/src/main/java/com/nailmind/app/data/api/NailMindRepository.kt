package com.nailmind.app.data.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import com.nailmind.app.data.config.AppConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NailMindRepository(
    private val service: NailMindApiService = NailMindApiClient.service
) {
    suspend fun register(name: String, email: String, password: String): AuthResponse =
        service.register(RegisterRequest(name = name, email = email, password = password))

    suspend fun login(email: String, password: String): AuthResponse =
        service.login(AuthRequest(email = email, password = password))

    suspend fun authMe(): AuthMeResponse = service.authMe()

    suspend fun logout(): StatusResponse = service.logout()

    suspend fun styleProfile(): StyleProfileDto = service.styleProfile()

    suspend fun updateStyleProfile(request: UpdateStyleProfileRequest): StyleProfileDto =
        service.updateStyleProfile(request)

    suspend fun skipStyleProfile(): StyleProfileDto = service.skipStyleProfile()

    suspend fun trackEvent(
        eventName: String,
        deviceId: String,
        sessionId: String,
        styleId: String? = null,
        storeId: String? = null,
        sourcePage: String? = null,
        sourceChannel: String? = null,
        payload: Map<String, Any>? = null
    ): TrackEventResponse = service.trackEvent(
        TrackEventRequest(
            eventName = eventName,
            deviceId = deviceId,
            styleId = styleId,
            storeId = storeId,
            sourcePage = sourcePage,
            sourceChannel = sourceChannel,
            sessionId = sessionId,
            payload = payload
        )
    )

    suspend fun home(): HomeResponse = service.home().normalized()

    suspend fun styles(tag: String? = null): StylesResponse = service.styles(tag).let { response ->
        response.copy(items = response.items.map { it.normalized() })
    }

    suspend fun searchStyles(query: String): SearchResponse = service.searchStyles(query).let { response ->
        response.copy(items = response.items.map { it.normalized() })
    }

    suspend fun meimeiChat(
        message: String,
        handImageUrl: String? = null,
        handImageKey: String? = null,
        lastHandAnalysis: MeimeiHandAnalysisDto? = null,
        history: List<MeimeiChatHistoryItemDto> = emptyList()
    ): MeimeiChatResponse = service.meimeiChat(
        MeimeiChatRequest(
            message = message,
            handImageUrl = handImageUrl,
            handImageKey = handImageKey,
            lastHandAnalysis = lastHandAnalysis,
            history = history
        )
    ).normalized()

    fun meimeiChatStream(
        message: String,
        handImageUrl: String? = null,
        handImageKey: String? = null,
        lastHandAnalysis: MeimeiHandAnalysisDto? = null,
        history: List<MeimeiChatHistoryItemDto> = emptyList()
    ): Flow<MeimeiStreamEvent> = flow {
        val response = service.meimeiChatStream(
            MeimeiChatRequest(
                message = message,
                handImageUrl = handImageUrl,
                handImageKey = handImageKey,
                lastHandAnalysis = lastHandAnalysis,
                history = history
            )
        )
        if (!response.isSuccessful) {
            val errorBody = response.errorBody()?.string().orEmpty()
            throw IOException("小美流式请求失败: HTTP ${response.code()} $errorBody")
        }
        val body = response.body() ?: throw IOException("小美流式响应为空")
        val parser = MeimeiSseParser()
        body.source().use { source ->
            var eventName = "message"
            val data = StringBuilder()

            while (!source.exhausted()) {
                val line = source.readUtf8Line() ?: break
                when {
                    line.isBlank() -> {
                        parser.parse(eventName, data.toString())?.let { event ->
                            emit(
                                if (event is MeimeiStreamEvent.Result) {
                                    event.copy(response = event.response.normalized())
                                } else {
                                    event
                                }
                            )
                        }
                        eventName = "message"
                        data.clear()
                    }
                    line.startsWith("event:") -> eventName = line.substringAfter(':').trim()
                    line.startsWith("data:") -> {
                        if (data.isNotEmpty()) data.append('\n')
                        data.append(line.substringAfter(':').trimStart())
                    }
                }
            }

            parser.parse(eventName, data.toString())?.let { event ->
                emit(
                    if (event is MeimeiStreamEvent.Result) {
                        event.copy(response = event.response.normalized())
                    } else {
                        event
                    }
                )
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun styleDetail(styleId: String): StyleDetailResponse = service.styleDetail(styleId).let { response ->
        response.copy(style = response.style.normalized())
    }

    suspend fun favorites(): StylesResponse = service.favorites().let { response ->
        response.copy(items = response.items.map { it.normalized() })
    }

    suspend fun setFavorite(styleId: String, favorited: Boolean): FavoriteToggleResponse {
        return if (favorited) service.addFavorite(styleId) else service.removeFavorite(styleId)
    }

    suspend fun uploadHandImage(file: File): TryOnUploadResponse {
        val requestBody = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return service.uploadHandImage(part)
    }

    suspend fun handImages(): HandImagesResponse = service.handImages().let { response ->
        response.copy(hands = response.hands.map { it.normalized() })
    }

    suspend fun syncTryOn(
        handId: String? = null,
        handImageId: Int? = null,
        styleId: Int,
        selectedLength: String,
        selectedShape: String
    ): SyncTryOnResponse = service.syncTryOn(
        SyncTryOnRequest(
            handId = handId,
            handImageId = handImageId,
            styleId = styleId,
            selectedLength = selectedLength,
            selectedShape = selectedShape
        )
    ).normalized()

    suspend fun tryOnHistory(): TryOnHistoryResponse = service.tryOnHistory().let { response ->
        response.copy(items = response.items.map { it.normalized() })
    }

    suspend fun resultImageBytesByUrl(url: String): ByteArray {
        val response = service.resultImageByUrl(AppConfig.normalizeMediaUrl(url) ?: url)
        if (!response.isSuccessful) {
            error("加载试戴结果失败: HTTP ${response.code()}")
        }
        return response.body()?.bytes() ?: error("试戴结果为空")
    }

    suspend fun uploadAsyncTryOnImage(file: File): LegacyTryOnUploadResponse {
        val requestBody = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return service.uploadTryOnImage(part)
    }

    suspend fun tryOnJobs(): TryOnJobsResponse = service.tryOnJobs().let { response ->
        response.copy(items = response.items.map { it.normalized() })
    }

    suspend fun createTryOnJob(
        styleId: String,
        sourceImageKey: String?,
        selectedLength: String,
        selectedShape: String
    ): TryOnJobDto = service.createTryOnJob(
        CreateTryOnJobRequest(
            styleId = styleId,
            sourceImageKey = sourceImageKey,
            selectedLength = selectedLength,
            selectedShape = selectedShape
        )
    ).normalized()

    suspend fun tryOnJob(jobId: String): TryOnJobDto = service.tryOnJob(jobId).normalized()

    suspend fun tryOnResult(jobId: String): TryOnJobDto = service.tryOnResult(jobId).normalized()

    suspend fun rerenderTryOn(jobId: String, selectedLength: String?, selectedShape: String?): TryOnJobDto =
        service.rerenderTryOn(
            jobId,
            RerenderTryOnJobRequest(selectedLength = selectedLength, selectedShape = selectedShape)
        ).normalized()

    suspend fun tryOnResultImageBytes(jobId: String): ByteArray {
        val response = service.resultImage(jobId)
        if (!response.isSuccessful) {
            error("加载试戴结果失败: HTTP ${response.code()}")
        }
        return response.body()?.bytes() ?: error("试戴结果为空")
    }

    suspend fun stores(): StoresResponse = service.stores()

    suspend fun storeDetail(storeId: String): StoreDto = service.storeDetail(storeId)

    suspend fun storeSlots(storeId: String): StoreSlotsResponse = service.storeSlots(storeId)

    suspend fun restorationPerformance(storeId: String): RestorationPerformanceResponse =
        service.restorationPerformance(storeId)

    suspend fun startChat(storeId: String, initialMessage: String = ""): ChatStartResponse =
        service.startChat(ChatStartRequest(storeId = storeId, initialMessage = initialMessage))

    suspend fun chatMessages(conversationId: String): ChatMessagesResponse = service.chatMessages(conversationId)

    suspend fun sendChatMessage(conversationId: String, body: String): ChatMessageResponse =
        service.sendChatMessage(conversationId, ChatMessageCreateRequest(body = body))

    suspend fun bookings(): BookingsResponse = service.bookings()

    suspend fun createBooking(
        storeId: String,
        styleId: String,
        slot: String,
        name: String,
        phone: String,
        note: String
    ): BookingDto = service.createBooking(
        BookingRequest(
            storeId = storeId,
            styleId = styleId,
            slot = slot,
            name = name,
            phone = phone,
            note = note
        )
    )

    suspend fun booking(bookingId: String): BookingDto = service.booking(bookingId)

    suspend fun confirmBooking(bookingId: String): BookingDto = service.confirmBooking(bookingId)

    suspend fun reviewBooking(
        bookingId: String,
        satisfactionScore: Int,
        actualWorkImageUrl: String
    ): BookingReviewDto = service.reviewBooking(
        bookingId,
        BookingReviewRequest(
            satisfactionScore = satisfactionScore,
            actualWorkImageUrl = actualWorkImageUrl
        )
    )

    suspend fun uploadBookingReviewImage(bookingId: String, file: File): BookingReviewImageUploadResponse {
        val requestBody = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        return service.uploadBookingReviewImage(bookingId, part)
    }

    suspend fun profile(): ProfileResponse = service.profile()

    suspend fun settings(): SettingsResponse = service.settings()
}

internal fun HomeResponse.normalized(): HomeResponse {
    val legacySceneStyles = sceneStyles.normalizeStyles()
    val currentSceneSections = sceneSections.normalizeStyles()

    return HomeResponse(
        hotKeywords = hotKeywords.orEmpty(),
        recommended = recommended.orEmpty().map { it.normalized() },
        hot = hot.orEmpty().map { it.normalized() },
        sceneSections = legacySceneStyles + currentSceneSections,
        sceneStyles = legacySceneStyles,
        ranking = ranking.orEmpty(),
        heatRanking = heatRanking.orEmpty(),
        tryOnRanking = tryOnRanking.orEmpty(),
        bookingRanking = bookingRanking.orEmpty(),
        trends = trends.orEmpty(),
        trendTopics = trendTopics.orEmpty(),
        trendKeywords = trendKeywords.orEmpty(),
        trendsUpdatedAt = trendsUpdatedAt.orEmpty()
    )
}

private fun Map<String, List<StyleDto>?>?.normalizeStyles(): Map<String, List<StyleDto>> =
    orEmpty()
        .mapValues { (_, styles) -> styles.orEmpty().map { it.normalized() } }
        .filterValues { it.isNotEmpty() }

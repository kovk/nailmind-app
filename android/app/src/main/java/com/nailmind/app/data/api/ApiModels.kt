package com.nailmind.app.data.api

import com.nailmind.app.data.config.AppConfig

data class AuthRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthUserDto(
    val name: String,
    val email: String,
    val preferences: List<String> = emptyList(),
    val needsStyleProfileOnboarding: Boolean = false
)

data class AuthResponse(
    val token: String,
    val user: AuthUserDto
)

data class AuthMeResponse(
    val user: AuthUserDto
)

data class StatusResponse(
    val status: String
)

data class StyleProfileTaxonomyDto(
    val nailShapes: List<String> = emptyList(),
    val colorFamilies: List<String> = emptyList(),
    val styles: List<String> = emptyList(),
    val effects: List<String> = emptyList(),
    val scenes: List<String> = emptyList()
)

data class StyleProfileDto(
    val nailShapes: List<String> = emptyList(),
    val colorFamilies: List<String> = emptyList(),
    val styles: List<String> = emptyList(),
    val effects: List<String> = emptyList(),
    val scenes: List<String> = emptyList(),
    val onboardingCompleted: Boolean = false,
    val updatedAt: String? = null,
    val taxonomy: StyleProfileTaxonomyDto? = null
)

data class UpdateStyleProfileRequest(
    val nailShapes: List<String> = emptyList(),
    val colorFamilies: List<String> = emptyList(),
    val styles: List<String> = emptyList(),
    val effects: List<String> = emptyList(),
    val scenes: List<String> = emptyList()
)

data class TrackEventRequest(
    val eventName: String,
    val deviceId: String? = null,
    val styleId: String? = null,
    val storeId: String? = null,
    val sourcePage: String? = null,
    val sourceChannel: String? = null,
    val sessionId: String? = null,
    val payload: Map<String, @JvmSuppressWildcards Any>? = null,
    val occurredAt: String? = null
)

data class TrackEventResponse(
    val eventId: String
)

data class HomeResponse(
    val hotKeywords: List<String>,
    val recommended: List<StyleDto>,
    val hot: List<StyleDto>,
    val sceneSections: Map<String, List<StyleDto>?>? = emptyMap(),
    val sceneStyles: Map<String, List<StyleDto>?>? = emptyMap(),
    val ranking: List<StyleRankingItemDto> = emptyList(),
    val heatRanking: List<StyleRankingItemDto> = emptyList(),
    val tryOnRanking: List<StyleRankingItemDto> = emptyList(),
    val bookingRanking: List<StyleRankingItemDto> = emptyList(),
    val trends: List<StyleRankingItemDto> = emptyList(),
    val trendTopics: List<TrendTopicDto> = emptyList(),
    val trendKeywords: List<String> = emptyList(),
    val trendsUpdatedAt: String = ""
)

data class TrendTopicDto(
    val id: String = "",
    val name: String = "",
    val summary: String = "",
    val styles: List<StyleDto> = emptyList(),
    val updatedAt: String = ""
)

data class StyleRankingItemDto(
    val rank: Int = 0,
    val score: Int = 0,
    val impressions: Int = 0,
    val clicks: Int = 0,
    val favorites: Int = 0,
    val tryOns: Int = 0,
    val bookings: Int = 0,
    val style: StyleDto
)

data class StyleTagGroupsDto(
    val nailShapes: List<String> = emptyList(),
    val vibes: List<String> = emptyList(),
    val effects: List<String> = emptyList(),
    val scenes: List<String> = emptyList()
)

data class StyleDto(
    val id: String,
    val name: String,
    val vibe: String = "",
    val price: String = "",
    val nailType: String = "",
    val skinTone: String = "",
    val tags: List<String> = emptyList(),
    val displayTags: List<String>? = emptyList(),
    val tagGroups: StyleTagGroupsDto? = null,
    val colors: List<String> = emptyList(),
    val imageUrl: String? = null,
    val tryOnStyleId: Int? = null
)

data class StylesResponse(
    val items: List<StyleDto>
)

data class SearchResponse(
    val query: String,
    val items: List<StyleDto>
)

data class MeimeiChatRequest(
    val message: String,
    val handImageUrl: String? = null,
    val handImageKey: String? = null,
    val lastHandAnalysis: MeimeiHandAnalysisDto? = null,
    val history: List<MeimeiChatHistoryItemDto> = emptyList()
)

data class MeimeiChatHistoryItemDto(
    val role: String,
    val content: String
)

data class MeimeiEntryDto(
    val type: String = "",
    val target: String = "",
    val label: String = ""
)

data class MeimeiHandAnalysisDto(
    val hasHand: Boolean = false,
    val skinTone: String = "",
    val skinUndertone: String = "",
    val handShape: String = "",
    val fingerProportion: String = "",
    val nailBed: String = "",
    val visibleNails: Int = 0,
    val imageQuality: String = "",
    val recommendedShapes: List<String> = emptyList(),
    val recommendedColors: List<String> = emptyList(),
    val recommendedStyles: List<String> = emptyList(),
    val reason: String = "",
    val confidence: Double = 0.0,
    val status: String = ""
)

data class MeimeiRecommendationDto(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val reason: String = "",
    val entry: MeimeiEntryDto? = null,
    val tags: List<String> = emptyList(),
    val nailType: String = "",
    val colorTone: String? = null
)

data class MeimeiChatResponse(
    val reply: String,
    val intent: String,
    val entry: MeimeiEntryDto? = null,
    val handAnalysis: MeimeiHandAnalysisDto? = null,
    val recommendedShapes: List<String> = emptyList(),
    val recommendedColors: List<String> = emptyList(),
    val recommendations: List<MeimeiRecommendationDto> = emptyList()
)

fun MeimeiRecommendationDto.normalized(): MeimeiRecommendationDto = copy(
    imageUrl = AppConfig.normalizeMediaUrl(imageUrl)
)

fun MeimeiChatResponse.normalized(): MeimeiChatResponse = copy(
    recommendations = recommendations.map { it.normalized() }
)

sealed interface MeimeiStreamEvent {
    data class Status(val stage: String, val message: String) : MeimeiStreamEvent
    data class Delta(val text: String) : MeimeiStreamEvent
    data class Result(val response: MeimeiChatResponse) : MeimeiStreamEvent
    data class Error(val message: String) : MeimeiStreamEvent
    data object Done : MeimeiStreamEvent
}

data class StyleDetailResponse(
    val style: StyleDto,
    val userCases: String,
    val canFavorite: Boolean,
    val canTryOn: Boolean,
    val favorited: Boolean
)

data class FavoriteToggleResponse(
    val styleId: String,
    val favorited: Boolean
)

data class TryOnUploadResponse(
    val hand_id: String,
    val image_url: String? = null,
    val db_id: Int,
    val message: String? = null
)

data class LegacyTryOnUploadResponse(
    val objectKey: String,
    val fileName: String? = null
)

data class HandImageDto(
    val id: String,
    val dbId: Int,
    val imageUrl: String,
    val sourceType: String,
    val skinTone: String,
    val handType: String
)

data class HandImagesResponse(
    val hands: List<HandImageDto>,
    val total: Int
)

data class SyncTryOnRequest(
    val handId: String? = null,
    val handImageId: Int? = null,
    val styleId: Int,
    val selectedLength: String = "natural_short",
    val selectedShape: String = "squoval"
)

data class SyncTryOnResponse(
    val result_url: String,
    val duration_ms: Int,
    val style_name: String,
    val source: String
)

data class TryOnHistoryItemDto(
    val id: String,
    val jobId: String? = null,
    val resultUrl: String,
    val durationMs: Int,
    val styleName: String,
    val styleId: String,
    val source: String,
    val selectedLength: String,
    val selectedShape: String,
    val detectedTraits: Map<String, String>? = null,
    val createdAt: String
)

fun StyleDto.normalized(): StyleDto = StyleDto(
    id = id.orEmpty(),
    name = name.orEmpty(),
    vibe = vibe.orEmpty(),
    price = price.orEmpty(),
    nailType = nailType.orEmpty(),
    skinTone = skinTone.orEmpty(),
    tags = tags.orEmpty(),
    displayTags = displayTags.orEmpty(),
    tagGroups = tagGroups,
    colors = colors.orEmpty(),
    imageUrl = AppConfig.normalizeMediaUrl(imageUrl),
    tryOnStyleId = tryOnStyleId
)

fun HandImageDto.normalized(): HandImageDto = copy(imageUrl = AppConfig.normalizeMediaUrl(imageUrl).orEmpty())

fun SyncTryOnResponse.normalized(): SyncTryOnResponse = copy(result_url = AppConfig.normalizeMediaUrl(result_url).orEmpty())

fun TryOnHistoryItemDto.normalized(): TryOnHistoryItemDto = copy(
    resultUrl = AppConfig.normalizeTryOnPreviewUrl(resultUrl, jobId).orEmpty()
)

data class TryOnHistoryResponse(
    val items: List<TryOnHistoryItemDto>,
    val total: Int
)

data class CreateTryOnJobRequest(
    val styleId: String,
    val sourceImageKey: String? = null,
    val selectedLength: String = "natural_short",
    val selectedShape: String = "squoval"
)

data class RerenderTryOnJobRequest(
    val selectedLength: String? = null,
    val selectedShape: String? = null
)

data class TryOnJobsResponse(
    val items: List<TryOnJobDto>
)

data class TryOnJobDto(
    val id: String,
    val userId: String,
    val styleId: String,
    val styleName: String,
    val sourceImageKey: String,
    val status: String,
    val stage: String,
    val progress: Int,
    val selectedLength: String,
    val selectedShape: String,
    val resultImageKey: String? = null,
    val detectedTraits: Map<String, String>? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val completedAt: String? = null,
    val resultImageUrl: String? = null,
    val results: List<TryOnJobResultDto> = emptyList(),
    val allResultsReady: Boolean = true,
    val firstResultDurationMs: Int? = null
)

fun TryOnJobDto.normalized(): TryOnJobDto = copy(
    resultImageUrl = AppConfig.normalizeMediaUrl(resultImageUrl),
    results = results.map { it.copy(resultImageUrl = AppConfig.normalizeMediaUrl(it.resultImageUrl)) }
)

data class TryOnJobResultDto(
    val provider: String,
    val label: String,
    val status: String,
    val resultImageUrl: String? = null,
    val durationMs: Int? = null,
    val errorMessage: String? = null,
    val completedAt: String? = null
)

data class StoreDto(
    val id: String,
    val name: String,
    val distance: String,
    val priceBand: String,
    val score: String,
    val slots: List<String>,
    val openHours: String,
    val artists: Int,
    val works: String
)

data class RestorationDimensionsDto(
    val similarity: Double,
    val color: Double,
    val detail: Double,
    val satisfaction: Double
)

data class RestorationCategoryDto(
    val styleCategory: String,
    val status: String,
    val sampleSize: Int,
    val minimumSampleSize: Int,
    val compositeScore: Double? = null,
    val dimensions: RestorationDimensionsDto? = null
)

data class RestorationPerformanceResponse(
    val storeId: String,
    val categories: List<RestorationCategoryDto> = emptyList()
)

data class StoresResponse(
    val items: List<StoreDto>
)

data class StoreSlotsResponse(
    val storeId: String,
    val slots: List<String>
)

data class ChatStartRequest(
    val storeId: String,
    val initialMessage: String = ""
)

data class ChatMessageCreateRequest(
    val body: String
)

data class ChatConversationDto(
    val id: String,
    val storeId: String,
    val storeName: String = "",
    val customerName: String = "",
    val customerEmail: String = "",
    val status: String = "open",
    val lastMessage: String = "",
    val lastMessageAt: String? = null,
    val createdAt: String = "",
    val updatedAt: String = ""
)

data class ChatMessageDto(
    val id: String,
    val conversationId: String,
    val senderRole: String,
    val senderUserId: Int? = null,
    val body: String,
    val createdAt: String,
    val readAt: String? = null
)

data class ChatStartResponse(
    val conversation: ChatConversationDto,
    val messages: List<ChatMessageDto> = emptyList()
)

data class ChatMessagesResponse(
    val conversation: ChatConversationDto,
    val items: List<ChatMessageDto> = emptyList()
)

data class ChatMessageResponse(
    val message: ChatMessageDto,
    val conversation: ChatConversationDto
)
data class BookingRequest(
    val storeId: String,
    val styleId: String,
    val slot: String,
    val name: String,
    val phone: String,
    val note: String = ""
)

data class BookingsResponse(
    val items: List<BookingDto>
)

data class BookingDto(
    val id: String,
    val status: String,
    val storeId: String,
    val storeName: String,
    val styleId: String,
    val styleName: String,
    val styleImageUrl: String? = null,
    val slot: String,
    val price: String,
    val name: String,
    val phone: String,
    val note: String,
    val createdAt: String,
    val confirmedAt: String? = null,
    val canReview: Boolean = false,
    val review: BookingReviewDto? = null
)

data class BookingReviewRequest(
    val satisfactionScore: Int,
    val actualWorkImageUrl: String
)

data class BookingReviewImageUploadResponse(val imageUrl: String)

data class BookingReviewDto(
    val id: String,
    val bookingId: String,
    val storeId: String,
    val styleId: String,
    val styleCategory: String,
    val similarityScore: Int,
    val colorScore: Int,
    val detailScore: Int,
    val satisfactionScore: Int,
    val comment: String = "",
    val actualWorkImageUrl: String? = null,
    val createdAt: String,
    val evaluationStatus: String = "completed",
    val evaluationSource: String = "ai",
    val evaluationModel: String? = null,
    val evaluationNote: String = ""
)

data class ProfileResponse(
    val profile: AuthUserDto,
    val favoritesCount: Int,
    val bookingCount: Int,
    val tryOnCount: Int
)

data class SettingsResponse(
    val stylePreferences: String,
    val notifications: String,
    val privacy: String
)

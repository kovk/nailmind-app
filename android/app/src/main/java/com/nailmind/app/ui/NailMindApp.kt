package com.nailmind.app.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.nailmind.app.BuildConfig
import com.nailmind.app.R
import com.nailmind.app.data.api.AuthResponse
import com.nailmind.app.data.api.AuthUserDto
import com.nailmind.app.data.api.BookingDto
import com.nailmind.app.data.api.BookingReviewDto
import com.nailmind.app.data.api.ChatMessageDto
import com.nailmind.app.data.api.HomeResponse
import com.nailmind.app.data.api.MeimeiChatResponse
import com.nailmind.app.data.api.MeimeiChatHistoryItemDto
import com.nailmind.app.data.api.MeimeiHandAnalysisDto
import com.nailmind.app.data.api.MeimeiRecommendationDto
import com.nailmind.app.data.api.MeimeiStreamEvent
import com.nailmind.app.data.api.NailMindApiClient
import com.nailmind.app.data.api.NailMindRepository
import com.nailmind.app.data.api.RestorationCategoryDto
import com.nailmind.app.data.api.SettingsResponse
import com.nailmind.app.data.api.StoreDto
import com.nailmind.app.data.api.StyleProfileTaxonomyDto
import com.nailmind.app.data.api.UpdateStyleProfileRequest
import com.nailmind.app.data.api.StyleTagGroupsDto
import com.nailmind.app.data.api.StyleDto
import com.nailmind.app.data.api.StyleRankingItemDto
import com.nailmind.app.data.api.TrendTopicDto
import com.nailmind.app.data.api.TryOnHistoryItemDto
import com.nailmind.app.data.api.TryOnJobDto
import com.nailmind.app.data.config.AppConfig
import com.nailmind.app.ui.theme.RoseAccent
import com.nailmind.app.ui.theme.RoseTint
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal const val BOOKING_REVIEW_REWARD_COPY = "完成评价，下次美甲立减 5 元"

private enum class MainTab(
    val title: String,
    val icon: ImageVector
) {
    Home("首页", Icons.Rounded.Home),
    Styles("款式", Icons.Rounded.GridView),
    TryOn("小美", Icons.Rounded.AutoAwesome),
    Booking("预约", Icons.Rounded.CalendarMonth),
    Profile("我的", Icons.Rounded.Person)
}

private sealed interface Screen {
    data object Login : Screen
    data object Register : Screen
    data class Tab(val tab: MainTab) : Screen
    data class StyleDetail(val styleId: String) : Screen
    data object Search : Screen
    data class SearchResult(val query: String) : Screen
    data object StyleFilter : Screen
    data object StyleFilterResult : Screen
    data object Ranking : Screen
    data object Trends : Screen
    data class TrendDetail(val topicId: String) : Screen
    data class TryOnUpload(val styleId: String, val fromFavorites: Boolean = false) : Screen
    data class TryOnProcessing(val styleId: String, val jobId: String) : Screen
    data class TryOnResult(val styleId: String, val jobId: String) : Screen
    data object TryOnHistory : Screen
    data object DiyDesigner : Screen
    data object Xiaomei : Screen
    data object Favorites : Screen
    data object BookingRecords : Screen
    data object Reviews : Screen
    data class StyleProfile(val onboarding: Boolean = false) : Screen
    data class StoreDetail(val storeId: String, val styleId: String? = null) : Screen
    data class Chat(val conversationId: String, val storeId: String, val storeName: String) : Screen
    data class BookingForm(val storeId: String, val styleId: String, val fixedStore: Boolean = false) : Screen
    data class BookingConfirm(val bookingId: String) : Screen
    data class BookingSuccess(val bookingId: String) : Screen
    data object Settings : Screen
}

private data class AuthUser(
    val name: String,
    val email: String,
    val preferences: List<String> = emptyList(),
    val needsStyleProfileOnboarding: Boolean = false
)

private data class NailStyle(
    val id: String,
    val name: String,
    val vibe: String,
    val price: String,
    val nailType: String,
    val skinTone: String,
    val colors: List<Color>,
    val tags: List<String>,
    val displayTags: List<String> = emptyList(),
    val tagGroups: StyleTagGroupsDto = StyleTagGroupsDto(),
    val imageUrl: String? = null,
    val tryOnStyleId: Int? = null
)

private data class RankedStyle(
    val rank: Int,
    val score: Int,
    val impressions: Int,
    val clicks: Int,
    val favorites: Int,
    val tryOns: Int,
    val bookings: Int,
    val style: NailStyle
)

private data class TrendTopic(
    val id: String,
    val name: String,
    val summary: String,
    val styles: List<NailStyle>,
    val updatedAt: String,
    val badge: String? = null,
    val provenance: String? = null
)

private data class ActivityBanner(
    val imageRes: Int,
    val contentDescription: String,
    val onClick: () -> Unit
)

internal enum class StyleBrowseTab(val title: String) {
    NailShape("甲型"),
    Effect("效果"),
    Vibe("风格")
}

internal data class StyleBrowseOption(
    val label: String,
    val keywords: List<String>
)

internal data class StyleFilterSelection(
    val tab: StyleBrowseTab,
    val option: StyleBrowseOption
)

private val nailShapeBrowseOptions = listOf(
    StyleBrowseOption("短方", listOf("短方")),
    StyleBrowseOption("短圆", listOf("短圆")),
    StyleBrowseOption("中方", listOf("中方")),
    StyleBrowseOption("中椭圆", listOf("中椭圆")),
    StyleBrowseOption("中短梯", listOf("中短梯")),
    StyleBrowseOption("长梯", listOf("长梯")),
    StyleBrowseOption("长椭圆", listOf("长椭圆"))
)

private val effectBrowseOptions = listOf(
    StyleBrowseOption("法式", listOf("法式")),
    StyleBrowseOption("渐变", listOf("渐变", "彩虹", "星空")),
    StyleBrowseOption("猫眼", listOf("猫眼", "银河", "流星")),
    StyleBrowseOption("纯色", listOf("纯色", "经典红", "奶油白", "裸色", "珍珠白")),
    StyleBrowseOption("手绘", listOf("手绘", "樱花", "花", "纹理")),
    StyleBrowseOption("镜面", listOf("镜面", "金", "香槟", "玫瑰金")),
    StyleBrowseOption("浮雕", listOf("浮雕", "大理石", "丝绒")),
    StyleBrowseOption("钻饰", listOf("钻", "闪钻", "奢华", "亮片"))
)

private val vibeBrowseOptions = listOf(
    StyleBrowseOption("韩系", listOf("韩系")),
    StyleBrowseOption("日系", listOf("日系")),
    StyleBrowseOption("中式", listOf("中式")),
    StyleBrowseOption("欧美", listOf("欧美")),
    StyleBrowseOption("节庆", listOf("节庆")),
    StyleBrowseOption("甜美", listOf("甜美")),
    StyleBrowseOption("酷感", listOf("酷感")),
    StyleBrowseOption("极繁", listOf("极繁"))
)

private data class StoreRecommendationUi(
    val isNewStore: Boolean = false,
    val newStoreProtection: Boolean = false,
    val rank: Int = Int.MAX_VALUE,
    val reason: String = ""
)

private data class Store(
    val id: String,
    val name: String,
    val distance: String,
    val priceBand: String,
    val score: String,
    val slots: List<String>,
    val openHours: String,
    val artists: Int,
    val works: String,
    val avatarRes: Int? = null,
    val coverTone: Color = Color(0xFFEAD8D1),
    val reviewCount: Int = 0,
    val statusText: String = "营业中",
    val address: String = "",
    val area: String = "",
    val tags: List<String> = emptyList(),
    val nearestSlot: String = slots.firstOrNull().orEmpty(),
    val matchScore: Int = 88,
    val couponText: String = "新客预约立减",
    val salesText: String = "",
    val recommendation: StoreRecommendationUi = StoreRecommendationUi()
)

private data class BookingRecord(
    val id: String,
    val status: String,
    val storeName: String,
    val styleId: String,
    val styleName: String,
    val styleImageUrl: String?,
    val slot: String,
    val canReview: Boolean,
    val review: BookingReviewDto?
)

private data class ReviewItem(
    val id: String,
    val storeName: String,
    val styleName: String,
    val styleImageUrl: String? = null,
    val slot: String,
    val rating: Int? = null,
    val comment: String = "",
    val similarityScore: Int? = null,
    val colorScore: Int? = null,
    val detailScore: Int? = null,
    val evaluationNote: String = ""
)

private data class BookingReviewDraft(
    val satisfactionScore: Int,
    val actualWorkFile: File
)
private data class UserSettings(
    val stylePreferences: String,
    val notifications: String,
    val privacy: String
)

private data class TryOnStatus(
    val jobId: String = "",
    val styleId: String = "",
    val styleName: String = "",
    val stage: String = "",
    val progress: Int = 0,
    val status: String = "",
    val analysisContext: TryOnAnalysisContext = TryOnAnalysisContext(),
    val errorMessage: String? = null
)

internal enum class TryOnLength(val label: String, val isShort: Boolean = false) {
    NaturalShort("自然短款", true),
    Medium("自然中长款"),
    Long("修长款"),
    Unknown("")
}

internal enum class TryOnShape(val label: String, val elongates: Boolean = false, val widens: Boolean = false) {
    Squoval("方圆甲"),
    Round("圆甲"),
    Oval("椭圆甲", elongates = true),
    Almond("杏仁甲", elongates = true),
    Square("方甲", widens = true),
    Unknown("")
}

internal data class DetectedHandTraits(
    val skinTone: String = "",
    val skinUndertone: String = "",
    val handShape: String = "",
    val nailBed: String = "",
    val colorHarmonyVerdict: String = "",
    val colorHarmonyReason: String = "",
    val colorHarmonySuggestion: String = ""
)

internal data class TryOnAnalysisContext(
    val length: TryOnLength = TryOnLength.Unknown,
    val shape: TryOnShape = TryOnShape.Unknown,
    val traits: DetectedHandTraits = DetectedHandTraits()
)

private data class TryOnRenderedResult(
    val provider: String,
    val label: String,
    val bitmap: Bitmap
)

internal data class TryOnAnalysis(
    val colorHarmony: String,
    val shapeAndLength: String,
    val outfitAdvice: String
)

private const val tryOnNotificationChannelId = "tryon_updates"

private fun ensureTryOnNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val manager = context.getSystemService(NotificationManager::class.java) ?: return
    val channel = NotificationChannel(
        tryOnNotificationChannelId,
        "试戴提醒",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "用于提醒试戴结果已生成"
    }
    manager.createNotificationChannel(channel)
}

private fun canPostNotifications(context: Context): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
}

private fun showTryOnReadyNotification(context: Context, styleName: String) {
    ensureTryOnNotificationChannel(context)
    if (!canPostNotifications(context)) return
    val notification = NotificationCompat.Builder(context, tryOnNotificationChannelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("试戴已完成")
        .setContentText("$styleName 已生成效果图，可前往“我的 - 试戴记录”查看")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()
    NotificationManagerCompat.from(context).notify(styleName.hashCode(), notification)
}

private val styles: List<NailStyle> = emptyList()

private fun mockDrawableUri(name: String): String = "android.resource://com.nailmind.app/drawable/$name"

private val acetateTrendCandidates = listOf(
    NailStyle(
        id = "1-11",
        name = "月光贝壳法式",
        vibe = "裸粉清透底叠加乳白法式边与月光贝壳碎片，轻盈精致。",
        price = "",
        nailType = "中椭圆",
        skinTone = "自然肤色、暖白皮",
        colors = listOf(Color(0xFFF5D9D4), Color(0xFFF7F4EE)),
        tags = listOf("中椭圆", "韩系", "甜美", "渐变", "钻饰", "日常", "约会"),
        displayTags = listOf("韩系", "甜美"),
        imageUrl = mockDrawableUri("acetate_candidate_01")
    ),
    NailStyle(
        id = "2-11",
        name = "裸粉冰晶醋酸",
        vibe = "低饱和裸粉底搭配单指冰晶贝壳点缀，通透温柔且适合日常。",
        price = "",
        nailType = "中椭圆",
        skinTone = "自然肤色、暖白皮",
        colors = listOf(Color(0xFFF0D2CC), Color(0xFFF8F8F3)),
        tags = listOf("中椭圆", "甜美", "韩系", "渐变", "钻饰", "日常", "约会"),
        displayTags = listOf("甜美", "韩系"),
        imageUrl = mockDrawableUri("acetate_candidate_02")
    ),
    NailStyle(
        id = "3-10",
        name = "丝缎月光醋酸",
        vibe = "清透裸粉与柔和月光碎片叠加，在丝缎光线下呈现细腻水光感。",
        price = "",
        nailType = "中椭圆",
        skinTone = "自然肤色、暖白皮",
        colors = listOf(Color(0xFFF3D4D0), Color(0xFFF5F0E8)),
        tags = listOf("中椭圆", "韩系", "甜美", "渐变", "猫眼", "日常", "约会"),
        displayTags = listOf("韩系", "甜美"),
        imageUrl = mockDrawableUri("acetate_candidate_03")
    ),
    NailStyle(
        id = "4-10",
        name = "星屑裸粉醋酸",
        vibe = "裸粉底搭配双指星屑贝壳点缀，保留日常感同时增加闪耀层次。",
        price = "",
        nailType = "中椭圆",
        skinTone = "自然肤色、暖白皮",
        colors = listOf(Color(0xFFECCFCB), Color(0xFFFFFBF5)),
        tags = listOf("中椭圆", "韩系", "甜美", "渐变", "猫眼", "日常", "约会"),
        displayTags = listOf("韩系", "甜美"),
        imageUrl = mockDrawableUri("acetate_candidate_04")
    )
)

private val acetateMockTrend = TrendTopic(
    id = "nailclaw-acetate-summer",
    name = "醋酸美甲",
    summary = "清透底色叠加珍珠粉、月光粉、贝壳或极光碎片，像冰块包裹细闪般轻盈有层次，是今年夏日通勤、约会和旅行都很出片的热门趋势。",
    styles = acetateTrendCandidates,
    updatedAt = "",
    badge = "NAILCLAW · 夏日趋势",
    provenance = "NailClaw 从近 7 天社区热帖中提炼出的候选款式"
)

private val beijingDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
private val beijingMonthDayTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")
private val beijingClockFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val beijingTimeZone = ZoneId.of("Asia/Shanghai")

internal fun formatBeijingDateTime(rawValue: String): String {
    val value = rawValue.trim()
    if (value.isBlank()) return ""
    val instant = parseApiInstant(value) ?: return value.take(16).replace('T', ' ')
    return beijingDateTimeFormatter.format(instant.atZone(beijingTimeZone))
}

internal fun parseApiInstant(rawValue: String): Instant? {
    val value = rawValue.trim()
    if (value.isBlank()) return null
    return runCatching { OffsetDateTime.parse(value).toInstant() }
        .recoverCatching { LocalDateTime.parse(value).toInstant(ZoneOffset.UTC) }
        .getOrNull()
}

private val reservationSampleStores: List<Store> = listOf(
    Store(
        id = "milai-xdu",
        name = "米莱美甲（厦大大学城商场店）",
        distance = "914m",
        priceBand = "¥86/人",
        score = "5.0",
        slots = listOf("今天 14:30", "今天 16:00", "明天 10:30"),
        openHours = "09:30-22:00",
        artists = 4,
        works = "857条评价",
        avatarRes = R.drawable.store_avatar_milai,
        coverTone = Color(0xFF8FA36F),
        reviewCount = 857,
        statusText = "营业中",
        address = "大学城商城50号",
        area = "厦门大学",
        tags = listOf("人气榜第5名", "收录2年", "不满意重做", "免费茶水小食"),
        nearestSlot = "今天 14:30 可约",
        matchScore = 96,
        couponText = "神券最高膨至30",
        salesText = "年售 900+"
    ),
    Store(
        id = "doris-baolong",
        name = "Doris 欣美甲美睫连锁（宝龙一城店）",
        distance = "13.0km",
        priceBand = "¥98/人",
        score = "4.7",
        slots = listOf("今天 15:00", "今天 18:30", "明天 11:00"),
        openHours = "10:30-22:00",
        artists = 5,
        works = "228条评价",
        avatarRes = R.drawable.store_avatar_doris,
        coverTone = Color(0xFFB84F45),
        reviewCount = 228,
        statusText = "营业中",
        address = "金盛路宝龙一城三号公馆西塔512-2室",
        area = "宝龙一城",
        tags = listOf("纯色销量榜第1名", "人气好店", "不满意重做"),
        nearestSlot = "今天 15:00 可约",
        matchScore = 91,
        couponText = "新客专享 9折",
        salesText = "年售 500+"
    ),
    Store(
        id = "feb-sm",
        name = "Feb·贰钥美甲工作室",
        distance = "13.3km",
        priceBand = "¥92/人",
        score = "4.9",
        slots = listOf("今天 13:30", "今天 19:00", "明天 13:00"),
        openHours = "13:00-22:00",
        artists = 3,
        works = "959条评价",
        avatarRes = R.drawable.store_avatar_feb,
        coverTone = Color(0xFF6D3E2C),
        reviewCount = 959,
        statusText = "营业中",
        address = "嘉禾路351号摩登大厦2314室",
        area = "SM广场",
        tags = listOf("纯色销量榜第10名", "收录2年", "有沙发位", "固定烟区"),
        nearestSlot = "今天 19:00 可约",
        matchScore = 94,
        couponText = "老客复购礼",
        salesText = "年售 800+"
    ),
    Store(
        id = "jinxi-sm",
        name = "今喜 Nail·日式美甲美睫",
        distance = "11.8km",
        priceBand = "¥105/人",
        score = "5.0",
        slots = listOf("今天 16:30", "明天 10:00", "明天 14:00"),
        openHours = "10:00-23:00",
        artists = 6,
        works = "1770条评价",
        avatarRes = R.drawable.store_avatar_jinxi,
        coverTone = Color(0xFF9A7C69),
        reviewCount = 1770,
        statusText = "营业中",
        address = "天成花园7号楼1204",
        area = "SM城市广场",
        tags = listOf("纯色销量榜第3名", "收录2年", "付费停车", "有沙发位"),
        nearestSlot = "今天 16:30 可约",
        matchScore = 98,
        couponText = "预约立减 20",
        salesText = "年售 1200+"
    ),
    Store(
        id = "seven-sm",
        name = "SEVEN Nail（SM店）",
        distance = "11.8km",
        priceBand = "¥78/人",
        score = "4.0",
        slots = listOf("今天 17:00", "明天 12:30", "明天 18:00"),
        openHours = "12:00-22:00",
        artists = 2,
        works = "42条评价",
        avatarRes = R.drawable.store_avatar_seven,
        coverTone = Color(0xFF70735C),
        reviewCount = 42,
        statusText = "营业中",
        address = "兴山路246号",
        area = "SM城市广场",
        tags = listOf("人气榜第6名", "有充电宝", "有沙发位", "会员免费补睫"),
        nearestSlot = "今天 17:00 可约",
        matchScore = 82,
        couponText = "会员补贴",
        salesText = "年售 120+"
    ),
    Store(
        id = "chenxiaoni-sm",
        name = "陈小妮·美甲美睫（SM鑫新景地店）",
        distance = "11.8km",
        priceBand = "¥89/人",
        score = "4.6",
        slots = listOf("今天 18:00", "明天 10:30", "明天 15:30"),
        openHours = "10:00-21:30",
        artists = 4,
        works = "230条评价",
        avatarRes = R.drawable.store_avatar_chenxiaoni,
        coverTone = Color(0xFF8A6D66),
        reviewCount = 230,
        statusText = "营业中",
        address = "嘉禾路396号鑫新景地大厦B413",
        area = "SM城市广场",
        tags = listOf("款式销量榜第9名", "收录4年", "付费停车", "有充电宝"),
        nearestSlot = "今天 18:00 可约",
        matchScore = 89,
        couponText = "款式升级券",
        salesText = "年售 300+"
    ),
    Store(
        id = "muse-new-xiamen",
        name = "MUSE Nail 新作研究所",
        distance = "2.2km",
        priceBand = "¥79/人",
        score = "",
        slots = listOf("今天 13:00", "今天 17:30", "明天 11:00"),
        openHours = "10:00-21:30",
        artists = 2,
        works = "暂无评价",
        coverTone = Color(0xFFC78F9B),
        reviewCount = 0,
        statusText = "新店",
        address = "思明区湖滨南路新景中心2楼",
        area = "思明区",
        tags = listOf("平台审核通过", "法式简约", "短甲友好"),
        nearestSlot = "今天 13:00 可约",
        matchScore = 86,
        couponText = "新店体验立减 20",
        salesText = "新店试营业",
        recommendation = StoreRecommendationUi(
            isNewStore = true,
            rank = 7,
            reason = "新店 · 擅长法式简约 · 今天可约"
        )
    ),
    Store(
        id = "light-nail-new",
        name = "LIGHT NAIL 光感美甲",
        distance = "4.6km",
        priceBand = "¥88/人",
        score = "",
        slots = listOf("今天 18:00", "明天 10:30", "明天 16:00"),
        openHours = "11:00-22:00",
        artists = 3,
        works = "暂无评价",
        coverTone = Color(0xFF9CAFC2),
        reviewCount = 0,
        statusText = "新店",
        address = "湖里区五缘湾天虹里3楼",
        area = "湖里区",
        tags = listOf("平台审核通过", "猫眼专门", "不满意重做"),
        nearestSlot = "今天 18:00 可约",
        matchScore = 83,
        couponText = "新客首单 85 折",
        salesText = "新店试营业",
        recommendation = StoreRecommendationUi(
            isNewStore = true,
            rank = 8,
            reason = "新店 · 擅长猫眼渐变 · 明天可约"
        )
    )
)
private val stores: List<Store> = reservationSampleStores
private val defaultHotKeywords: List<String> = emptyList()

private fun safeStyleColor(raw: String): Color {
    val value = raw.trim()
    if (value.isBlank()) return Color(0xFFF3CBD6)
    return runCatching { Color(android.graphics.Color.parseColor(value)) }.getOrElse {
        when {
            "裸" in value || "粉" in value -> Color(0xFFF3CBD6)
            "奶" in value || "白" in value || "珍珠" in value -> Color(0xFFF7F2EA)
            "红" in value || "玫瑰" in value -> Color(0xFFC85B5D)
            "橄榄" in value || "绿" in value -> Color(0xFF8FA36F)
            "黑" in value -> Color(0xFF171414)
            "银" in value || "灰" in value -> Color(0xFFB8B2B0)
            "蓝" in value -> Color(0xFF9EB8D8)
            "棕" in value || "咖" in value || "茶" in value -> Color(0xFFC7A28A)
            "金" in value -> Color(0xFFD7B15E)
            else -> Color(0xFFF3CBD6)
        }
    }
}

private fun safeStyleColors(rawColors: List<String>): List<Color> {
    val parsed = rawColors.map(::safeStyleColor).ifEmpty {
        listOf(Color(0xFFF3CBD6), Color(0xFFF7F2EA))
    }
    return if (parsed.size == 1) {
        listOf(parsed.first(), parsed.first().copy(alpha = 0.72f))
    } else {
        parsed
    }
}

private fun StyleDto.toUi(): NailStyle = NailStyle(
    id = id,
    name = name,
    vibe = vibe,
    price = price,
    nailType = nailType,
    skinTone = skinTone,
    colors = safeStyleColors(colors),
    tags = tags,
    displayTags = displayTags.orEmpty(),
    tagGroups = tagGroups ?: StyleTagGroupsDto(),
    imageUrl = imageUrl,
    tryOnStyleId = tryOnStyleId
)

private fun StyleRankingItemDto.toUi(): RankedStyle = RankedStyle(
    rank = rank,
    score = score,
    impressions = impressions,
    clicks = clicks,
    favorites = favorites,
    tryOns = tryOns,
    bookings = bookings,
    style = style.toUi()
)

private fun TrendTopicDto.toUi(): TrendTopic = TrendTopic(
    id = id,
    name = name,
    summary = summary,
    styles = styles.map { it.toUi() }.filter { !it.imageUrl.isNullOrBlank() },
    updatedAt = updatedAt
)

private fun StoreDto.toUi(): Store {
    val mock = reservationSampleStores.firstOrNull { it.id == id }
    val fallbackArea = name.substringAfter("\uFF08", "").substringBefore("\uFF09", "").ifBlank { "\u9644\u8FD1\u5546\u5708" }
    return Store(
        id = id,
        name = name,
        distance = distance,
        priceBand = priceBand,
        score = score,
        slots = slots,
        openHours = openHours,
        artists = artists,
        works = works,
        avatarRes = mock?.avatarRes,
        coverTone = mock?.coverTone ?: Color(0xFFEAD8D1),
        reviewCount = recommendation?.metrics?.verifiedReviewCount ?: 0,
        statusText = mock?.statusText ?: "\u8425\u4E1A\u4E2D",
        address = mock?.address ?: "\u95E8\u5E97\u5730\u5740\u8BF7\u4EE5\u5230\u5E97\u524D\u786E\u8BA4\u4E3A\u51C6",
        area = mock?.area ?: fallbackArea,
        tags = mock?.tags ?: listOf("\u652F\u6301\u9884\u7EA6", "\u8BD5\u6234\u540C\u6B3E", "\u5230\u5E97\u786E\u8BA4"),
        nearestSlot = mock?.nearestSlot ?: slots.firstOrNull()?.let { "$it \u53EF\u7EA6" } ?: "\u4ECA\u65E5\u53EF\u7EA6",
        matchScore = mock?.matchScore ?: 88,
        couponText = mock?.couponText ?: "\u65B0\u5BA2\u9884\u7EA6\u7ACB\u51CF",
        salesText = mock?.salesText ?: works,
        recommendation = StoreRecommendationUi(
            isNewStore = recommendation?.isNewStore ?: mock?.recommendation?.isNewStore ?: false,
            newStoreProtection = recommendation?.newStoreProtection ?: mock?.recommendation?.newStoreProtection ?: false,
            rank = recommendation?.rank ?: mock?.recommendation?.rank ?: Int.MAX_VALUE,
            reason = recommendation?.reason ?: mock?.recommendation?.reason.orEmpty()
        )
    )
}

private fun rankBookingStores(items: List<Store>): List<Store> {
    return items.distinctBy { it.id }
        .withIndex()
        .sortedWith(compareBy<IndexedValue<Store>> { it.value.recommendation.rank }.thenBy { it.index })
        .map { it.value }
}

private fun storesWithDemoFallback(fetchedStores: List<Store>): List<Store> {
    return if (BuildConfig.BOOKING_DEMO_STORES_ENABLED) {
        fetchedStores + reservationSampleStores
    } else {
        fetchedStores
    }
}
private fun AuthUserDto.toUi(): AuthUser = AuthUser(
    name = name,
    email = email,
    preferences = preferences,
    needsStyleProfileOnboarding = needsStyleProfileOnboarding
)

private fun BookingDto.toUi(): BookingRecord = BookingRecord(
    id = id,
    status = status,
    storeName = storeName,
    styleId = styleId,
    styleName = styleName,
    styleImageUrl = styleImageUrl,
    slot = slot,
    canReview = canReview,
    review = review
)

private fun buildDisplayedTryOnHistory(
    items: List<TryOnHistoryItemDto>,
    status: TryOnStatus
): List<TryOnHistoryItemDto> {
    val merged = items.toMutableList()
    val shouldShowPending = status.jobId.isNotBlank() &&
        status.styleId.isNotBlank() &&
        status.status in setOf("queued", "processing")
    if (shouldShowPending && merged.none { it.jobId == status.jobId }) {
        merged.add(
            0,
            TryOnHistoryItemDto(
                id = "pending-${status.jobId}",
                jobId = status.jobId,
                resultUrl = "",
                durationMs = 0,
                styleName = status.styleName.ifBlank { "试戴处理中" },
                styleId = status.styleId,
                source = "pending",
                selectedLength = "",
                selectedShape = "",
                createdAt = java.time.Instant.now().toString()
            )
        )
    }
    return merged
        .sortedByDescending { parseApiInstant(it.createdAt) ?: Instant.MIN }
        .distinctBy { it.styleId }
}

private fun SettingsResponse.toUi(): UserSettings = UserSettings(
    stylePreferences = stylePreferences,
    notifications = notifications,
    privacy = privacy
)

private fun selectedLengthToApi(value: String): String = when (value) {
    "自然短甲" -> "natural_short"
    "中短" -> "medium_short"
    "修长" -> "elongated"
    else -> "natural_short"
}

private fun selectedShapeToApi(value: String): String = when (value) {
    "方圆" -> "squoval"
    "椭圆" -> "oval"
    "杏仁" -> "almond"
    else -> "squoval"
}

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): File {
    val file = File(context.cacheDir, "nailmind-hand-${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { output ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
    }
    return file
}

private fun copyUriToCache(context: Context, uri: Uri): File? {
    val input = context.contentResolver.openInputStream(uri) ?: return null
    val file = File(context.cacheDir, "nailmind-hand-${System.currentTimeMillis()}.jpg")
    input.use { stream ->
        FileOutputStream(file).use { output -> stream.copyTo(output) }
    }
    return file
}

private fun createCameraImageFile(context: Context): File =
    File(context.cacheDir, "nailmind-camera-${System.currentTimeMillis()}.jpg")

private fun saveBitmapToGallery(context: Context, bitmap: Bitmap, styleName: String): Result<Unit> {
    return runCatching {
        val resolver = context.contentResolver
        val displayName = "nailmind-${styleName.takeIf { it.isNotBlank() } ?: "tryon"}-${System.currentTimeMillis()}.png"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/NailMind")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val uri = resolver.insert(collection, values) ?: throw IOException("无法创建图片保存记录")
        try {
            resolver.openOutputStream(uri)?.use { output ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
                    throw IOException("图片写入失败")
                }
            } ?: throw IOException("无法打开图片输出流")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            }
        } catch (error: Exception) {
            resolver.delete(uri, null, null)
            throw error
        }
    }
}

private fun galleryPermission(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_IMAGES
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}

private fun stageLabel(stage: String): String = when (stage) {
    "queued" -> "排队中"
    "preparing" -> "准备任务"
    "loading_image" -> "加载手部照片"
    "vision_pipeline" -> "识别手部与甲床"
    "rendering" -> "渲染试戴效果"
    "completed" -> "已完成"
    "failed" -> "处理失败"
    else -> stage.ifBlank { "排队中" }
}

private fun Map<String, String>.firstTrait(vararg keys: String): String = keys
    .firstNotNullOfOrNull { key -> get(key)?.trim()?.takeIf { it.isNotBlank() } }
    .orEmpty()

private fun tryOnLength(value: String): TryOnLength = when (value) {
    "natural_short", "short" -> TryOnLength.NaturalShort
    "medium" -> TryOnLength.Medium
    "long" -> TryOnLength.Long
    else -> TryOnLength.Unknown
}

private fun tryOnShape(value: String): TryOnShape = when (value) {
    "squoval" -> TryOnShape.Squoval
    "round" -> TryOnShape.Round
    "oval" -> TryOnShape.Oval
    "almond" -> TryOnShape.Almond
    "square" -> TryOnShape.Square
    else -> TryOnShape.Unknown
}

internal fun tryOnAnalysisContext(
    selectedLength: String,
    selectedShape: String,
    detectedTraits: Map<String, String> = emptyMap()
): TryOnAnalysisContext = TryOnAnalysisContext(
    length = tryOnLength(selectedLength),
    shape = tryOnShape(selectedShape),
    traits = DetectedHandTraits(
        skinTone = detectedTraits.firstTrait("skinTone", "skin_tone", "skin", "tone"),
        skinUndertone = detectedTraits.firstTrait("skinUndertone", "skin_undertone", "undertone"),
        handShape = detectedTraits.firstTrait("handShape", "hand_shape", "handType", "hand_type"),
        nailBed = detectedTraits.firstTrait("nailBed", "nail_bed"),
        colorHarmonyVerdict = detectedTraits.firstTrait("colorHarmonyVerdict", "color_harmony_verdict"),
        colorHarmonyReason = detectedTraits.firstTrait("colorHarmonyReason", "color_harmony_reason"),
        colorHarmonySuggestion = detectedTraits.firstTrait("colorHarmonySuggestion", "color_harmony_suggestion")
    )
)

internal fun tryOnHistoryAnalysisContext(item: TryOnHistoryItemDto): TryOnAnalysisContext =
    tryOnAnalysisContext(
        selectedLength = item.selectedLength,
        selectedShape = item.selectedShape,
        detectedTraits = item.detectedTraits.orEmpty()
    )

private fun TryOnJobDto.toAnalysisContext(): TryOnAnalysisContext = tryOnAnalysisContext(
    selectedLength = selectedLength,
    selectedShape = selectedShape,
    detectedTraits = detectedTraits.orEmpty()
)

private enum class SkinUndertone { Warm, Cool, Neutral, Unknown }

private fun skinUndertone(value: String): SkinUndertone = when {
    "冷" in value -> SkinUndertone.Cool
    listOf("暖", "黄", "小麦").any(value::contains) -> SkinUndertone.Warm
    listOf("中性", "自然").any(value::contains) -> SkinUndertone.Neutral
    else -> SkinUndertone.Unknown
}

private fun styleColorUndertone(value: String): SkinUndertone = when {
    listOf("蓝", "紫", "银", "灰", "冷调").any(value::contains) -> SkinUndertone.Cool
    listOf("红", "橙", "黄", "金", "棕", "奶茶", "豆沙", "暖调").any(value::contains) -> SkinUndertone.Warm
    listOf("透明", "中性", "裸色").any(value::contains) -> SkinUndertone.Neutral
    else -> SkinUndertone.Unknown
}

private fun shapeNeedsElongation(handShape: String): Boolean =
    listOf("宽", "短", "肉", "圆", "甲床短").any(handShape::contains)

internal fun buildTryOnAnalysis(
    styleName: String,
    nailType: String,
    skinTone: String,
    tags: List<String>,
    context: TryOnAnalysisContext
): TryOnAnalysis {
    val detectedSkin = context.traits.skinTone
    val detectedHand = context.traits.handShape
    val detectedNailBed = context.traits.nailBed
    val modelHarmonyVerdict = context.traits.colorHarmonyVerdict
    val modelHarmonyReason = context.traits.colorHarmonyReason
    val modelHarmonySuggestion = context.traits.colorHarmonySuggestion
    val styleText = (listOf(styleName) + tags).joinToString(" ")
    val targetSkin = skinTone.trim().takeUnless { it.isBlank() || it == "通用" }
    val detectedUndertone = skinUndertone(context.traits.skinUndertone.ifBlank { detectedSkin })
    val targetUndertone = targetSkin?.let(::skinUndertone) ?: SkinUndertone.Unknown
    val colorUndertone = styleColorUndertone(styleText)
    val colorHarmony = when {
        modelHarmonyReason.isNotBlank() -> buildString {
            append("大模型判断")
            if (modelHarmonyVerdict.isNotBlank()) append("：$modelHarmonyVerdict")
            append("。$modelHarmonyReason")
            if (modelHarmonySuggestion.isNotBlank()) append(" 建议：$modelHarmonySuggestion")
        }
        detectedUndertone != SkinUndertone.Unknown && targetUndertone != SkinUndertone.Unknown && detectedUndertone == targetUndertone ->
            "本次识别为$detectedSkin，款式标注适合$targetSkin，色温方向一致，整体协调。"
        detectedUndertone != SkinUndertone.Unknown && targetUndertone != SkinUndertone.Unknown ->
            "本次识别为$detectedSkin，而款式主要面向$targetSkin，色温存在反差，协调度一般；可换成更贴近肤色色温的款式。"
        detectedUndertone != SkinUndertone.Unknown && colorUndertone != SkinUndertone.Unknown && detectedUndertone == colorUndertone ->
            "本次识别为$detectedSkin，款式颜色与肤色底调方向一致，整体协调。"
        detectedUndertone != SkinUndertone.Unknown && colorUndertone != SkinUndertone.Unknown ->
            "本次识别为$detectedSkin，款式颜色与肤色形成冷暖反差，视觉上更醒目，但协调度一般；可降低颜色饱和度让上手效果更柔和。"
        detectedSkin.isNotBlank() && targetSkin != null ->
            "已识别为$detectedSkin，但款式的肤色标签为$targetSkin，现有标签不足以可靠判断是否协调，建议以自然光下的试戴效果为准。"
        detectedSkin.isNotBlank() ->
            "已识别为$detectedSkin，但款式没有明确的适配肤色标签，暂不能可靠判断是否协调，建议在自然光下确认真实色差。"
        targetSkin != null ->
            "这款配色主要面向$targetSkin，但本次没有识别到可靠肤色，暂不能判断是否协调。"
        else ->
            "本次没有取得可靠肤色和款式适配标签，暂不能判断是否协调；建议在自然光下查看真实色差。"
    }

    val lengthLabel = context.length.label
    val shapeLabel = context.shape.label.ifBlank { nailType.trim().ifBlank { "自然甲型" } }
    val lengthContext = lengthLabel.takeIf { it.isNotBlank() }?.let { "$it、" }.orEmpty()
    val handDescription = listOf(detectedHand, detectedNailBed).filter { it.isNotBlank() }.joinToString("、")
    val shapeAndLength = when {
        handDescription.isBlank() ->
            "本次采用$lengthContext$shapeLabel，但没有识别到可靠手型，暂不能判断是否适合；可优先选择不过度超出指尖的长度。"
        shapeNeedsElongation(handDescription) && context.shape.widens && context.length.isShort ->
            "识别到$handDescription，本次$lengthContext${shapeLabel}会强化横向宽度，适配度一般；更建议中等长度的椭圆或杏仁甲。"
        shapeNeedsElongation(handDescription) && context.shape.elongates ->
            "识别到$handDescription，本次$lengthContext${shapeLabel}能延伸纵向线条，甲型和长度适合当前手型。"
        shapeNeedsElongation(handDescription) ->
            "识别到$handDescription，本次$lengthContext${shapeLabel}修饰力度有限，适配度一般；可适当增加长度或改为偏椭圆甲型。"
        else ->
            "识别到$handDescription，本次$lengthContext${shapeLabel}不会明显破坏手指比例，甲型和长度整体适合。"
    }

    val outfitAdvice = when {
        listOf("通勤", "职场").any(styleText::contains) ->
            "适合通勤穿搭，可搭配白衬衫、针织衫或浅色西装，用简洁首饰呼应甲面的精致感。"
        listOf("日常", "法式", "裸", "奶茶").any(styleText::contains) ->
            "适合日常穿搭，推荐米白针织、牛仔或低饱和基础款，整体会显得清爽耐看。"
        listOf("酷", "黑", "猫眼", "镜面", "金属").any(styleText::contains) ->
            "适合酷感或晚间穿搭，可搭配黑灰色服装、皮质单品和银色配饰，突出光泽与层次。"
        listOf("甜美", "粉", "花", "手绘").any(styleText::contains) ->
            "适合甜美约会穿搭，可搭配浅色针织、连衣裙和小体量配饰，让整体更轻盈。"
        listOf("中式", "新中式", "红", "金").any(styleText::contains) ->
            "适合新中式或节庆穿搭，可搭配缎面、盘扣元素和金色配饰，强化精致氛围。"
        else ->
            "适合简约日常穿搭，选择与甲面同色系的上衣或配饰，就能让整体造型更统一。"
    }

    return TryOnAnalysis(colorHarmony, shapeAndLength, outfitAdvice)
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NailMindApp() {
    val context = LocalContext.current
    val sharedPreferences = remember(context) {
        context.getSharedPreferences(AppConfig.preferencesName, Context.MODE_PRIVATE)
    }
    val deviceId = remember {
        sharedPreferences.getString(AppConfig.deviceIdPreference, null)
            ?: UUID.randomUUID().toString().also {
                sharedPreferences.edit().putString(AppConfig.deviceIdPreference, it).apply()
            }
    }
    val sessionId = remember {
        UUID.randomUUID().toString().also {
            sharedPreferences.edit().putString(AppConfig.sessionIdPreference, it).apply()
        }
    }
    val repository = remember { NailMindRepository() }
    val coroutineScope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(MainTab.Home) }
    var xiaomeiInput by rememberSaveable { mutableStateOf("") }
    var xiaomeiLoading by remember { mutableStateOf(false) }
    val xiaomeiMessages = remember { mutableStateListOf<XiaomeiChatMessage>() }
    val stack = remember {
        mutableStateListOf<Screen>(
            if (sharedPreferences.getString(AppConfig.authTokenPreference, null).isNullOrBlank()) Screen.Login else Screen.Tab(MainTab.Home)
        )
    }
    val favorites = remember { mutableStateListOf<String>() }
    var selectedLength by remember { mutableStateOf("自然短甲") }
    var selectedShape by remember { mutableStateOf("方圆") }
    var selectedStoreId by remember { mutableStateOf("") }
    var authUser by remember { mutableStateOf<AuthUser?>(null) }
    var authToken by remember { mutableStateOf(sharedPreferences.getString(AppConfig.authTokenPreference, null)) }
    var authLoading by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }
    var styleItems by remember { mutableStateOf(styles) }
    fun styleById(styleId: String): NailStyle? =
        styleItems.firstOrNull { it.id == styleId } ?: acetateTrendCandidates.firstOrNull { it.id == styleId }
    var styleBrowseTabName by rememberSaveable { mutableStateOf(StyleBrowseTab.NailShape.name) }
    var styleBrowseOptionLabel by rememberSaveable { mutableStateOf(nailShapeBrowseOptions.first().label) }
    var styleBrowseSortMode by rememberSaveable { mutableStateOf("热度高") }
    var styleBrowseQuery by rememberSaveable { mutableStateOf("") }
    var selectedStyleFilters by remember { mutableStateOf(emptySet<StyleFilterSelection>()) }
    var homeRecommended by remember { mutableStateOf(styles.take(2)) }
    var homeSceneSections by remember { mutableStateOf(emptyMap<String, List<NailStyle>>()) }
    var rankingItems by remember { mutableStateOf(emptyList<RankedStyle>()) }
    var heatRankingItems by remember { mutableStateOf(emptyList<RankedStyle>()) }
    var tryOnRankingItems by remember { mutableStateOf(emptyList<RankedStyle>()) }
    var bookingRankingItems by remember { mutableStateOf(emptyList<RankedStyle>()) }
    var trendItems by remember { mutableStateOf(emptyList<RankedStyle>()) }
    var trendTopics by remember { mutableStateOf(emptyList<TrendTopic>()) }
    var trendKeywords by remember { mutableStateOf(emptyList<String>()) }
    var trendsUpdatedAt by remember { mutableStateOf("") }
    var hotKeywords by remember { mutableStateOf(defaultHotKeywords) }
    var storeItems by remember { mutableStateOf(stores) }
    var restorationPerformance by remember { mutableStateOf(emptyMap<String, List<RestorationCategoryDto>>()) }
    var bookingRecords by remember { mutableStateOf(emptyList<BookingRecord>()) }
    var pendingBooking by remember { mutableStateOf<BookingDto?>(null) }
    var userSettings by remember { mutableStateOf(UserSettings("", "", "")) }
    var searchResults by remember { mutableStateOf(styleItems) }
    var tryOnStatus by remember { mutableStateOf(TryOnStatus()) }
    var tryOnHistoryItems by remember { mutableStateOf(emptyList<TryOnHistoryItemDto>()) }
    var tryOnHistoryManageMode by remember { mutableStateOf(false) }
    var latestTryOnBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var latestTryOnResults by remember { mutableStateOf(emptyList<TryOnRenderedResult>()) }
    var lastTryOnSourceFile by remember { mutableStateOf<File?>(null) }
    var lastTryOnHandId by remember { mutableStateOf<String?>(null) }
    var bookingSubmitting by remember { mutableStateOf(false) }
    var bookingError by remember { mutableStateOf<String?>(null) }
    var chatMessages by remember { mutableStateOf(emptyList<ChatMessageDto>()) }
    var chatLoading by remember { mutableStateOf(false) }
    var chatSending by remember { mutableStateOf(false) }
    var chatError by remember { mutableStateOf<String?>(null) }
    var merchantReplyNotice by rememberSaveable { mutableStateOf(false) }
    var merchantReplyPreview by rememberSaveable { mutableStateOf("") }
    var merchantReplyConversationId by rememberSaveable { mutableStateOf("") }
    var merchantReplyStoreId by rememberSaveable { mutableStateOf("") }
    var merchantReplyStoreName by rememberSaveable { mutableStateOf("") }
    var tryOnSubmitting by remember { mutableStateOf(false) }
    var tryOnError by remember { mutableStateOf<String?>(null) }
    var pageRefreshing by remember { mutableStateOf(false) }
    var tryOnPollJob by remember { mutableStateOf<Job?>(null) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !canPostNotifications(context)) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun persistPendingTryOn(jobId: String, styleId: String) {
        sharedPreferences.edit()
            .putString(AppConfig.pendingTryOnJobPreference, jobId)
            .putString(AppConfig.pendingTryOnStylePreference, styleId)
            .apply()
    }

    fun clearPendingTryOn() {
        sharedPreferences.edit()
            .remove(AppConfig.pendingTryOnJobPreference)
            .remove(AppConfig.pendingTryOnStylePreference)
            .apply()
    }

    fun resetToLogin() {
        authUser = null
        authToken = null
        authError = null
        favorites.clear()
        bookingRecords = emptyList()
        pendingBooking = null
        userSettings = UserSettings("", "", "")
        tryOnStatus = TryOnStatus()
        tryOnHistoryItems = emptyList()
        tryOnPollJob?.cancel()
        tryOnPollJob = null
        latestTryOnBitmap = null
        latestTryOnResults = emptyList()
        lastTryOnSourceFile = null
        lastTryOnHandId = null
        bookingError = null
        tryOnError = null
        sharedPreferences.edit().remove(AppConfig.authTokenPreference).apply()
        clearPendingTryOn()
        currentTab = MainTab.Home
        stack.clear()
        stack.add(Screen.Login)
    }

    fun completeAuth(response: AuthResponse) {
        val signedInUser = response.user.toUi()
        authUser = signedInUser
        authToken = response.token
        sharedPreferences.edit().putString(AppConfig.authTokenPreference, response.token).apply()
        currentTab = MainTab.Home
        stack.clear()
        stack.add(
            if (signedInUser.needsStyleProfileOnboarding) Screen.StyleProfile(onboarding = true)
            else Screen.Tab(MainTab.Home)
        )
    }

    fun trackEvent(
        eventName: String,
        styleId: String? = null,
        storeId: String? = null,
        sourcePage: String? = null,
        sourceChannel: String? = null,
        payload: Map<String, Any>? = null
    ) {
        coroutineScope.launch {
            runCatching {
                repository.trackEvent(
                    eventName = eventName,
                    deviceId = deviceId,
                    sessionId = sessionId,
                    styleId = styleId,
                    storeId = storeId,
                    sourcePage = sourcePage,
                    sourceChannel = sourceChannel,
                    payload = payload
                )
            }
        }
    }

    fun sourcePageFor(screen: Screen): String = when (screen) {
        is Screen.Tab -> when (screen.tab) {
            MainTab.Home -> "home"
            MainTab.Styles -> "styles"
            MainTab.TryOn -> "tryon"
            MainTab.Booking -> "booking"
            MainTab.Profile -> "profile"
        }
        Screen.Search -> "search"
        is Screen.SearchResult -> "search_result"
        Screen.StyleFilter -> "style_filter"
        Screen.StyleFilterResult -> "style_filter_result"
        Screen.Ranking -> "ranking"
        Screen.Trends -> "trends"
        is Screen.TrendDetail -> "trend_detail"
        is Screen.StyleDetail -> "style_detail"
        is Screen.TryOnUpload -> "tryon_upload"
        is Screen.TryOnProcessing -> "tryon_processing"
        is Screen.TryOnResult -> "tryon_result"
        Screen.TryOnHistory -> "tryon_history"
        Screen.DiyDesigner -> "diy_designer"
        Screen.Xiaomei -> "meimei_assistant"
        Screen.Favorites -> "favorites"
        Screen.BookingRecords -> "booking_records"
        Screen.Reviews -> "reviews"
        is Screen.StyleProfile -> "style_profile"
        is Screen.StoreDetail -> "store_detail"
        is Screen.BookingForm -> "booking_form"
        is Screen.BookingConfirm -> "booking_confirm"
        is Screen.BookingSuccess -> "booking_success"
        is Screen.Chat -> "chat"
        Screen.Settings -> "settings"
        Screen.Login -> "login"
        Screen.Register -> "register"
    }

    fun go(screen: Screen, trackStyleNavigation: Boolean = true) {
        if (trackStyleNavigation && screen is Screen.StyleDetail) {
            trackEvent(
                eventName = "style_click",
                styleId = screen.styleId,
                sourcePage = sourcePageFor(stack.lastOrNull() ?: Screen.Tab(currentTab)),
                sourceChannel = "app_navigation"
            )
        }
        if (screen is Screen.Tab) {
            currentTab = screen.tab
        }
        stack.add(screen)
    }

    fun openStyleDetail(
        styleId: String,
        sourcePage: String,
        sourceChannel: String,
        payload: Map<String, Any>? = null
    ) {
        if (styleId.isBlank()) return
        trackEvent(
            eventName = "style_click",
            styleId = styleId,
            sourcePage = sourcePage,
            sourceChannel = sourceChannel,
            payload = payload
        )
        go(Screen.StyleDetail(styleId), trackStyleNavigation = false)
    }

    fun refreshTryOnHistory() {
        coroutineScope.launch {
            runCatching { repository.tryOnHistory().items }
                .onSuccess { tryOnHistoryItems = it }
        }
    }

    fun openTryOnHistoryResult(item: TryOnHistoryItemDto) {
        coroutineScope.launch {
            runCatching {
                val imageBytes = repository.resultImageBytesByUrl(item.resultUrl)
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            }.onSuccess { bitmap ->
                val resultJobId = item.jobId ?: item.id
                latestTryOnBitmap = bitmap
                latestTryOnResults = listOf(TryOnRenderedResult("legacy", "试戴结果", bitmap))
                tryOnStatus = TryOnStatus(
                    jobId = resultJobId,
                    styleId = item.styleId,
                    styleName = item.styleName,
                    stage = item.source,
                    progress = 100,
                    status = "completed",
                    analysisContext = tryOnHistoryAnalysisContext(item)
                )
                go(Screen.TryOnResult(item.styleId, resultJobId))
            }.onFailure { error ->
                tryOnError = error.message ?: "打开试戴记录失败"
            }
        }
    }

    fun monitorTryOnJob(styleId: String, jobId: String, navigateToProcessing: Boolean = false) {
        tryOnPollJob?.cancel()
        tryOnPollJob = coroutineScope.launch {
            if (navigateToProcessing) {
                go(Screen.TryOnProcessing(styleId, jobId))
            }
            var firstResultShown = false
            while (isActive) {
                runCatching { repository.tryOnJob(jobId) }
                    .onSuccess { job ->
                        tryOnStatus = TryOnStatus(
                            jobId = job.id,
                            styleId = job.styleId,
                            styleName = job.styleName,
                            stage = job.stage,
                            progress = job.progress,
                            status = job.status,
                            analysisContext = job.toAnalysisContext(),
                            errorMessage = job.errorMessage
                        )
                        when (job.status) {
                            "completed" -> {
                                val renderedResults = job.results
                                    .filter { it.status == "completed" && !it.resultImageUrl.isNullOrBlank() }
                                    .mapNotNull { result ->
                                        runCatching {
                                            val bytes = repository.resultImageBytesByUrl(result.resultImageUrl.orEmpty())
                                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.let { bitmap ->
                                                TryOnRenderedResult(result.provider, result.label, bitmap)
                                            }
                                        }.getOrNull()
                                    }
                                if (renderedResults.isNotEmpty()) {
                                    latestTryOnResults = renderedResults
                                    latestTryOnBitmap = renderedResults.first().bitmap
                                } else if (latestTryOnBitmap == null) {
                                    val imageBytes = repository.tryOnResultImageBytes(jobId)
                                    latestTryOnBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                }
                                refreshTryOnHistory()
                                clearPendingTryOn()
                                tryOnSubmitting = false
                                val currentScreen = stack.lastOrNull()
                                val viewingJob = currentScreen == Screen.TryOnProcessing(styleId, jobId) ||
                                    currentScreen == Screen.TryOnResult(styleId, jobId)
                                if (viewingJob) {
                                    stack[stack.lastIndex] = Screen.TryOnResult(styleId, jobId)
                                } else if (!firstResultShown) {
                                    showTryOnReadyNotification(
                                        context,
                                        styleById(styleId)?.name ?: job.styleName
                                    )
                                }
                                firstResultShown = true
                                if (job.allResultsReady) return@launch
                            }

                            "failed" -> {
                                clearPendingTryOn()
                                tryOnSubmitting = false
                                tryOnError = job.errorMessage ?: "试戴生成失败，请更换更清晰的手部照片后重试"
                                return@launch
                            }
                        }
                    }
                    .onFailure { error ->
                        tryOnStatus = tryOnStatus.copy(errorMessage = error.message ?: "试戴任务状态获取失败")
                    }
                delay(2000)
            }
        }
    }

    fun launchTryOn(styleId: String, sourceFile: File, sourceChannel: String) {
        coroutineScope.launch {
            tryOnSubmitting = true
            tryOnError = null
            latestTryOnBitmap = null
            latestTryOnResults = emptyList()
            runCatching {
                lastTryOnSourceFile = sourceFile
                val upload = repository.uploadAsyncTryOnImage(sourceFile)
                repository.createTryOnJob(
                    styleId = styleId,
                    sourceImageKey = upload.objectKey,
                    selectedLength = selectedLengthToApi(selectedLength),
                    selectedShape = selectedShapeToApi(selectedShape)
                )
            }.onSuccess { job ->
                trackEvent(
                    eventName = "tryon_source_select",
                    styleId = styleId,
                    sourcePage = "tryon_upload",
                    sourceChannel = sourceChannel,
                    payload = mapOf("fileName" to sourceFile.name)
                )
                requestNotificationPermissionIfNeeded()
                persistPendingTryOn(job.id, styleId)
                tryOnStatus = TryOnStatus(
                    jobId = job.id,
                    styleId = styleId,
                    styleName = styleById(styleId)?.name.orEmpty(),
                    stage = job.stage,
                    progress = job.progress,
                    status = job.status,
                    analysisContext = job.toAnalysisContext(),
                    errorMessage = job.errorMessage
                )
                tryOnSubmitting = false
                monitorTryOnJob(styleId, job.id, navigateToProcessing = true)
            }.onFailure { error ->
                tryOnError = error.message ?: "创建试戴任务失败"
                tryOnSubmitting = false
            }
        }
    }

    fun rerunTryOn(styleId: String, jobId: String) {
        coroutineScope.launch {
            tryOnSubmitting = true
            tryOnError = null
            latestTryOnBitmap = null
            latestTryOnResults = emptyList()
            runCatching {
                val canRerenderExistingJob = Regex("^tryon-\\d+$").matches(jobId) || Regex("^job-\\d+$").matches(jobId)
                if (canRerenderExistingJob) {
                    repository.rerenderTryOn(
                        jobId = jobId,
                        selectedLength = selectedLengthToApi(selectedLength),
                        selectedShape = selectedShapeToApi(selectedShape)
                    )
                } else {
                    val lastFile = lastTryOnSourceFile ?: error("缺少上一张手部照片，请重新上传")
                    val upload = repository.uploadAsyncTryOnImage(lastFile)
                    repository.createTryOnJob(
                        styleId = styleId,
                        sourceImageKey = upload.objectKey,
                        selectedLength = selectedLengthToApi(selectedLength),
                        selectedShape = selectedShapeToApi(selectedShape)
                    )
                }
            }.onSuccess { job ->
                persistPendingTryOn(job.id, styleId)
                tryOnStatus = TryOnStatus(
                    jobId = job.id,
                    styleId = styleId,
                    styleName = styleById(styleId)?.name.orEmpty(),
                    stage = job.stage,
                    progress = job.progress,
                    status = job.status,
                    analysisContext = job.toAnalysisContext(),
                    errorMessage = job.errorMessage
                )
                tryOnSubmitting = false
                stack[stack.lastIndex] = Screen.TryOnProcessing(styleId, job.id)
                monitorTryOnJob(styleId, job.id)
            }.onFailure { error ->
                tryOnError = error.message ?: "重新生成试戴失败"
                tryOnSubmitting = false
            }
        }
    }

    fun back() {
        if (stack.size > 1) {
            stack.removeLast()
            val top = stack.last()
            if (top is Screen.Tab) currentTab = top.tab
        }
    }

    suspend fun loadPublicCatalog() {
        val home = repository.home()
        val fetchedStyles = repository.styles().items.map { it.toUi() }.filter { !it.imageUrl.isNullOrBlank() }
        val fetchedStores = runCatching { repository.stores().items.map { it.toUi() } }
            .getOrElse { emptyList() }
        val displayedStores = rankBookingStores(storesWithDemoFallback(fetchedStores))

        styleItems = fetchedStyles
        storeItems = displayedStores
        hotKeywords = home.hotKeywords
        homeRecommended = home.recommended.map { it.toUi() }.filter { !it.imageUrl.isNullOrBlank() }
        homeSceneSections = home.sceneSections
            .orEmpty()
            .ifEmpty { home.sceneStyles.orEmpty() }
            .mapValues { (_, sceneStyles) ->
                sceneStyles.orEmpty().map { it.toUi() }.filter { !it.imageUrl.isNullOrBlank() }
            }
        rankingItems = home.ranking.map { it.toUi() }.filter { !it.style.imageUrl.isNullOrBlank() }
        heatRankingItems = (home.heatRanking.ifEmpty { home.ranking }).map { it.toUi() }.filter { !it.style.imageUrl.isNullOrBlank() }
        tryOnRankingItems = (home.tryOnRanking.ifEmpty { home.ranking }).map { it.toUi() }.filter { !it.style.imageUrl.isNullOrBlank() }
        bookingRankingItems = (home.bookingRanking.ifEmpty { home.ranking }).map { it.toUi() }.filter { !it.style.imageUrl.isNullOrBlank() }
        trendItems = home.trends.map { it.toUi() }.filter { !it.style.imageUrl.isNullOrBlank() }
        trendTopics = home.trendTopics.map { it.toUi() }.filter { it.styles.isNotEmpty() }
        trendKeywords = home.trendKeywords
        trendsUpdatedAt = home.trendsUpdatedAt
        searchResults = styleItems
        if (selectedStoreId !in storeItems.map { it.id }) {
            selectedStoreId = storeItems.firstOrNull()?.id.orEmpty()
        }
    }

    suspend fun loadUserData() {
        val me = repository.authMe().user.toUi()
        val fetchedFavorites = repository.favorites().items.map { it.id }
        val fetchedBookings = repository.bookings().items.map { it.toUi() }
        val fetchedSettings = repository.settings().toUi()
        val fetchedTryOnHistory = repository.tryOnHistory().items

        authUser = me
        if (me.needsStyleProfileOnboarding && stack.lastOrNull() is Screen.Tab) {
            stack.clear()
            stack.add(Screen.StyleProfile(onboarding = true))
        }
        favorites.clear()
        favorites.addAll(fetchedFavorites)
        bookingRecords = fetchedBookings
        userSettings = fetchedSettings
        tryOnHistoryItems = fetchedTryOnHistory
    }

    suspend fun bootstrapData() {
        loadPublicCatalog()
        if (!authToken.isNullOrBlank()) {
            loadUserData()
        }
    }

    fun refreshPageData() {
        coroutineScope.launch {
            pageRefreshing = true
            runCatching { bootstrapData() }
                .onFailure { error ->
                    Toast.makeText(context, error.message ?: "刷新失败，请稍后再试", Toast.LENGTH_SHORT).show()
                }
            pageRefreshing = false
        }
    }

    fun toggleFavorite(styleId: String) {
        val shouldFavorite = !favorites.contains(styleId)
        coroutineScope.launch {
            runCatching { repository.setFavorite(styleId, shouldFavorite) }
                .onSuccess {
                    if (shouldFavorite) favorites.add(styleId) else favorites.remove(styleId)
                }
        }
    }

    fun openBookingForStyle(styleId: String) {
        coroutineScope.launch {
            val fetchedStores = runCatching { repository.stores(styleId).items.map { it.toUi() } }.getOrElse { emptyList() }
            val rankedStores = rankBookingStores(storesWithDemoFallback(fetchedStores))
            storeItems = rankedStores
            val firstStore = rankedStores.firstOrNull()
            if (firstStore == null) {
                Toast.makeText(context, "当前还没有可预约门店", Toast.LENGTH_SHORT).show()
                return@launch
            }
            selectedStoreId = firstStore.id
            go(Screen.BookingForm(firstStore.id, styleId))
        }
    }

    val displayedTryOnHistoryItems = buildDisplayedTryOnHistory(tryOnHistoryItems, tryOnStatus)

    fun updateMerchantReplyNotice(messages: List<ChatMessageDto>, conversationId: String, storeId: String, storeName: String) {
        val latestMerchantMessage = messages.lastOrNull { it.senderRole != "customer" } ?: return
        merchantReplyNotice = true
        merchantReplyPreview = latestMerchantMessage.body
        merchantReplyConversationId = conversationId
        merchantReplyStoreId = storeId
        merchantReplyStoreName = storeName
    }
    fun findBooking(bookingId: String): BookingDto? {
        if (pendingBooking?.id == bookingId) return pendingBooking
        val record = bookingRecords.firstOrNull { it.id == bookingId } ?: return null
        return BookingDto(
            id = record.id,
            status = record.status,
            storeId = "",
            storeName = record.storeName,
            styleId = "",
            styleName = record.styleName,
            slot = record.slot,
            price = "",
            name = authUser?.name ?: "",
            phone = "",
            note = "",
            createdAt = "",
            confirmedAt = null
        )
    }

    LaunchedEffect(Unit) {
        NailMindApiClient.setAuthTokenProvider { authToken }
        runCatching { loadPublicCatalog() }
        if (!authToken.isNullOrBlank()) {
            runCatching { loadUserData() }
                .onFailure { resetToLogin() }
        }
    }

    LaunchedEffect(authToken) {
        NailMindApiClient.setAuthTokenProvider { authToken }
        if (!authToken.isNullOrBlank()) {
            val pendingJobId = sharedPreferences.getString(AppConfig.pendingTryOnJobPreference, null)
            val pendingStyleId = sharedPreferences.getString(AppConfig.pendingTryOnStylePreference, null)
            if (!pendingJobId.isNullOrBlank() && !pendingStyleId.isNullOrBlank()) {
                monitorTryOnJob(pendingStyleId, pendingJobId)
            }
        }
    }

    val current = stack.last()
    val isAuthenticated = authToken != null

    // Keep the visible home catalog in sync with operations-side recommendation changes.
    LaunchedEffect(current, authToken) {
        if (authToken.isNullOrBlank() || current !is Screen.Tab || current.tab != MainTab.Home) return@LaunchedEffect
        runCatching { loadPublicCatalog() }
        while (isActive) {
            delay(10_000)
            runCatching { loadPublicCatalog() }
        }
    }

    BackHandler(enabled = stack.size > 1 && current !is Screen.Login && current !is Screen.Register) {
        back()
    }
    val showHomeChrome = current is Screen.Tab && (current.tab == MainTab.Home || current.tab == MainTab.TryOn)
    val hideAppChrome = showHomeChrome || current is Screen.DiyDesigner || current is Screen.Xiaomei || current is Screen.StoreDetail || current is Screen.Chat
    val styleDetailStyle = (current as? Screen.StyleDetail)?.let { screen ->
        styleById(screen.styleId)
    }
    fun shareStyle(style: NailStyle?) {
        if (style == null) return
        trackEvent(
            eventName = "style_share",
            styleId = style.id,
            sourcePage = "style_detail",
            sourceChannel = "native_share"
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, style.name)
            putExtra(Intent.EXTRA_TEXT, "看看这款美甲：${style.name} ${AppConfig.apiBaseUrl}admin")
        }
        context.startActivity(Intent.createChooser(intent, "分享款式"))
    }
    val topBarTitle = when (current) {
        Screen.Login -> "登录"
        Screen.Register -> "注册"
        is Screen.Tab -> current.tab.title
        is Screen.StyleDetail -> "款式详情"
        Screen.Search -> "搜索款式"
        is Screen.SearchResult -> "搜索结果"
        Screen.StyleFilter -> "筛选"
        Screen.StyleFilterResult -> "筛选结果"
        Screen.Ranking -> "热门排行榜"
        Screen.Trends -> "近7天热门趋势"
        is Screen.TrendDetail -> "趋势详情"
        is Screen.TryOnUpload -> "上传手部照片"
        is Screen.TryOnProcessing -> "手部识别中"
        is Screen.TryOnResult -> "试戴结果"
        Screen.TryOnHistory -> "试戴记录"
        Screen.DiyDesigner -> "DIY设计"
        Screen.Xiaomei -> "小美"
        Screen.Favorites -> "我的收藏"
        Screen.BookingRecords -> "预约记录"
        Screen.Reviews -> "我的评价"
        is Screen.StyleProfile -> "风格档案"
        is Screen.StoreDetail -> "门店详情"
        is Screen.Chat -> "咨询"
        is Screen.BookingForm -> "填写预约"
        is Screen.BookingConfirm -> "确认预约"
        is Screen.BookingSuccess -> "预约成功"
        Screen.Settings -> "设置"
    }
    val isPrimaryTabTopBar = current is Screen.Tab && when (current.tab) {
        MainTab.Styles, MainTab.Booking, MainTab.Profile -> true
        else -> false
    }

    Box(Modifier.fillMaxSize()) {
    Scaffold(
        topBar = {
            if (isAuthenticated && !hideAppChrome) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            topBarTitle,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            letterSpacing = if (isPrimaryTabTopBar) 2.sp else 0.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        if (stack.size > 1) {
                            IconButton(onClick = ::back) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回")
                            }
                        }
                    },
                    actions = {
                        if (current is Screen.StyleDetail) {
                            IconButton(onClick = { shareStyle(styleDetailStyle) }) {
                                Icon(Icons.Rounded.Share, contentDescription = "分享")
                            }
                        }
                        if (current is Screen.TryOnHistory) {
                            TextButton(onClick = { tryOnHistoryManageMode = !tryOnHistoryManageMode }) {
                                Text(if (tryOnHistoryManageMode) "完成" else "管理")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        bottomBar = {
            if (isAuthenticated && current is Screen.Tab) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .height(62.dp)
                            .padding(horizontal = 8.dp)
                            .padding(top = 3.dp, bottom = 3.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        MainTab.entries.forEach { tab ->
                            val selected = currentTab == tab
                            val isTryOn = tab == MainTab.TryOn
                            TextButton(
                                onClick = {
                                    if (isTryOn) {
                                        xiaomeiInput = ""
                                        xiaomeiMessages.clear()
                                        go(Screen.Xiaomei)
                                    } else {
                                        currentTab = tab
                                        stack.clear()
                                        stack.add(Screen.Tab(tab))
                                    }
                                },
                            modifier = Modifier
                                .weight(1f)
                                .height(if (isTryOn) 62.dp else 56.dp),
                            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                        ) {
                            if (isTryOn) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    RabbitTabIcon(selected = selected, modifier = Modifier.size(52.dp))
                                }
                            } else {
                                Column(
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title,
                                        tint = if (selected) RoseAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.height(3.dp))
                                    Text(
                                        text = tab.title,
                                        color = if (selected) RoseAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.86f),
                                        fontSize = 12.sp,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
            } else if (isAuthenticated && current is Screen.StyleDetail && styleDetailStyle != null) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp)
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { toggleFavorite(styleDetailStyle.id) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (favorites.contains(styleDetailStyle.id)) "已收藏" else "收藏")
                        }
                        Button(
                            onClick = { go(Screen.TryOnUpload(styleDetailStyle.id)) },
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text("AI试戴")
                        }
                        OutlinedButton(
                            onClick = { openBookingForStyle(styleDetailStyle.id) },
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Text("预约同款")
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        val contentPadding = if (hideAppChrome) {
            PaddingValues(0.dp)
        } else {
            innerPadding
        }
        AnimatedContent(
            targetState = current,
            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) { screen ->
            when (screen) {
                Screen.Login -> AuthScreen(
                    title = "欢迎回到 Nail Mind",
                    subtitle = "登录后才能使用试戴、收藏、预约和个人中心。",
                    primaryLabel = "登录",
                    secondaryLabel = "没有账号，去注册",
                    initialName = "",
                    initialEmail = "",
                    initialPassword = "",
                    showNameField = false,
                    loading = authLoading,
                    errorMessage = authError,
                    onPrimary = { _, email, password ->
                        coroutineScope.launch {
                            authLoading = true
                            authError = null
                            runCatching { repository.login(email = email, password = password) }
                                .onSuccess {
                                    completeAuth(it)
                                    runCatching {
                                        loadPublicCatalog()
                                        loadUserData()
                                    }
                                        .onFailure { error -> authError = error.message ?: "初始化首页数据失败" }
                                }
                                .onFailure { error ->
                                    authError = error.message ?: "登录失败"
                                }
                            authLoading = false
                        }
                    },
                    onSecondary = {
                        stack.clear()
                        stack.add(Screen.Register)
                    }
                )

                Screen.Register -> AuthScreen(
                    title = "创建你的账号",
                    subtitle = "注册后才能保存收藏、试戴记录和预约订单。",
                    primaryLabel = "注册并进入",
                    secondaryLabel = "已有账号，去登录",
                    initialName = "",
                    initialEmail = "",
                    initialPassword = "",
                    showNameField = true,
                    loading = authLoading,
                    errorMessage = authError,
                    onPrimary = { name, email, password ->
                        coroutineScope.launch {
                            authLoading = true
                            authError = null
                            runCatching { repository.register(name = name.ifBlank { "新用户" }, email = email, password = password) }
                                .onSuccess {
                                    completeAuth(it)
                                    runCatching {
                                        loadPublicCatalog()
                                        loadUserData()
                                    }
                                        .onFailure { error -> authError = error.message ?: "初始化首页数据失败" }
                                }
                                .onFailure { error ->
                                    authError = error.message ?: "注册失败"
                                }
                            authLoading = false
                        }
                    },
                    onSecondary = {
                        stack.clear()
                        stack.add(Screen.Login)
                    }
                )

                is Screen.StyleProfile -> {
                    fun finishStyleProfile() {
                        authUser = authUser?.copy(needsStyleProfileOnboarding = false)
                        if (screen.onboarding) {
                            currentTab = MainTab.Home
                            stack.clear()
                            stack.add(Screen.Tab(MainTab.Home))
                        } else {
                            back()
                        }
                    }
                    StyleProfileScreen(
                        repository = repository,
                        onboarding = screen.onboarding,
                        onComplete = ::finishStyleProfile
                    )
                }

                is Screen.Tab -> when (screen.tab) {
                    MainTab.Home -> HomeScreen(
                        recommended = homeRecommended,
                        sceneSections = homeSceneSections,
                        refreshing = pageRefreshing,
                        onRefresh = ::refreshPageData,
                        onSearch = { go(Screen.Search) },
                        onSeeMore = {
                            currentTab = MainTab.Styles
                            stack.clear()
                            stack.add(Screen.Tab(MainTab.Styles))
                        },
                        onRanking = { go(Screen.Ranking) },
                        onAiPick = {
                            go(Screen.DiyDesigner)
                        },
                        onTrend = {
                            go(Screen.Trends)
                        },
                        onStyleQuiz = { go(Screen.Xiaomei) },
                        onStyleClick = { openStyleDetail(it, "home", "home_style_card") }
                    )
                    MainTab.Styles -> StylesScreen(
                        styles = styleItems,
                        refreshing = pageRefreshing,
                        selectedTabName = styleBrowseTabName,
                        selectedOptionLabel = styleBrowseOptionLabel,
                        sortMode = styleBrowseSortMode,
                        query = styleBrowseQuery,
                        onSelectedTabChange = { tab ->
                            styleBrowseTabName = tab.name
                            styleBrowseOptionLabel = tab.options().first().label
                        },
                        onSelectedOptionChange = { styleBrowseOptionLabel = it },
                        onSortModeChange = { styleBrowseSortMode = it },
                        onQueryChange = { styleBrowseQuery = it },
                        onOpenFilter = { go(Screen.StyleFilter) },
                        onRefresh = ::refreshPageData,
                        onStyleClick = { openStyleDetail(it, "styles", "style_grid_card") }
                    )
                    MainTab.TryOn -> DiyDesignerScreen(
                        onBackHome = {
                            currentTab = MainTab.Home
                            stack.clear()
                            stack.add(Screen.Tab(MainTab.Home))
                        },
                        onSave = {
                            Toast.makeText(context, "DIY款式已保存", Toast.LENGTH_SHORT).show()
                        },
                        onTryOn = {
                            val target = styleItems.firstOrNull()?.id
                            if (target == null) {
                                Toast.makeText(context, "暂无可试戴款式", Toast.LENGTH_SHORT).show()
                            } else {
                                go(Screen.TryOnUpload(target))
                            }
                        }
                    )
                    MainTab.Booking -> BookingScreen(
                        stores = storeItems,
                        onStoreClick = { go(Screen.StoreDetail(it, null)) }
                    )
                    MainTab.Profile -> ProfileScreen(
                        user = authUser ?: AuthUser("账户", ""),
                        favoritesCount = favorites.size,
                        tryOnHistoryCount = displayedTryOnHistoryItems.size,
                        hasMerchantReply = merchantReplyNotice,
                        merchantReplyPreview = merchantReplyPreview,
                        onMessageCenter = {
                            merchantReplyNotice = false
                            if (merchantReplyConversationId.isNotBlank()) {
                                go(Screen.Chat(merchantReplyConversationId, merchantReplyStoreId, merchantReplyStoreName.ifBlank { "门店咨询" }))
                            } else {
                                Toast.makeText(context, "暂无新的商家回复", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onFavorites = { go(Screen.Favorites) },
                        onTryOnHistory = { go(Screen.TryOnHistory) },
                        onRecords = { go(Screen.BookingRecords) },
                        onReviews = { go(Screen.Reviews) },
                        onStyleProfile = { go(Screen.StyleProfile()) },
                        onSettings = { go(Screen.Settings) }
                    )
                }

                is Screen.StyleDetail -> {
                    val style = styleById(screen.styleId) ?: return@AnimatedContent
                    StyleDetailScreen(
                        style = style,
                        favorite = favorites.contains(style.id),
                        onToggleFavorite = { toggleFavorite(style.id) },
                        onTryOn = { go(Screen.TryOnUpload(style.id)) },
                        onBook = { openBookingForStyle(style.id) }
                    )
                }

                Screen.Search -> SearchScreen(
                    hotKeywords = hotKeywords,
                    onSearch = {
                        coroutineScope.launch {
                            searchResults = runCatching { repository.searchStyles(it).items.map { item -> item.toUi() } }
                                .getOrDefault(styleItems.filter { style -> style.name.contains(it) || style.vibe.contains(it) || style.tags.any { tag -> tag.contains(it) } })
                            go(Screen.SearchResult(it))
                        }
                    }
                )

                is Screen.SearchResult -> SearchResultScreen(
                    query = screen.query,
                    result = searchResults,
                    onStyleClick = {
                        openStyleDetail(
                            styleId = it,
                            sourcePage = "search_result",
                            sourceChannel = "search_result_card",
                            payload = mapOf("query" to screen.query)
                        )
                    }
                )

                Screen.StyleFilter -> StyleFilterScreen(
                    selectedFilters = selectedStyleFilters,
                    onToggleFilter = { selection ->
                        selectedStyleFilters = if (selection in selectedStyleFilters) {
                            selectedStyleFilters - selection
                        } else {
                            selectedStyleFilters + selection
                        }
                    },
                    onClear = { selectedStyleFilters = emptySet() },
                    onConfirm = { go(Screen.StyleFilterResult) }
                )

                Screen.StyleFilterResult -> {
                    val filtered = styleItems.filter { it.matchesStyleFilters(selectedStyleFilters) }
                    StyleFilterResultScreen(
                        selectedFilters = selectedStyleFilters,
                        result = filtered,
                        onEditFilters = ::back,
                        onStyleClick = {
                            openStyleDetail(
                                styleId = it,
                                sourcePage = "style_filter_result",
                                sourceChannel = "style_filter_result_card",
                                payload = mapOf(
                                    "filters" to selectedStyleFilters
                                        .map { "${it.tab.name}:${it.option.label}" }
                                        .sorted()
                                )
                            )
                        }
                    )
                }

                Screen.Ranking -> RankingScreen(
                    heatItems = heatRankingItems.ifEmpty { rankingItems },
                    tryOnItems = tryOnRankingItems.ifEmpty { rankingItems },
                    bookingItems = bookingRankingItems.ifEmpty { rankingItems },
                    refreshing = pageRefreshing,
                    onRefresh = ::refreshPageData,
                    onStyleClick = { openStyleDetail(it, "ranking", "ranking_row") }
                )

                Screen.Trends -> TrendsScreen(
                    topics = trendTopics,
                    items = trendItems,
                    keywords = trendKeywords,
                    updatedAt = trendsUpdatedAt,
                    refreshing = pageRefreshing,
                    onRefresh = ::refreshPageData,
                    onTopicClick = { go(Screen.TrendDetail(it)) }
                )

                is Screen.TrendDetail -> {
                    val topic = trendTopicsForDisplay(
                        topics = trendTopics,
                        items = trendItems,
                        updatedAt = trendsUpdatedAt
                    ).firstOrNull { it.id == screen.topicId }
                    if (topic == null) {
                        EmptyState("趋势不存在", "返回热门趋势后重新选择。")
                    } else {
                        TrendDetailScreen(
                            topic = topic,
                            onStyleClick = {
                                openStyleDetail(
                                    styleId = it,
                                    sourcePage = "trend_detail",
                                    sourceChannel = "trend_candidate_card",
                                    payload = mapOf("trendId" to topic.id)
                                )
                            },
                            onTryOn = { go(Screen.TryOnUpload(it)) }
                        )
                    }
                }

                Screen.DiyDesigner -> DiyDesignerScreen(
                    onBackHome = {
                        currentTab = MainTab.Home
                        stack.clear()
                        stack.add(Screen.Tab(MainTab.Home))
                    },
                    onSave = {
                        Toast.makeText(context, "DIY款式已保存为草稿", Toast.LENGTH_SHORT).show()
                        back()
                    },
                    onTryOn = {
                        val target = styleItems.firstOrNull()?.id
                        if (target == null) {
                            Toast.makeText(context, "暂无可试戴款式", Toast.LENGTH_SHORT).show()
                        } else {
                            go(Screen.TryOnUpload(target))
                        }
                    }
                )

                Screen.Xiaomei -> XiaomeiAssistantSheet(
                    repository = repository,
                    input = xiaomeiInput,
                    loading = xiaomeiLoading,
                    messages = xiaomeiMessages,
                    onInputChange = { xiaomeiInput = it },
                    onLoadingChange = { xiaomeiLoading = it },
                    onDismiss = ::back,
                    onOpenDiy = { go(Screen.DiyDesigner) },
                    onOpenStyles = { go(Screen.Tab(MainTab.Styles)) },
                    onOpenFavorites = { go(Screen.Favorites) },
                    onOpenBooking = { go(Screen.Tab(MainTab.Booking)) },
                    onOpenTryOn = { styleId ->
                        val targetStyleId = styleId.ifBlank { styleItems.firstOrNull()?.id.orEmpty() }
                        if (targetStyleId.isNotBlank()) {
                            go(Screen.TryOnUpload(targetStyleId))
                        }
                    },
                    onOpenStyle = { styleId ->
                        openStyleDetail(styleId, "assistant", "assistant_style_link")
                    }
                )

                is Screen.TryOnUpload -> TryOnUploadScreen(
                    style = styleById(screen.styleId) ?: return@AnimatedContent,
                    fromFavorites = screen.fromFavorites,
                    loading = tryOnSubmitting,
                    errorMessage = tryOnError,
                    lastSourceFile = lastTryOnSourceFile,
                    onStartProcessing = { sourceFile, sourceChannel -> launchTryOn(screen.styleId, sourceFile, sourceChannel) }
                )

                is Screen.TryOnProcessing -> {
                    TryOnProcessingScreen(
                        stage = tryOnStatus.stage,
                        progress = tryOnStatus.progress,
                        errorMessage = tryOnStatus.errorMessage,
                        onDone = { go(Screen.TryOnResult(screen.styleId, screen.jobId)) },
                        onLeave = {
                            currentTab = MainTab.Home
                            stack.clear()
                            stack.add(Screen.Tab(MainTab.Home))
                        }
                    )
                }

                is Screen.TryOnResult -> TryOnResultScreen(
                    style = styleById(screen.styleId) ?: return@AnimatedContent,
                    favorite = favorites.contains(screen.styleId),
                    resultBitmap = latestTryOnBitmap,
                    results = latestTryOnResults,
                    analysisContext = tryOnStatus.analysisContext,
                    onOpenStyle = { openStyleDetail(screen.styleId, "tryon_result", "result_style_button") },
                    onRetake = { go(Screen.TryOnUpload(screen.styleId)) },
                    onToggleFavorite = { toggleFavorite(screen.styleId) },
                    onBook = { openBookingForStyle(screen.styleId) },
                    onSaveImage = { selectedBitmap ->
                        val bitmap = selectedBitmap ?: latestTryOnBitmap
                        if (bitmap == null) {
                            Toast.makeText(context, "试戴图还没生成完成", Toast.LENGTH_SHORT).show()
                        } else {
                            saveBitmapToGallery(context, bitmap, styleById(screen.styleId)?.name ?: "tryon")
                                .onSuccess {
                                    Toast.makeText(context, "图片已保存到相册", Toast.LENGTH_SHORT).show()
                                }
                                .onFailure { error ->
                                    Toast.makeText(context, error.message ?: "保存失败，请稍后再试", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                )

                Screen.TryOnHistory -> TryOnHistoryScreen(
                    items = displayedTryOnHistoryItems,
                    styles = styleItems,
                    refreshing = pageRefreshing,
                    manageMode = tryOnHistoryManageMode,
                    onRefresh = ::refreshPageData,
                    onShare = { selected ->
                        if (selected.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "我的 Nail Mind 试戴记录：${selected.joinToString("、") { it.styleName }}")
                            }
                            context.startActivity(Intent.createChooser(intent, "分享试戴记录"))
                        }
                    },
                    onMoveToFavorites = { selected ->
                        selected.map { it.styleId }.filter { it.isNotBlank() }.forEach { styleId ->
                            if (!favorites.contains(styleId)) favorites.add(styleId)
                        }
                        Toast.makeText(context, "已移入收藏", Toast.LENGTH_SHORT).show()
                    },
                    onDelete = { selected ->
                        tryOnHistoryItems = tryOnHistoryItems.filterNot { item ->
                            selected.any { it.id == item.id }
                        }
                        Toast.makeText(context, "已从当前列表删除", Toast.LENGTH_SHORT).show()
                    },
                    onOpenResult = { openTryOnHistoryResult(it) },
                    onTryAgain = { go(Screen.TryOnUpload(it, true)) }
                )

                Screen.Favorites -> FavoritesScreen(
                    styles = (styleItems + acetateTrendCandidates)
                        .distinctBy { it.id }
                        .filter { favorites.contains(it.id) },
                    refreshing = pageRefreshing,
                    onRefresh = ::refreshPageData,
                    onStyleClick = { openStyleDetail(it, "favorites", "favorite_style_button") },
                    onRetake = { go(Screen.TryOnUpload(it, true)) },
                    onBook = ::openBookingForStyle
                )

                Screen.BookingRecords -> BookingRecordsScreen(
                    records = bookingRecords,
                    refreshing = pageRefreshing,
                    onRefresh = ::refreshPageData
                )

                Screen.Reviews -> ReviewsScreen(
                    records = bookingRecords,
                    onSubmit = { bookingId, draft ->
                        val imageUrl = repository.uploadBookingReviewImage(bookingId, draft.actualWorkFile).imageUrl
                        repository.reviewBooking(
                            bookingId = bookingId,
                            satisfactionScore = draft.satisfactionScore,
                            actualWorkImageUrl = imageUrl
                        )
                        bookingRecords = repository.bookings().items.map { it.toUi() }
                    }
                )

                is Screen.StoreDetail -> {
                    val store = storeItems.firstOrNull { it.id == screen.storeId } ?: return@AnimatedContent
                    LaunchedEffect(store.id) {
                        if (restorationPerformance[store.id] == null) {
                            runCatching { repository.restorationPerformance(store.id).categories }
                                .onSuccess { categories ->
                                    restorationPerformance = restorationPerformance + (store.id to categories)
                                }
                        }
                    }
                    StoreDetailScreen(
                        store = store,
                        restorationCategories = restorationPerformance[store.id].orEmpty(),
                        onBack = ::back,
                        onConsult = {
                            coroutineScope.launch {
                                chatLoading = true
                                chatError = null
                                runCatching {
                                    repository.startChat(store.id, "你好，我想咨询一下这家门店。")
                                }.onSuccess { response ->
                                    chatMessages = response.messages
                                    val chatStoreName = response.conversation.storeName.ifBlank { store.name }
                                    updateMerchantReplyNotice(response.messages, response.conversation.id, store.id, chatStoreName)
                                    go(Screen.Chat(response.conversation.id, store.id, chatStoreName))
                                }.onFailure { error ->
                                    chatError = error.message ?: "发起咨询失败"
                                    Toast.makeText(context, chatError ?: "发起咨询失败", Toast.LENGTH_SHORT).show()
                                }
                                chatLoading = false
                            }
                        },
                        onBook = {
                            selectedStoreId = store.id
                            val targetStyleId = screen.styleId ?: styleItems.firstOrNull()?.id
                            if (targetStyleId == null) {
                                Toast.makeText(context, "当前还没有可预约款式", Toast.LENGTH_SHORT).show()
                            } else {
                                go(Screen.BookingForm(store.id, targetStyleId, fixedStore = true))
                            }
                        }
                    )
                }

                is Screen.Chat -> {
                    LaunchedEffect(screen.conversationId) {
                        chatLoading = true
                        chatError = null
                        runCatching { repository.chatMessages(screen.conversationId).items }
                            .onSuccess {
                                chatMessages = it
                                updateMerchantReplyNotice(it, screen.conversationId, screen.storeId, screen.storeName)
                            }
                            .onFailure { error -> chatError = error.message ?: "加载聊天失败" }
                        chatLoading = false
                    }
                    StoreChatScreen(
                        storeName = screen.storeName,
                        messages = chatMessages,
                        loading = chatLoading,
                        sending = chatSending,
                        errorMessage = chatError,
                        onBack = ::back,
                        onRefresh = {
                            coroutineScope.launch {
                                chatLoading = true
                                chatError = null
                                runCatching { repository.chatMessages(screen.conversationId).items }
                                    .onSuccess {
                                        chatMessages = it
                                        updateMerchantReplyNotice(it, screen.conversationId, screen.storeId, screen.storeName)
                                    }
                                    .onFailure { error -> chatError = error.message ?: "加载聊天失败" }
                                chatLoading = false
                            }
                        },
                        onSend = { body ->
                            coroutineScope.launch {
                                chatSending = true
                                chatError = null
                                runCatching {
                                    repository.sendChatMessage(screen.conversationId, body)
                                    repository.chatMessages(screen.conversationId).items
                                }.onSuccess {
                                    chatMessages = it
                                    updateMerchantReplyNotice(it, screen.conversationId, screen.storeId, screen.storeName)
                                }
                                    .onFailure { error ->
                                        chatError = error.message ?: "发送失败"
                                        Toast.makeText(context, chatError ?: "发送失败", Toast.LENGTH_SHORT).show()
                                    }
                                chatSending = false
                            }
                        }
                    )
                }

                is Screen.BookingForm -> {
                    val style = styleById(screen.styleId) ?: return@AnimatedContent
                    BookingFormScreen(
                        initialStyle = style,
                        styleOptions = (styleItems + acetateTrendCandidates).distinctBy { it.id },
                        initialStoreId = screen.storeId,
                        storeOptions = storeItems,
                        fixedStore = screen.fixedStore,
                        submitting = bookingSubmitting,
                        errorMessage = bookingError,
                        onSubmit = { storeId, styleId, name, phone, note, slot ->
                            coroutineScope.launch {
                                bookingSubmitting = true
                                bookingError = null
                                runCatching {
                                    repository.createBooking(
                                        storeId = storeId,
                                        styleId = styleId,
                                        slot = slot,
                                        name = name,
                                        phone = phone,
                                        note = note
                                    )
                                }.onSuccess { booking ->
                                    pendingBooking = booking
                                    bookingRecords = listOf(booking.toUi()) + bookingRecords.filterNot { it.id == booking.id }
                                    go(Screen.BookingConfirm(booking.id))
                                }.onFailure { error ->
                                    bookingError = error.message ?: "创建预约失败"
                                }
                                bookingSubmitting = false
                            }
                        }
                    )
                }
                is Screen.BookingConfirm -> {
                    val booking = findBooking(screen.bookingId) ?: return@AnimatedContent
                    BookingConfirmScreen(
                        booking = booking,
                        loading = bookingSubmitting,
                        errorMessage = bookingError,
                        onConfirm = {
                            coroutineScope.launch {
                                bookingSubmitting = true
                                bookingError = null
                                runCatching { repository.confirmBooking(screen.bookingId) }
                                    .onSuccess { confirmed ->
                                        pendingBooking = confirmed
                                        bookingRecords = bookingRecords.map { if (it.id == confirmed.id) confirmed.toUi() else it }
                                        go(Screen.BookingSuccess(screen.bookingId))
                                    }
                                    .onFailure { error ->
                                        bookingError = error.message ?: "确认预约失败"
                                    }
                                bookingSubmitting = false
                            }
                        }
                    )
                }

                is Screen.BookingSuccess -> BookingSuccessScreen(
                    booking = findBooking(screen.bookingId) ?: return@AnimatedContent,
                    onRecords = { go(Screen.BookingRecords) },
                    onBackHome = {
                        currentTab = MainTab.Home
                        stack.clear()
                        stack.add(Screen.Tab(MainTab.Home))
                    }
                )

                Screen.Settings -> SettingsScreen(
                    settings = userSettings,
                    onLogout = {
                        coroutineScope.launch {
                            runCatching { repository.logout() }
                            resetToLogin()
                        }
                    }
                )
            }
        }
    }
    }
}

private enum class XiaomeiChatRole {
    User,
    Assistant
}

private data class XiaomeiChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: XiaomeiChatRole,
    val text: String,
    val imagePath: String? = null,
    val response: MeimeiChatResponse? = null,
    val isError: Boolean = false,
    val isStreaming: Boolean = false
)

private data class XiaomeiChatSession(
    val id: String,
    val title: String,
    val updatedAt: Long,
    val messages: List<XiaomeiChatMessage>
)

private const val XiaomeiHistoryPreference = "xiaomei_chat_history_v1"
private const val XiaomeiHistoryRetentionMs = 3L * 24L * 60L * 60L * 1000L

private fun loadXiaomeiHistory(context: Context): List<XiaomeiChatSession> {
    val raw = context.getSharedPreferences(AppConfig.preferencesName, Context.MODE_PRIVATE)
        .getString(XiaomeiHistoryPreference, "[]")
        .orEmpty()
    return runCatching {
        val now = System.currentTimeMillis()
        val array = JSONArray(raw)
        buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                val updatedAt = item.optLong("updatedAt", 0L)
                if (updatedAt <= 0L || now - updatedAt > XiaomeiHistoryRetentionMs) continue
                val messageArray = item.optJSONArray("messages") ?: JSONArray()
                val messages = buildList {
                    for (messageIndex in 0 until messageArray.length()) {
                        val message = messageArray.optJSONObject(messageIndex) ?: continue
                        add(
                            XiaomeiChatMessage(
                                id = message.optString("id", UUID.randomUUID().toString()),
                                role = if (message.optString("role") == "assistant") XiaomeiChatRole.Assistant else XiaomeiChatRole.User,
                                text = message.optString("text"),
                                imagePath = message.optString("imagePath").ifBlank { null },
                                isError = message.optBoolean("isError", false)
                            )
                        )
                    }
                }
                if (messages.isNotEmpty()) {
                    add(
                        XiaomeiChatSession(
                            id = item.optString("id", updatedAt.toString()),
                            title = item.optString("title").ifBlank { "小美咨询" },
                            updatedAt = updatedAt,
                            messages = messages
                        )
                    )
                }
            }
        }.sortedByDescending { it.updatedAt }
    }.getOrElse { emptyList() }
}

private fun saveXiaomeiSession(context: Context, messages: List<XiaomeiChatMessage>) {
    val storableMessages = messages.filter {
        !it.isError && !it.isStreaming && (it.text.isNotBlank() || !it.imagePath.isNullOrBlank())
    }
    if (storableMessages.isEmpty()) return
    val firstUser = storableMessages.firstOrNull { it.role == XiaomeiChatRole.User }
    val title = firstUser?.text?.trim()
        ?.take(18)
        ?.ifBlank { null }
        ?: if (!firstUser?.imagePath.isNullOrBlank()) "手部照片推荐" else "小美咨询"
    val now = System.currentTimeMillis()
    val existing = loadXiaomeiHistory(context)
    val session = XiaomeiChatSession(
        id = now.toString(),
        title = title,
        updatedAt = now,
        messages = storableMessages.map { it.copy(response = null) }
    )
    val array = JSONArray()
    (listOf(session) + existing)
        .filter { now - it.updatedAt <= XiaomeiHistoryRetentionMs }
        .distinctBy { it.id }
        .take(30)
        .forEach { item ->
            val messageArray = JSONArray()
            item.messages.forEach { message ->
                messageArray.put(
                    JSONObject()
                        .put("id", message.id)
                        .put("role", if (message.role == XiaomeiChatRole.Assistant) "assistant" else "user")
                        .put("text", message.text)
                        .put("imagePath", message.imagePath ?: "")
                        .put("isError", message.isError)
                )
            }
            array.put(
                JSONObject()
                    .put("id", item.id)
                    .put("title", item.title)
                    .put("updatedAt", item.updatedAt)
                    .put("messages", messageArray)
            )
        }
    context.getSharedPreferences(AppConfig.preferencesName, Context.MODE_PRIVATE)
        .edit()
        .putString(XiaomeiHistoryPreference, array.toString())
        .apply()
}

private fun formatXiaomeiHistoryTime(updatedAt: Long): String {
    return beijingMonthDayTimeFormatter.format(Instant.ofEpochMilli(updatedAt).atZone(beijingTimeZone))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun XiaomeiAssistantSheet(
    repository: NailMindRepository,
    input: String,
    loading: Boolean,
    messages: SnapshotStateList<XiaomeiChatMessage>,
    onInputChange: (String) -> Unit,
    onLoadingChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onOpenDiy: () -> Unit,
    onOpenStyles: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenBooking: () -> Unit,
    onOpenTryOn: (String) -> Unit,
    onOpenStyle: (String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val chatListState = rememberLazyListState()
    var pendingImageFile by remember { mutableStateOf<File?>(null) }
    var showHistory by remember { mutableStateOf(false) }
    var historySessions by remember { mutableStateOf(loadXiaomeiHistory(context)) }
    var streamingStatus by remember { mutableStateOf("小美正在分析...") }

    fun openEntry(entryTarget: String?, styleId: String? = null) {
        when (entryTarget) {
            "diy" -> onOpenDiy()
            "styles" -> onOpenStyles()
            "favorites" -> onOpenFavorites()
            "booking" -> onOpenBooking()
            "tryon", "tryon_upload" -> onOpenTryOn(styleId.orEmpty())
        }
    }

    fun closeXiaomei() {
        saveXiaomeiSession(context, messages)
        historySessions = loadXiaomeiHistory(context)
        messages.clear()
        pendingImageFile = null
        onInputChange("")
        onDismiss()
    }

    fun applyAssistantResponse(messageId: String, result: MeimeiChatResponse) {
        val completedMessage = XiaomeiChatMessage(
            id = messageId,
            role = XiaomeiChatRole.Assistant,
            text = result.toXiaomeiDisplayText(),
            response = result,
            isStreaming = false
        )
        val index = messages.indexOfFirst { it.id == messageId }
        if (index >= 0) {
            messages[index] = completedMessage
        } else {
            messages.add(completedMessage)
        }
        val directTarget = result.entry?.target
        if (result.recommendations.isEmpty() && directTarget in setOf("diy", "styles", "favorites", "booking")) {
            openEntry(directTarget)
        }
    }

    fun sendMessage(rawText: String, imageFile: File?) {
        val content = rawText.trim()
        if ((content.isBlank() && imageFile == null) || loading) return
        onInputChange("")
        pendingImageFile = null
        onLoadingChange(true)
        streamingStatus = "小美正在分析..."
        val displayText = content.ifBlank { "帮我看看这张手部照片" }
        val lastHandAnalysis = messages.asReversed()
            .mapNotNull { it.response?.handAnalysis }
            .firstOrNull { it.hasHand && it.status != "unavailable" }
        val history = messages.takeLast(12).map { message ->
            MeimeiChatHistoryItemDto(
                role = if (message.role == XiaomeiChatRole.Assistant) "assistant" else "user",
                content = message.text.take(500)
            )
        }
        messages.add(
            XiaomeiChatMessage(
                role = XiaomeiChatRole.User,
                text = displayText,
                imagePath = imageFile?.absolutePath
            )
        )
        coroutineScope.launch {
            val assistantMessageId = UUID.randomUUID().toString()
            var receivedStreamContent = false
            var receivedResult = false

            fun appendDelta(delta: String) {
                if (delta.isEmpty()) return
                receivedStreamContent = true
                val index = messages.indexOfFirst { it.id == assistantMessageId }
                if (index >= 0) {
                    val current = messages[index]
                    messages[index] = current.copy(
                        text = current.text + delta,
                        isStreaming = true
                    )
                } else {
                    messages.add(
                        XiaomeiChatMessage(
                            id = assistantMessageId,
                            role = XiaomeiChatRole.Assistant,
                            text = delta,
                            isStreaming = true
                        )
                    )
                }
            }

            runCatching {
                var handImageUrl: String? = null
                var handImageKey: String? = null
                if (imageFile != null) {
                    val upload = repository.uploadHandImage(imageFile)
                    handImageUrl = upload.image_url
                    handImageKey = upload.hand_id
                }

                val requestMessage = if (imageFile != null) {
                    content.ifBlank { "根据这张手部照片推荐适合的甲型、色号和款式" }
                } else {
                    content
                }
                repository.meimeiChatStream(
                    message = requestMessage,
                    handImageUrl = handImageUrl,
                    handImageKey = handImageKey,
                    lastHandAnalysis = lastHandAnalysis,
                    history = history
                ).collect { event ->
                    when (event) {
                        is MeimeiStreamEvent.Status -> {
                            streamingStatus = event.message.ifBlank { "小美正在分析..." }
                        }
                        is MeimeiStreamEvent.Delta -> {
                            splitMeimeiStreamText(event.text).forEach { chunk ->
                                appendDelta(chunk)
                                delay(36)
                            }
                        }
                        is MeimeiStreamEvent.Result -> {
                            receivedResult = true
                            applyAssistantResponse(assistantMessageId, event.response)
                        }
                        is MeimeiStreamEvent.Error -> throw IOException(event.message)
                        MeimeiStreamEvent.Done -> Unit
                    }
                }

                if (!receivedResult) {
                    throw IOException("小美流式响应未返回完整结果")
                }
            }.onFailure { error ->
                    val partialIndex = messages.indexOfFirst { it.id == assistantMessageId }
                    if (!receivedStreamContent && partialIndex >= 0) {
                        messages.removeAt(partialIndex)
                    } else if (partialIndex >= 0) {
                        messages[partialIndex] = messages[partialIndex].copy(isStreaming = false)
                    }
                    val message = error.message.orEmpty()
                    val userFacing = when {
                        message.contains("connection abort", ignoreCase = true) -> "刚才网络断了一下，可以直接再发一次。"
                        message.contains("timeout", ignoreCase = true) -> "这次分析等太久了，可以换张更清晰的照片再试。"
                        else -> "小美暂时没处理好，换个说法或重新发一次照片。"
                    }
                    Toast.makeText(context, userFacing, Toast.LENGTH_SHORT).show()
                }
            onLoadingChange(false)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val file = copyUriToCache(context, uri)
            if (file != null) {
                pendingImageFile = file
            } else {
                Toast.makeText(context, "读取图片失败，请重试", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openImagePicker() {
        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    val showTypingBubble = loading && messages.lastOrNull()?.isStreaming != true
    val streamingTextLength = messages.lastOrNull()?.takeIf { it.isStreaming }?.text?.length ?: 0
    LaunchedEffect(messages.size, loading, streamingTextLength) {
        val targetIndex = messages.size + if (showTypingBubble) 1 else 0
        if (targetIndex > 0) {
            chatListState.scrollToItem(targetIndex)
        }
    }

    BackHandler(onBack = ::closeXiaomei)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFFDDED),
                        Color(0xFFFFF4F8),
                        Color(0xFFF8FAE9)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(900f, 1300f)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(start = 18.dp, top = 12.dp, end = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        IconButton(onClick = ::closeXiaomei) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "关闭小美",
                                modifier = Modifier.size(28.dp),
                                tint = Color.Black
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.bottom_nav_diy_icon),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("小美", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                historySessions = loadXiaomeiHistory(context)
                                showHistory = true
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text("历史", fontSize = 14.sp, color = Color(0xFF8D6E7A))
                        }
                    }
                }

                LazyColumn(
                    state = chatListState,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (messages.isEmpty()) {
                        item(key = "intro-spacer") {
                            Spacer(Modifier.height(1.dp))
                        }
                    } else {
                        item(key = "time") {
                            XiaomeiTimePill()
                        }
                    }
                    items(messages, key = { it.id }) { message ->
                        XiaomeiChatBubble(
                            message = message,
                            onOpenStyle = onOpenStyle,
                            onTryOn = onOpenTryOn
                        )
                    }
                    if (showTypingBubble) {
                        item(key = "typing") {
                            XiaomeiTypingBubble(streamingStatus)
                        }
                    }
                }
            }

            if (messages.isEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .imePadding()
                        .navigationBarsPadding()
                        .padding(bottom = 124.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    XiaomeiPromptChip("💅 根据我的手型和肤色推荐") { sendMessage("根据我的手型和肤色推荐适合的甲型、色号和款式", null) }
                    XiaomeiPromptChip("✨ 适合上班通勤的简约款") { sendMessage("适合上班通勤的简约美甲，显白一点", null) }
                    XiaomeiPromptChip("🌸 甜酷氛围感美甲推荐") { sendMessage("推荐甜酷氛围感美甲", null) }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
                color = Color.White,
                shape = MaterialTheme.shapes.extraLarge,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE7E1E4)),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pendingImageFile?.let { file ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SubcomposeAsyncImage(
                                model = file,
                                contentDescription = "待发送图片",
                                modifier = Modifier
                                    .size(74.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFFF4EEF1)),
                                contentScale = ContentScale.Crop
                            ) {
                                when (painter.state) {
                                    is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                                    else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Rounded.PhotoCamera, contentDescription = null, tint = Color(0xFFB98AA0))
                                    }
                                }
                            }
                            Text("图片已添加，可继续输入想要的风格", modifier = Modifier.weight(1f), fontSize = 13.sp, color = Color(0xFF85727B))
                            TextButton(onClick = { pendingImageFile = null }, enabled = !loading) {
                                Text("移除", color = Color(0xFFB37A90))
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        val canSend = input.trim().isNotBlank() || pendingImageFile != null
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            BasicTextField(
                                value = input,
                                onValueChange = onInputChange,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                singleLine = true,
                                enabled = !loading,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    fontSize = 14.sp,
                                    color = Color(0xFF6D666A)
                                ),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (input.isEmpty()) {
                                            Text(
                                                "发消息或附上一张手图",
                                                fontSize = 14.sp,
                                                color = Color(0xFF9D969B),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable(enabled = !loading) { openImagePicker() },
                            shape = MaterialTheme.shapes.extraLarge,
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Black)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("+", fontSize = 22.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                            }
                        }
                        Surface(
                            modifier = Modifier
                                .size(width = 52.dp, height = 34.dp)
                                .clickable(enabled = !loading && canSend) {
                                    sendMessage(input, pendingImageFile)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (!loading && canSend) Color(0xFFE7A9C0) else Color(0xFFE9E2E5)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (loading) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color(0xFFE7A9C0))
                                } else {
                                    Text("发送", fontSize = 12.sp, color = if (canSend) Color.White else Color(0xFFAAA0A5), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showHistory) {
            XiaomeiHistoryDialog(
                sessions = historySessions,
                onDismiss = { showHistory = false },
                onOpenSession = { session ->
                    messages.clear()
                    messages.addAll(session.messages)
                    pendingImageFile = null
                    onInputChange("")
                    showHistory = false
                }
            )
        }
    }
}

@Composable
private fun XiaomeiTimePill() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.White.copy(alpha = 0.42f),
            shape = RoundedCornerShape(999.dp)
        ) {
            Text(
                text = beijingClockFormatter.format(Instant.now().atZone(beijingTimeZone)),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8D747E)
            )
        }
    }
}

@Composable
private fun XiaomeiHistoryDialog(
    sessions: List<XiaomeiChatSession>,
    onDismiss: () -> Unit,
    onOpenSession: (XiaomeiChatSession) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭", color = Color(0xFF8D6E7A))
            }
        },
        title = { Text("最近三天", fontWeight = FontWeight.Bold) },
        text = {
            if (sessions.isEmpty()) {
                Text("还没有可查看的历史对话。", color = Color(0xFF756B70))
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(sessions, key = { it.id }) { session ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenSession(session) },
                            color = Color.White,
                            shape = RoundedCornerShape(18.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEDE4E8))
                        ) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(session.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C2529), maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(formatXiaomeiHistoryTime(session.updatedAt), fontSize = 12.sp, color = Color(0xFF8D8086))
                            }
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFFFBF8F7)
    )
}

@Composable
private fun XiaomeiChatBubble(
    message: XiaomeiChatMessage,
    onOpenStyle: (String) -> Unit,
    onTryOn: (String) -> Unit
) {
    val isUser = message.role == XiaomeiChatRole.User
    val bubbleModifier = if (isUser && message.imagePath == null) {
        Modifier.widthIn(min = 46.dp, max = 252.dp)
    } else {
        Modifier.fillMaxWidth(if (isUser) 0.72f else 0.78f)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Surface(
            modifier = bubbleModifier,
            color = when {
                message.isError -> Color(0xFFFFF1F1)
                isUser -> Color(0xFFEFA0C5)
                else -> Color(0xFFFFFBFC)
            },
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = 14.dp,
                bottomEnd = 14.dp
            ),
            shadowElevation = if (isUser) 0.dp else 1.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isUser) Color(0xFFE992BA) else Color.White.copy(alpha = 0.64f)
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = if (isUser) 14.dp else 18.dp, vertical = if (isUser) 10.dp else 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                message.imagePath?.let { path ->
                    SubcomposeAsyncImage(
                        model = File(path),
                        contentDescription = "聊天图片",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFF4EEF1)),
                        contentScale = ContentScale.Crop
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                            else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Rounded.PhotoCamera, contentDescription = null, tint = Color(0xFFB98AA0))
                            }
                        }
                    }
                }
                if (message.text.isNotBlank()) {
                    Text(
                        text = message.text,
                        fontSize = if (isUser) 17.sp else 16.sp,
                        lineHeight = if (isUser) 24.sp else 25.sp,
                        color = if (message.isError) MaterialTheme.colorScheme.error else Color(0xFF261F23),
                        fontWeight = if (isUser) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
        message.response?.let { result ->
            result.handAnalysis?.takeIf { it.hasHand }?.let { analysis ->
                XiaomeiHandAnalysisCard(analysis)
            }
            if (result.recommendations.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    items(result.recommendations, key = { it.id }) { item ->
                        XiaomeiRecommendationCard(
                            item = item,
                            onOpenStyle = { onOpenStyle(item.id) },
                            onTryOn = { onTryOn(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun XiaomeiHandAnalysisCard(analysis: MeimeiHandAnalysisDto) {
    val skinDescription = listOf(analysis.skinTone, analysis.skinUndertone)
        .filter { it.isNotBlank() }
        .distinct()
        .joinToString("·")
    val traits = buildList {
        if (skinDescription.isNotBlank()) add("肤色" to skinDescription)
        if (analysis.handShape.isNotBlank()) add("手型" to analysis.handShape)
        if (analysis.fingerProportion.isNotBlank()) add("手指比例" to analysis.fingerProportion)
        if (analysis.nailBed.isNotBlank()) add("甲床" to analysis.nailBed)
        if (analysis.visibleNails > 0) add("可见指甲" to "${analysis.visibleNails} 枚")
    }

    Surface(
        modifier = Modifier.fillMaxWidth(0.92f),
        color = Color(0xFFFFFBFC),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1DDE6)),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("手部分析", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2A2226))
                Text("已识别", fontSize = 12.sp, color = Color(0xFFD56598))
            }
            traits.forEach { (label, value) ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(label, fontSize = 14.sp, color = Color(0xFF88767E))
                    Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF352B30))
                }
            }
            XiaomeiAnalysisTags("适合甲型", analysis.recommendedShapes)
            XiaomeiAnalysisTags("适合色系", analysis.recommendedColors)
            XiaomeiAnalysisTags("适合风格", analysis.recommendedStyles)
            if (analysis.reason.isNotBlank()) {
                Text(analysis.reason, fontSize = 13.sp, lineHeight = 20.sp, color = Color(0xFF75666D))
            }
        }
    }
}

@Composable
private fun XiaomeiAnalysisTags(label: String, values: List<String>) {
    val visibleValues = values.filter { it.isNotBlank() }.distinct()
    if (visibleValues.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, fontSize = 13.sp, color = Color(0xFF88767E))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            visibleValues.forEach { value ->
                Surface(color = Color(0xFFFFEAF3), shape = RoundedCornerShape(50)) {
                    Text(
                        text = value,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        fontSize = 13.sp,
                        color = Color(0xFFB94C7D)
                    )
                }
            }
        }
    }
}

private fun MeimeiChatResponse.toXiaomeiDisplayText(): String {
    if (handAnalysis?.hasHand == false) {
        return "这张没看清手部，换一张手指和指甲完整露出的照片。"
    }
    if (intent == "beauty_consultation.recommend_clarify" || intent == "unknown.fallback") {
        return reply
    }
    if (recommendations.isNotEmpty()) {
        return reply.ifBlank { "我挑了几款更适合你的，先看这几款。" }
    }
    return reply
        .replace("入口", "")
        .replace("根据你的手型和肤色", "按你的需求")
        .replace("根据手型和肤色", "按你的需求")
        .trim()
        .ifBlank { "好呀。" }
}

@Composable
private fun XiaomeiTypingBubble(status: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(0.62f),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp, bottomEnd = 22.dp, bottomStart = 6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEAE3E6))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color(0xFFE7A9C0))
            Text(status.ifBlank { "小美正在分析..." }, fontSize = 15.sp, color = Color(0xFF6F5962))
        }
    }
}

@Composable
private fun XiaomeiRecommendationCard(
    item: MeimeiRecommendationDto,
    onOpenStyle: () -> Unit,
    onTryOn: () -> Unit
) {
    Surface(
        modifier = Modifier.width(210.dp),
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFECE3E7)),
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF7EEF2)),
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                    else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = RoseAccent)
                    }
                }
            }
            Text(item.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(item.reason, fontSize = 12.sp, lineHeight = 17.sp, color = Color(0xFF7B7176), maxLines = 2, overflow = TextOverflow.Ellipsis)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onOpenStyle, modifier = Modifier.weight(1f)) { Text("详情", fontSize = 12.sp) }
                Button(onClick = onTryOn, modifier = Modifier.weight(1f)) { Text("试戴", fontSize = 12.sp) }
            }
        }
    }
}

@Composable
private fun XiaomeiPromptChip(text: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        color = Color.White,
        shape = MaterialTheme.shapes.extraLarge,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9E4E7))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontSize = 16.sp,
            color = Color(0xFF242124)
        )
    }
}

@Composable
private fun XiaomeiActionChip(text: String, icon: ImageVector) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.extraLarge,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4DEE2))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.Black)
            Text(text, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LegacyHomeScreen(
    recommended: List<NailStyle>,
    hot: List<NailStyle>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onSearch: () -> Unit,
    onSeeMore: () -> Unit,
    onRanking: () -> Unit,
    onStyleClick: (String) -> Unit
) {
    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeHero(onSearch = onSearch)
            HomeSectionHeader(title = "推荐", actionText = "查看更多", onAction = onSeeMore)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.35f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                recommended.take(2).forEach { style ->
                    HomeFeaturedCard(
                        style = style,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = { onStyleClick(style.id) }
                    )
                }
                repeat((2 - recommended.take(2).size).coerceAtLeast(0)) {
                    Spacer(Modifier.weight(1f))
                }
            }
            HomeSectionHeader(title = "热门款式", actionText = "排行榜", onAction = onRanking)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                hot.take(3).forEach { style ->
                    HomeCompactCard(
                        style = style,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        onClick = { onStyleClick(style.id) }
                    )
                }
                repeat((3 - hot.take(3).size).coerceAtLeast(0)) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RabbitTabIcon(selected: Boolean, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.bottom_nav_diy_icon),
        contentDescription = if (selected) "DIY" else "DIY",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    recommended: List<NailStyle>,
    sceneSections: Map<String, List<NailStyle>>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onSearch: () -> Unit,
    onSeeMore: () -> Unit,
    onRanking: () -> Unit,
    onAiPick: () -> Unit,
    onTrend: () -> Unit,
    onStyleQuiz: () -> Unit,
    onStyleClick: (String) -> Unit
) {
    val categories = homeRecommendationCategories
    var selectedHomeCategory by rememberSaveable { mutableStateOf(categories.first()) }
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        selectedHomeCategory = categories[pagerState.currentPage]
    }

    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            HomeTopHero(onSearch = onSearch)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    ActivityBannerPlaceholder(
                        onStyleQuiz = onStyleQuiz,
                        onTrend = onTrend,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp)
                    )
                }
                item {
                    HomeFeatureNavigation(
                        onRanking = onRanking,
                        onAiPick = onAiPick,
                        onTrend = onTrend,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                stickyHeader {
                    HomeCategoryTabs(
                        categories = categories,
                        selectedCategory = selectedHomeCategory,
                        onCategoryChange = { category ->
                            selectedHomeCategory = category
                            val targetPage = categories.indexOf(category).coerceAtLeast(0)
                            coroutineScope.launch { pagerState.animateScrollToPage(targetPage) }
                        },
                        modifier = Modifier
                    )
                }
                item {
                    HomeRecommendationPager(
                        categories = categories,
                        recommended = recommended,
                        sceneSections = sceneSections,
                        pagerState = pagerState,
                        onStyleClick = onStyleClick
                    )
                }
            }
        }
    }
}
@Composable
private fun HomeTopHero(onSearch: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_header_nail_mind),
                contentDescription = "Nail Mind",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(40.dp)
                    .offset(y = 0.dp)
                    .clickable(onClick = onSearch),
                color = Color(0xFFFFF8FB),
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 0.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4AFC4))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = RoseAccent.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "搜索款式 / 风格 / 门店",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f),
                        fontSize = 13.sp
                    )
                    Text(
                        text = "搜索",
                        color = Color(0xFFC87598),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(0.dp))
    }
}

@Composable
private fun ActivityBannerPlaceholder(
    onStyleQuiz: () -> Unit,
    onTrend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val banners = remember(onStyleQuiz, onTrend) {
        listOf(
            ActivityBanner(
                imageRes = R.drawable.home_activity_banner_1,
                contentDescription = "测试适合你的美甲风格",
                onClick = onStyleQuiz
            ),
            ActivityBanner(
                imageRes = R.drawable.home_activity_banner_2,
                contentDescription = "夏日美甲大合集",
                onClick = onTrend
            )
        )
    }
    val pagerState = rememberPagerState(pageCount = { banners.size })

    LaunchedEffect(pagerState) {
        while (true) {
            delay(4_000)
            if (!pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % banners.size)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1837f / 856f)
            .clip(MaterialTheme.shapes.medium)
    ) { page ->
        val banner = banners[page]
        Image(
            painter = painterResource(id = banner.imageRes),
            contentDescription = banner.contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = banner.onClick),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun HomeFeatureNavigation(
    onRanking: () -> Unit,
    onAiPick: () -> Unit,
    onTrend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(82.dp),
        color = Color.White,
        shape = RoundedCornerShape(13.dp),
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 10.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeFeatureItem(title = "排 行 榜", iconRes = R.drawable.home_feature_rank_icon, onClick = onRanking)
            HomeFeatureItem(title = "定 制", iconRes = R.drawable.home_feature_diy_icon, iconOffsetX = 3.dp, onClick = onAiPick)
            HomeFeatureItem(title = "热 门 趋 势", iconRes = R.drawable.home_feature_trend_icon, onClick = onTrend)
        }
    }
}

@Composable
private fun HomeFeatureItem(title: String, iconRes: Int, iconOffsetX: androidx.compose.ui.unit.Dp = 0.dp, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(96.dp)
            .fillMaxHeight()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(58.dp)
                .offset(x = iconOffsetX),
            contentScale = ContentScale.Fit
        )
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-2).dp),
            fontSize = 12.sp,
            lineHeight = 13.sp,
            color = Color(0xFF6F686B),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun WaterfallStyleCard(
    style: NailStyle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val visibleTags = remember(style.displayTags, style.tags) { homeDisplayTags(style) }
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 0.dp
    ) {
        Column {
            GradientThumb(
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.76f),
                showBorder = false
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = style.name,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF151318),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (visibleTags.isNotEmpty()) {
                    Text(
                        text = visibleTags.joinToString("  ") { "#$it" },
                        color = Color(0xFF9B735F),
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun homeDisplayTags(style: NailStyle): List<String> =
    style.displayTags
        .ifEmpty { style.tags }
        .asSequence()
        .map { it.trim() }
        .filter { it.isNotEmpty() && it !in setOf("推荐", "试戴推荐") }
        .distinct()
        .take(2)
        .toList()

@Composable
private fun HomeHero(onSearch: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Nail Mind",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "发现适合你的美甲风格",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    fontSize = 13.sp
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.Rounded.NotificationsNone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(23.dp)
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSearch),
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
                Text(
                    text = "搜索款式 / 风格 / 门店",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f),
                    fontSize = 13.sp
                )
                Text(
                    text = "输入关键词",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

private val homeRecommendationCategories = listOf("推荐", "日常", "通勤", "约会", "旅游", "个性")

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeRecommendationPager(
    categories: List<String>,
    recommended: List<NailStyle>,
    sceneSections: Map<String, List<NailStyle>>,
    pagerState: PagerState,
    onStyleClick: (String) -> Unit
) {
    val currentCategory = categories.getOrElse(pagerState.currentPage) { categories.first() }
    val currentFeed = remember(recommended, sceneSections, currentCategory) {
        homeFeedForCategory(recommended, sceneSections, currentCategory)
    }
    val rowCount = ((currentFeed.size + 1) / 2).coerceAtLeast(1)
    val pagerHeight = if (currentFeed.isEmpty()) 180.dp else ((rowCount * 304) + ((rowCount - 1) * 6)).dp

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(pagerHeight),
        verticalAlignment = Alignment.Top
    ) { page ->
        val category = categories[page]
        val pageFeed = remember(recommended, sceneSections, category) {
            homeFeedForCategory(recommended, sceneSections, category)
        }
        if (pageFeed.isEmpty()) {
            EmptyState(
                title = "暂无推荐款式",
                subtitle = "下拉刷新后会展示适合你的美甲款式"
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                pageFeed.chunked(2).forEach { rowStyles ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        rowStyles.forEach { style ->
                            WaterfallStyleCard(
                                style = style,
                                modifier = Modifier.weight(1f),
                                onClick = { onStyleClick(style.id) }
                            )
                        }
                        if (rowStyles.size == 1) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeCategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val tabWidth = 78.dp

    LaunchedEffect(selectedCategory, categories) {
        val index = categories.indexOf(selectedCategory)
        if (index >= 0) {
            val targetIndex = if (index <= 1) 0 else (index - 1).coerceAtMost(categories.lastIndex)
            listState.animateScrollToItem(targetIndex)
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .background(Color.White),
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories, key = { it }) { category ->
            Box(
                modifier = Modifier
                    .width(tabWidth)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                HomeCategoryTab(
                    text = category,
                    selected = selectedCategory == category,
                    onClick = { onCategoryChange(category) }
                )
            }
        }
    }
}
@Composable
private fun HomeCategoryTab(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .height(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Color.White else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = if (selected) 7.dp else 0.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = if (selected) RoseAccent else MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1
        )
        Spacer(Modifier.height(3.dp))
        Box(
            modifier = Modifier
                .width(if (selected) 18.dp else 0.dp)
                .height(2.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(if (selected) RoseAccent else Color.Transparent)
        )
    }
}
private fun homeFeedForCategory(
    recommended: List<NailStyle>,
    sceneSections: Map<String, List<NailStyle>>,
    category: String
): List<NailStyle> = if (category == "推荐") {
    recommended.distinctBy { it.id }
} else {
    sceneSections[category].orEmpty().distinctBy { it.id }
}

@Composable
private fun HomeSectionHeader(
    title: String,
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(18.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        )
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.clickable(onClick = onAction),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = actionText,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun HomeFeaturedCard(
    style: NailStyle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        )
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            GradientThumb(
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.82f)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = style.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = style.vibe,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun HomeCompactCard(
    style: NailStyle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        )
    ) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            GradientThumb(
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.88f)
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = style.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = style.tags.firstOrNull() ?: style.vibe,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RankingScreen(
    heatItems: List<RankedStyle>,
    tryOnItems: List<RankedStyle>,
    bookingItems: List<RankedStyle>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onStyleClick: (String) -> Unit
) {
    var selectedMetric by rememberSaveable { mutableStateOf("热度最高") }
    var metricMenuExpanded by remember { mutableStateOf(false) }
    val items = when (selectedMetric) {
        "试戴最多" -> tryOnItems
        "预约最多" -> bookingItems
        else -> heatItems
    }
    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box {
                    Surface(
                        modifier = Modifier.clickable { metricMenuExpanded = true },
                        color = RoseTint,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            "$selectedMetric  ▾",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            color = RoseAccent,
                            fontSize = 13.sp
                        )
                    }
                    DropdownMenu(expanded = metricMenuExpanded, onDismissRequest = { metricMenuExpanded = false }) {
                        listOf("热度最高", "试戴最多", "预约最多").forEach { metric ->
                            DropdownMenuItem(
                                text = { Text(metric) },
                                onClick = {
                                    selectedMetric = metric
                                    metricMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            items(items, key = { it.style.id }) { item ->
                RankingRow(item = item, onClick = { onStyleClick(item.style.id) })
            }
            if (items.isEmpty()) item { EmptyState("暂无排行数据", "有用户产生互动后，榜单会自动更新。") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrendsScreen(
    topics: List<TrendTopic>,
    items: List<RankedStyle>,
    keywords: List<String>,
    updatedAt: String,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onTopicClick: (String) -> Unit
) {
    val displayTopics = trendTopicsForDisplay(topics = topics, items = items, updatedAt = updatedAt)
    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (keywords.isNotEmpty()) {
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(keywords.take(6)) { keyword ->
                            Surface(color = RoseTint, shape = RoundedCornerShape(50)) {
                                Text(keyword, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), color = RoseAccent, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            items(displayTopics, key = { it.id }) { topic ->
                TrendTopicCard(topic = topic, onClick = { onTopicClick(topic.id) })
            }
            if (displayTopics.isEmpty()) item { EmptyState("暂无热门趋势", "新趋势完成审核后会展示在这里。") }
            if (updatedAt.isNotBlank()) item {
                Text("数据更新于 ${formatBeijingDateTime(updatedAt)}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f), fontSize = 11.sp)
            }
        }
    }
}

private fun trendTopicsForDisplay(
    topics: List<TrendTopic>,
    items: List<RankedStyle>,
    updatedAt: String
): List<TrendTopic> {
    val apiTopics = topics.ifEmpty {
        if (items.isEmpty()) emptyList() else listOf(
            TrendTopic(
                id = "legacy-trends",
                name = "本周热门美甲",
                summary = "从近期受到关注的美甲款式中，挑选出适合继续探索和试戴的代表设计。",
                styles = items.map { it.style },
                updatedAt = updatedAt
            )
        )
    }
    return listOf(acetateMockTrend) + apiTopics.filterNot { it.id == acetateMockTrend.id }
}

@Composable
private fun TrendTopicCard(topic: TrendTopic, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.26f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = topic.styles.firstOrNull()?.imageUrl,
                contentDescription = topic.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(RoseTint)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(topic.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    topic.summary,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    buildString {
                        append("${topic.styles.size} 款候选")
                        if (!topic.badge.isNullOrBlank()) append(" · NailClaw 分析")
                    },
                    color = RoseAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = "查看趋势", tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f))
        }
    }
}

@Composable
private fun TrendDetailScreen(
    topic: TrendTopic,
    onStyleClick: (String) -> Unit,
    onTryOn: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                topic.badge?.takeIf { it.isNotBlank() }?.let { badge ->
                    Surface(color = RoseTint, shape = RoundedCornerShape(50)) {
                        Text(
                            badge,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            color = RoseAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(topic.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(
                    topic.summary,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
                topic.provenance?.takeIf { it.isNotBlank() }?.let { provenance ->
                    Text(provenance, color = RoseAccent, fontSize = 13.sp)
                }
            }
        }
        items(topic.styles.chunked(2), key = { row -> row.joinToString("-") { it.id } }) { rowStyles ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowStyles.forEach { style ->
                    TrendCandidateCard(
                        style = style,
                        onClick = { onStyleClick(style.id) },
                        onTryOn = { onTryOn(style.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowStyles.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TrendCandidateCard(
    style: NailStyle,
    onClick: () -> Unit,
    onTryOn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SubcomposeAsyncImage(
            model = style.imageUrl,
            contentDescription = style.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.82f)
                .clip(RoundedCornerShape(18.dp))
                .background(RoseTint)
                .clickable(onClick = onClick)
        )
        Text(
            style.name,
            modifier = Modifier.clickable(onClick = onClick),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            style.vibe,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
            fontSize = 12.sp,
            lineHeight = 17.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Button(
            onClick = onTryOn,
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoseAccent)
        ) {
            Text("AI 试戴", fontSize = 13.sp)
        }
    }
}

@Composable
private fun TrendStyleTile(style: NailStyle, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SubcomposeAsyncImage(
            model = style.imageUrl,
            contentDescription = style.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.86f)
                .clip(RoundedCornerShape(18.dp))
                .background(RoseTint),
            loading = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                }
            }
        )
        Text(
            style.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RankingRow(item: RankedStyle, onClick: () -> Unit) {
    val rank = item.rank
    val style = item.style
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.36f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = when (rank) {
                    1 -> MaterialTheme.colorScheme.primary
                    2 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.72f)
                    3 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.48f)
                    else -> RoseTint
                },
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = rank.toString().padStart(2, '0'),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    color = if (rank <= 3) MaterialTheme.colorScheme.onPrimary else RoseAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            GradientThumb(style = style, modifier = Modifier.size(82.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(style.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(style.vibe, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f), fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    if (item.score > 0) "热度 ${item.score}  ·  试戴 ${item.tryOns}  ·  预约 ${item.bookings}" else "暂无互动数据",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StylesScreen(
    styles: List<NailStyle>,
    refreshing: Boolean,
    selectedTabName: String,
    selectedOptionLabel: String,
    sortMode: String,
    query: String,
    onSelectedTabChange: (StyleBrowseTab) -> Unit,
    onSelectedOptionChange: (String) -> Unit,
    onSortModeChange: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onOpenFilter: () -> Unit,
    onRefresh: () -> Unit,
    onStyleClick: (String) -> Unit
) {
    val selectedTab = StyleBrowseTab.entries.firstOrNull { it.name == selectedTabName } ?: StyleBrowseTab.NailShape
    val options = selectedTab.options()
    val selectedOption = options.firstOrNull { it.label == selectedOptionLabel } ?: options.first()

    LaunchedEffect(selectedTabName, selectedOptionLabel) {
        if (options.none { it.label == selectedOptionLabel }) {
            onSelectedOptionChange(options.first().label)
        }
    }

    val queryMatchedStyles = styles.filter { it.matchesStyleQuery(query) }
    val categoryMatchedStyles = queryMatchedStyles.filter { it.matchesBrowseOption(selectedTab, selectedOption) }
    val filteredStyles = when (sortMode) {
        "收藏多" -> categoryMatchedStyles.sortedByDescending { it.tags.size }
        "新上架" -> categoryMatchedStyles.asReversed()
        else -> categoryMatchedStyles
    }

    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .offset(y = 0.dp)
                .padding(top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyleSearchField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                )
                Surface(
                    modifier = Modifier
                        .width(86.dp)
                        .height(36.dp)
                        .clickable(onClick = onOpenFilter),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "筛选",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.76f),
                            maxLines = 1
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .clip(RoundedCornerShape(0.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(0.dp))
            ) {
                StyleBrowseTab.values().forEach { tab ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .clickable { onSelectedTabChange(tab) },
                        color = if (selectedTab == tab) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else Color.White
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab.title,
                                color = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f),
                                fontSize = 14.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 24.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 0.dp, top = 6.dp, end = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(74.dp)
                        .clip(RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 18.dp, bottomEnd = 18.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f), RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 18.dp, bottomEnd = 18.dp))
                        .padding(vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    options.forEach { option ->
                        val selected = selectedOption.label == option.label
                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .clickable { onSelectedOptionChange(option.label) },
                            color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = option.label,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 9.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("热度高", "收藏多", "新上架").forEach { mode ->
                            StyleSortChip(
                                text = mode,
                                selected = sortMode == mode,
                                onClick = { onSortModeChange(mode) }
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 96.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (filteredStyles.isEmpty()) {
                            item {
                                EmptyState("没有匹配款式", "换个关键词，或切换到其他${selectedTab.title}小类。")
                            }
                        } else {
                            items(filteredStyles.chunked(2), key = { rowStyles -> rowStyles.joinToString("|") { it.id } }) { rowStyles ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowStyles.forEach { style ->
                                        StyleBrowseGridCard(
                                            style = style,
                                            modifier = Modifier.weight(1f),
                                            onClick = { onStyleClick(style.id) }
                                        )
                                    }
                                    if (rowStyles.size == 1) {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StyleSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                lineHeight = 16.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = "输入关键词",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun StyleSortChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .height(30.dp)
            .clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
        )
    ) {
        Box(
            modifier = Modifier
                .width(70.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 13.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun StyleBrowseGridCard(
    style: NailStyle,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(218.dp)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small,
        shadowElevation = 0.dp
    ) {
        Column {
            GradientThumb(
                style = style,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                showBorder = false
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(
                    text = style.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = style.tags.firstOrNull() ?: style.vibe,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun StyleBrowseTab.options(): List<StyleBrowseOption> = when (this) {
    StyleBrowseTab.NailShape -> nailShapeBrowseOptions
    StyleBrowseTab.Effect -> effectBrowseOptions
    StyleBrowseTab.Vibe -> vibeBrowseOptions
}

private fun NailStyle.searchCorpus(): String =
    listOf(name, vibe, nailType, skinTone, tags.joinToString(" ")).joinToString(" ")

private fun NailStyle.matchesStyleQuery(query: String): Boolean {
    val trimmed = query.trim()
    if (trimmed.isBlank()) return true
    return searchCorpus().contains(trimmed, ignoreCase = true)
}

private fun NailStyle.matchesBrowseOption(tab: StyleBrowseTab, option: StyleBrowseOption): Boolean {
    val normalizedKeywords = option.keywords.map { it.trim() }.filter { it.isNotBlank() }
    val groupedTags = when (tab) {
        StyleBrowseTab.NailShape -> listOf(nailType) + tagGroups.nailShapes
        StyleBrowseTab.Effect -> tagGroups.effects.ifEmpty { tags }
        StyleBrowseTab.Vibe -> tagGroups.vibes.ifEmpty { tags }
    }.map { it.trim() }.filter { it.isNotBlank() }
    return when (tab) {
        StyleBrowseTab.NailShape,
        StyleBrowseTab.Effect,
        StyleBrowseTab.Vibe -> normalizedKeywords.any { keyword ->
            groupedTags.any { tag -> tag.equals(keyword, ignoreCase = true) }
        }
    }
}

private fun Int.floorMod(modulus: Int): Int = ((this % modulus) + modulus) % modulus

@Composable
private fun TryOnHubScreen(
    favorites: List<NailStyle>,
    hot: List<NailStyle>,
    systemRecommended: List<NailStyle>,
    onHotPick: (String) -> Unit,
    onFavoritePick: (String) -> Unit,
    onRecommendedPick: (String) -> Unit,
    onDiy: () -> Unit
) {
    val context = LocalContext.current
    var activePanel by remember { mutableStateOf<TryOnPanel?>(null) }
    val activeItems = when (activePanel) {
        TryOnPanel.Hot -> hot
        TryOnPanel.Favorites -> favorites
        TryOnPanel.Recommended -> systemRecommended
        null -> emptyList()
    }
    val activePick: (String) -> Unit = when (activePanel) {
        TryOnPanel.Hot -> onHotPick
        TryOnPanel.Favorites -> onFavoritePick
        TryOnPanel.Recommended -> onRecommendedPick
        null -> onHotPick
    }
    val fallbackStyle = systemRecommended.firstOrNull() ?: hot.firstOrNull() ?: favorites.firstOrNull()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.width(72.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TryOnSideButton("热门", Icons.Rounded.Star, activePanel == TryOnPanel.Hot) { activePanel = TryOnPanel.Hot }
                    TryOnSideButton("收藏", Icons.Rounded.FavoriteBorder, activePanel == TryOnPanel.Favorites) { activePanel = TryOnPanel.Favorites }
                    TryOnSideButton("推荐", Icons.Rounded.AutoAwesome, activePanel == TryOnPanel.Recommended) { activePanel = TryOnPanel.Recommended }
                    TryOnSideButton("DIY", Icons.Rounded.BookmarkBorder, false, onDiy)
                }

                if (activePanel != null) {
                    TryOnSlideOutList(
                        title = activePanel!!.title,
                        styles = activeItems,
                        onPick = activePick,
                        modifier = Modifier.width(148.dp)
                    )
                }

                TryOnUploadIllustration(
                    modifier = Modifier.weight(1f),
                    onUpload = {
                        fallbackStyle?.let { onHotPick(it.id) }
                            ?: Toast.makeText(context, "暂无可试戴款式", Toast.LENGTH_SHORT).show()
                    }
                )

                Column(
                    modifier = Modifier.width(58.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "拍摄时保持手部清晰、手指自然张开、光线充足。", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier
                            .width(54.dp)
                            .height(86.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("拍摄\n参考", textAlign = TextAlign.Center, fontSize = 13.sp, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}

private enum class TryOnPanel(val title: String) {
    Hot("热门款式"),
    Favorites("收藏款式"),
    Recommended("系统推荐")
}

@Composable
private fun TryOnSideButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(width = 64.dp, height = 78.dp)
            .clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent,
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = if (selected) 0.55f else 0.32f)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
            Spacer(Modifier.height(6.dp))
            Text(label, fontSize = 13.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun TryOnSlideOutList(
    title: String,
    styles: List<NailStyle>,
    onPick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.42f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
    ) {
        Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (styles.isEmpty()) {
                Text("暂无数据", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(styles.take(8)) { style ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { onPick(style.id) }
                                .padding(7.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GradientThumb(style = style, modifier = Modifier.size(42.dp))
                            Column(Modifier.weight(1f)) {
                                Text(style.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("点击试戴", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TryOnUploadIllustration(modifier: Modifier = Modifier, onUpload: () -> Unit) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(0.72f),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f),
                shape = MaterialTheme.shapes.large,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(68.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.34f)
                    )
                    Spacer(Modifier.height(14.dp))
                    Text("上传手部图片开始试戴", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f), fontSize = 15.sp)
                }
            }
        }
        OutlinedButton(
            onClick = onUpload,
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Rounded.PhotoCamera, contentDescription = null, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(12.dp))
            Text("拍照上传", fontSize = 18.sp)
        }
    }
}

private data class DiyColorChoice(
    val name: String,
    val color: Color,
    val glitter: Boolean = false
)

private data class DiyNailState(
    val color: Color,
    val effect: String = "裸粉"
)

private val diyShapeOptions = listOf("短方", "短圆", "中方", "中椭圆", "中短梯", "长梯", "长椭圆")

private val DiyHotPink = Color(0xFFD993AE)

private val diyShapeDescriptions = mapOf(
    "短方" to "两侧轻微收窄，前缘平直偏短，适合日常百搭，修饰手型。",
    "短圆" to "短款圆润边角，干净耐看，适合通勤与短甲用户。",
    "中方" to "线条利落，甲面存在感更强，适合简约纯色和法式。",
    "中椭圆" to "边缘柔和，显手指修长，适合粉色、裸色和细闪。",
    "中短梯" to "自然收窄，兼顾利落与柔和，适合轻法式。",
    "长梯" to "视觉拉长明显，适合亮片、猫眼和复杂装饰。",
    "长椭圆" to "修长温柔，适合渐变、晕染和珠光材质。"
)

private fun diyShapeImageRes(shape: String): Int = when (shape) {
    "短方" -> R.drawable.diy_nail_short_square
    "短圆" -> R.drawable.diy_nail_short_round
    "中方" -> R.drawable.diy_nail_medium_square
    "中椭圆" -> R.drawable.diy_nail_medium_oval
    "中短梯" -> R.drawable.diy_nail_medium_ladder
    "长梯" -> R.drawable.diy_nail_long_ladder
    "长椭圆" -> R.drawable.diy_nail_long_oval
    else -> R.drawable.diy_nail_short_square
}

private val diyColorChoices = listOf(
    DiyColorChoice("裸粉", Color(0xFFF6D3D9)),
    DiyColorChoice("樱花粉", Color(0xFFF09AA6)),
    DiyColorChoice("豆沙粉", Color(0xFFD69AAE)),
    DiyColorChoice("奶茶", Color(0xFFF1C6AA)),
    DiyColorChoice("蜜桃", Color(0xFFFFB39B)),
    DiyColorChoice("玫瑰红", Color(0xFFC85B5D)),
    DiyColorChoice("亮片", Color(0xFFEBA9C0), glitter = true),
    DiyColorChoice("酒红", Color(0xFFD31A36)),
    DiyColorChoice("灰粉", Color(0xFFB89090)),
    DiyColorChoice("珍珠白", Color(0xFFF7F2EA)),
    DiyColorChoice("黑曜", Color(0xFF171414))
)

@Composable
private fun DiyDesignerScreen(
    onBackHome: () -> Unit,
    onSave: () -> Unit,
    onTryOn: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var selectedShape by remember { mutableStateOf<String?>("短方") }
    var selectedColor by remember { mutableStateOf(diyColorChoices.first()) }
    var overallTab by remember { mutableStateOf("主色") }
    var singleTab by remember { mutableStateOf("颜色") }
    var selectedNail by remember { mutableStateOf(7) }
    val nails = remember {
        mutableStateListOf<DiyNailState>().apply {
            repeat(10) { add(DiyNailState(diyColorChoices.first().color)) }
        }
    }
    val context = LocalContext.current

    BackHandler { onBackHome() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        DiyTopHeader(
            title = when (step) {
                1 -> "选择甲片"
                2 -> "整体设计"
                3 -> "单指编辑"
                else -> "完成设计"
            },
            onBackHome = onBackHome,
            onHelp = {
                Toast.makeText(context, "按步骤选择甲型、整体风格和单指细节，完成后可保存或试戴。", Toast.LENGTH_LONG).show()
            }
        )
        DiyProgress(step = step)

        when (step) {
            1 -> DiyShapeStep(
                selectedShape = selectedShape,
                onShapeSelected = { selectedShape = it },
                onNext = { if (selectedShape != null) step = 2 }
            )
            2 -> DiyOverallStep(
                shape = selectedShape.orEmpty(),
                nails = nails,
                selectedColor = selectedColor,
                activeTab = overallTab,
                onTabChange = { overallTab = it },
                onColorSelected = { choice ->
                    selectedColor = choice
                    nails.indices.forEach { index ->
                        nails[index] = nails[index].copy(color = choice.color, effect = choice.name)
                    }
                },
                onPrevious = { step = 1 },
                onNext = { step = 3 }
            )
            3 -> DiySingleEditStep(
                shape = selectedShape.orEmpty(),
                nails = nails,
                selectedNail = selectedNail,
                selectedColor = selectedColor,
                activeTab = singleTab,
                onTabChange = { singleTab = it },
                onNailSelected = { selectedNail = it },
                onColorSelected = { choice ->
                    selectedColor = choice
                    nails[selectedNail] = nails[selectedNail].copy(color = choice.color, effect = choice.name)
                },
                onCopyAll = {
                    val current = nails[selectedNail]
                    nails.indices.forEach { index -> nails[index] = current }
                },
                onResetCurrent = {
                    nails[selectedNail] = DiyNailState(diyColorChoices.first().color)
                    selectedColor = diyColorChoices.first()
                },
                onPrevious = { step = 2 },
                onFinish = { step = 4 }
            )
            else -> DiyFinishStep(
                shape = selectedShape.orEmpty(),
                nails = nails,
                onPrevious = { step = 3 },
                onSave = onSave,
                onTryOn = onTryOn
            )
        }
    }
}

@Composable
private fun DiyTopHeader(title: String, onBackHome: () -> Unit, onHelp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        IconButton(
            onClick = onBackHome,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回首页", tint = Color.Black, modifier = Modifier.size(28.dp))
        }
        Text(
            title,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(32.dp)
                .clickable(onClick = onHelp),
            shape = MaterialTheme.shapes.large,
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Black)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("?", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
private fun DiyProgress(step: Int) {
    val labels = listOf("选择甲片", "整体设计", "单指编辑", "完成设计")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            labels.forEachIndexed { index, _ ->
                val number = index + 1
                val complete = number < step
                val active = number == step
                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = MaterialTheme.shapes.large,
                    color = if (complete || active) DiyHotPink else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (complete || active) DiyHotPink else Color(0xFFDADADA))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            if (complete) "✓" else number.toString(),
                            color = if (complete || active) Color.White else Color(0xFF777777),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
                if (index < labels.lastIndex) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(if (number < step) DiyHotPink else Color(0xFFE1E1E1))
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEachIndexed { index, label ->
                val number = index + 1
                Text(
                    label,
                    color = if (number <= step) DiyHotPink else Color(0xFF666666),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(64.dp)
                )
            }
        }
    }
}

@Composable
private fun DiyShapeStep(
    selectedShape: String?,
    onShapeSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(224.dp)
                .offset(y = (-12).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.diy_shape_hero),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.BottomCenter
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-46).dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                diyShapeOptions.chunked(4).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEach { shape ->
                            DiyShapeCard(
                                shape = shape,
                                selected = selectedShape == shape,
                                modifier = Modifier.weight(1f),
                                onClick = { onShapeSelected(shape) }
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = RoseTint.copy(alpha = 0.78f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("♥  甲型介绍", color = DiyHotPink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(
                                diyShapeDescriptions[selectedShape] ?: "请选择一种甲型，系统会根据甲型生成整体设计预览。",
                                color = Color(0xFF333333),
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        }
                        Image(
                            painter = painterResource(id = diyShapeImageRes(selectedShape ?: "短方")),
                            contentDescription = null,
                            modifier = Modifier.size(width = 64.dp, height = 78.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            DiyPrimaryButton(
                text = "下一步：整体设计",
                enabled = selectedShape != null,
                onClick = onNext
            )
            Spacer(Modifier.height(42.dp))
        }
    }
}

@Composable
private fun DiyShapeCard(shape: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(108.dp)
            .clickable(onClick = onClick),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (selected) DiyHotPink else Color(0xFFE8E8E8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = diyShapeImageRes(shape)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                contentScale = ContentScale.Fit
            )
            Text(shape, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
    }
}

@Composable
private fun DiyOverallStep(
    shape: String,
    nails: List<DiyNailState>,
    selectedColor: DiyColorChoice,
    activeTab: String,
    onTabChange: (String) -> Unit,
    onColorSelected: (DiyColorChoice) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        DiySectionTitle("整体效果", "为你的专属美甲设定整体风格", shape)
        DiyPreviewPanel(shape = shape, nails = nails, modifier = Modifier.height(218.dp))
        DiyDesignPanel(
            tabs = listOf("主色", "效果", "材质", "法式"),
            activeTab = activeTab,
            onTabChange = onTabChange,
            selectedColor = selectedColor,
            onColorSelected = onColorSelected,
            currentLabel = "当前主色：${selectedColor.name}"
        )
        Spacer(Modifier.weight(1f))
        DiyStepButtons(previousText = "上一步", nextText = "下一步：单指编辑", onPrevious = onPrevious, onNext = onNext)
    }
}

@Composable
private fun DiySingleEditStep(
    shape: String,
    nails: List<DiyNailState>,
    selectedNail: Int,
    selectedColor: DiyColorChoice,
    activeTab: String,
    onTabChange: (String) -> Unit,
    onNailSelected: (Int) -> Unit,
    onColorSelected: (DiyColorChoice) -> Unit,
    onCopyAll: () -> Unit,
    onResetCurrent: () -> Unit,
    onPrevious: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        DiySectionTitle("单指编辑", "编辑每一个指甲的颜色与细节", shape)
        DiyPreviewPanel(
            shape = shape,
            nails = nails,
            selectedIndex = selectedNail,
            onNailSelected = onNailSelected,
            footerText = "当前编辑：右手无名指",
            modifier = Modifier.height(218.dp)
        )
        DiyDesignPanel(
            tabs = listOf("颜色", "花色", "手绘", "装饰", "贴纸"),
            activeTab = activeTab,
            onTabChange = onTabChange,
            selectedColor = selectedColor,
            onColorSelected = onColorSelected,
            currentLabel = "当前颜色：${selectedColor.name}",
            extraActions = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onCopyAll, modifier = Modifier.weight(1f), shape = MaterialTheme.shapes.large) {
                        Text("▣  复制到全部", color = RoseAccent)
                    }
                    OutlinedButton(onClick = onResetCurrent, modifier = Modifier.weight(1f), shape = MaterialTheme.shapes.large) {
                        Text("↻  重置当前", color = RoseAccent)
                    }
                }
            }
        )
        Spacer(Modifier.weight(1f))
        DiyStepButtons(previousText = "上一步", nextText = "完成", onPrevious = onPrevious, onNext = onFinish)
        Text("完成后将生成整副款式图", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = Color(0xFF777777), fontSize = 12.sp)
    }
}

@Composable
private fun DiyFinishStep(
    shape: String,
    nails: List<DiyNailState>,
    onPrevious: () -> Unit,
    onSave: () -> Unit,
    onTryOn: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DiySectionTitle("完成设计", "查看你设计好的整体款式效果", shape)
        DiyPreviewPanel(shape = shape, nails = nails, modifier = Modifier.height(360.dp), finishMode = true)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 4.dp
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    listOf(nails.firstOrNull()?.effect ?: "裸粉", "亮片", "晕染", shape).forEach { tag ->
                        Surface(color = RoseTint.copy(alpha = 0.55f), shape = MaterialTheme.shapes.large) {
                            Text(tag, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), color = RoseAccent, fontSize = 14.sp)
                        }
                    }
                }
                Text("✧  你的款式图已生成，可保存或立即试戴", color = Color(0xFF333333), fontSize = 15.sp)
            }
        }
        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = onPrevious,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("上一步", color = RoseAccent, fontSize = 17.sp)
        }
        OutlinedButton(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text("⇩  保存款式", color = RoseAccent, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
        DiyPrimaryButton(text = "✧  立即试戴", onClick = onTryOn)
    }
}

@Composable
private fun DiySectionTitle(title: String, subtitle: String, shape: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(title, fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(subtitle, fontSize = 16.sp, color = Color(0xFF444444), lineHeight = 21.sp)
        }
        Surface(
            color = RoseTint.copy(alpha = 0.62f),
            shape = MaterialTheme.shapes.large
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("已选甲型：$shape", fontSize = 14.sp, color = Color.Black)
                DiyNailPreview(shape = shape, color = Color(0xFFF6C7CF), modifier = Modifier.size(width = 24.dp, height = 36.dp))
            }
        }
    }
}

@Composable
private fun DiyPreviewPanel(
    shape: String,
    nails: List<DiyNailState>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    onNailSelected: ((Int) -> Unit)? = null,
    footerText: String? = null,
    finishMode: Boolean = false
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = RoseTint.copy(alpha = 0.35f),
        shape = MaterialTheme.shapes.large,
        shadowElevation = if (finishMode) 0.dp else 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            nails.chunked(5).forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEachIndexed { columnIndex, nail ->
                        val index = rowIndex * 5 + columnIndex
                        DiyNailPreview(
                            shape = shape,
                            color = nail.color,
                            glitter = nail.effect == "亮片",
                            selected = selectedIndex == index,
                            modifier = Modifier
                                .size(width = if (finishMode) 62.dp else 54.dp, height = if (finishMode) 112.dp else 92.dp)
                                .clickable(enabled = onNailSelected != null) { onNailSelected?.invoke(index) }
                        )
                    }
                }
                if (rowIndex == 0) Spacer(Modifier.height(if (finishMode) 22.dp else 12.dp))
            }
            footerText?.let {
                Spacer(Modifier.height(6.dp))
                Text(it, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = RoseAccent, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun DiyDesignPanel(
    tabs: List<String>,
    activeTab: String,
    onTabChange: (String) -> Unit,
    selectedColor: DiyColorChoice,
    onColorSelected: (DiyColorChoice) -> Unit,
    currentLabel: String,
    extraActions: (@Composable () -> Unit)? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = MaterialTheme.shapes.large,
        shadowElevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            tab,
                            color = if (tab == activeTab) RoseAccent else Color(0xFF333333),
                            fontSize = 17.sp,
                            fontWeight = if (tab == activeTab) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(44.dp)
                                .height(3.dp)
                                .background(if (tab == activeTab) RoseAccent else Color.Transparent)
                                .clickable { onTabChange(tab) }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("色环", fontSize = 14.sp, color = Color(0xFF333333))
                    DiyColorWheel()
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("常用色", fontSize = 14.sp, color = Color(0xFF333333))
                        Text(currentLabel, fontSize = 14.sp, color = Color(0xFF555555))
                    }
                    diyColorChoices.chunked(6).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                            row.forEach { choice ->
                                DiyColorSwatch(
                                    choice = choice,
                                    selected = choice.name == selectedColor.name,
                                    onClick = { onColorSelected(choice) }
                                )
                            }
                        }
                    }
                }
            }
            extraActions?.invoke()
        }
    }
}

@Composable
private fun DiyColorWheel() {
    Canvas(modifier = Modifier.size(64.dp)) {
        drawCircle(
            brush = Brush.sweepGradient(
                listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red
                )
            ),
            radius = size.minDimension / 2,
            style = Stroke(width = 12.dp.toPx())
        )
    }
}

@Composable
private fun DiyColorSwatch(choice: DiyColorChoice, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick),
        color = choice.color,
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (selected) RoseAccent else Color(0xFFE4E4E4))
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (choice.glitter) {
                Text("✦", color = Color.White.copy(alpha = 0.92f), fontSize = 17.sp)
            }
            if (selected) {
                Surface(modifier = Modifier.size(22.dp), shape = MaterialTheme.shapes.large, color = RoseAccent) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("✓", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun DiyNailPreview(
    shape: String,
    color: Color,
    modifier: Modifier = Modifier,
    glitter: Boolean = false,
    selected: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = nailPathForShape(shape, w, h)
        if (selected) {
            drawRoundRect(
                color = RoseAccent.copy(alpha = 0.85f),
                topLeft = Offset(0f, 0f),
                size = Size(w, h),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(width = 2.dp.toPx())
            )
        }
        drawPath(path, color = color.copy(alpha = 0.95f))
        drawPath(
            path,
            brush = Brush.verticalGradient(
                listOf(Color.White.copy(alpha = 0.36f), Color.Transparent, RoseAccent.copy(alpha = 0.10f))
            )
        )
        drawLine(
            color = Color.White.copy(alpha = 0.78f),
            start = Offset(w * 0.34f, h * 0.18f),
            end = Offset(w * 0.44f, h * 0.68f),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
        if (glitter) {
            listOf(
                Offset(w * 0.58f, h * 0.35f),
                Offset(w * 0.66f, h * 0.52f),
                Offset(w * 0.50f, h * 0.62f)
            ).forEach { center ->
                drawCircle(Color.White.copy(alpha = 0.82f), radius = 2.5.dp.toPx(), center = center)
            }
        }
    }
}

private fun nailPathForShape(shape: String, w: Float, h: Float): Path {
    return Path().apply {
        val top = h * 0.05f
        val bottom = h * 0.95f
        when (shape) {
            "长椭圆" -> {
                moveTo(w * 0.5f, top)
                cubicTo(w * 0.84f, h * 0.07f, w * 0.84f, h * 0.78f, w * 0.55f, bottom)
                cubicTo(w * 0.52f, h * 0.98f, w * 0.48f, h * 0.98f, w * 0.45f, bottom)
                cubicTo(w * 0.16f, h * 0.78f, w * 0.16f, h * 0.07f, w * 0.5f, top)
                close()
            }
            "中椭圆" -> {
                moveTo(w * 0.5f, top)
                cubicTo(w * 0.82f, h * 0.06f, w * 0.84f, h * 0.76f, w * 0.64f, h * 0.91f)
                cubicTo(w * 0.56f, h * 0.98f, w * 0.44f, h * 0.98f, w * 0.36f, h * 0.91f)
                cubicTo(w * 0.16f, h * 0.76f, w * 0.18f, h * 0.06f, w * 0.5f, top)
                close()
            }
            "长梯" -> {
                moveTo(w * 0.24f, top)
                lineTo(w * 0.76f, top)
                cubicTo(w * 0.82f, h * 0.15f, w * 0.74f, h * 0.62f, w * 0.62f, bottom)
                lineTo(w * 0.38f, bottom)
                cubicTo(w * 0.26f, h * 0.62f, w * 0.18f, h * 0.15f, w * 0.24f, top)
                close()
            }
            "中短梯" -> {
                moveTo(w * 0.22f, top)
                lineTo(w * 0.78f, top)
                cubicTo(w * 0.84f, h * 0.16f, w * 0.80f, h * 0.65f, w * 0.67f, bottom)
                lineTo(w * 0.33f, bottom)
                cubicTo(w * 0.20f, h * 0.65f, w * 0.16f, h * 0.16f, w * 0.22f, top)
                close()
            }
            "短方" -> {
                moveTo(w * 0.20f, h * 0.12f)
                quadraticTo(w * 0.22f, top, w * 0.34f, top)
                lineTo(w * 0.66f, top)
                quadraticTo(w * 0.78f, top, w * 0.80f, h * 0.12f)
                cubicTo(w * 0.84f, h * 0.38f, w * 0.76f, h * 0.78f, w * 0.64f, h * 0.92f)
                lineTo(w * 0.36f, h * 0.92f)
                cubicTo(w * 0.24f, h * 0.78f, w * 0.16f, h * 0.38f, w * 0.20f, h * 0.12f)
                close()
            }
            "短圆" -> {
                moveTo(w * 0.28f, top)
                quadraticTo(w * 0.18f, top, w * 0.18f, h * 0.18f)
                lineTo(w * 0.18f, h * 0.82f)
                quadraticTo(w * 0.18f, bottom, w * 0.34f, bottom)
                lineTo(w * 0.66f, bottom)
                quadraticTo(w * 0.82f, bottom, w * 0.82f, h * 0.82f)
                lineTo(w * 0.82f, h * 0.18f)
                quadraticTo(w * 0.82f, top, w * 0.72f, top)
                close()
            }
            "中方" -> {
                moveTo(w * 0.24f, top)
                quadraticTo(w * 0.16f, top, w * 0.16f, h * 0.16f)
                lineTo(w * 0.16f, h * 0.84f)
                quadraticTo(w * 0.16f, bottom, w * 0.30f, bottom)
                lineTo(w * 0.70f, bottom)
                quadraticTo(w * 0.84f, bottom, w * 0.84f, h * 0.84f)
                lineTo(w * 0.84f, h * 0.16f)
                quadraticTo(w * 0.84f, top, w * 0.76f, top)
                close()
            }
            else -> {
                moveTo(w * 0.5f, top)
                cubicTo(w * 0.82f, h * 0.06f, w * 0.84f, h * 0.76f, w * 0.64f, h * 0.91f)
                cubicTo(w * 0.56f, h * 0.98f, w * 0.44f, h * 0.98f, w * 0.36f, h * 0.91f)
                cubicTo(w * 0.16f, h * 0.76f, w * 0.18f, h * 0.06f, w * 0.5f, top)
                close()
            }
        }
    }
}

@Composable
private fun DiyHeroHand(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val skin = Color(0xFFFFDAD7).copy(alpha = 0.7f)
        repeat(5) { index ->
            val x = size.width * (0.22f + index * 0.14f)
            val top = size.height * (0.08f + index * 0.02f)
            drawRoundRect(
                color = skin,
                topLeft = Offset(x, top),
                size = Size(size.width * 0.09f, size.height * 0.62f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx(), 24.dp.toPx())
            )
            drawRoundRect(
                color = Color(0xFFF6C7CF),
                topLeft = Offset(x + size.width * 0.016f, top + size.height * 0.02f),
                size = Size(size.width * 0.058f, size.height * 0.15f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )
        }
    }
}

@Composable
private fun DiyPrimaryButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth(0.86f)
                .height(52.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = DiyHotPink, disabledContainerColor = DiyHotPink.copy(alpha = 0.32f))
        ) {
            Text(text, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DiyStepButtons(previousText: String, nextText: String, onPrevious: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onPrevious,
            modifier = Modifier
                .weight(0.9f)
                .height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(previousText, color = RoseAccent, fontSize = 17.sp)
        }
        Button(
            onClick = onNext,
            modifier = Modifier
                .weight(1.6f)
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = RoseAccent)
        ) {
            Text(nextText, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun FilterPill(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.32f))
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            textAlign = TextAlign.Center,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BookingScreen(stores: List<Store>, onStoreClick: (String) -> Unit) {
    var activeFilter by remember { mutableStateOf("推荐") }
    var distanceFilter by remember { mutableStateOf("智能推荐") }
    var ratingFilter by remember { mutableStateOf<String?>(null) }
    var priceFilter by remember { mutableStateOf<String?>(null) }
    var showPriceRangeDialog by remember { mutableStateOf(false) }
    var pendingMinPrice by remember { mutableStateOf("") }
    var pendingMaxPrice by remember { mutableStateOf("") }
    var minPrice by remember { mutableStateOf<Int?>(null) }
    var maxPrice by remember { mutableStateOf<Int?>(null) }

    val filteredStores = remember(stores, activeFilter, distanceFilter, ratingFilter, priceFilter, minPrice, maxPrice) {
        var result = stores.asSequence()

        result = when (distanceFilter) {
            "3km内" -> result.filter { parseDistanceMeters(it.distance) <= 3000 }
            "10km内" -> result.filter { parseDistanceMeters(it.distance) <= 10000 }
            else -> result
        }

        if (ratingFilter == "只看4星") {
            result = result.filter { (it.score.toFloatOrNull() ?: 0f) >= 4f }
        }

        if (priceFilter == "输入价格区间") {
            result = result.filter { store ->
                val price = parsePriceValue(store.priceBand)
                val aboveMin = minPrice?.let { price >= it * 100 } ?: true
                val belowMax = maxPrice?.let { price <= it * 100 } ?: true
                aboveMin && belowMax
            }
        }

        val list = result.toList()
        when (activeFilter) {
            "评分" -> list.sortedByDescending { it.score.toFloatOrNull() ?: 0f }
            "价格" -> if (priceFilter == "价格最高") {
                list.sortedByDescending { parsePriceValue(it.priceBand) }
            } else {
                list.sortedBy { parsePriceValue(it.priceBand) }
            }
            "推荐" -> if (distanceFilter == "距离最近") list.sortedBy { parseDistanceMeters(it.distance) } else list
            else -> list.sortedBy { parseDistanceMeters(it.distance) }
        }
    }

    if (showPriceRangeDialog) {
        AlertDialog(
            onDismissRequest = { showPriceRangeDialog = false },
            title = { Text("输入价格区间") },
            text = {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = pendingMinPrice,
                        onValueChange = { pendingMinPrice = it.filter(Char::isDigit) },
                        modifier = Modifier.weight(1f),
                        label = { Text("最低") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = pendingMaxPrice,
                        onValueChange = { pendingMaxPrice = it.filter(Char::isDigit) },
                        modifier = Modifier.weight(1f),
                        label = { Text("最高") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        minPrice = pendingMinPrice.toIntOrNull()
                        maxPrice = pendingMaxPrice.toIntOrNull()
                        priceFilter = "输入价格区间"
                        activeFilter = "价格"
                        showPriceRangeDialog = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPriceRangeDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 0.dp, end = 12.dp, bottom = 10.dp)
        ) {
            BookingFilterBar(
                activeFilter = activeFilter,
                distanceFilter = distanceFilter,
                ratingFilter = ratingFilter,
                priceFilter = priceFilter,
                onDistanceFilterChange = {
                    activeFilter = "推荐"
                    distanceFilter = it
                },
                onRatingFilterChange = {
                    activeFilter = "评分"
                    ratingFilter = it
                },
                onPriceFilterChange = {
                    activeFilter = "价格"
                    priceFilter = it
                    if (it != "输入价格区间") {
                        minPrice = null
                        maxPrice = null
                    }
                },
                onPriceRangeClick = {
                    activeFilter = "价格"
                    pendingMinPrice = minPrice?.toString().orEmpty()
                    pendingMaxPrice = maxPrice?.toString().orEmpty()
                    showPriceRangeDialog = true
                }
            )
        }

        if (stores.isEmpty()) {
            EmptyState("暂无可预约门店", "接入真实门店数据后，这里会展示支持预约的门店与时段。")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 18.dp, top = 12.dp, end = 18.dp, bottom = 112.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (filteredStores.isEmpty()) {
                    item { EmptyState("暂无门店", "调整筛选后再试。") }
                } else {
                    items(filteredStores) { store ->
                        StoreCard(store = store, onClick = { onStoreClick(store.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingFilterBar(
    activeFilter: String,
    distanceFilter: String,
    ratingFilter: String?,
    priceFilter: String?,
    onDistanceFilterChange: (String) -> Unit,
    onRatingFilterChange: (String) -> Unit,
    onPriceFilterChange: (String) -> Unit,
    onPriceRangeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BookingFilterMenu(
            text = "推荐",
            selected = activeFilter == "推荐",
            selectedOption = distanceFilter,
            options = listOf("智能推荐", "距离最近", "3km内", "10km内"),
            onOptionClick = onDistanceFilterChange
        )
        BookingFilterMenu(
            text = "评分",
            selected = activeFilter == "评分",
            selectedOption = ratingFilter,
            options = listOf("评分最高", "只看4星"),
            onOptionClick = onRatingFilterChange
        )
        BookingFilterMenu(
            text = "价格",
            selected = activeFilter == "价格",
            selectedOption = priceFilter,
            options = listOf("价格最低", "价格最高", "输入价格区间"),
            onOptionClick = { option ->
                if (option == "输入价格区间") {
                    onPriceRangeClick()
                } else {
                    onPriceFilterChange(option)
                }
            }
        )
    }
}

@Composable
private fun BookingFilterMenu(
    text: String,
    selected: Boolean,
    selectedOption: String?,
    options: List<String>,
    onOptionClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        BookingFilterChip(
            text = text,
            selected = selected,
            expanded = expanded,
            onClick = { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            fontWeight = if (selectedOption == option) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        expanded = false
                        onOptionClick(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun BookingFilterChip(text: String, selected: Boolean, expanded: Boolean, onClick: () -> Unit) {
    val displayText = text.toList().joinToString(" ")
    Surface(
        modifier = Modifier
            .width(76.dp)
            .height(28.dp)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.primary.copy(alpha = if (selected) 0.24f else 0.18f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                displayText,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.width(4.dp))
            Text(
                if (expanded) "▲" else "▼",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun parseDistanceMeters(distance: String): Int {
    val raw = distance.trim()
    val number = raw.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: return Int.MAX_VALUE
    return if (raw.contains("km", ignoreCase = true)) (number * 1000).toInt() else number.toInt()
}

private fun parsePriceValue(price: String): Int {
    val number = Regex("""\d+(\.\d+)?""").find(price)?.value?.toFloatOrNull() ?: return Int.MAX_VALUE
    return (number * 100).toInt()
}

private fun averagePriceText(store: Store): String {
    val existingPrice = Regex("""\d+(\.\d+)?""").find(store.priceBand)?.value?.toFloatOrNull()
    val price = existingPrice?.toInt()
        ?: (88 + (store.id + store.name).fold(0) { acc, char -> acc + char.code }.floorMod(80))
    return "人均¥$price"
}

@Composable
private fun ProfileScreen(
    user: AuthUser,
    favoritesCount: Int,
    tryOnHistoryCount: Int,
    hasMerchantReply: Boolean,
    merchantReplyPreview: String,
    onMessageCenter: () -> Unit,
    onFavorites: () -> Unit,
    onTryOnHistory: () -> Unit,
    onRecords: () -> Unit,
    onReviews: () -> Unit,
    onStyleProfile: () -> Unit,
    onSettings: () -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(74.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = user.name.take(1).ifBlank { "N" },
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(user.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(user.email, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f), fontSize = 14.sp)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                ProfileQuickCard(
                    title = "我的收藏",
                    subtitle = "$favoritesCount 个款式",
                    imageRes = R.drawable.profile_icon_favorites,
                    modifier = Modifier.weight(1f),
                    onClick = onFavorites
                )
                ProfileQuickCard(
                    title = "试戴记录",
                    subtitle = "$tryOnHistoryCount 条记录",
                    imageRes = R.drawable.profile_icon_tryon_history,
                    modifier = Modifier.weight(1f),
                    onClick = onTryOnHistory
                )
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("我的预约", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                        ProfileActionIcon("订单", imageRes = R.drawable.profile_booking_order, onClick = onRecords)
                        ProfileActionIcon("评价", imageRes = R.drawable.profile_booking_review, onClick = onReviews)
                        ProfileActionIcon("售后", imageRes = R.drawable.profile_booking_after_sale) {
                            Toast.makeText(context, "售后功能待接入", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("更多功能", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                ProfileMenuRow(
                    "风格档案",
                    "管理甲型、色系、风格、效果和场景",
                    Icons.Rounded.Person,
                    onClick = onStyleProfile
                )
                ProfileMenuRow(
                    title = "消息中心",
                    subtitle = if (hasMerchantReply) "商家已回复：${merchantReplyPreview.ifBlank { "请查看咨询消息" }}" else "查看通知、预约提醒和系统消息",
                    icon = Icons.Rounded.NotificationsNone,
                    showBadge = hasMerchantReply,
                    onClick = onMessageCenter
                )
                ProfileMenuRow("设置", "通知、隐私与偏好设置", Icons.Rounded.Storefront, onClick = onSettings)
            }
        }
    }
}

private val fallbackStyleProfileTaxonomy = StyleProfileTaxonomyDto(
    nailShapes = listOf("短方", "短圆", "中方", "中椭圆", "中短梯", "长梯", "长椭圆"),
    colorFamilies = listOf(
        "透明系", "裸色系", "粉色系", "红色系", "白色系", "黑色系", "灰银系",
        "金色系", "棕色系", "蓝色系", "绿色系", "紫色系", "橙黄色系"
    ),
    styles = listOf("韩系", "日系", "中式", "欧美", "节庆", "甜美", "酷感", "极繁"),
    effects = listOf("法式", "渐变", "猫眼", "纯色", "手绘", "镜面", "浮雕", "钻饰"),
    scenes = listOf("日常", "通勤", "约会", "旅游", "个性")
)

private fun StyleProfileTaxonomyDto.withFallbacks(): StyleProfileTaxonomyDto = StyleProfileTaxonomyDto(
    nailShapes = nailShapes.ifEmpty { fallbackStyleProfileTaxonomy.nailShapes },
    colorFamilies = colorFamilies.ifEmpty { fallbackStyleProfileTaxonomy.colorFamilies },
    styles = styles.ifEmpty { fallbackStyleProfileTaxonomy.styles },
    effects = effects.ifEmpty { fallbackStyleProfileTaxonomy.effects },
    scenes = scenes.ifEmpty { fallbackStyleProfileTaxonomy.scenes }
)

@Composable
private fun StyleProfileScreen(
    repository: NailMindRepository,
    onboarding: Boolean,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var reloadKey by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var saving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var step by rememberSaveable { mutableStateOf(0) }
    var taxonomy by remember { mutableStateOf(fallbackStyleProfileTaxonomy) }
    var nailShapes by remember { mutableStateOf(emptyList<String>()) }
    var colorFamilies by remember { mutableStateOf(emptyList<String>()) }
    var profileStyles by remember { mutableStateOf(emptyList<String>()) }
    var effects by remember { mutableStateOf(emptyList<String>()) }
    var scenes by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(reloadKey) {
        loading = true
        errorMessage = null
        runCatching { repository.styleProfile() }
            .onSuccess { profile ->
                taxonomy = (profile.taxonomy ?: fallbackStyleProfileTaxonomy).withFallbacks()
                nailShapes = profile.nailShapes
                colorFamilies = profile.colorFamilies
                profileStyles = profile.styles
                effects = profile.effects
                scenes = profile.scenes
            }
            .onFailure { error ->
                errorMessage = error.message ?: "风格档案加载失败"
            }
        loading = false
    }

    val categoryTitles = listOf("甲型", "色系", "风格", "效果", "场景")
    val categoryDescriptions = listOf(
        "选择你常用或想尝试的甲型",
        "选择更衬你的常用色系",
        "选择你偏爱的整体风格",
        "选择喜欢的甲面效果",
        "选择最常用的美甲场景"
    )
    val options = when (step) {
        0 -> taxonomy.nailShapes
        1 -> taxonomy.colorFamilies
        2 -> taxonomy.styles
        3 -> taxonomy.effects
        else -> taxonomy.scenes
    }
    val selected = when (step) {
        0 -> nailShapes
        1 -> colorFamilies
        2 -> profileStyles
        3 -> effects
        else -> scenes
    }

    fun updateCurrent(values: List<String>) {
        when (step) {
            0 -> nailShapes = values
            1 -> colorFamilies = values
            2 -> profileStyles = values
            3 -> effects = values
            else -> scenes = values
        }
    }

    fun toggleOption(option: String) {
        if (option in selected) {
            updateCurrent(selected - option)
            return
        }
        if (selected.size >= 2) {
            Toast.makeText(context, "每类最多选择两个", Toast.LENGTH_SHORT).show()
            return
        }
        updateCurrent(selected + option)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    if (onboarding) "先认识你的美甲偏好" else "更新你的美甲偏好",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "每类最多选择两个，之后也可以随时修改。",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    fontSize = 14.sp
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categoryTitles.forEachIndexed { index, title ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(if (index == step) 34.dp else 28.dp),
                            shape = RoundedCornerShape(50),
                            color = if (index <= step) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (index + 1).toString(),
                                    color = if (index <= step) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            title,
                            fontSize = 11.sp,
                            color = if (index == step) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                            fontWeight = if (index == step) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
        if (loading) {
            item {
                Box(Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else if (errorMessage != null) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(errorMessage.orEmpty(), color = MaterialTheme.colorScheme.onErrorContainer)
                        OutlinedButton(onClick = { reloadKey += 1 }) { Text("重新加载") }
                    }
                }
            }
        } else {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(categoryTitles[step], fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text(
                        categoryDescriptions[step],
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                        fontSize = 14.sp
                    )
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    options.chunked(3).forEach { rowOptions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowOptions.forEach { option ->
                                StyleProfileChoice(
                                    label = option,
                                    selected = option in selected,
                                    modifier = Modifier.weight(1f),
                                    onClick = { toggleOption(option) }
                                )
                            }
                            repeat(3 - rowOptions.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (step > 0) {
                        OutlinedButton(
                            onClick = { step -= 1 },
                            modifier = Modifier.weight(0.8f).height(52.dp),
                            enabled = !saving
                        ) {
                            Text("上一步")
                        }
                    }
                    Button(
                        onClick = {
                            if (step < categoryTitles.lastIndex) {
                                step += 1
                            } else {
                                scope.launch {
                                    saving = true
                                    errorMessage = null
                                    runCatching {
                                        repository.updateStyleProfile(
                                            UpdateStyleProfileRequest(
                                                nailShapes = nailShapes,
                                                colorFamilies = colorFamilies,
                                                styles = profileStyles,
                                                effects = effects,
                                                scenes = scenes
                                            )
                                        )
                                    }.onSuccess {
                                        Toast.makeText(context, "风格档案已保存", Toast.LENGTH_SHORT).show()
                                        onComplete()
                                    }.onFailure { error ->
                                        errorMessage = error.message ?: "保存失败，请稍后再试"
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                    }
                                    saving = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1.4f).height(52.dp),
                        enabled = !saving
                    ) {
                        if (saving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(if (step < categoryTitles.lastIndex) "下一步" else "保存档案")
                        }
                    }
                }
            }
            if (onboarding) {
                item {
                    TextButton(
                        onClick = {
                            scope.launch {
                                saving = true
                                runCatching { repository.skipStyleProfile() }
                                    .onSuccess { onComplete() }
                                    .onFailure { error ->
                                        Toast.makeText(
                                            context,
                                            error.message ?: "暂时无法跳过，请稍后再试",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                saving = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !saving
                    ) {
                        Text("暂时跳过")
                    }
                }
            }
        }
    }
}

@Composable
private fun StyleProfileChoice(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.heightIn(min = 50.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
        else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.24f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                label,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ProfileQuickCard(
    title: String,
    subtitle: String,
    icon: ImageVector? = null,
    imageRes: Int? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(38.dp),
                    contentScale = ContentScale.Fit
                )
            } else if (icon != null) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            }
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun ProfileActionIcon(
    title: String,
    icon: ImageVector? = null,
    imageRes: Int? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        } else if (icon != null) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
        }
        Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ProfileMenuRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    showBadge: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(23.dp))
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f), fontSize = 12.sp)
            }
            if (showBadge) {
                Surface(shape = RoundedCornerShape(999.dp), color = Color(0xFFF25F86)) {
                    Text("新", modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.34f))
        }
    }
}

@Composable
private fun StyleDetailScreen(
    style: NailStyle,
    favorite: Boolean,
    onToggleFavorite: () -> Unit,
    onTryOn: () -> Unit,
    onBook: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                GradientThumb(style = style, modifier = Modifier.fillMaxWidth().aspectRatio(1.08f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (index == 0) 8.dp else 6.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                    if (index == 0) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
                                )
                        )
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(style.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                TagRow(style.tags)
            }
        }
        item {
            DetailTextSection(
                title = "款式介绍",
                body = "温柔法式设计，奶白色打底搭配细腻勾边，通勤显白不挑肤色。简约耐看，日常通勤与约会都能轻松驾驭。"
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("用户评价", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                ReviewCard(
                    author = "小鹿酱",
                    rating = 5,
                    body = "颜色很温柔，显白又百搭，通勤约会都很合适！短甲也能撑起来，越看越喜欢。",
                    style = style
                )
                ReviewCard(
                    author = "甜甜圈",
                    rating = 5,
                    body = "很显手干净，法式边缘做得很细致。做完上手后照明间变精致了。",
                    style = style
                )
            }
        }
    }
}

@Composable
private fun DetailMetricCard(entries: List<Pair<String, String>>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            entries.forEachIndexed { index, (label, value) ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                    Text(
                        text = value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                if (index != entries.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 14.dp)
                            .width(1.dp)
                            .height(42.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailTextSection(title: String, body: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(
                body,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ReviewCard(
    author: String,
    rating: Int,
    body: String,
    style: NailStyle
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GradientThumb(
                style = style,
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(author, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        repeat(rating) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB84D),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Text(
                    body,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SearchScreen(hotKeywords: List<String>, onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("输入关键词") },
            trailingIcon = {
                IconButton(onClick = { onSearch(query.ifBlank { hotKeywords.first() }) }) {
                    Icon(Icons.Rounded.Search, contentDescription = "搜索")
                }
            }
        )
        Text("热门搜索", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
            hotKeywords.forEach {
                AssistChip(
                    onClick = { onSearch(it) },
                    label = { Text(it) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            }
        }
    }
}

@Composable
private fun SearchResultScreen(query: String, result: List<NailStyle>, onStyleClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("“$query” 的结果", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        items(result) { style ->
            HotListItem(style = style, onClick = { onStyleClick(style.id) })
        }
    }
}

internal fun matchesStyleFilterSelections(
    selectedFilters: Set<StyleFilterSelection>,
    matchesOption: (tab: StyleBrowseTab, option: StyleBrowseOption) -> Boolean
): Boolean {
    if (selectedFilters.isEmpty()) return true
    return selectedFilters.groupBy { it.tab }.all { (tab, selections) ->
        selections.any { selection -> matchesOption(tab, selection.option) }
    }
}

private fun NailStyle.matchesStyleFilters(selectedFilters: Set<StyleFilterSelection>): Boolean =
    matchesStyleFilterSelections(selectedFilters) { tab, option ->
        matchesBrowseOption(tab, option)
    }

@Composable
private fun StyleFilterScreen(
    selectedFilters: Set<StyleFilterSelection>,
    onToggleFilter: (StyleFilterSelection) -> Unit,
    onClear: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("选择你想看的标签", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "同一类可多选，不同类别会组合筛选",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
                    )
                }
            }
            items(StyleBrowseTab.entries, key = { it.name }) { tab ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(tab.title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    tab.options().chunked(3).forEach { rowOptions ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowOptions.forEach { option ->
                                val selection = StyleFilterSelection(tab, option)
                                StyleFilterOption(
                                    label = option.label,
                                    selected = selection in selectedFilters,
                                    onClick = { onToggleFilter(selection) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(3 - rowOptions.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onClear, enabled = selectedFilters.isNotEmpty()) {
                    Text("清空")
                }
                Button(
                    onClick = onConfirm,
                    enabled = selectedFilters.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        if (selectedFilters.isEmpty()) "请选择标签" else "查看筛选结果（${selectedFilters.size}）",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun StyleFilterOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                label,
                fontSize = 14.sp,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun StyleFilterResultItem(style: NailStyle, onClick: () -> Unit) {
    val tagSummary = buildList {
        addAll(style.displayTags)
        addAll(style.tags)
        addAll(style.tagGroups.vibes)
        addAll(style.tagGroups.effects)
        addAll(style.tagGroups.nailShapes)
        if (isEmpty()) add(style.vibe)
    }.map { it.trim() }.filter { it.isNotBlank() }.distinct().take(6).joinToString("、")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(22.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GradientThumb(style = style, modifier = Modifier.size(88.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    style.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    tagSummary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f)
            )
        }
    }
}

@Composable
private fun StyleFilterResultScreen(
    selectedFilters: Set<StyleFilterSelection>,
    result: List<NailStyle>,
    onEditFilters: () -> Unit,
    onStyleClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "共 ${result.size} 款",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
                )
                TextButton(onClick = onEditFilters) { Text("修改筛选") }
            }
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    selectedFilters.sortedWith(compareBy({ it.tab.ordinal }, { it.option.label })),
                    key = { "${it.tab.name}:${it.option.label}" }
                ) { selection ->
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            selection.option.label,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
        if (result.isEmpty()) {
            item {
                EmptyState("暂无筛选结果", "试试减少标签，或换一组条件。")
            }
        } else {
            items(result, key = { it.id }) { style ->
                StyleFilterResultItem(style = style, onClick = { onStyleClick(style.id) })
            }
        }
    }
}

@Composable
private fun TryOnUploadScreen(
    style: NailStyle,
    fromFavorites: Boolean,
    loading: Boolean,
    errorMessage: String?,
    lastSourceFile: File?,
    onStartProcessing: (File, String) -> Unit
) {
    val context = LocalContext.current
    var pendingCameraFile by remember { mutableStateOf<File?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val file = pendingCameraFile
        if (saved && file != null && file.exists() && file.length() > 0) {
            onStartProcessing(file, "camera")
        } else if (saved) {
            Toast.makeText(context, "拍照文件保存失败，请重试", Toast.LENGTH_SHORT).show()
        }
        pendingCameraFile = null
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val file = copyUriToCache(context, uri)
            if (file != null) {
                onStartProcessing(file, "gallery")
            } else {
                Toast.makeText(context, "读取图片失败，请重试", Toast.LENGTH_SHORT).show()
            }
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            val file = createCameraImageFile(context)
            pendingCameraFile = file
            cameraLauncher.launch(
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            )
        } else {
            Toast.makeText(context, "未授予相机权限，无法拍照试戴", Toast.LENGTH_SHORT).show()
        }
    }
    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Toast.makeText(context, "未授予相册权限，无法选择图片", Toast.LENGTH_SHORT).show()
        }
    }

    fun openCamera() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val file = createCameraImageFile(context)
            pendingCameraFile = file
            cameraLauncher.launch(
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            )
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun openGallery() {
        val permission = galleryPermission()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            galleryPermissionLauncher.launch(permission)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(style.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        if (fromFavorites) "来自我的收藏，试戴效果会一并保存。" else "当前选择的试戴款式。",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("上传提示", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("请保持手部清晰、手指自然张开、光线充足，并尽量露出完整指甲。", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f))
                    Text("姿势示例", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        TryOnPoseExample(
                            imageRes = R.drawable.tryon_pose_open_hand,
                            label = "自然张开",
                            modifier = Modifier.weight(1f)
                        )
                        TryOnPoseExample(
                            imageRes = R.drawable.tryon_pose_curled_hand,
                            label = "轻握露甲",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (!errorMessage.isNullOrBlank()) {
                        Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = ::openCamera, modifier = Modifier.weight(1f), enabled = !loading) { Text(if (loading) "处理中..." else "拍照") }
                        OutlinedButton(onClick = ::openGallery, modifier = Modifier.weight(1f), enabled = !loading) { Text("从相册上传") }
                    }
                    TextButton(onClick = { lastSourceFile?.let { onStartProcessing(it, "history") } }, enabled = !loading && lastSourceFile != null) { Text("使用上次手部照片") }
                }
            }
        }
    }
}

@Composable
private fun TryOnPoseExample(imageRes: Int, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = label,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f), fontSize = 12.sp)
    }
}

@Composable
private fun TryOnProcessingScreen(
    stage: String,
    progress: Int,
    errorMessage: String?,
    onDone: () -> Unit,
    onLeave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(
                Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                CircularProgressIndicator()
                Text("正在生成你的试戴效果", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("当前进度: ${stageLabel(stage)} · $progress%", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f))
                Text(
                    "你可以先去浏览其他款式，结果生成后会保存在试戴记录里。",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
                errorMessage?.takeIf { it.isNotBlank() }?.let {
                    Text(it, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.error)
                }
                if (progress >= 100 && errorMessage.isNullOrBlank()) {
                    Button(onClick = onDone) { Text("查看试戴结果") }
                } else {
                    OutlinedButton(onClick = onLeave) { Text("先去逛逛，稍后查看") }
                }
            }
        }
    }
}

@Composable
private fun TryOnResultScreen(
    style: NailStyle,
    favorite: Boolean,
    resultBitmap: Bitmap?,
    results: List<TryOnRenderedResult>,
    analysisContext: TryOnAnalysisContext,
    onOpenStyle: () -> Unit,
    onRetake: () -> Unit,
    onToggleFavorite: () -> Unit,
    onBook: () -> Unit,
    onSaveImage: (Bitmap?) -> Unit
) {
    val displayResults = results.ifEmpty {
        resultBitmap?.let { listOf(TryOnRenderedResult("legacy", "试戴结果", it)) }.orEmpty()
    }
    val analysis = buildTryOnAnalysis(
        styleName = style.name,
        nailType = style.nailType,
        skinTone = style.skinTone,
        tags = style.tags + style.displayTags + style.tagGroups.vibes + style.tagGroups.effects + style.tagGroups.scenes,
        context = analysisContext
    )
    val pagerState = rememberPagerState(pageCount = { maxOf(displayResults.size, 1) })
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            if (displayResults.isEmpty()) {
                ResultCanvas(style = style, resultBitmap = null)
            } else {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ResultCanvas(style = style, resultBitmap = displayResults[page].bitmap)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                displayResults[page].label,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "${page + 1} / ${displayResults.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(style.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        if (displayResults.isNotEmpty()) {
            item {
                TryOnAnalysisCard(analysis)
            }
        }
        item {
            OutlinedButton(
                onClick = onOpenStyle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("查看款式详情")
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onRetake, modifier = Modifier.weight(1f)) { Text("重新上传") }
                OutlinedButton(onClick = onToggleFavorite, modifier = Modifier.weight(1f)) { Text(if (favorite) "已收藏" else "收藏款式") }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = onBook, modifier = Modifier.weight(1f)) { Text("预约同款") }
                OutlinedButton(
                    onClick = { onSaveImage(displayResults.getOrNull(pagerState.currentPage)?.bitmap ?: resultBitmap) },
                    modifier = Modifier.weight(1f),
                    enabled = displayResults.isNotEmpty() || resultBitmap != null
                ) { Text("保存图片") }
            }
        }
    }
}

@Composable
private fun TryOnAnalysisCard(analysis: TryOnAnalysis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "试戴适配分析",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            TryOnAnalysisItem("颜色与肤色", analysis.colorHarmony)
            TryOnAnalysisItem("甲型与长度", analysis.shapeAndLength)
            TryOnAnalysisItem("穿搭建议", analysis.outfitAdvice)
        }
    }
}

@Composable
private fun TryOnAnalysisItem(title: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            content,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TryOnHistoryScreen(
    items: List<TryOnHistoryItemDto>,
    styles: List<NailStyle>,
    refreshing: Boolean,
    manageMode: Boolean,
    onRefresh: () -> Unit,
    onShare: (List<TryOnHistoryItemDto>) -> Unit,
    onMoveToFavorites: (List<TryOnHistoryItemDto>) -> Unit,
    onDelete: (List<TryOnHistoryItemDto>) -> Unit,
    onOpenResult: (TryOnHistoryItemDto) -> Unit,
    onTryAgain: (String) -> Unit
) {
    val selectedIds = remember { mutableStateListOf<String>() }
    LaunchedEffect(manageMode, items) {
        if (!manageMode) selectedIds.clear()
        selectedIds.removeAll { id -> items.none { it.id == id } }
    }
    if (items.isEmpty()) {
        EmptyState("还没有试戴记录", "上传一张手部照片后，生成过的效果图都会保存在这里。")
        return
    }
    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, top = 20.dp, end = 20.dp, bottom = if (manageMode) 132.dp else 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(items) { item ->
                    val selected = selectedIds.contains(item.id)
                    TryOnHistoryCard(
                        item = item,
                        manageMode = manageMode,
                        selected = selected,
                        onToggleSelected = {
                            if (selected) selectedIds.remove(item.id) else selectedIds.add(item.id)
                        },
                        onOpenResult = { onOpenResult(item) },
                        onTryAgain = { onTryAgain(item.styleId) }
                    )
                }
            }
            if (manageMode) {
                val selectedItems = items.filter { selectedIds.contains(it.id) }
                TryOnHistoryManageBar(
                    allSelected = selectedIds.size == items.size,
                    selectedCount = selectedItems.size,
                    onToggleAll = {
                        if (selectedIds.size == items.size) {
                            selectedIds.clear()
                        } else {
                            selectedIds.clear()
                            selectedIds.addAll(items.map { it.id })
                        }
                    },
                    onShare = { onShare(selectedItems) },
                    onMoveToFavorites = { onMoveToFavorites(selectedItems) },
                    onDelete = {
                        onDelete(selectedItems)
                        selectedIds.clear()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun TryOnHistoryCard(
    item: TryOnHistoryItemDto,
    manageMode: Boolean,
    selected: Boolean,
    onToggleSelected: () -> Unit,
    onOpenResult: () -> Unit,
    onTryAgain: () -> Unit
) {
    val isPending = item.source == "pending" || item.resultUrl.isBlank()
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = manageMode, onClick = onToggleSelected)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (manageMode) {
                Checkbox(checked = selected, onCheckedChange = { onToggleSelected() })
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        SubcomposeAsyncImage(
                            model = item.resultUrl.takeIf { it.isNotBlank() },
                            contentDescription = item.styleName,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                                else -> Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = if (isPending) "生成中" else "结果待查看",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(item.styleName, fontWeight = FontWeight.SemiBold)
                        Text(
                            if (isPending) "试戴结果生成中" else "最近一次试戴结果",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
                        )
                        Text(
                            formatBeijingDateTime(item.createdAt),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.52f)
                        )
                    }
                }
                if (!manageMode) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onOpenResult,
                            modifier = Modifier.weight(1f),
                            enabled = !isPending
                        ) {
                            Text("查看结果")
                        }
                        Button(onClick = onTryAgain, modifier = Modifier.weight(1f)) {
                            Text("再次试戴")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TryOnHistoryManageBar(
    allSelected: Boolean,
    selectedCount: Int,
    onToggleAll: () -> Unit,
    onShare: () -> Unit,
    onMoveToFavorites: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.clickable(onClick = onToggleAll),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = allSelected, onCheckedChange = { onToggleAll() })
                Text("全选", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f))
            }
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = onShare, enabled = selectedCount > 0) {
                Text("分享")
            }
            OutlinedButton(onClick = onMoveToFavorites, enabled = selectedCount > 0) {
                Text("移入收藏")
            }
            Button(
                onClick = onDelete,
                enabled = selectedCount > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2D55))
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("删除")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreen(
    styles: List<NailStyle>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onStyleClick: (String) -> Unit,
    onRetake: (String) -> Unit,
    onBook: (String) -> Unit
) {
    if (styles.isEmpty()) {
        EmptyState("还没有收藏", "在款式详情页或试戴结果页收藏喜欢的款式。")
        return
    }
    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(styles) { style ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GradientThumb(style = style, modifier = Modifier.size(88.dp))
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(style.name, fontWeight = FontWeight.SemiBold)
                                Text("收藏的款式会保存在这里，方便你再次试戴或预约。", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(onClick = { onStyleClick(style.id) }, modifier = Modifier.weight(1f)) { Text("查看详情") }
                            OutlinedButton(onClick = { onRetake(style.id) }, modifier = Modifier.weight(1f)) { Text("再次试戴") }
                            Button(onClick = { onBook(style.id) }, modifier = Modifier.weight(1f)) { Text("预约") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreDetailScreen(
    store: Store,
    restorationCategories: List<RestorationCategoryDto>,
    onBack: () -> Unit,
    onConsult: () -> Unit,
    onBook: () -> Unit
) {
    val context = LocalContext.current
    val primary = Color(0xFFF25F86)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F6))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 112.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { StoreDetailHero(onBack = onBack, primary = primary) }
            item {
                StoreDetailInfoCard(
                    store = store,
                    primary = primary,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .offset(y = (-42).dp)
                )
            }
            item {
                StoreDetailGallery(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .offset(y = (-42).dp)
                )
            }
            item {
                StoreProjectSection(
                    primary = primary,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .offset(y = (-42).dp)
                )
            }
            if (restorationCategories.isNotEmpty()) {
                item {
                    RestorationPerformanceSection(
                        categories = restorationCategories,
                        modifier = Modifier.padding(horizontal = 12.dp).offset(y = (-42).dp)
                    )
                }
            }
        }
        StoreDetailBottomBar(
            primary = primary,
            onConsult = onConsult,
            onBook = onBook,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun RestorationPerformanceSection(
    categories: List<RestorationCategoryDto>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("综合还原表现", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "来自已完成真实订单的长期评价，不由单张图片直接打分。",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                fontSize = 12.sp
            )
            categories.forEach { category ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(category.styleCategory, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                            Text(
                                category.compositeScore?.let { "${it.toInt()} / 100" } ?: "样本积累中",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (category.status == "published" && category.dimensions != null) {
                            val dimensions = category.dimensions
                            Text(
                                "相似 ${dimensions.similarity.toInt()} · 色彩 ${dimensions.color.toInt()} · 细节 ${dimensions.detail.toInt()} · 满意 ${dimensions.satisfaction.toInt()}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                            )
                            Text("基于 ${category.sampleSize} 个已完成订单", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.46f))
                        } else {
                            Text(
                                "已有 ${category.sampleSize} 个样本，满 ${category.minimumSampleSize} 个后展示综合表现",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreDetailHero(onBack: () -> Unit, primary: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.store_detail_hero),
            contentDescription = "门店环境",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp).clickable(onClick = onBack),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回", tint = Color(0xFF222222), modifier = Modifier.size(23.dp))
                }
            }
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.FavoriteBorder, contentDescription = "收藏", tint = primary, modifier = Modifier.size(25.dp))
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 8.dp else 7.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = if (index == 0) 1f else 0.62f))
                )
            }
        }
    }
}

@Composable
private fun StoreDetailInfoCard(store: Store, primary: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painter = painterResource(R.drawable.store_detail_logo),
                    contentDescription = "门店 Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .border(1.dp, primary.copy(alpha = 0.35f), RoundedCornerShape(25.dp))
                )
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        store.name.ifBlank { "Luna Nail 美甲工作室" },
                        color = Color(0xFF222222),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    StoreDetailMetaLine(
                        listOf("评分 ${store.score}", averagePriceText(store).replace("人均", "人均 "), "距你 ${store.distance}"),
                        color = Color(0xFF666666)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(store.statusText, color = primary, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
                        StoreDetailDivider()
                        Text(store.openHours, color = Color(0xFF666666), fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFEEEEEE))
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = primary, modifier = Modifier.size(18.dp))
                Text(
                    "地址： ${store.address.ifBlank { store.area.ifBlank { "静安区恩园路88号3楼305室" } }}",
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF222222),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color(0xFF666666), modifier = Modifier.size(17.dp))
            }
        }
    }
}
@Composable
private fun StoreDetailMetaLine(items: List<String>, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        items.forEachIndexed { index, item ->
            Text(item, color = color, fontSize = 11.sp, lineHeight = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (index < items.lastIndex) StoreDetailDivider()
        }
    }
}

@Composable
private fun StoreDetailDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(14.dp)
            .background(Color(0xFF666666).copy(alpha = 0.42f))
    )
}

@Composable
private fun StoreDetailGallery(modifier: Modifier = Modifier) {
    StoreDetailSectionCard(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("图片展示", color = Color(0xFF222222), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.weight(1f))
            Text("查看更多", color = Color(0xFF666666), fontSize = 12.sp)
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color(0xFF666666), modifier = Modifier.size(16.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            StoreGalleryImage(R.drawable.store_gallery_environment_1, Modifier.weight(1f))
            StoreGalleryImage(R.drawable.store_gallery_nail_work, Modifier.weight(1f))
            StoreGalleryImage(R.drawable.store_gallery_environment_2, Modifier.weight(1f))
        }
    }
}

@Composable
private fun StoreGalleryImage(imageRes: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(9.dp))
    )
}

private data class StoreProjectItem(
    val name: String,
    val subtitle: String,
    val price: String,
    val duration: String,
    val imageRes: Int,
    val hot: Boolean = false
)

private val storeProjectItems = listOf(
    StoreProjectItem("纯色本甲", "可选多色  |  显白百搭", "¥128", "60分钟", R.drawable.diy_nail_short_square),
    StoreProjectItem("本甲（颜色+款式+钻饰）", "款式丰富  |  精致闪耀", "¥228", "120分钟", R.drawable.diy_nail_medium_oval, hot = true),
    StoreProjectItem("法式本甲", "经典优雅  |  气质百搭", "¥158", "90分钟", R.drawable.diy_nail_short_round),
    StoreProjectItem("猫眼美甲", "磁吸效果  |  高级显白", "¥198", "90分钟", R.drawable.diy_nail_long_oval, hot = true)
)

@Composable
private fun StoreProjectSection(primary: Color, modifier: Modifier = Modifier) {
    StoreDetailSectionCard(modifier = modifier) {
        Text("项目", color = Color(0xFF222222), fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            storeProjectItems.forEach { item ->
                StoreProjectRow(item = item, primary = primary)
            }
        }
    }
}

@Composable
private fun StoreProjectRow(item: StoreProjectItem, primary: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFFF6F8)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(item.imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp)
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.name, color = Color(0xFF222222), fontSize = 15.sp, lineHeight = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.subtitle, modifier = Modifier.weight(1f, fill = false), color = Color(0xFF666666), fontSize = 12.sp, lineHeight = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    if (item.hot) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, primary.copy(alpha = 0.45f))) {
                            Text("热门推荐", modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp), color = primary, fontSize = 10.sp, lineHeight = 12.sp)
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(item.price, color = primary, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color(0xFF666666), modifier = Modifier.size(18.dp))
            }
        }
    }
}
@Composable
private fun StoreDetailSectionCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp), content = content)
    }
}@Composable
private fun StoreDetailBottomBar(primary: Color, onConsult: () -> Unit, onBook: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onConsult,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEED0D7)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF222222))
            ) {
                Icon(Icons.Rounded.ChatBubbleOutline, contentDescription = null, tint = primary, modifier = Modifier.size(21.dp))
                Spacer(Modifier.width(7.dp))
                Text("咨询", color = Color(0xFF222222), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onBook,
                modifier = Modifier
                    .weight(1.45f)
                    .height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Icon(Icons.Rounded.CalendarMonth, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Text("预约", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
private fun chatTimeLabel(value: String): String {
    if (value.isBlank()) return "刚刚"
    return formatBeijingDateTime(value)
}

@Composable
private fun StoreChatScreen(
    storeName: String,
    messages: List<ChatMessageDto>,
    loading: Boolean,
    sending: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onSend: (String) -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }
    val primary = Color(0xFFF25F86)
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F6))
            .imePadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "返回", tint = Color(0xFF222222))
            }
            Column(Modifier.weight(1f)) {
                Text(storeName.ifBlank { "门店咨询" }, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF222222), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("和商家实时沟通预约、款式和到店信息", fontSize = 12.sp, color = Color(0xFF776B70), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            TextButton(onClick = onRefresh, enabled = !loading) { Text("刷新") }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (loading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primary, modifier = Modifier.size(24.dp))
                    }
                }
            }
            if (!errorMessage.isNullOrBlank()) {
                item {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFFEEF2)) {
                        Text(errorMessage, modifier = Modifier.padding(12.dp), color = primary, fontSize = 13.sp)
                    }
                }
            }
            if (!loading && messages.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.ChatBubbleOutline, contentDescription = null, tint = primary, modifier = Modifier.size(34.dp))
                        Text("还没有消息", fontWeight = FontWeight.SemiBold, color = Color(0xFF222222))
                        Text("可以直接向商家咨询款式、档期和到店细节", fontSize = 13.sp, color = Color(0xFF776B70), textAlign = TextAlign.Center)
                    }
                }
            }
            items(messages, key = { it.id }) { message ->
                val isMine = message.senderRole == "customer"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
                ) {
                    Column(
                        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.widthIn(max = 290.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isMine) primary else Color.White,
                            shadowElevation = 1.dp
                        ) {
                            Text(
                                message.body,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                color = if (isMine) Color.White else Color(0xFF222222),
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                        Text(if (isMine) "我 · ${chatTimeLabel(message.createdAt)}" else "商家 · ${chatTimeLabel(message.createdAt)}", fontSize = 11.sp, color = Color(0xFF8D8086))
                    }
                }
            }
        }

        Surface(color = Color.White, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("输入要咨询的内容") },
                    minLines = 1,
                    maxLines = 4,
                    enabled = !sending
                )
                Button(
                    onClick = {
                        val body = input.trim()
                        if (body.isNotEmpty()) {
                            input = ""
                            onSend(body)
                        }
                    },
                    enabled = input.isNotBlank() && !sending,
                    colors = ButtonDefaults.buttonColors(containerColor = primary)
                ) {
                    Text(if (sending) "发送中" else "发送", color = Color.White)
                }
            }
        }
    }
}
@Composable
private fun BookingFormScreen(
    initialStyle: NailStyle,
    styleOptions: List<NailStyle>,
    initialStoreId: String,
    storeOptions: List<Store>,
    fixedStore: Boolean,
    submitting: Boolean,
    errorMessage: String?,
    onSubmit: (String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var selectedStoreId by remember(initialStoreId, storeOptions) {
        mutableStateOf(initialStoreId.takeIf { id -> storeOptions.any { it.id == id } } ?: storeOptions.firstOrNull()?.id.orEmpty())
    }
    var selectedStyleId by remember(initialStyle.id, styleOptions) { mutableStateOf(initialStyle.id) }
    val selectedStyle = styleOptions.firstOrNull { it.id == selectedStyleId } ?: initialStyle
    val rankedStoreOptions = remember(storeOptions) { rankBookingStores(storeOptions) }
    val selectedStore = rankedStoreOptions.firstOrNull { it.id == selectedStoreId }
    val slotOptions = remember(selectedStoreId, selectedStore?.slots) {
        (selectedStore?.slots ?: emptyList())
            .filter { it.isNotBlank() }
            .map { it.removePrefix("最早 ").removeSuffix(" 可约") }
            .distinct()
    }
    var selectedSlot by remember(selectedStoreId, slotOptions) { mutableStateOf(slotOptions.firstOrNull().orEmpty()) }
    val canSubmit = selectedStore != null && selectedSlot.isNotBlank() && !submitting

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (fixedStore) {
            item {
                CompactValue("预约门店", selectedStore?.name ?: "门店信息加载中")
            }
            item {
                Text("选择款式", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(styleOptions, key = { it.id }) { style ->
                        BookingStyleOptionCard(
                            style = style,
                            selected = style.id == selectedStyleId,
                            onClick = { selectedStyleId = style.id }
                        )
                    }
                }
            }
        } else {
            item { CompactValue("预约款式", selectedStyle.name) }
            item {
                Text("选择门店", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(Modifier.height(10.dp))
                if (rankedStoreOptions.isEmpty()) {
                    EmptyState("暂无可预约门店", "请稍后下拉刷新门店数据。")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(rankedStoreOptions, key = { it.id }) { store ->
                            BookingStoreOptionCard(
                                store = store,
                                selected = store.id == selectedStoreId,
                                onClick = { selectedStoreId = store.id }
                            )
                        }
                    }
                }
            }
        }
        if (selectedStore != null) {
            item {
                Text("选择到店时间", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(slotOptions) { slot ->
                        val selected = slot == selectedSlot
                        Surface(
                            modifier = Modifier.clickable { selectedSlot = slot },
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                        ) {
                            Text(
                                slot,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
        item {
            OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(), label = { Text("联系人姓名") })
        }
        item {
            OutlinedTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth(), label = { Text("手机号") })
        }
        item {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 120.dp),
                label = { Text("备注") }
            )
        }
        if (!errorMessage.isNullOrBlank()) {
            item {
                Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }
        }
        item {
            Button(
                onClick = {
                    selectedStore?.let { store -> onSubmit(store.id, selectedStyle.id, name, phone, note, selectedSlot) }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canSubmit
            ) { Text(if (submitting) "提交中..." else "提交预约") }
        }
    }
}

@Composable
private fun BookingStoreOptionCard(store: Store, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(272.dp).height(138.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (selected) RoseTint else MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) RoseAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(Modifier.fillMaxSize().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            StoreCover(store = store, modifier = Modifier.size(76.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(store.name, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    if (selected) Icon(Icons.Rounded.Star, contentDescription = null, tint = RoseAccent, modifier = Modifier.size(17.dp))
                }
                Text(
                    if (store.recommendation.isNewStore && store.reviewCount == 0) "${store.distance}  ·  暂无评价" else "${store.distance}  ·  ${store.score} 分",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                    fontSize = 12.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    StoreMetaPill(text = if (store.recommendation.newStoreProtection) "新店保护" else "智能推荐", highlight = true)
                    if (store.recommendation.isNewStore && !store.recommendation.newStoreProtection) StoreMetaPill(text = "新店")
                }
                Text(
                    store.recommendation.reason.ifBlank { "综合表现与你的需求匹配" },
                    color = if (store.recommendation.newStoreProtection) RoseAccent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BookingStyleOptionCard(style: NailStyle, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(178.dp).height(92.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (selected) RoseTint else MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) RoseAccent else MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GradientThumb(
                style = style,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)),
                showBorder = false
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(style.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(style.tags.firstOrNull() ?: style.vibe, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
@Composable
private fun BookingConfirmScreen(booking: BookingDto, loading: Boolean, errorMessage: String?, onConfirm: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionHeader("确认订单", "请确认到店时间与门店信息") }
        item {
            InfoGrid(
                listOf(
                    "款式" to booking.styleName,
                    "门店" to booking.storeName,
                    "时间" to booking.slot
                )
            )
        }
        if (!errorMessage.isNullOrBlank()) {
            item {
                Text(errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }
        }
        item {
            Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth(), enabled = !loading) { Text(if (loading) "确认中..." else "确认预约") }
        }
    }
}

@Composable
private fun BookingSuccessScreen(booking: BookingDto, onRecords: () -> Unit, onBackHome: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(
                Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("预约成功", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("到店时间 ${booking.slot}\n${booking.storeName}", textAlign = TextAlign.Center)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onRecords) { Text("查看预约记录") }
                    Button(onClick = onBackHome) { Text("返回首页") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingRecordsScreen(
    records: List<BookingRecord>,
    refreshing: Boolean,
    onRefresh: () -> Unit
) {
    if (records.isEmpty()) {
        EmptyState("暂无预约记录", "完成预约后，这里会展示到店时间、门店和状态。")
        return
    }
    PullToRefreshBox(isRefreshing = refreshing, onRefresh = onRefresh, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(records) { record ->
                CompactActionCard(
                    title = "${record.status} | ${record.slot}",
                    subtitle = "${record.storeName} · ${record.styleName}",
                    primary = "查看门店",
                    secondary = "再次预约",
                    onPrimary = {},
                    onSecondary = {}
                )
            }
        }
    }
}

@Composable
private fun ReviewsScreen(
    records: List<BookingRecord>,
    onSubmit: suspend (String, BookingReviewDraft) -> Unit
) {
    var selectedTab by remember { mutableStateOf("待评价") }
    var editingItem by remember { mutableStateOf<ReviewItem?>(null) }
    val pendingReviews = remember(records) { buildPendingReviewItems(records) }
    val finishedReviews = remember(records) { buildFinishedReviewItems(records) }
    val currentItems = if (selectedTab == "待评价") pendingReviews else finishedReviews

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 112.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            ReviewTabBar(
                selected = selectedTab,
                pendingCount = pendingReviews.size,
                finishedCount = finishedReviews.size,
                onSelect = { selectedTab = it }
            )
        }
        if (currentItems.isEmpty()) {
            item {
                EmptyState(
                    title = if (selectedTab == "待评价") "暂无待评价订单" else "暂无已评价内容",
                    subtitle = if (selectedTab == "待评价") "完成到店后，这里会出现可评价的美甲订单。" else "评价完成后，会沉淀在这里方便回看。"
                )
            }
        } else {
            items(currentItems) { item ->
                UserReviewCard(
                    item = item,
                    pending = selectedTab == "待评价",
                    onReview = { editingItem = item }
                )
            }
        }
    }
    editingItem?.let { item ->
        BookingReviewDialog(
            item = item,
            onDismiss = { editingItem = null },
            onSubmit = onSubmit
        )
    }
}

@Composable
private fun ReviewTabBar(selected: String, pendingCount: Int, finishedCount: Int, onSelect: (String) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.background,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f))
    ) {
        Row(Modifier.padding(3.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ReviewTab(
                title = "待评价",
                count = pendingCount,
                selected = selected == "待评价",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("待评价") }
            )
            ReviewTab(
                title = "已评价",
                count = finishedCount,
                selected = selected == "已评价",
                modifier = Modifier.weight(1f),
                onClick = { onSelect("已评价") }
            )
        }
    }
}

@Composable
private fun ReviewTab(title: String, count: Int, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .clickable(onClick = onClick),
        color = if (selected) Color.White else MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                fontSize = 14.sp,
                lineHeight = 16.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1
            )
            Spacer(Modifier.width(5.dp))
            Text(
                count.toString(),
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.72f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.42f),
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun UserReviewCard(item: ReviewItem, pending: Boolean, onReview: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f))
    ) {
        Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GradientThumb(
                    style = NailStyle(
                        id = item.id,
                        name = item.styleName,
                        vibe = item.storeName,
                        price = "",
                        nailType = "",
                        skinTone = "",
                        colors = listOf(RoseAccent.copy(alpha = 0.82f), Color(0xFFF7D8E4)),
                        tags = emptyList(),
                        imageUrl = item.styleImageUrl
                    ),
                    modifier = Modifier.size(58.dp)
                )
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.storeName, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(item.styleName, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), fontSize = 12.sp, lineHeight = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(item.slot, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.46f), fontSize = 12.sp, lineHeight = 15.sp, maxLines = 1)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (pending) RoseTint else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        if (pending) "待评" else "已评",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
            if (pending) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.28f), modifier = Modifier.size(17.dp))
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = onReview,
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                    ) {
                        Text("去评价", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = null,
                                tint = if (index < (item.rating ?: 5)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(item.comment.ifBlank { "已完成真实订单评价" }, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), fontSize = 12.sp, lineHeight = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Text(
                        "AI评测：相似 ${item.similarityScore}/5 · 色彩 ${item.colorScore}/5 · 细节 ${item.detailScore}/5",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 11.sp
                    )
                    if (item.evaluationNote.isNotBlank()) {
                        Text(item.evaluationNote, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.46f), maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@Composable
private fun BookingReviewDialog(
    item: ReviewItem,
    onDismiss: () -> Unit,
    onSubmit: suspend (String, BookingReviewDraft) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var satisfaction by remember { mutableStateOf(5) }
    var submitting by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var actualWorkUri by remember { mutableStateOf<Uri?>(null) }
    var actualWorkFile by remember { mutableStateOf<File?>(null) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            actualWorkUri = uri
            actualWorkFile = copyUriToCache(context, uri)
            if (actualWorkFile == null) error = "读取作品照片失败，请重新选择"
        }
    }

    AlertDialog(
        onDismissRequest = { if (!submitting) onDismiss() },
        title = { Text("评价实际效果") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("${item.storeName} · ${item.styleName}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f), fontSize = 13.sp)
                ReviewEvidencePreview("实际效果", actualWorkUri, Modifier.fillMaxWidth())
                OutlinedButton(
                    onClick = { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (actualWorkFile == null) "上传实际效果照片" else "重新选择实际效果照片") }
                ReviewScoreRow("整体满意度", satisfaction) { satisfaction = it }
                Text(
                    BOOKING_REVIEW_REWARD_COPY,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
            }
        },
        confirmButton = {
            Button(
                enabled = !submitting && actualWorkFile != null,
                onClick = {
                    scope.launch {
                        submitting = true
                        error = null
                        val evidenceFile = actualWorkFile ?: return@launch
                        val draft = BookingReviewDraft(satisfaction, evidenceFile)
                        runCatching { onSubmit(item.id, draft) }
                            .onSuccess { onDismiss() }
                            .onFailure { error = it.message ?: "提交评价失败" }
                        submitting = false
                    }
                }
            ) { Text(if (submitting) "提交中..." else "提交评价") }
        },
        dismissButton = { TextButton(enabled = !submitting, onClick = onDismiss) { Text("取消") } }
    )
}

@Composable
private fun ReviewEvidencePreview(label: String, model: Any?, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
        Surface(
            modifier = Modifier.fillMaxWidth().height(132.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            if (model == null) {
                Box(contentAlignment = Alignment.Center) { Text("待上传", fontSize = 12.sp) }
            } else {
                SubcomposeAsyncImage(model = model, contentDescription = label, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()) {
                    val state = painter.state
                    if (state is AsyncImagePainter.State.Success) SubcomposeAsyncImageContent()
                    else Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(Modifier.size(20.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ReviewScoreRow(label: String, score: Int, onScoreChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f), fontSize = 13.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            (1..5).forEach { value ->
                Icon(
                    Icons.Rounded.Star,
                    contentDescription = "$label $value 分",
                    tint = if (value <= score) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                    modifier = Modifier.size(24.dp).clickable { onScoreChange(value) }
                )
            }
        }
    }
}

private fun buildPendingReviewItems(records: List<BookingRecord>): List<ReviewItem> {
    return records.filter { it.canReview }.map { record ->
        ReviewItem(
            id = record.id,
            storeName = record.storeName,
            styleName = record.styleName,
            styleImageUrl = record.styleImageUrl,
            slot = record.slot
        )
    }
}

private fun buildFinishedReviewItems(records: List<BookingRecord>): List<ReviewItem> {
    return records.mapNotNull { record ->
        val review = record.review ?: return@mapNotNull null
        ReviewItem(
            id = record.id,
            storeName = record.storeName,
            styleName = record.styleName,
            styleImageUrl = record.styleImageUrl,
            slot = record.slot,
            rating = review.satisfactionScore,
            comment = review.comment,
            similarityScore = review.similarityScore,
            colorScore = review.colorScore,
            detailScore = review.detailScore,
            evaluationNote = review.evaluationNote
        )
    }
}
@Composable
private fun SettingsScreen(settings: UserSettings, onLogout: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { CompactValue("风格偏好", settings.stylePreferences) }
        item { CompactValue("消息通知", settings.notifications) }
        item { CompactValue("隐私设置", settings.privacy) }
        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("退出登录", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}

@Composable
private fun AuthScreen(
    title: String,
    subtitle: String,
    primaryLabel: String,
    secondaryLabel: String,
    initialName: String,
    initialEmail: String,
    initialPassword: String,
    showNameField: Boolean,
    loading: Boolean,
    errorMessage: String?,
    onPrimary: (String, String, String) -> Unit,
    onSecondary: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var password by remember { mutableStateOf(initialPassword) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
                if (showNameField) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("昵称") }
                    )
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("邮箱") }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("密码") }
                )
                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
                Button(
                    onClick = { onPrimary(name, email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                ) {
                    Text(if (loading) "处理中..." else primaryLabel)
                }
                TextButton(onClick = onSecondary, modifier = Modifier.align(Alignment.End), enabled = !loading) {
                    Text(secondaryLabel)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(subtitle, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.68f))
    }
}

@Composable
private fun StyleCard(style: NailStyle, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GradientThumb(style = style, modifier = Modifier.fillMaxWidth().aspectRatio(0.88f))
            Text(style.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(style.vibe, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp, maxLines = 2)
        }
    }
}

@Composable
private fun HomeStyleCard(style: NailStyle, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            GradientThumb(style = style, modifier = Modifier.fillMaxWidth().aspectRatio(1.02f))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(style.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(style.vibe, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp, maxLines = 2)
            }
            Text(label, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PrototypeHomeRow(style: NailStyle, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradientThumb(style = style, modifier = Modifier.size(72.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(style.name, fontWeight = FontWeight.SemiBold)
            Text(
                "款式详情",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
        }
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f))
    }
}

@Composable
private fun StyleGridRow(style: NailStyle, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GradientThumb(style = style, modifier = Modifier.size(92.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(style.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(style.vibe, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), maxLines = 2)
            TagRow(style.tags)
        }
    }
}

@Composable
private fun HotListItem(style: NailStyle, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradientThumb(style = style, modifier = Modifier.size(76.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(style.name, fontWeight = FontWeight.SemiBold)
            Text(style.vibe, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
    }
}

@Composable
private fun HomeHotItem(style: NailStyle, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradientThumb(style = style, modifier = Modifier.size(72.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(style.name, fontWeight = FontWeight.SemiBold)
            Text(
                style.tags.joinToString(" · "),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                maxLines = 1
            )
            Text(
                "点击查看款式详情",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
    }
}

@Composable
private fun PrototypeHotRow(style: NailStyle, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GradientThumb(style = style, modifier = Modifier.size(72.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(style.name, fontWeight = FontWeight.SemiBold)
            Text(
                "款式详情",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
        }
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.28f))
    }
}

@Composable
private fun StoreCard(store: Store, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StoreCover(
                store = store,
                modifier = Modifier
                    .width(106.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        store.name,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                    if (!(store.recommendation.isNewStore && store.reviewCount == 0)) {
                        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(Icons.Rounded.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(13.dp))
                                Text(store.score, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    StoreMetaPill(text = if (store.recommendation.isNewStore) "新店" else store.statusText, highlight = true)
                    StoreMetaPill(text = store.distance)
                    StoreMetaPill(text = averagePriceText(store))
                }

                Text(
                    store.recommendation.reason.ifBlank { store.address.ifBlank { store.area } },
                    modifier = Modifier.fillMaxWidth(),
                    color = if (store.recommendation.newStoreProtection) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = RoseTint) {
                    Text(
                        if (store.nearestSlot.isBlank()) "最近可约时间待确认" else "最近可约 ${store.nearestSlot}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 10.sp,
                        lineHeight = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreMetaPill(text: String, highlight: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (highlight) Color(0xFFEFF7EF) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
            color = if (highlight) Color(0xFF3E7A48) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
            fontSize = 11.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}
@Composable
private fun StoreCover(
    store: Store,
    modifier: Modifier = Modifier.size(92.dp)
) {
    val avatarRes = store.avatarRes
    if (avatarRes != null) {
        Image(
            painter = painterResource(avatarRes),
            contentDescription = null,
            modifier = modifier.clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        return
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        store.coverTone.copy(alpha = 0.92f),
                        store.coverTone.copy(alpha = 0.56f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.42f)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.48f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(5) { index ->
                val x = size.width * (0.18f + index * 0.15f)
                drawLine(
                    color = Color.White.copy(alpha = 0.14f + index * 0.025f),
                    start = Offset(x, size.height * 0.18f),
                    end = Offset(x + size.width * 0.08f, size.height * 0.82f),
                    strokeWidth = size.minDimension / 12f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
@Composable
private fun TagRow(tags: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
        tags.forEach { tag ->
            Surface(
                color = RoseTint,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    tag,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = RoseAccent,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun GradientThumb(style: NailStyle, modifier: Modifier = Modifier, showBorder: Boolean = true) {
    val thumbModifier = if (showBorder) {
        modifier
            .clip(MaterialTheme.shapes.medium)
            .background(Brush.linearGradient(style.colors))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f), MaterialTheme.shapes.medium)
    } else {
        modifier
            .background(Brush.linearGradient(style.colors))
    }

    Box(
        modifier = thumbModifier
    ) {
        val imageUrl = style.imageUrl
        if (!imageUrl.isNullOrBlank()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = style.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                    else -> Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = size.minDimension / 7f
                        repeat(5) { index ->
                            val step = size.width / 6
                            drawLine(
                                color = Color.White.copy(alpha = 0.24f + index * 0.05f),
                                start = Offset(step * (index + 1), size.height * 0.18f),
                                end = Offset(step * (index + 1), size.height * 0.82f),
                                strokeWidth = stroke,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = size.minDimension / 7f
                repeat(5) { index ->
                    val step = size.width / 6
                    drawLine(
                        color = Color.White.copy(alpha = 0.24f + index * 0.05f),
                        start = Offset(step * (index + 1), size.height * 0.18f),
                        end = Offset(step * (index + 1), size.height * 0.82f),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultCanvas(style: NailStyle, resultBitmap: Bitmap?) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.88f)
                .padding(18.dp)
        ) {
            if (resultBitmap != null) {
                Image(
                    bitmap = resultBitmap.asImageBitmap(),
                    contentDescription = "AI试戴结果",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.large)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "试戴图生成后会显示在这里",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(
                if (resultBitmap != null) "AI 试戴结果" else "等待试戴结果",
                modifier = Modifier.align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
            )
        }
    }
}

@Composable
private fun InfoGrid(entries: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        entries.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { (label, value) ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), fontSize = 12.sp)
                            Text(value, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun CompactActionCard(
    title: String,
    subtitle: String,
    primary: String,
    secondary: String?,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit = {}
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = onPrimary, modifier = Modifier.weight(1f)) { Text(primary) }
                if (secondary != null) {
                    OutlinedButton(onClick = onSecondary, modifier = Modifier.weight(1f)) { Text(secondary) }
                }
            }
        }
    }
}

@Composable
private fun ProfileEntry(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f), fontSize = 13.sp)
        }
        Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
    }
}

@Composable
private fun CompactValue(label: String, value: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(value, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun EmptyState(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(
                Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Rounded.BookmarkBorder, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(subtitle, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
            }
        }
    }
}

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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.nailmind.app.R
import com.nailmind.app.data.api.AuthResponse
import com.nailmind.app.data.api.AuthUserDto
import com.nailmind.app.data.api.BookingDto
import com.nailmind.app.data.api.HomeResponse
import com.nailmind.app.data.api.MeimeiChatResponse
import com.nailmind.app.data.api.MeimeiRecommendationDto
import com.nailmind.app.data.api.NailMindApiClient
import com.nailmind.app.data.api.NailMindRepository
import com.nailmind.app.data.api.SettingsResponse
import com.nailmind.app.data.api.StoreDto
import com.nailmind.app.data.api.StyleTagGroupsDto
import com.nailmind.app.data.api.StyleDto
import com.nailmind.app.data.api.TryOnHistoryItemDto
import com.nailmind.app.data.config.AppConfig
import com.nailmind.app.ui.theme.RoseAccent
import com.nailmind.app.ui.theme.RoseTint
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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
    data object Ranking : Screen
    data class TryOnUpload(val styleId: String, val fromFavorites: Boolean = false) : Screen
    data class TryOnProcessing(val styleId: String, val jobId: String) : Screen
    data class TryOnResult(val styleId: String, val jobId: String) : Screen
    data object TryOnHistory : Screen
    data object DiyDesigner : Screen
    data object Xiaomei : Screen
    data object Favorites : Screen
    data object BookingRecords : Screen
    data object Reviews : Screen
    data class StoreDetail(val storeId: String, val styleId: String? = null) : Screen
    data class BookingForm(val storeId: String, val styleId: String) : Screen
    data class BookingConfirm(val bookingId: String) : Screen
    data class BookingSuccess(val bookingId: String) : Screen
    data object Settings : Screen
}

private data class AuthUser(
    val name: String,
    val email: String,
    val preferences: List<String> = emptyList()
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
    val tagGroups: StyleTagGroupsDto = StyleTagGroupsDto(),
    val imageUrl: String? = null,
    val tryOnStyleId: Int? = null
)

private enum class StyleBrowseTab(val title: String) {
    NailShape("甲型"),
    Effect("效果"),
    Vibe("风格")
}

private data class StyleBrowseOption(
    val label: String,
    val keywords: List<String>
)

private val nailShapeBrowseOptions = listOf(
    StyleBrowseOption("短方", listOf("短方")),
    StyleBrowseOption("短圆", listOf("短圆")),
    StyleBrowseOption("中方", listOf("中方")),
    StyleBrowseOption("中椭圆", listOf("中椭圆")),
    StyleBrowseOption("中短梯", listOf("中短梯")),
    StyleBrowseOption("长梯", listOf("长梯")),
    StyleBrowseOption("长椭圆", listOf("长椭圆")),
    StyleBrowseOption("尖水滴", listOf("尖水滴"))
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
    StyleBrowseOption("日常", listOf("日常")),
    StyleBrowseOption("酷感", listOf("酷感")),
    StyleBrowseOption("极繁", listOf("极繁"))
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
    val coverTone: Color = Color(0xFFEAD8D1),
    val reviewCount: Int = 0,
    val statusText: String = "营业中",
    val address: String = "",
    val area: String = "",
    val tags: List<String> = emptyList(),
    val nearestSlot: String = slots.firstOrNull().orEmpty(),
    val matchScore: Int = 88,
    val couponText: String = "新客预约立减",
    val salesText: String = ""
)

private data class BookingRecord(
    val id: String,
    val status: String,
    val storeName: String,
    val styleName: String,
    val slot: String
)

private data class ReviewItem(
    val id: String,
    val storeName: String,
    val styleName: String,
    val slot: String,
    val rating: Int? = null,
    val comment: String = ""
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
    val errorMessage: String? = null
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
    tagGroups = tagGroups ?: StyleTagGroupsDto(),
    imageUrl = imageUrl,
    tryOnStyleId = tryOnStyleId
)

private fun StoreDto.toUi(): Store = Store(
    id = id,
    name = name,
    distance = distance,
    priceBand = priceBand,
    score = score,
    slots = slots,
    openHours = openHours,
    artists = artists,
    works = works,
    reviewCount = works.filter { it.isDigit() }.toIntOrNull() ?: 0,
    area = name.substringAfter("（", "").substringBefore("）", "").ifBlank { "附近商圈" },
    address = "门店地址请以到店前确认为准",
    tags = listOf("支持预约", "试戴同款", "到店确认"),
    nearestSlot = slots.firstOrNull()?.let { "$it 可约" } ?: "今日可约",
    matchScore = 88,
    salesText = works
)

private fun AuthUserDto.toUi(): AuthUser = AuthUser(name = name, email = email, preferences = preferences)

private fun BookingDto.toUi(): BookingRecord = BookingRecord(
    id = id,
    status = status,
    storeName = storeName,
    styleName = styleName,
    slot = slot
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
    return merged.sortedByDescending { it.createdAt }.distinctBy { it.styleId }
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
    var styleBrowseTabName by rememberSaveable { mutableStateOf(StyleBrowseTab.NailShape.name) }
    var styleBrowseOptionLabel by rememberSaveable { mutableStateOf(nailShapeBrowseOptions.first().label) }
    var styleBrowseSortMode by rememberSaveable { mutableStateOf("热度高") }
    var styleBrowseQuery by rememberSaveable { mutableStateOf("") }
    var homeRecommended by remember { mutableStateOf(styles.take(2)) }
    var homeHot by remember { mutableStateOf(styles) }
    var hotKeywords by remember { mutableStateOf(defaultHotKeywords) }
    var storeItems by remember { mutableStateOf(stores) }
    var bookingRecords by remember { mutableStateOf(emptyList<BookingRecord>()) }
    var pendingBooking by remember { mutableStateOf<BookingDto?>(null) }
    var userSettings by remember { mutableStateOf(UserSettings("", "", "")) }
    var searchResults by remember { mutableStateOf(styleItems) }
    var tryOnStatus by remember { mutableStateOf(TryOnStatus()) }
    var tryOnHistoryItems by remember { mutableStateOf(emptyList<TryOnHistoryItemDto>()) }
    var tryOnHistoryManageMode by remember { mutableStateOf(false) }
    var latestTryOnBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var lastTryOnSourceFile by remember { mutableStateOf<File?>(null) }
    var lastTryOnHandId by remember { mutableStateOf<String?>(null) }
    var bookingSubmitting by remember { mutableStateOf(false) }
    var bookingError by remember { mutableStateOf<String?>(null) }
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
        authUser = response.user.toUi()
        authToken = response.token
        sharedPreferences.edit().putString(AppConfig.authTokenPreference, response.token).apply()
        currentTab = MainTab.Home
        stack.clear()
        stack.add(Screen.Tab(MainTab.Home))
    }

    fun go(screen: Screen) {
        if (screen is Screen.Tab) {
            currentTab = screen.tab
        }
        stack.add(screen)
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
                tryOnStatus = TryOnStatus(
                    jobId = resultJobId,
                    styleId = item.styleId,
                    styleName = item.styleName,
                    stage = item.source,
                    progress = 100,
                    status = "completed"
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
                            errorMessage = job.errorMessage
                        )
                        when (job.status) {
                            "completed" -> {
                                val imageBytes = repository.tryOnResultImageBytes(jobId)
                                latestTryOnBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                refreshTryOnHistory()
                                clearPendingTryOn()
                                tryOnSubmitting = false
                                val currentScreen = stack.lastOrNull()
                                val viewingJob = currentScreen == Screen.TryOnProcessing(styleId, jobId) ||
                                    currentScreen == Screen.TryOnResult(styleId, jobId)
                                if (viewingJob) {
                                    stack[stack.lastIndex] = Screen.TryOnResult(styleId, jobId)
                                } else {
                                    showTryOnReadyNotification(
                                        context,
                                        styleItems.firstOrNull { it.id == styleId }?.name ?: job.styleName
                                    )
                                }
                                return@launch
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
                    styleName = styleItems.firstOrNull { it.id == styleId }?.name.orEmpty(),
                    stage = job.stage,
                    progress = job.progress,
                    status = job.status,
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
                    styleName = styleItems.firstOrNull { it.id == styleId }?.name.orEmpty(),
                    stage = job.stage,
                    progress = job.progress,
                    status = job.status,
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
            .ifEmpty { reservationSampleStores }

        styleItems = fetchedStyles
        storeItems = fetchedStores
        hotKeywords = home.hotKeywords
        homeRecommended = home.recommended.map { it.toUi() }.filter { !it.imageUrl.isNullOrBlank() }
        homeHot = home.hot.map { it.toUi() }.filter { !it.imageUrl.isNullOrBlank() }
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
        val firstStore = storeItems.firstOrNull()
        if (firstStore == null) {
            Toast.makeText(context, "当前还没有可预约门店", Toast.LENGTH_SHORT).show()
            return
        }
        selectedStoreId = firstStore.id
        go(Screen.BookingForm(firstStore.id, styleId))
    }

    val displayedTryOnHistoryItems = buildDisplayedTryOnHistory(tryOnHistoryItems, tryOnStatus)

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
    BackHandler(enabled = stack.size > 1 && current !is Screen.Login && current !is Screen.Register) {
        back()
    }
    val showHomeChrome = current is Screen.Tab && (current.tab == MainTab.Home || current.tab == MainTab.TryOn)
    val hideAppChrome = showHomeChrome || current is Screen.DiyDesigner || current is Screen.Xiaomei || current is Screen.StoreDetail
    val styleDetailStyle = (current as? Screen.StyleDetail)?.let { screen ->
        styleItems.firstOrNull { it.id == screen.styleId } ?: styleItems.firstOrNull()
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
        Screen.Ranking -> "热门排行榜"
        is Screen.TryOnUpload -> "上传手部照片"
        is Screen.TryOnProcessing -> "手部识别中"
        is Screen.TryOnResult -> "试戴结果"
        Screen.TryOnHistory -> "试戴记录"
        Screen.DiyDesigner -> "DIY设计"
        Screen.Xiaomei -> "小美"
        Screen.Favorites -> "我的收藏"
        Screen.BookingRecords -> "预约记录"
        Screen.Reviews -> "我的评价"
        is Screen.StoreDetail -> "门店详情"
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

                is Screen.Tab -> when (screen.tab) {
                    MainTab.Home -> HomeScreen(
                        recommended = homeRecommended,
                        hot = homeHot,
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
                            currentTab = MainTab.Styles
                            stack.clear()
                            stack.add(Screen.Tab(MainTab.Styles))
                        },
                        onStyleClick = { go(Screen.StyleDetail(it)) }
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
                        onRefresh = ::refreshPageData,
                        onStyleClick = { go(Screen.StyleDetail(it)) }
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
                        onFavorites = { go(Screen.Favorites) },
                        onTryOnHistory = { go(Screen.TryOnHistory) },
                        onRecords = { go(Screen.BookingRecords) },
                        onReviews = { go(Screen.Reviews) },
                        onSettings = { go(Screen.Settings) }
                    )
                }

                is Screen.StyleDetail -> {
                    val style = styleItems.firstOrNull { it.id == screen.styleId } ?: return@AnimatedContent
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
                        trackEvent(
                            eventName = "search_result_click",
                            styleId = it,
                            sourcePage = "search_result",
                            payload = mapOf("query" to screen.query)
                        )
                        go(Screen.StyleDetail(it))
                    }
                )

                Screen.Ranking -> RankingScreen(
                    styles = (homeHot + homeRecommended + styleItems).distinctBy { it.id }.take(12),
                    onStyleClick = { go(Screen.StyleDetail(it)) }
                )

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
                        if (styleId.isNotBlank()) {
                            go(Screen.StyleDetail(styleId))
                        }
                    }
                )

                is Screen.TryOnUpload -> TryOnUploadScreen(
                    style = styleItems.firstOrNull { it.id == screen.styleId } ?: return@AnimatedContent,
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
                    style = styleItems.firstOrNull { it.id == screen.styleId } ?: return@AnimatedContent,
                    favorite = favorites.contains(screen.styleId),
                    resultStatus = tryOnStatus.status,
                    resultBitmap = latestTryOnBitmap,
                    onOpenStyle = { go(Screen.StyleDetail(screen.styleId)) },
                    onRetake = { go(Screen.TryOnUpload(screen.styleId)) },
                    onToggleFavorite = { toggleFavorite(screen.styleId) },
                    onBook = { openBookingForStyle(screen.styleId) },
                    onSaveImage = {
                        val bitmap = latestTryOnBitmap
                        if (bitmap == null) {
                            Toast.makeText(context, "试戴图还没生成完成", Toast.LENGTH_SHORT).show()
                        } else {
                            saveBitmapToGallery(context, bitmap, styleItems.firstOrNull { it.id == screen.styleId }?.name ?: "tryon")
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
                    styles = styleItems.filter { favorites.contains(it.id) },
                    refreshing = pageRefreshing,
                    onRefresh = ::refreshPageData,
                    onStyleClick = { go(Screen.StyleDetail(it)) },
                    onRetake = { go(Screen.TryOnUpload(it, true)) },
                    onBook = ::openBookingForStyle
                )

                Screen.BookingRecords -> BookingRecordsScreen(
                    records = bookingRecords,
                    refreshing = pageRefreshing,
                    onRefresh = ::refreshPageData
                )

                Screen.Reviews -> ReviewsScreen(records = bookingRecords)

                is Screen.StoreDetail -> {
                    val store = storeItems.firstOrNull { it.id == screen.storeId } ?: return@AnimatedContent
                    StoreDetailScreen(
                        store = store,
                        onBack = ::back,
                        onBook = {
                            selectedStoreId = store.id
                            val targetStyleId = screen.styleId ?: styleItems.firstOrNull()?.id
                            if (targetStyleId == null) {
                                Toast.makeText(context, "当前还没有可预约款式", Toast.LENGTH_SHORT).show()
                            } else {
                                go(Screen.BookingForm(store.id, targetStyleId))
                            }
                        }
                    )
                }

                is Screen.BookingForm -> {
                    val store = storeItems.firstOrNull { it.id == selectedStoreId } ?: return@AnimatedContent
                    val style = styleItems.firstOrNull { it.id == screen.styleId } ?: return@AnimatedContent
                    BookingFormScreen(
                        store = store,
                        style = style,
                        storeOptions = storeItems,
                        submitting = bookingSubmitting,
                        errorMessage = bookingError,
                        onStoreChange = { selectedStoreId = it },
                        onSubmit = { name, phone, note, slot ->
                            coroutineScope.launch {
                                bookingSubmitting = true
                                bookingError = null
                                runCatching {
                                    repository.createBooking(
                                        storeId = selectedStoreId,
                                        styleId = screen.styleId,
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
    val isError: Boolean = false
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
    val storableMessages = messages.filter { it.text.isNotBlank() || !it.imagePath.isNullOrBlank() }
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
    return SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(Date(updatedAt))
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

    fun addAssistantResponse(result: MeimeiChatResponse) {
        messages.add(
            XiaomeiChatMessage(
                role = XiaomeiChatRole.Assistant,
                text = result.toXiaomeiDisplayText(),
                response = result
            )
        )
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
        val displayText = content.ifBlank { "帮我看看这张手部照片" }
        messages.add(
            XiaomeiChatMessage(
                role = XiaomeiChatRole.User,
                text = displayText,
                imagePath = imageFile?.absolutePath
            )
        )
        coroutineScope.launch {
            runCatching {
                if (imageFile != null) {
                    val upload = repository.uploadHandImage(imageFile)
                    repository.meimeiChat(
                        message = content.ifBlank { "根据这张手部照片推荐适合的甲型、色号和款式" },
                        handImageUrl = upload.image_url,
                        handImageKey = upload.hand_id
                    )
                } else {
                    repository.meimeiChat(content)
                }
            }.onSuccess(::addAssistantResponse)
                .onFailure { error ->
                    messages.add(
                        XiaomeiChatMessage(
                            role = XiaomeiChatRole.Assistant,
                            text = error.message ?: "小美暂时没处理好，换个说法或重新发一次照片。",
                            isError = true
                        )
                    )
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

    LaunchedEffect(messages.size, loading) {
        val targetIndex = messages.size + if (loading) 1 else 0
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
                .padding(start = 18.dp, top = 48.dp, end = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp),
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
                        IconButton(onClick = ::closeXiaomei) {
                            Text("×", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
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
                    if (loading) {
                        item(key = "typing") {
                            XiaomeiTypingBubble()
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
                    XiaomeiPromptChip("🎨 我想自己 DIY 一款", onClick = onOpenDiy)
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding(),
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
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .clickable(enabled = !loading) { openImagePicker() },
                            shape = MaterialTheme.shapes.extraLarge,
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.Black)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Rounded.PhotoCamera, contentDescription = "添加图片", tint = Color.Black, modifier = Modifier.size(21.dp))
                            }
                        }
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
                text = SimpleDateFormat("HH:mm", Locale.CHINA).format(Date()),
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
private fun XiaomeiTypingBubble() {
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
            Text("小美正在分析...", fontSize = 15.sp, color = Color(0xFF6F5962))
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
    hot: List<NailStyle>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onSearch: () -> Unit,
    onSeeMore: () -> Unit,
    onRanking: () -> Unit,
    onAiPick: () -> Unit,
    onTrend: () -> Unit,
    onStyleClick: (String) -> Unit
) {
    val recommendationFeed = (recommended + hot).distinctBy { it.id }
    var selectedHomeCategory by remember { mutableStateOf("推荐") }
    val displayedRecommendationFeed = remember(recommendationFeed, selectedHomeCategory) {
        filterHomeRecommendationFeed(recommendationFeed, selectedHomeCategory)
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
            item { ActivityBannerPlaceholder(modifier = Modifier.padding(start = 12.dp, top = 8.dp, end = 12.dp)) }
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
                    selectedCategory = selectedHomeCategory,
                    onCategoryChange = { selectedHomeCategory = it },
                    modifier = Modifier
                )
            }
            if (displayedRecommendationFeed.isEmpty()) {
                item {
                    EmptyState(
                        title = "暂无推荐款式",
                        subtitle = "下拉刷新后会展示适合你的美甲款式"
                    )
                }
            } else {
                items(displayedRecommendationFeed.chunked(2)) { rowStyles ->
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
private fun ActivityBannerPlaceholder(modifier: Modifier = Modifier) {
    val banners = remember {
        listOf(
            R.drawable.home_activity_banner_1,
            R.drawable.home_activity_banner_2
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
        Image(
            painter = painterResource(id = banners[page]),
            contentDescription = "活动 Banner",
            modifier = Modifier.fillMaxSize(),
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
            HomeFeatureItem(title = "D I Y", iconRes = R.drawable.home_feature_diy_icon, iconOffsetX = 3.dp, onClick = onAiPick)
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
    val sellingPoint = style.tags.firstOrNull()?.let { "#$it  ${style.vibe}" } ?: style.vibe

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
                Text(
                    text = sellingPoint,
                    color = Color(0xFF9A6A35),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

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

@Composable
private fun HomeCategoryTabs(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("推荐", "日常", "通勤", "约会", "旅游", "个性")
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEach { category ->
                Box(
                    modifier = Modifier
                        .width(78.dp)
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

private fun filterHomeRecommendationFeed(styles: List<NailStyle>, category: String): List<NailStyle> {
    if (category == "推荐") return styles
    val keywords = when (category) {
        "日常" -> listOf("日常", "纯色", "简约", "裸", "奶油", "通勤")
        "通勤" -> listOf("通勤", "简约", "法式", "裸", "低调", "纯色")
        "约会" -> listOf("约会", "甜", "粉", "花", "玫瑰", "温柔")
        "旅游" -> listOf("旅游", "夏", "海", "度假", "彩", "亮片")
        "个性" -> listOf("个性", "黑", "棋盘", "金属", "银", "酷", "暗")
        else -> emptyList()
    }
    val matched = styles.filter { style ->
        val source = buildString {
            append(style.name)
            append(' ')
            append(style.vibe)
            append(' ')
            append(style.tags.joinToString(" "))
        }
        keywords.any { keyword -> source.contains(keyword, ignoreCase = true) }
    }
    return matched.ifEmpty { styles }
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

@Composable
private fun RankingScreen(styles: List<NailStyle>, onStyleClick: (String) -> Unit) {
    val rankedStyles = styles.take(12)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader("热门排行榜", "根据当前站内推荐与热门款式临时排序，正式数据接入后会自动更新。")
        }
        items(rankedStyles) { style ->
            val rank = rankedStyles.indexOf(style) + 1
            RankingRow(rank = rank, style = style, onClick = { onStyleClick(style.id) })
        }
        if (rankedStyles.isEmpty()) {
            item {
                EmptyState("暂无排行榜", "款式数据加载后会自动生成榜单。")
            }
        }
    }
}

@Composable
private fun RankingRow(rank: Int, style: NailStyle, onClick: () -> Unit) {
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
                    "热度 ${100 - rank * 3} · 试戴推荐",
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
                        .clickable {
                            onQueryChange("")
                            onSelectedOptionChange(options.first().label)
                        },
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
                LazyColumn(
                    modifier = Modifier
                        .width(74.dp)
                        .height((options.size * 44 + 12).dp)
                        .clip(RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 18.dp, bottomEnd = 18.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f), RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 18.dp, bottomEnd = 18.dp)),
                    contentPadding = PaddingValues(vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(options, key = { it.label }) { option ->
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

private val diyShapeOptions = listOf("短方", "短圆", "中方", "中椭圆", "中短梯", "长梯", "长椭圆", "尖水滴")

private val DiyHotPink = Color(0xFFD993AE)

private val diyShapeDescriptions = mapOf(
    "短方" to "两侧轻微收窄，前缘平直偏短，适合日常百搭，修饰手型。",
    "短圆" to "短款圆润边角，干净耐看，适合通勤与短甲用户。",
    "中方" to "线条利落，甲面存在感更强，适合简约纯色和法式。",
    "中椭圆" to "边缘柔和，显手指修长，适合粉色、裸色和细闪。",
    "中短梯" to "自然收窄，兼顾利落与柔和，适合轻法式。",
    "长梯" to "视觉拉长明显，适合亮片、猫眼和复杂装饰。",
    "长椭圆" to "修长温柔，适合渐变、晕染和珠光材质。",
    "尖水滴" to "尖端收束明显，氛围感强，适合精致、仙气或舞台风。"
)

private fun diyShapeImageRes(shape: String): Int = when (shape) {
    "短方" -> R.drawable.diy_nail_short_square
    "短圆" -> R.drawable.diy_nail_short_round
    "中方" -> R.drawable.diy_nail_medium_square
    "中椭圆" -> R.drawable.diy_nail_medium_oval
    "中短梯" -> R.drawable.diy_nail_medium_ladder
    "长梯" -> R.drawable.diy_nail_long_ladder
    "长椭圆" -> R.drawable.diy_nail_long_oval
    "尖水滴" -> R.drawable.diy_nail_stiletto
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
            "尖水滴" -> {
                moveTo(w * 0.5f, top)
                cubicTo(w * 0.88f, h * 0.12f, w * 0.82f, h * 0.56f, w * 0.58f, bottom)
                cubicTo(w * 0.54f, h * 0.99f, w * 0.46f, h * 0.99f, w * 0.42f, bottom)
                cubicTo(w * 0.18f, h * 0.56f, w * 0.12f, h * 0.12f, w * 0.5f, top)
                close()
            }
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
    var activeFilter by remember { mutableStateOf("附近") }
    var distanceFilter by remember { mutableStateOf("距离最近") }
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
                    activeFilter = "附近"
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
            text = "附近",
            selected = activeFilter == "附近",
            selectedOption = distanceFilter,
            options = listOf("距离最近", "3km内", "10km内"),
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
    onFavorites: () -> Unit,
    onTryOnHistory: () -> Unit,
    onRecords: () -> Unit,
    onReviews: () -> Unit,
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
            ProfileMenuRow(
                title = "我的作品",
                subtitle = "查看已保存的 DIY 设计与试戴作品",
                icon = Icons.Rounded.GridView,
                onClick = { Toast.makeText(context, "我的作品功能待接入", Toast.LENGTH_SHORT).show() }
            )
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
                ProfileMenuRow("风格档案", "管理肤色、偏好和常用甲型", Icons.Rounded.Person) {
                    Toast.makeText(context, "风格档案功能待接入", Toast.LENGTH_SHORT).show()
                }
                ProfileMenuRow("消息中心", "查看通知、预约提醒和系统消息", Icons.Rounded.NotificationsNone) {
                    Toast.makeText(context, "消息中心功能待接入", Toast.LENGTH_SHORT).show()
                }
                ProfileMenuRow("设置", "通知、隐私与偏好设置", Icons.Rounded.Storefront, onSettings)
            }
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
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            onStartProcessing(saveBitmapToCache(context, bitmap), "camera")
        }
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
            cameraLauncher.launch(null)
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
            cameraLauncher.launch(null)
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
    resultStatus: String,
    resultBitmap: Bitmap?,
    onOpenStyle: () -> Unit,
    onRetake: () -> Unit,
    onToggleFavorite: () -> Unit,
    onBook: () -> Unit,
    onSaveImage: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            ResultCanvas(style = style, resultBitmap = resultBitmap)
        }
        item {
            Text(style.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        item {
            Text(
                if (resultStatus == "completed" || resultStatus.isBlank()) "试戴图已生成，可查看上手效果并决定是否预约同款。" else "试戴结果暂不可用，请重新上传手部照片后再试。",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )
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
                    onClick = onSaveImage,
                    modifier = Modifier.weight(1f),
                    enabled = resultBitmap != null
                ) { Text("保存图片") }
            }
        }
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
                            item.createdAt.take(16).replace('T', ' '),
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
private fun StoreDetailScreen(store: Store, onBack: () -> Unit, onBook: () -> Unit) {
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
        }
        StoreDetailBottomBar(
            primary = primary,
            onConsult = { Toast.makeText(context, "咨询功能待接入", Toast.LENGTH_SHORT).show() },
            onBook = onBook,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
@Composable
private fun BookingFormScreen(
    store: Store,
    style: NailStyle,
    storeOptions: List<Store>,
    submitting: Boolean,
    errorMessage: String?,
    onStoreChange: (String) -> Unit,
    onSubmit: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val selectedSlot = store.slots.firstOrNull() ?: store.nearestSlot.ifBlank { "今天 18:00" }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionHeader("预约信息", "系统已带入款式和门店信息") }
        item { CompactValue("预约款式", style.name) }
        item { CompactValue("预约门店", store.name) }
        item { CompactValue("可选时间", selectedSlot) }
        item { CompactValue("到店位置", store.address.ifBlank { store.area }) }
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
            TextButton(onClick = { storeOptions.firstOrNull { it.id != store.id }?.let { onStoreChange(it.id) } }) { Text("切换其他门店") }
        }
        item {
            Button(onClick = { onSubmit(name, phone, note, selectedSlot) }, modifier = Modifier.fillMaxWidth(), enabled = !submitting) { Text(if (submitting) "提交中..." else "提交预约") }
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
private fun ReviewsScreen(records: List<BookingRecord>) {
    var selectedTab by remember { mutableStateOf("待评价") }
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
                UserReviewCard(item = item, pending = selectedTab == "待评价")
            }
        }
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
private fun UserReviewCard(item: ReviewItem, pending: Boolean) {
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
                        tags = emptyList()
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
                        onClick = {},
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                    ) {
                        Text("去评价", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            tint = if (index < (item.rating ?: 5)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(item.comment, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f), fontSize = 12.sp, lineHeight = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

private fun buildPendingReviewItems(records: List<BookingRecord>): List<ReviewItem> {
    val completed = records.filter { it.status.contains("完成") || it.status.contains("completed", ignoreCase = true) }
    val source = completed.ifEmpty { records.take(2) }
    return source.mapIndexed { index, record ->
        ReviewItem(
            id = "pending-${record.id}-$index",
            storeName = record.storeName,
            styleName = record.styleName,
            slot = record.slot
        )
    }
}

private fun buildFinishedReviewItems(records: List<BookingRecord>): List<ReviewItem> {
    val source = records.drop(2).take(2)
    return source.mapIndexed { index, record ->
        ReviewItem(
            id = "finished-${record.id}-$index",
            storeName = record.storeName,
            styleName = record.styleName,
            slot = record.slot,
            rating = if (index % 2 == 0) 5 else 4,
            comment = if (index % 2 == 0) "款式还原度高，修型很细致" else "门店服务很耐心，下次还会约"
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
                    .fillMaxHeight(),
                label = store.area.ifBlank { store.name.take(4) },
                showStatus = true
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

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    StoreMetaPill(text = store.statusText, highlight = true)
                    StoreMetaPill(text = store.distance)
                    StoreMetaPill(text = averagePriceText(store))
                }

                Text(
                    store.address.ifBlank { store.area },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
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
    modifier: Modifier = Modifier.size(92.dp),
    label: String = store.name.take(4),
    showStatus: Boolean = true
) {
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
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Icon(Icons.Rounded.Storefront, contentDescription = null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(if (showStatus) 24.dp else 22.dp))
            Text(
                label,
                color = Color.White,
                fontSize = if (showStatus) 13.sp else 18.sp,
                lineHeight = if (showStatus) 15.sp else 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (showStatus) {
                Surface(color = Color.Black.copy(alpha = 0.22f), shape = RoundedCornerShape(10.dp)) {
                    Text(
                        store.couponText,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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

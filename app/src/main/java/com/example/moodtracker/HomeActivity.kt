package com.example.moodtracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtracker.ui.theme.MoodTrackerTheme
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val username = intent.getStringExtra("USERNAME") ?: "User"
        setContent {
            MoodTrackerTheme {
                MainScreen(username = username)
            }
        }
    }
}

@Composable
fun MainScreen(username: String) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Summary", "History", "Profile")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.List, Icons.Filled.DateRange, Icons.Filled.Person)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFE1BEE7).copy(alpha = 0.6f)) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item, tint = Color.Black) },
                        label = { Text(item, color = Color.Black) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> HomeScreenContent(username = username)
                1 -> SummaryScreen()
                2 -> HistoryScreen()
                3 -> ProfileScreen(username = username)
            }
        }
    }
}

@Composable
fun HomeScreenContent(username: String) {
    val context = LocalContext.current
    val philippineZone = ZoneId.of("Asia/Manila")
    val philippineTime = ZonedDateTime.now(philippineZone)
    val day = philippineTime.format(DateTimeFormatter.ofPattern("dd"))
    val month = philippineTime.format(DateTimeFormatter.ofPattern("MMM").withLocale(java.util.Locale.ENGLISH)).uppercase()

    val quizQuestions = listOf(
        "Have you been sleeping well lately?",
        "Did you eat a healthy meal today?",
        "Have you exercised in the last 3 days?",
        "Did you spend some time outdoors?",
        "Have you connected with a friend or loved one today?"
    )
    var currentQuestionIndex by remember { mutableStateOf(0) }

    val history = MoodHistoryRepository.getMoodHistory()
    val weeklyHistory = history.filter { it.zonedDateTime.isAfter(ZonedDateTime.now().minus(1, ChronoUnit.WEEKS)) }
    val loggedDays = weeklyHistory.map { it.zonedDateTime.toLocalDate() }.distinct().count()

    fun nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % quizQuestions.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F0))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "How are you feeling today?",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, AssessMoodActivity::class.java)
                            intent.putExtra("USERNAME", username)
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5E6FF)),
                    ) {
                        Text(text = "Assess Now", color = Color.Black)
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF7F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hi, ${username.replaceFirstChar { it.uppercase() }}!",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color.Black
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFFFFD140))
                .padding(16.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFFCDD2))
                        .padding(16.dp),
                ) {
                    Column {
                        Text(text = "Today", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(40.dp)
                                    .background(Color(0xFFF48FB1))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = day, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = month, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE1BEE7))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Mood Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                            CircularProgressIndicator(
                                progress = 1f,
                                modifier = Modifier.size(80.dp),
                                color = Color.White.copy(alpha = 0.3f),
                                strokeWidth = 8.dp
                            )
                            CircularProgressIndicator(
                                progress = loggedDays / 7f,
                                modifier = Modifier.size(80.dp),
                                color = Color(0xFF673AB7),
                                strokeWidth = 8.dp
                            )
                            Text(text = "$loggedDays/7", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFBBDEFB))
                    .padding(16.dp)
            ) {
                Column {
                    Text(text = "Yes or No Quiz", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                    Text(
                        text = quizQuestions[currentQuestionIndex],
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { nextQuestion() },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5E6FF)),
                            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 12.dp)
                        ) {
                            Text(text = "Yes", color = Color.Black, fontSize = 16.sp)
                        }
                        Button(
                            onClick = { nextQuestion() },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5E6FF)),
                            contentPadding = PaddingValues(horizontal = 40.dp, vertical = 12.dp)
                        ) {
                            Text(text = "No", color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen() {
    val history = MoodHistoryRepository.getMoodHistory()

    val moodColors = mapOf(
        "Awesome" to Color(0xFFFFD54F),
        "Great" to Color(0xFFFFF176),
        "Loved" to Color(0xFFF06292),
        "Okay" to Color(0xFFC5E1A5),
        "Bored" to Color(0xFFE0E0E0),
        "Annoyed" to Color(0xFFFFB74D),
        "Sleepy" to Color(0xFF7986CB),
        "Sad" to Color(0xFF90A4AE),
        "Upset" to Color(0xFFE57373)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Your Mood Summary",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val weeklyHistory = history.filter { it.zonedDateTime.isAfter(ZonedDateTime.now().minus(1, ChronoUnit.WEEKS)) }
        WeeklySummary(weeklyHistory, moodColors)
    }
}

@Composable
fun WeeklySummary(history: List<MoodHistory>, moodColors: Map<String, Color>) {

    val moodCounts = history.groupingBy { it.mood }.eachCount()
    val total = moodCounts.values.sum().toFloat()
    val loggedDays = history.map { it.zonedDateTime.toLocalDate() }.distinct().count()
    val dominantMood = moodCounts.maxByOrNull { it.value }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(140.dp)) {
                        var startAngle = -90f
                        if(total > 0) {
                             moodCounts.entries.forEach { (mood, count) ->
                                val sweepAngle = (count / total) * 360f
                                drawArc(
                                    color = moodColors[mood] ?: Color.Gray,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = 35f)
                                )
                                startAngle += sweepAngle
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                         Text(text = "$loggedDays/7", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
                         Text(text = "This week's log", fontSize = 12.sp, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    moodCounts.keys.forEach { mood ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(moodColors[mood] ?: Color.Gray, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = mood, fontSize = 14.sp, color = Color.Black)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = moodCounts[mood].toString(), fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mood Over Time", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                MoodBarChart(history, moodColors)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if(dominantMood != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("You felt ${dominantMood.key} ${dominantMood.value} out of 7 days", color = Color.Black)
                    Text("Dominant Mood: ${dominantMood.key} ", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun MoodBarChart(history: List<MoodHistory>, moodColors: Map<String, Color>) {
    val dailyMoods = history.groupBy { it.zonedDateTime.toLocalDate() }.mapValues { entry -> entry.value.map { it.mood } }
    val today = ZonedDateTime.now().toLocalDate()
    val days = listOf(
        today.with(DayOfWeek.MONDAY),
        today.with(DayOfWeek.TUESDAY),
        today.with(DayOfWeek.WEDNESDAY),
        today.with(DayOfWeek.THURSDAY),
        today.with(DayOfWeek.FRIDAY),
        today.with(DayOfWeek.SATURDAY),
        today.with(DayOfWeek.SUNDAY)
    )
    
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        days.chunked(3).forEach { rowDays ->
             Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowDays.forEach { day -> 
                    val moodsForDay = dailyMoods[day] ?: emptyList()
                    DayMoodPieChart(day.dayOfWeek.name.substring(0,3), moodsForDay, moodColors)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
         Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            val allMoods = listOf("Awesome", "Great", "Loved", "Okay", "Bored", "Annoyed", "Sleepy", "Sad", "Upset")
            val firstRowMoods = allMoods.subList(0, 5)
            val secondRowMoods = allMoods.subList(5, allMoods.size)

            Row(horizontalArrangement = Arrangement.Center) {
                firstRowMoods.forEach { mood ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
                        Box(modifier = Modifier.size(8.dp).background(moodColors[mood] ?: Color.Gray, CircleShape))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = mood, fontSize = 10.sp, color = Color.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                secondRowMoods.forEach { mood ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
                        Box(modifier = Modifier.size(8.dp).background(moodColors[mood] ?: Color.Gray, CircleShape))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = mood, fontSize = 10.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun DayMoodPieChart(day: String, moods: List<String>, moodColors: Map<String, Color>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Canvas(modifier = Modifier.size(40.dp)) {
            if (moods.isNotEmpty()) {
                var startAngle = -90f
                val sweepAngle = 360f / moods.size
                moods.forEach { mood ->
                    drawArc(
                        color = moodColors[mood] ?: Color.Gray,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true
                    )
                    startAngle += sweepAngle
                }
            } else {
                drawCircle(color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTimeFilter by remember { mutableStateOf<String?>(null) } // "Week", "Month", or null
    val historyItems = MoodHistoryRepository.getMoodHistory()

    val filteredHistory = historyItems.filter {
        val matchesSearch = searchQuery.isBlank() ||
                it.date.contains(searchQuery, ignoreCase = true) ||
                it.mood.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedTimeFilter) {
            "Week" -> it.zonedDateTime.isAfter(ZonedDateTime.now().minus(1, ChronoUnit.WEEKS))
            "Month" -> it.zonedDateTime.isAfter(ZonedDateTime.now().minus(1, ChronoUnit.MONTHS))
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F0))
            .padding(16.dp)
    ) {
        Text("Your Mood History", fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Search...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray, 
                    unfocusedBorderColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                textStyle = TextStyle(color = Color.Black)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { selectedTimeFilter = if (selectedTimeFilter == "Week") null else "Week" },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedTimeFilter == "Week") Color(0xFFE1BEE7) else Color.Transparent
                )
            ) { Text("Week", color = Color.Black) }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = { selectedTimeFilter = if (selectedTimeFilter == "Month") null else "Month" },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedTimeFilter == "Month") Color(0xFFE1BEE7) else Color.Transparent
                )
            ) { Text("Month", color = Color.Black) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(filteredHistory) { item ->
                MoodHistoryItem(item)
            }
        }
    }
}

@Composable
fun MoodHistoryItem(item: MoodHistory) {
    Row(verticalAlignment = Alignment.Top) {
        Card(
            modifier = Modifier.padding(end = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(12.dp)) {
                Text(item.date.split(" ").first(), fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
                Text(item.date.split(" ").last(), fontSize = 16.sp, color = Color.Black)
            }
        }

        Column {
            Text(item.day, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(item.dayOfWeek, fontSize = 14.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if(item.mood.contains("Great")) Color(0xFFC8E6C9) else Color(0xFFBBDEFB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.mood, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(item.time, fontSize = 12.sp, color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.tags.forEach { tag ->
                            Chip(tag)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                     Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
                        Text(item.note, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontSize = 14.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    val chipColor = when(text) {
        "Music" -> Color(0xFFF48FB1)
        "Sleep" -> Color(0xFF9575CD)
        "Weather" -> Color(0xFF4FC3F7)
        "Games" -> Color(0xFF81C784)
        "Friends" -> Color(0xFFFFB74D)
        else -> Color.LightGray
    }
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = chipColor.copy(alpha = 0.7f))) {
        Text(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp, color = Color.Black)
    }
}

data class MoodHistory(val date: String, val day: String, val dayOfWeek: String, val mood: String, val time: String, val tags: List<String>, val note: String, val zonedDateTime: ZonedDateTime)


@Composable
fun ProfileScreen(username: String) {
    val history = MoodHistoryRepository.getMoodHistory()
    val moodsLogged = history.size

    var editableUsername by remember { mutableStateOf(username) }
    var editableBio by remember { mutableStateOf("Taking it one day at a time") }
    var showNameDialog by remember { mutableStateOf(false) }
    var showBioDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    color = Color(0xFFFADBD8),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(100.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "User Icon",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = editableUsername.replaceFirstChar { it.uppercase() },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = editableBio,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoRow(label = "Joined", value = "March 2025")
                ProfileInfoRow(label = "Streak", value = "12 days logged in a row")
                ProfileInfoRow(label = "Moods Logged", value = moodsLogged.toString())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                EditProfileRow(icon = R.drawable.edit, text = "Change Name") {
                    showNameDialog = true
                }
                EditProfileRow(icon = R.drawable.update_bio, text = "Update Bio/Quote") {
                    showBioDialog = true
                }
            }
        }

        if (showNameDialog) {
            EditDialog(
                title = "Change Name",
                initialValue = editableUsername,
                onDismiss = { showNameDialog = false },
                onSave = { newName ->
                    editableUsername = newName
                    showNameDialog = false
                }
            )
        }

        if (showBioDialog) {
            EditDialog(
                title = "Update Bio/Quote",
                initialValue = editableBio,
                onDismiss = { showBioDialog = false },
                onSave = { newBio ->
                    editableBio = newBio
                    showBioDialog = false
                }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label:", fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, color = Color.DarkGray)
    }
}

@Composable
fun EditProfileRow(@DrawableRes icon: Int, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun EditDialog(
    title: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onSave(text) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Preview(showBackground = true, device = "spec:width=360dp,height=740dp,dpi=480")
@Composable
fun MainScreenPreview() {
    MoodTrackerTheme {
        MainScreen(username = "User")
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=800dp,dpi=480")
@Composable
fun HistoryScreenPreview() {
    MoodTrackerTheme {
        HistoryScreen()
    }
}

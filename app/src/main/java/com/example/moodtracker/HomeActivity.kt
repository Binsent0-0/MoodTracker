package com.example.moodtracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtracker.ui.theme.MoodTrackerTheme
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
                3 -> ProfileScreen()
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
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { context.startActivity(Intent(context, AssessMoodActivity::class.java)) },
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
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = 1f,
                                modifier = Modifier.size(80.dp),
                                color = Color.White.copy(alpha = 0.3f),
                                strokeWidth = 8.dp
                            )
                            CircularProgressIndicator(
                                progress = 6f / 7f,
                                modifier = Modifier.size(80.dp),
                                color = Color(0xFF673AB7),
                                strokeWidth = 8.dp
                            )
                            Text(text = "6/7", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
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
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Week", "Month")

    val moodData = mapOf(
        "Awesome" to 3f,
        "Great" to 2f,
        "Bored" to 1f,
        "Annoyed" to 1f
    )

    val moodColors = mapOf(
        "Great" to Color(0xFF81C784),      // Green
        "Bored" to Color(0xFFE57373),      // Red
        "Annoyed" to Color(0xFFFFB74D),    // Orange
        "Sleepy" to Color(0xFF9575CD),     // Purple
        "Awesome" to Color(0xFF4FC3F7)     // Blue
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

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(4.dp)
                        .background(Color(0xFFE1BEE7), shape = RoundedCornerShape(2.dp))
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, color = if (selectedTab == index) Color.Black else Color.Gray) },
                    modifier = if (selectedTab == index) Modifier.background(Color(0xFFE1BEE7).copy(alpha = 0.4f)) else Modifier.background(Color.White)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) { // Week view
            WeeklySummary(moodData, moodColors)
        }
    }
}

@Composable
fun WeeklySummary(moodData: Map<String, Float>, moodColors: Map<String, Color>) {
    val total = moodData.values.sum()

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
                        moodData.entries.forEach { (mood, value) ->
                            val sweepAngle = (value / total) * 360f
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
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                         Text(text = "6/7", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Black)
                         Text(text = "This week's log", fontSize = 12.sp, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    moodData.keys.forEach { mood ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(moodColors[mood] ?: Color.Gray, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = mood, fontSize = 14.sp, color = Color.Black)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = moodData[mood]?.toInt().toString(), fontWeight = FontWeight.Bold, color = Color.Black)
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
                MoodBarChart(moodColors)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("You felt Awesome 3 out of 7 days", color = Color.Black)
                Text("Dominant Mood: Great ❤️", color = Color.Black)
            }
        }
    }
}

@Composable
fun MoodBarChart(moodColors: Map<String, Color>) {
    val dailyMoods = mapOf(
        "Tue" to ("Awesome" to 0.7f),
        "Wed" to ("Great" to 1.0f),
        "Thu" to ("Bored" to 0.8f),
        "Fri" to ("Annoyed" to 0.6f),
        "Sat" to ("Sleepy" to 0.5f)
    )
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val maxBarHeight = 150.dp

    Column {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(maxBarHeight)) {
            // Grid lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val lineHeight = size.height / 7
                for (i in 1..7) {
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(x = 40f, y = i * lineHeight),
                        end = Offset(x = size.width, y = i * lineHeight),
                        strokeWidth = 1f
                    )
                }
            }
            
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                days.forEach { day ->
                    val moodEntry = dailyMoods[day]
                    val barHeightFraction = moodEntry?.second ?: 0f
                    val mood = moodEntry?.first
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (barHeightFraction > 0f) {
                             Box(modifier = Modifier
                                .width(35.dp)
                                .fillMaxHeight(barHeightFraction)
                                .background(moodColors[mood] ?: Color.Gray, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f, fill=true))
                        }
                       
                    }
                }
            }
             Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                 days.forEach { day ->
                    Text(text = day, fontSize = 12.sp, color = Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
             val legendMoods = listOf("Great", "Bored", "Annoyed", "Sleepy", "Awesome")
             legendMoods.forEach { mood ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
                    Box(modifier = Modifier.size(8.dp).background(moodColors[mood] ?: Color.Gray, CircleShape))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = mood, fontSize = 10.sp, color = Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val historyItems = MoodHistoryRepository.getMoodHistory()

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
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Gray, unfocusedBorderColor = Color.LightGray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = { /*TODO*/ }) { Text("Week", color = Color.Black) }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = { /*TODO*/ }) { Text("Month", color = Color.Black) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(historyItems) { item ->
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

data class MoodHistory(val date: String, val day: String, val dayOfWeek: String, val mood: String, val time: String, val tags: List<String>, val note: String)


@Composable
fun ProfileScreen() {
    Text("Profile Screen")
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

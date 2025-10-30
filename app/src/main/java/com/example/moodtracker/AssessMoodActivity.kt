package com.example.moodtracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtracker.ui.theme.MoodTrackerTheme

class AssessMoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodTrackerTheme {
                AssessMoodScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessMoodScreen() {
    val context = LocalContext.current
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    val moods = listOf(
        Mood("Awesome", R.drawable.mood_awesome, Color(0xFFFFD54F)),
        Mood("Great", R.drawable.mood_great, Color(0xFFFFF176)),
        Mood("Loved", R.drawable.mood_loved, Color(0xFFF06292)),
        Mood("Okay", R.drawable.mood_okay, Color(0xFFC5E1A5)),
        Mood("Bored", R.drawable.mood_bored, Color(0xFFE0E0E0)),
        Mood("Annoyed", R.drawable.mood_annoyed, Color(0xFFFFB74D)),
        Mood("Sleepy", R.drawable.mood_sleepy, Color(0xFF7986CB)),
        Mood("Sad", R.drawable.mood_sad, Color(0xFF90A4AE)),
        Mood("Upset", R.drawable.mood_upset, Color(0xFFE57373))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { (context as? Activity)?.finish() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFFFF7F0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How are you feeling right now?",
                fontSize = 25.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)

            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(moods) { mood ->
                    MoodItem(
                        mood = mood,
                        isSelected = selectedMood == mood,
                        onMoodSelected = { 
                            selectedMood = mood
                            val intent = Intent(context, AddDetailsActivity::class.java)
                            intent.putExtra("MOOD_NAME", mood.name)
                            intent.putExtra("MOOD_ICON", mood.icon as Int) // Assuming icon is an Int resource
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

data class Mood(val name: String, val icon: Any, val color: Color)

@Composable
fun MoodItem(mood: Mood, isSelected: Boolean, onMoodSelected: () -> Unit) {
    val borderModifier = if (isSelected) {
        Modifier.border(4.dp, Color.Black, CircleShape)
    } else {
        Modifier
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onMoodSelected() }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .then(borderModifier)
                .background(mood.color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when (mood.icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = mood.icon,
                        contentDescription = mood.name,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                is Int -> {
                    Image(
                        painter = painterResource(id = mood.icon),
                        contentDescription = mood.name,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = mood.name, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, device = "spec:width=360dp,height=640dp,dpi=480")
@Composable
fun AssessMoodScreenPreview() {
    MoodTrackerTheme {
        AssessMoodScreen()
    }
}

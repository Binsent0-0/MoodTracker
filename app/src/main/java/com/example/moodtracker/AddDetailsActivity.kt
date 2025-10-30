package com.example.moodtracker

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtracker.ui.theme.MoodTrackerTheme

class AddDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val moodName = intent.getStringExtra("MOOD_NAME") ?: "Unknown"
        val moodIcon = intent.getIntExtra("MOOD_ICON", R.drawable.mood_okay)
        setContent {
            MoodTrackerTheme {
                AddDetailsScreen(moodName = moodName, moodIcon = moodIcon)
            }
        }
    }
}

data class Cause(val text: String, val icon: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDetailsScreen(moodName: String, moodIcon: Int) {
    val context = LocalContext.current
    var note by remember { mutableStateOf("") }
    val selectedCauses = remember { mutableStateListOf<Cause>() }

    val causes = listOf(
        Cause("Music", R.drawable.music),
        Cause("Weather", R.drawable.weather),
        Cause("School", R.drawable.school),
        Cause("Pets", R.drawable.pets),
        Cause("Environment", R.drawable.environment),
        Cause("Home", R.drawable.house),
        Cause("Sleep", R.drawable.sleep),
        Cause("Work", R.drawable.work),
        Cause("Friends", R.drawable.friends),
        Cause("Games", R.drawable.games)
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
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = moodIcon),
                contentDescription = moodName,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What's making you feel $moodName?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    causes.subList(0, 3).forEach { cause ->
                        CauseChip(cause = cause, onCauseSelected = { if (selectedCauses.contains(it)) selectedCauses.remove(it) else selectedCauses.add(it) })
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     causes.subList(3, 5).forEach { cause ->
                        CauseChip(cause = cause, onCauseSelected = { if (selectedCauses.contains(it)) selectedCauses.remove(it) else selectedCauses.add(it) })
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     causes.subList(5, 8).forEach { cause ->
                        CauseChip(cause = cause, onCauseSelected = { if (selectedCauses.contains(it)) selectedCauses.remove(it) else selectedCauses.add(it) })
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     causes.subList(8, 10).forEach { cause ->
                        CauseChip(cause = cause, onCauseSelected = { if (selectedCauses.contains(it)) selectedCauses.remove(it) else selectedCauses.add(it) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.note_bg),
                    contentDescription = "Note background",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(modifier = Modifier.padding(24.dp)) {
                    TextField(
                        value = note,
                        onValueChange = { note = it },
                        placeholder = { Text("Add a note...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.Transparent, 
                            unfocusedContainerColor = Color.Transparent, 
                            focusedIndicatorColor = Color.Transparent, 
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                     Button(
                         onClick = { 
                            MoodHistoryRepository.addMood(moodName, selectedCauses, note)
                            Toast.makeText(context, "Mood logged successfully!", Toast.LENGTH_SHORT).show()
                            (context as? Activity)?.finish()
                          },
                         modifier = Modifier.align(Alignment.End)
                     ) {
                        Text("Check-in")
                    }
                }
            }
        }
    }
}

@Composable
fun CauseChip(cause: Cause, onCauseSelected: (Cause) -> Unit) {
    var isSelected by remember { mutableStateOf(false) }
    val backgroundColor = if(isSelected) Color.DarkGray else Color.LightGray.copy(alpha = 0.5f)
    val textColor = if(isSelected) Color.White else Color.Black

    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.clickable { 
            isSelected = !isSelected
            onCauseSelected(cause)
         }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = cause.icon), 
                contentDescription = null, 
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = cause.text,
                fontSize = 14.sp,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true, device = "spec:width=360dp,height=740dp,dpi=480")
@Composable
fun AddDetailsScreenPreview() {
    MoodTrackerTheme {
        AddDetailsScreen(moodName = "Loved", moodIcon = R.drawable.mood_loved)
    }
}

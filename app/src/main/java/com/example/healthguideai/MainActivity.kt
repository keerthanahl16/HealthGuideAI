package com.example.healthguideai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthguideai.ui.theme.HealthGuideAITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthGuideAITheme {
                HealthGuideApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthGuideApp() {

    var selectedScreen by remember { mutableStateOf(0) }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("HealthGuide AI") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Health"
                    )
                }
            )
        },

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = selectedScreen == 0,
                    onClick = { selectedScreen = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedScreen == 1,
                    onClick = { selectedScreen = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                    label = { Text("History") }
                )

                NavigationBarItem(
                    selected = selectedScreen == 2,
                    onClick = { selectedScreen = 2 },
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                    label = { Text("About") }
                )
            }
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues)
        ) {

            when (selectedScreen) {

                0 -> HomeScreen()

                1 -> HistoryScreen()

                2 -> AboutScreen()

            }
        }
    }
}

@Composable
fun HomeScreen() {

    var symptoms by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Enter Symptoms",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = symptoms,
                    onValueChange = { symptoms = it },
                    label = { Text("Example: fever, cold") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        result = when {
                            symptoms.contains("fever", true) ->
                                "AI Suggestion: Drink water, rest well and monitor temperature."

                            symptoms.contains("cold", true) ->
                                "AI Suggestion: Stay warm and drink hot fluids."

                            symptoms.contains("headache", true) ->
                                "AI Suggestion: Rest your eyes and stay hydrated."

                            else ->
                                "AI Suggestion: Maintain hydration and consult a doctor if symptoms continue."
                        }

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Get AI Health Tips")
                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        if (result.isNotEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "AI Response",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(result)
                }
            }
        }
    }
}

@Composable
fun HistoryScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "History",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "History Feature Coming Soon",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Future updates will store your previous health queries.")
    }
}

@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "About HealthGuide AI",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "HealthGuide AI is an Android application designed to provide basic health suggestions based on user symptoms using AI-inspired logic."
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Developed as part of Android App Development Internship."
        )
    }
}
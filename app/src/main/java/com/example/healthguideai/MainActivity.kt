package com.example.healthguideai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HealthGuideApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthGuideApp() {
    var selectedScreen by remember { mutableIntStateOf(0) }
    val history = remember { mutableStateListOf<String>() }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("HealthGuide AI") },
                navigationIcon = { Icon(Icons.Default.Favorite,null) }
            )
        },

        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = selectedScreen == 0,
                    onClick = { selectedScreen = 0 },
                    icon = { Icon(Icons.Default.Home,null) },
                    label = { Text("Dashboard") }
                )

                NavigationBarItem(
                    selected = selectedScreen == 1,
                    onClick = { selectedScreen = 1 },
                    icon = { Icon(Icons.AutoMirrored.Filled.List,null) },
                    label = { Text("History") }
                )

                NavigationBarItem(
                    selected = selectedScreen == 2,
                    onClick = { selectedScreen = 2 },
                    icon = { Icon(Icons.Default.Info,null) },
                    label = { Text("AI Chat") }
                )

                NavigationBarItem(
                    selected = selectedScreen == 3,
                    onClick = { selectedScreen = 3 },
                    icon = { Icon(Icons.Default.Info,null) },
                    label = { Text("About") }
                )
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE8F5E9), Color(0xFFE3F2FD))
                    )
                )
                .padding(padding)
        ) {

            when(selectedScreen){

                0 -> DashboardScreen(history)
                1 -> HistoryScreen(history)
                2 -> ChatScreen()
                3 -> AboutScreen()
            }
        }
    }
}

@Composable
fun DashboardScreen(history: MutableList<String>) {
    var symptoms by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    var heartRate by remember { mutableIntStateOf(72) }
    var oxygen by remember { mutableIntStateOf(98) }
    var steps by remember { mutableIntStateOf(4200) }
    var calories by remember { mutableIntStateOf(260) }
    var water by remember { mutableIntStateOf(4) }

    val stress = if(heartRate > 90) "High" else "Normal"
    val healthScore = (steps/100 + oxygen + water).coerceAtMost(100)

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            Text("Health Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween){

                HealthCard("Heart Rate","$heartRate BPM")
                HealthCard("SpO₂","$oxygen %")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween){

                HealthCard("Steps","$steps")
                HealthCard("Calories","$calories kcal")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.SpaceBetween){

                HealthCard("Stress",stress)
                HealthCard("Health Score","$healthScore")
            }

            Spacer(modifier = Modifier.height(10.dp))

            HealthCard("Water Intake","$water / 8")

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {

                    heartRate = Random.nextInt(60,100)
                    oxygen = Random.nextInt(95,100)
                    steps += Random.nextInt(50,200)
                    calories += Random.nextInt(10,30)

                },
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Refresh Health Data")
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text("Symptom Analyzer",
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = symptoms,
                onValueChange = { symptoms = it },
                label = { Text("Enter symptoms") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                    result = when{

                        symptoms.contains("fever",true) ->
                            "Possible infection. Stay hydrated."

                        symptoms.contains("cough",true) ->
                            "Drink warm fluids and rest."

                        symptoms.contains("chest pain",true) ->
                            "⚠ Seek medical help immediately."

                        else ->
                            "Consult a doctor for accurate diagnosis."
                    }

                    history.add("$symptoms → $result")

                    coroutineScope.launch{
                        listState.animateScrollToItem(1)
                    }

                },
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Analyze Symptoms")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if(result.isNotEmpty()){

                Card{

                    Text(
                        result,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HealthCard(title:String,value:String){
    Card(
        modifier = Modifier
            .padding(4.dp)
            .width(160.dp),
        shape = RoundedCornerShape(18.dp)
    ){

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(title)

            Spacer(modifier = Modifier.height(6.dp))

            Text(value,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }
    }
}

@Composable
fun HistoryScreen(history:List<String>){
    LazyColumn(modifier = Modifier.padding(16.dp)){

        items(history){

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ){

                Text(it,Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun ChatScreen(){
    var question by remember{ mutableStateOf("") }
    var answer by remember{ mutableStateOf("") }

    Column(modifier = Modifier.padding(20.dp)){

        Text("AI Health Assistant",fontSize = 22.sp)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = question,
            onValueChange = {question = it},
            label = {Text("Ask health question")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {

            answer = "For serious conditions consult a medical professional."

        }){

            Text("Ask AI")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if(answer.isNotEmpty()){

            Card{

                Text(answer,Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun AboutScreen(){
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text("HealthGuide AI",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        Text("Features")
        Text("• Real-time health dashboard")
        Text("• Stress indicator")
        Text("• Health score prediction")
        Text("• Symptom analyzer")
        Text("• AI health assistant")

        Spacer(modifier = Modifier.height(20.dp))

        Text("Built using Kotlin + Jetpack Compose")
    }
}

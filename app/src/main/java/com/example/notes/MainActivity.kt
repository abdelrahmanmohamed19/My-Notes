package com.example.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.notes.db.Notes
import com.example.notes.db.NotesDao
import com.example.notes.db.myDatabase
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val database = Room.databaseBuilder(this, myDatabase::class.java, "myDatabase").allowMainThreadQueries().build()
            val dao = database.getDao()
            val navController =NavHostController(this)

            NotesTheme {
                Scaffold(floatingActionButton = { FloatingButton(navController = navController) }) {
                    Box(modifier = Modifier.padding(it)){
                        Navigation(navController = navController, dao =dao )
                    }

                }

            }
        }
    }
}


@Composable
fun Navigation(navController : NavHostController , dao: NotesDao) {
    NavHost(navController = navController , startDestination = "home") {
        composable("home"){ HomeScreen(navController,dao)}

        composable("details/{id}", arguments = listOf(navArgument(name = "id"){type = NavType.IntType})){
            val id = it.arguments?.getInt("id")
            DetailsScreen(dao,id ?: -1)
        }
        composable("add"){ AddNotesScreen(navController,dao)}
    }
}


@Composable
fun HomeScreen(navController: NavHostController, dao: NotesDao){
    var list : List<Notes> by remember { mutableStateOf(emptyList()) }
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Button(onClick = {list = dao.getAllNotes()}) {
            Text(text = "Refresh", fontSize = 15.sp , fontWeight = FontWeight.Bold)
        }
        LazyColumn{
            itemsIndexed(list) {index ,item ->
                Card(elevation = CardDefaults.cardElevation(12.dp), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()){
                    Button(onClick = {navController.navigate("details/$index")}) {
                        Text(text = item.title, fontSize = 30.sp , fontWeight = FontWeight.Bold )
                    }
                }
            }
        }
    }
}
@Composable
fun FloatingButton(navController: NavHostController){
    Button(onClick = {navController.navigate("add")}) {
        Text(text = "+")
    }
}

@Composable
fun DetailsScreen(dao: NotesDao , id : Int) {
    val item = dao.getNote(id)
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        Card(elevation = CardDefaults.cardElevation(12.dp), shape = RoundedCornerShape(12.dp)) {
             Text(text = item.title , fontSize = 25.sp , fontWeight = FontWeight.Bold)
             Text(text = item.content , fontSize = 20.sp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNotesScreen(navController: NavHostController, dao: NotesDao) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(elevation = CardDefaults.cardElevation(12.dp), shape = RoundedCornerShape(12.dp)) {
            var title by remember { mutableStateOf(TextFieldValue()) }
            var content by remember { mutableStateOf(TextFieldValue()) }
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text(text = "Enter your Note Title Here") })
            TextField(
                value = content,
                onValueChange = { title = it },
                placeholder = { Text(text = "Enter your Note Here") })
            Button(onClick = {
                dao.addNote(Notes(title = title.text , content = content.text))
                Toast.makeText(context, "your Note is Added", Toast.LENGTH_SHORT).show()
                navController.navigate("home")
            }) {
                Text(text = "Add your Note", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


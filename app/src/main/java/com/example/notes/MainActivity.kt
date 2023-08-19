package com.example.notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.db.Notes
import com.example.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel> ()


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NotesTheme {
                        Navigation(navController = navController, viewModel = viewModel)

                }

            }
        }
    }



@Composable
fun Navigation(navController : NavHostController ,viewModel: MainViewModel) {
    NavHost(navController = navController , startDestination = "home") {
        composable("home"){ HomeScreen(navController,viewModel)}
        composable("add"){ AddNotesScreen(navController, viewModel)}
        composable("details/{id}/{title}/{content}", arguments = listOf(
            navArgument(name = "id"){type = NavType.IntType},
            navArgument(name = "title"){type = NavType.StringType},
            navArgument(name = "content"){type = NavType.StringType})){
            val id = it.arguments?.getInt("id")
            val title = it.arguments?.getString("title")
            val content = it.arguments?.getString("content")
            DetailsScreen(navController,id !! ,title !!,content !!,viewModel)
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController,viewModel: MainViewModel){
    var search by remember { mutableStateOf(TextFieldValue()) }
    var list by remember{ mutableStateOf(emptyList<Notes>()) }
    val colors = listOf(R.color.first,R.color.second,R.color.third,R.color.fourth,R.color.fifth,R.color.sixth)
    val scope = rememberCoroutineScope()
    Scaffold(floatingActionButton = { FloatingButton(navController = navController)},topBar = { AppTobBar(viewModel)}) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .background(colorResource(id = R.color.main))
        .fillMaxSize()
        .padding(it)) {

        TextField(value = search, onValueChange = {search = it}, placeholder = { Text(text = "Find your Note")}, modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = colorResource(id = R.color.main),
                cursorColor = Color.Black,
                containerColor = Color.White
            ))

        LazyVerticalGrid(columns = GridCells.Fixed(2)){

            if (search.text.isEmpty()) {
               list = viewModel.listMain.value
            }

            else if (search.text.isNotEmpty()) {

                scope.launch {

                    list = viewModel.getNote(search.text).value

                }
            }
            itemsIndexed(list) {index ,item ->
                val color = colors[index % colors.size]
                Box(modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { navController.navigate("details/${item.id}/${item.title}/${item.content}") }){
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(color))) {
                        Text(text = item.title, fontSize = 25.sp,  color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                        Text(text = item.content, fontSize = 15.sp, color = Color.Black , modifier = Modifier.padding(6.dp))
                    }
                }
                }
            }
        }
    }
}

@Composable
fun FloatingButton(navController: NavHostController){
    Button(onClick = {navController.navigate("add")}, colors = ButtonDefaults.buttonColors(Color.Black)) {
        Text(text = "+", color = Color.White, fontSize = 25.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTobBar(viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    TopAppBar(title = { Text(text = "Notes", fontSize = 25.sp, color = Color.White, fontWeight = FontWeight.Bold) },colors = TopAppBarDefaults.smallTopAppBarColors(colorResource(id = R.color.main)),
        actions = {
            IconButton(onClick = {
                scope.launch {
                    viewModel.clear()
                    Toast.makeText(context,"your notes list is cleared",Toast.LENGTH_SHORT).show()
                } }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavHostController,
    id: Int,
    noteTitle: String,
    noteContent: String,
    viewModel: MainViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.main))) {
        var title by remember { mutableStateOf(noteTitle) }
        var content by remember { mutableStateOf(noteContent) }
        TextField(value = title, onValueChange = { title = it }, placeholder = { Text(text = "Note Title",fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 12.dp, end = 12.dp),
            textStyle = TextStyle(fontSize = 25.sp, color = Color.White,fontWeight = FontWeight.Bold),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.White,
                containerColor = colorResource(id = R.color.main)
            ))
        TextField(value = content, onValueChange = { content = it }, placeholder = { Text(text = "Enter your Note Here",fontSize = 20.sp, color = Color.White)}, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.80f)
            .padding(horizontal = 12.dp),
            textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor =  colorResource(id = R.color.main),
                unfocusedIndicatorColor = colorResource(id = R.color.main),
                cursorColor = Color.White,
                containerColor = colorResource(id = R.color.main)
            ))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                if (title.isNotEmpty() && content.isNotEmpty()){
                    scope.launch {
                        viewModel.updateNote(Notes(id = id ,title = title , content = content))
                        Toast.makeText(context, "your Note is Updated", Toast.LENGTH_SHORT).show()
                        navController.navigate("home")
                    }

                }
                else {Toast.makeText(context,"Title or Content is empty",Toast.LENGTH_SHORT).show()}
            }, modifier = Modifier
                .padding(top = 8.dp , bottom = 8.dp), colors = ButtonDefaults.buttonColors(Color.DarkGray)) {
                Text(text = "Update", fontSize = 20.sp,fontWeight = FontWeight.Bold, color = Color.White)
            }
            Button(onClick = {
                scope.launch {
                    viewModel.deleteNote(Notes(id = id ,title = title , content = content))
                    Toast.makeText(context, "your Note is Deleted", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                }

            }, modifier = Modifier
                .padding(top = 8.dp , bottom = 8.dp, start = 12.dp), colors = ButtonDefaults.buttonColors(Color.DarkGray)) {
                Text(text = "Delete", fontSize = 20.sp,fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNotesScreen(navController: NavHostController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(id = R.color.main))
        .padding(top = 24.dp)) {
            var title by remember { mutableStateOf(TextFieldValue()) }
            var content by remember { mutableStateOf(TextFieldValue()) }
            TextField(value = title, onValueChange = { title = it }, placeholder = { Text(text = "Note Title",fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)}, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 25.sp, color = Color.White,fontWeight = FontWeight.Bold),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Black,
                    cursorColor = Color.White,
                    containerColor = colorResource(id = R.color.main)
                ))
            TextField(value = content, onValueChange = { content = it }, placeholder = { Text(text = "Enter your Note Here",fontSize = 20.sp, color = Color.White)}, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.80f)
                .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor =  colorResource(id = R.color.main),
                    unfocusedIndicatorColor = colorResource(id = R.color.main),
                    cursorColor = Color.White,
                    containerColor = colorResource(id = R.color.main)
                ))
            Button(onClick = {
                if(title.text.isNotEmpty() && content.text.isNotEmpty()){
                    scope.launch {
                        viewModel.insertNote(Notes(title = title.text , content = content.text))
                        Toast.makeText(context, "your Note is Added", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") }
                    }

                else {Toast.makeText(context,"Title or Content is empty",Toast.LENGTH_SHORT).show()}},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 8.dp),
                colors =ButtonDefaults.buttonColors(Color.DarkGray)) {
                Text(text = "Add your Note", fontSize = 20.sp,fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }


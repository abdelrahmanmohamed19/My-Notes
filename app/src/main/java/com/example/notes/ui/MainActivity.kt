package com.example.notes.ui

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.R
import com.example.notes.model.local.Notes
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<NotesViewModel> ()


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
fun Navigation(navController : NavHostController ,viewModel: NotesViewModel) {
    NavHost(navController = navController , startDestination = "home") {
        composable("home"){ HomeScreen(navController,viewModel) }
        composable("add"){ AddNotesScreen(navController, viewModel) }
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
fun HomeScreen(navController: NavHostController,viewModel: NotesViewModel){
    var search by remember { mutableStateOf(TextFieldValue()) }
    val colors = listOf(
        R.color.first,
        R.color.second,
        R.color.third,
        R.color.fourth,
        R.color.fifth,
        R.color.sixth
    )
    val scope = rememberCoroutineScope()
    if (search.text.isEmpty()) {
        LaunchedEffect1(key1 = true){
            scope.launch{
                viewModel.getAllNotes()
            }
        }
    }
    Scaffold(floatingActionButton = { FloatingButton(navController = navController) },topBar = { AppTobBar(viewModel) }) { it ->
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(it)) {

        TextField(value = search, onValueChange = {search = it}, placeholder = { Text(text = "find your note")}, modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = colorResource(id = R.color.main),
                cursorColor = Color.White,
                containerColor = colorResource(id = R.color.main)
            ))

        LazyVerticalGrid(columns = GridCells.Fixed(2)){
            if (search.text.isNotEmpty()) {
                scope.launch {
                    viewModel.getNote(search.text)
                    }
                }

            itemsIndexed(viewModel.notesList) {index ,item ->
                val color = colors[index % colors.size]
                Box(modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { navController.navigate("details/${item.id}/${item.title}/${item.content}") }){
                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(color))) {
                        Text(text = item.title.toUpperCase(), fontSize = 35.sp,  color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(text = item.date, fontSize = 10.sp, color = Color.DarkGray)
                    }
                }
                }
            }
        }
    }
}


@Composable
fun FloatingButton(navController: NavHostController){
    Button(onClick = {navController.navigate("add")}, colors = ButtonDefaults.buttonColors(colorResource(id = R.color.main))) {
        Text(text = "+", color = Color.White, fontSize = 25.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTobBar(viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()
    var isMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    TopAppBar(title = { Text(text = "Notes", fontSize = 25.sp, color = Color.White, fontWeight = FontWeight.Bold) },colors = TopAppBarDefaults.smallTopAppBarColors(Color.Black),
        actions = {
            Box{
                IconButton(onClick = {isMenuExpanded = !isMenuExpanded})
                {
                    Icon(imageVector = Icons.Filled.List, contentDescription = "", tint = Color.White)
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                        .background(Color.DarkGray)
                        .padding(end = 12.dp)
                ){
                    DropdownMenuItem(
                        text = { Text(text = "filter in ASC order") },
                        onClick = {scope.launch {
                            viewModel.getAllNotesASC()
                            isMenuExpanded = false
                        }}
                    )
                    DropdownMenuItem(
                        text = { Text(text = "filter by Date") },
                        onClick = {scope.launch {
                            viewModel.getAllNotesDateASC()
                            isMenuExpanded = false
                        }}
                    )
                }
            }

            IconButton(onClick = {
                scope.launch {
                    viewModel.clear()
                    Toast.makeText(context,"your notes list is cleared",Toast.LENGTH_SHORT).show()
                } }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "", tint = Color.White)
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
    viewModel: NotesViewModel
) {
    val scope = rememberCoroutineScope()
    val currentDate = remember {Date()}
    val dateFormat = SimpleDateFormat("M/d/yyyy", Locale.US)
    val formattedDate = dateFormat.format(currentDate)
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        var title by remember { mutableStateOf(noteTitle) }
        var content by remember { mutableStateOf(noteContent) }
        TextField(value = title, onValueChange = { title = it }, placeholder = { Text(text = "Note Title".toUpperCase(),fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 12.dp, end = 12.dp),
            textStyle = TextStyle(fontSize = 25.sp, color = Color.White,fontWeight = FontWeight.Bold),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.White,
                containerColor = Color.Black
            ))
        TextField(value = content, onValueChange = { content = it }, placeholder = { Text(text = "Enter your Note Here",fontSize = 20.sp, color = Color.White)}, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.90f)
            .padding(horizontal = 12.dp),
            textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor =  Color.Black,
                unfocusedIndicatorColor = Color.Black,
                cursorColor = Color.White,
                containerColor = Color.Black
            ))

        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                if (title.isNotEmpty() && content.isNotEmpty()){
                    scope.launch {
                        viewModel.updateNote(Notes(id = id ,title = title.toUpperCase(), content = content , date = formattedDate.toString()))
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
                    viewModel.deleteNote(Notes(id = id ,title = title.toUpperCase() , content = content , date = formattedDate.toString()))
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
fun AddNotesScreen(navController: NavHostController, viewModel: NotesViewModel) {
    val scope = rememberCoroutineScope()
    val currentDate = remember {Date()}
    val dateFormat = SimpleDateFormat("M/d/yyyy", Locale.US)
    val formattedDate = dateFormat.format(currentDate)
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(top = 24.dp)) {
            var title by remember { mutableStateOf(TextFieldValue()) }
            var content by remember { mutableStateOf(TextFieldValue()) }
            TextField(value = title, onValueChange = { title = it }, placeholder = { Text(text = "Note Title".toUpperCase(),fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)}, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 25.sp, color = Color.White,fontWeight = FontWeight.Bold),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Black,
                    cursorColor = Color.White,
                    containerColor = Color.Black
                ))
            TextField(value = content, onValueChange = { content = it }, placeholder = { Text(text = "Enter your Note Here",fontSize = 20.sp, color = Color.White)}, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor =Color.Black,
                    cursorColor = Color.White,
                    containerColor = Color.Black
                ))
            Button(onClick = {
                if(title.text.isNotEmpty() && content.text.isNotEmpty()){
                    scope.launch {
                        viewModel.insertNote(Notes(title = title.text.toUpperCase() , content = content.text , date = formattedDate.toString()))
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


package com.rkd.mytictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.rkd.mytictactoe.ui.theme.MyTicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTicTacToeTheme {
                Surface {
                    TTTScreen()
                }
            }
        }
    }
}

@Composable
fun TTTScreen() {

    val playerTurn= remember { mutableStateOf(true) }
  Header(playerTurn)
}

@Composable
fun Header(playerTurn: MutableState<Boolean>){

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTicTacToeTheme {
       // Greeting("Android")
    }
}
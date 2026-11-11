package com.rkd.mytictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rkd.mytictactoe.ui.theme.MyTicTacToeTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class Win {
    PLAYER,
    AI,
    DRAW
}

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
    val playerTurn = remember { mutableStateOf(true) }
    val win = remember { mutableStateOf<Win?>(null) }

    // Use mutableStateListOf so Compose detects changes to individual indices
    val moves = remember {
        mutableStateListOf<Boolean?>(null, null, null, null, null, null, null, null, null)
    }

    // AI turn logic runs whenever playerTurn changes to false and game is not finished
    LaunchedEffect(playerTurn.value, win.value) {
        if (!playerTurn.value && win.value == null) {
            delay(1000)

            // Gather all available indices
            val emptyIndices = moves.indices.filter { moves[it] == null }
            if (emptyIndices.isNotEmpty()) {
                val randomIndex = emptyIndices[Random.nextInt(emptyIndices.size)]
                moves[randomIndex] = false
                val currentWin = checkGame(moves)
                if (currentWin != null) {
                    win.value = currentWin
                } else {
                    playerTurn.value = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(16.dp))

        Header(playerTurn = playerTurn.value)

        Spacer(modifier = Modifier.height(30.dp))

        Board(
            moves = moves,
            onCellClick = { position ->
                if (playerTurn.value && moves[position] == null && win.value == null) {
                    moves[position] = true
                    val currentWin = checkGame(moves)
                    if (currentWin != null) {
                        win.value = currentWin
                    } else {
                        playerTurn.value = false
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (!playerTurn.value && win.value == null) {
            CircularProgressIndicator(color = Color.Red, modifier = Modifier.padding(16.dp))
        }

        if (win.value != null) {
            val winText = when (win.value) {
                Win.PLAYER -> "Player has won \uD83C\uDF89"
                Win.AI -> "AI has won \uD83D\uDE24"
                Win.DRAW -> "It's a draw \uD83D\uDE33"
                null -> ""
            }

            Text(
                text = winText,
                fontSize = 24.sp,
                modifier = Modifier.padding(12.dp)
            )

            Button(onClick = {
                for (i in 0..8) moves[i] = null
                win.value = null
                playerTurn.value = true
            }) {
                Text("Play Again")
            }
        }
    }
}

@Composable
fun Header(playerTurn: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val playerBoxColor = if (playerTurn) Color.Blue else Color.LightGray
        val aiBoxColor = if (!playerTurn) Color.Red else Color.LightGray

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(playerBoxColor)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Player", color = Color.White)
        }

        Spacer(modifier = Modifier.width(40.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(aiBoxColor)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "AI", color = Color.White)
        }
    }
}

@Composable
fun Board(moves: List<Boolean?>, onCellClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(24.dp)
            .background(Color.LightGray)
    ) {
        // Horizontal lines
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.Black)) {}
            Row(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.Black)) {}
        }

        // Vertical lines
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black)) {}
            Column(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black)) {}
        }

        // 3x3 Grid of clickable cells
        Column(modifier = Modifier.fillMaxSize()) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        val index = i * 3 + j
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onCellClick(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            getComposableFromMove(moves[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getComposableFromMove(move: Boolean?) {
    when (move) {
        true -> Image(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "X",
            modifier = Modifier.fillMaxSize(0.8f),
            colorFilter = ColorFilter.tint(Color.Blue)
        )
        false -> Image(
            painter = painterResource(id = R.drawable.ic_o),
            contentDescription = "O",
            modifier = Modifier.fillMaxSize(0.8f),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        null -> {}
    }
}

fun checkGame(m: List<Boolean?>): Win? {
    val winningCombinations = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
        listOf(0, 4, 8), listOf(2, 4, 6)             // Diagonals
    )

    for (combo in winningCombinations) {
        val (a, b, c) = combo
        if (m[a] != null && m[a] == m[b] && m[b] == m[c]) {
            return if (m[a] == true) Win.PLAYER else Win.AI
        }
    }

    return if (m.all { it != null }) Win.DRAW else null
}
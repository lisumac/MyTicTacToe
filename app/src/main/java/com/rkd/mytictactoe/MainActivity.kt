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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    // true = Player 1 (X), false = Player 2 (O)
    val playerTurn = remember { mutableStateOf(true) }

    // mutableStateListOf triggers recomposition when an item updates
    val moves = remember {
        mutableStateListOf<Boolean?>(null, null, null, null, null, null, null, null, null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tic Tac Toe", fontSize = 30.sp, modifier = Modifier.padding(16.dp))

        Header(playerTurn = playerTurn.value)

        Spacer(modifier = Modifier.height(40.dp))

        Board(
            moves = moves,
            onCellClick = { index ->
                if (moves[index] == null) {
                    moves[index] = playerTurn.value
                    playerTurn.value = !playerTurn.value // Alternates between true and false
                }
            }
        )
    }
}

@Composable
fun Header(playerTurn: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val p1Color = if (playerTurn) Color.Blue else Color.LightGray
        val p2Color = if (!playerTurn) Color.Red else Color.LightGray

        Box(
            modifier = Modifier
                .width(110.dp)
                .background(p1Color)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Player 1 (X)", color = Color.White)
        }

        Spacer(modifier = Modifier.width(30.dp))

        Box(
            modifier = Modifier
                .width(110.dp)
                .background(p2Color)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Player 2 (O)", color = Color.White)
        }
    }
}

@Composable
fun Board(moves: List<Boolean?>, onCellClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(32.dp)
            .background(Color.LightGray)
    ) {
        // Horizontal grid lines
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.Black)) {}
            Row(modifier = Modifier.height(2.dp).fillMaxWidth().background(Color.Black)) {}
        }

        // Vertical grid lines
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black)) {}
            Column(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.Black)) {}
        }

        // Clickable 3x3 cells
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
                            getComposebleFromMove(moves[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getComposebleFromMove(move: Boolean?) {
    when (move) {
        true -> Image(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "X",
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.Blue)
        )
        false -> Image(
            painter = painterResource(id = R.drawable.ic_o),
            contentDescription = "O",
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        null -> {}
    }
}
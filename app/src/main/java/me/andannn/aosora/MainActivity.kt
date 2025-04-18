package me.andannn.aosora

import android.graphics.Paint
import android.graphics.Paint.VERTICAL_TEXT_FLAG
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import me.andannn.aosora.ui.theme.AosoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AosoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    val text = "「春は、曙。1212121212121212」"
                    Box(
                        Modifier.padding(innerPadding).background(Color.White).fillMaxSize().drawWithContent {
                            drawIntoCanvas { canvas ->
                                val paint = Paint().apply { textSize = 64.sp.toPx() }
                                // Draw text vertically
                                paint.flags = paint.flags or VERTICAL_TEXT_FLAG
                                val height = paint.measureText(text)
                                canvas.nativeCanvas.drawText(
                                    text,
                                    0,
                                    text.length,
                                    size.width / 2,
                                    (size.height - height) / 2,
                                    paint
                                )
                            }
                        }
                    ) {}
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AosoraTheme {
        Greeting("Android")
    }
}
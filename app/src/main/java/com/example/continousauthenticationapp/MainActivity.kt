package com.example.continousauthenticationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.continousauthenticationapp.ui.theme.ContinousAuthenticationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContinousAuthenticationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataCollectionStatus()
                }
            }
        }
    }
}

@Composable
fun DataCollectionStatus() {
    var touchStatus by remember { mutableStateOf("Collecting Touch Data: Inactive") }
    var keystrokeStatus by remember { mutableStateOf("Collecting Keystroke Data: Inactive") }
    var powerStatus by remember { mutableStateOf("Collecting Power Data: Inactive") }

    Column {
        Text(text = touchStatus, modifier = Modifier.padding(16.dp))
        Text(text = keystrokeStatus, modifier = Modifier.padding(16.dp))
        Text(text = powerStatus, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DataCollectionStatusPreview() {
    ContinousAuthenticationAppTheme {
        DataCollectionStatus()
    }
}

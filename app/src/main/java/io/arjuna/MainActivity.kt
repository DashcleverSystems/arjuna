package io.arjuna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.arjuna.proto.BlockedWebsite
import io.arjuna.proto.BlockedWebsites
import io.arjuna.ui.theme.ArjunaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            applicationContext.blockedWebsitesStore.updateData { currentState: BlockedWebsites ->
                val xd = BlockedWebsite.newBuilder()
                    .setDomain("test")
                    .build()
                currentState.toBuilder()
                    .addWebsites(xd)
                    .build()
            }
        }
        enableEdgeToEdge()
        setContent {
            ArjunaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WebsitesBlockingCard()
                    }
                }
            }
        }
    }
}

@Composable
fun WebsitesBlockingCard() {
    Card {
        Text(text = "Here we will allow blocking any website")
    }
}
package io.arjuna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.arjuna.ui.theme.ArjunaTheme
import io.arjuna.viewmodel.SimpleViewModelFactory

class MainActivity : ComponentActivity() {

    private val blockedWebsitesViewModel by
    viewModels<BlockedWebsitesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            BlockedWebsitesViewModel(application.blockedWebsitesStore)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            ArjunaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding: PaddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>
                                by blockedWebsitesViewModel.blockedWebsites
                                    .collectAsState(emptySet())
                        BlockedWebsites(Modifier.padding(innerPadding), blockedWebsites)
                    }
                }
            }
        }
    }
}


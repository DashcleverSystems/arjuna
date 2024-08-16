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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import io.arjuna.proto.BlockedWebsites
import io.arjuna.ui.theme.ArjunaTheme
import io.arjuna.viewmodel.SimpleViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

            val blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>
                    by blockedWebsitesViewModel.blockedWebsites.collectAsState()

            ArjunaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        BlockedWebsitesCard(blockedWebsites)
                    }
                }
            }
        }
    }
}

@Composable
fun BlockedWebsitesCard(
    blockedWebsites: Set<BlockedWebsitesViewModel.BlockedWebsite>
) {
    blockedWebsites.forEach {
        Card {
            Text(text = it.mainDomain)
        }
    }
}

class BlockedWebsitesViewModel(private val dataStore: DataStore<BlockedWebsites>) :
    ViewModel() {

    data class BlockedWebsite(val mainDomain: String)

    private val _blockedWebsites = MutableStateFlow(emptySet<BlockedWebsite>())
    public val blockedWebsites: StateFlow<Set<BlockedWebsite>> = _blockedWebsites.asStateFlow()

    init {
    }
}
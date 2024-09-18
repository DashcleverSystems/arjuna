package io.arjuna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import io.arjuna.blockedwebsites.BlockedWebsiteRepository
import io.arjuna.blockedwebsites.BlockedWebsitesViewModel
import io.arjuna.blockedwebsites.blockedWebsitesStore
import io.arjuna.ui.theme.ArjunaTheme
import io.arjuna.viewmodel.SimpleViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    private val blockedWebsitesViewModel by
    viewModels<BlockedWebsitesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            val repository = BlockedWebsiteRepository(
                CoroutineScope(Dispatchers.Main),
                application.blockedWebsitesStore
            )
            BlockedWebsitesViewModel(repository)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            ArjunaTheme {
                ArjunaNavGraph(blockedWebsitesViewModel = blockedWebsitesViewModel)
            }
        }
    }
}


package io.arjuna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import io.arjuna.blockedwebsites.BlockedWebsitesViewModel
import io.arjuna.blockedwebsites.blockedWebsitesStore
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


        setContent {
            ArjunaTheme {
                ArjunaNavGraph(blockedWebsitesViewModel = blockedWebsitesViewModel)
            }
        }
    }
}


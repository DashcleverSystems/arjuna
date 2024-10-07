package io.arjuna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.arjuna.schedule.application.SchedulesViewModel
import io.arjuna.schedule.infra.proto.ScheduleRepository
import io.arjuna.schedule.infra.proto.schedulesStore
import io.arjuna.ui.theme.ArjunaTheme
import io.arjuna.viewmodel.SimpleViewModelFactory
import io.arjuna.websites.WebsiteRepository
import io.arjuna.websites.WebsitesViewModel
import io.arjuna.websites.blockedWebsitesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {

    private val websitesViewModel by
    viewModels<WebsitesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            val repository = WebsiteRepository(
                CoroutineScope(Dispatchers.Main),
                application.blockedWebsitesStore
            )
            WebsitesViewModel(repository)
        }
    })

    private val schedulesViewModel by
    viewModels<SchedulesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            val scheduleRepository = ScheduleRepository(
                CoroutineScope(Dispatchers.Main),
                application.schedulesStore
            )
            val websiteRepository = WebsiteRepository(
                CoroutineScope(Dispatchers.Main),
                application.blockedWebsitesStore
            )
            SchedulesViewModel(scheduleRepository, websiteRepository)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isWarningScreen = intent.getBooleanExtra("warn", false)
        val blockedUrl = intent.getStringExtra("url") ?: ""
        val warningDestination = ArjunaDestinations.WARN.replace(
            "{${DestinationArgs.WEBSITE_DOMAIN}}",
            blockedUrl.substringBefore("/")
        )

        setContent {
            ArjunaTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopBarComposable() }
                ) { innerPadding ->
                    ArjunaNavGraph(
                        websitesViewModel,
                        schedulesViewModel,
                        Modifier.padding(innerPadding),
                        startDestination = if (isWarningScreen) warningDestination else ArjunaDestinations.HOME
                    )
                }
            }
        }
    }
}


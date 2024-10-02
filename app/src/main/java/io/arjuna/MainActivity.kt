package io.arjuna

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.arjuna.blockedwebsites.BlockedWebsiteRepository
import io.arjuna.blockedwebsites.BlockedWebsitesViewModel
import io.arjuna.blockedwebsites.blockedWebsitesStore
import io.arjuna.schedule.application.SchedulesViewModel
import io.arjuna.schedule.infra.proto.ScheduleRepository
import io.arjuna.schedule.infra.proto.schedulesStore
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

    private val schedulesViewModel by
    viewModels<SchedulesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            val scheduleRepository = ScheduleRepository(
                CoroutineScope(Dispatchers.Main),
                application.schedulesStore
            )
            val websiteRepository = BlockedWebsiteRepository(
                CoroutineScope(Dispatchers.Main),
                application.blockedWebsitesStore
            )
            SchedulesViewModel(scheduleRepository, websiteRepository)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ArjunaTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp(30F)),
                    topBar = { TopBarComposable() }
                ) { innerPadding ->
                    ArjunaNavGraph(
                        blockedWebsitesViewModel,
                        schedulesViewModel,
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


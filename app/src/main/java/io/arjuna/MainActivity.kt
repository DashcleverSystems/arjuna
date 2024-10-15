package io.arjuna

import android.content.ComponentName
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.arjuna.appcheck.AppStatusService
import io.arjuna.schedule.application.SchedulesViewModel
import io.arjuna.schedule.infra.proto.ScheduleRepository
import io.arjuna.schedule.infra.proto.schedulesStore
import io.arjuna.ui.theme.ArjunaTheme
import io.arjuna.viewmodel.SimpleViewModelFactory
import io.arjuna.websites.UrlInterceptorService
import io.arjuna.websites.WebsiteRepository
import io.arjuna.websites.WebsitesViewModel
import io.arjuna.websites.blockedWebsitesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val DEFAULT_COROUTINE_SCOPE = CoroutineScope(SupervisorJob() + Dispatchers.Default)

class MainActivity : ComponentActivity() {

    private val websitesViewModel by
    viewModels<WebsitesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            val repository = WebsiteRepository(
                DEFAULT_COROUTINE_SCOPE,
                application.blockedWebsitesStore
            )
            WebsitesViewModel(repository)
        }
    })

    private val schedulesViewModel by
    viewModels<SchedulesViewModel>(factoryProducer = {
        SimpleViewModelFactory {
            val scheduleRepository = ScheduleRepository(
                DEFAULT_COROUTINE_SCOPE,
                application.schedulesStore
            )
            val websiteRepository = WebsiteRepository(
                DEFAULT_COROUTINE_SCOPE,
                application.blockedWebsitesStore
            )
            SchedulesViewModel(scheduleRepository, websiteRepository)
        }
    })

    private val appStatusService = AppStatusService { UrlInterceptorService::class.java }

    private var canBlockWebsites by mutableStateOf(false)

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
                        this.canBlockWebsites,
                        this.websitesViewModel,
                        this.schedulesViewModel,
                        Modifier.padding(innerPadding),
                        startDestination = if (isWarningScreen) warningDestination else ArjunaDestinations.HOME
                    )
                }
            }
        }
    }

    override fun onResume() {
        this.canBlockWebsites = appStatusService.isAllowedToBlockWebsites(this.application)
        super.onResume()
    }

}


package io.arjuna

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.arjuna.appcheck.AppStatusService
import io.arjuna.apps.InstalledAppsLoader
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
import kotlinx.coroutines.SupervisorJob
import java.lang.reflect.Method


val DEFAULT_COROUTINE_SCOPE = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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

    private val appStatusService by lazy {
        AppStatusService(application) { ActivityInterceptorService::class.java }
    }

    private var canBlockWebsites by mutableStateOf(false)

    private var canOperateFromBackground by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isWarningScreen = intent.getBooleanExtra("warn", false)
        val blockedUrl = intent.getStringExtra("url") ?: ""
        val warningDestination = ArjunaDestinations.WARN.replace(
            "{${DestinationArgs.WEBSITE_DOMAIN}}",
            blockedUrl.substringBefore("/")
        )

        setContent {
            val navController: NavHostController = rememberNavController()
            val navActions = NavActions(navController)
            ArjunaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBarComposable(navActions) },
                ) { innerPadding ->
                    ArjunaNavGraph(
                        this.canBlockWebsites,
                        this.canOperateFromBackground,
                        { this.goToXiaomiPermissionsSettings() },
                        this.websitesViewModel,
                        this.schedulesViewModel,
                        InstalledAppsLoader(this.baseContext.packageManager),
                        navController,
                        Modifier.padding(innerPadding),
                        startDestination = if (isWarningScreen) warningDestination else ArjunaDestinations.HOME
                    )
                }
            }
        }
    }

    override fun onResume() {
        this.canBlockWebsites =
            this.appStatusService.isAllowedToBlockWebsites()
        this.canOperateFromBackground = this.isBackgroundStartActivityPermissionGranted()
        super.onResume()
    }

}

private fun Context.isBackgroundStartActivityPermissionGranted(): Boolean {
    try {
        val mgr = this.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val m: Method = AppOpsManager::class.java.getMethod(
            "checkOpNoThrow",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType,
            String::class.java
        )
        val result =
            m.invoke(mgr, OP_BACKGROUND_START_ACTIVITY, Process.myUid(), this.packageName) as Int
        return result == AppOpsManager.MODE_ALLOWED
    } catch (e: Exception) {
        Log.d("Exception", e.toString())
    }
    return true
}

private fun Context.goToXiaomiPermissionsSettings() {
    val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
    intent.setClassName(
        "com.miui.securitycenter",
        "com.miui.permcenter.permissions.PermissionsEditorActivity"
    )
    intent.putExtra("extra_pkgname", this.packageName)
    startActivity(intent)
}

const val OP_BACKGROUND_START_ACTIVITY = 10021


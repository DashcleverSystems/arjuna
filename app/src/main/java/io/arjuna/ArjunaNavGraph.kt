package io.arjuna

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.arjuna.schedule.application.SchedulesViewModel
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.view.ScheduleDetails
import io.arjuna.schedule.view.ScheduleDetailsState
import io.arjuna.schedule.view.SchedulesOverview
import io.arjuna.websites.Website
import io.arjuna.websites.WebsitesViewModel
import java.util.UUID

@Composable
fun ArjunaNavGraph(
    isAppAllowedToBlock: Boolean,
    websitesViewModel: WebsitesViewModel,
    schedulesViewModel: SchedulesViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ArjunaDestinations.HOME,
) {

    val navActions = NavActions(navController)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(ArjunaDestinations.HOME) {
            val websites by websitesViewModel.websites
                .collectAsState(initial = emptySet())
            val schedules by schedulesViewModel.schedules.collectAsState(initial = emptySet())
            val homeElementsModifier = Modifier.fillMaxWidth(0.8f).padding(5.dp)
            HomeComposable(
                homeElementsModifier,
                isAppAllowedToBlock,
                websites,
                onWebsiteRemove = { websiteToRemove: Website ->
                    websitesViewModel.removeWebsiteToBlock(websiteToRemove)
                },
                onWebsiteAdd = { websiteToBlock: String? ->
                    if (websiteToBlock != null) {
                        websitesViewModel.addWebsiteToBlock(
                            Website(mainDomain = websiteToBlock)
                        )
                    }
                },
                schedulesOverviewProvider = { modifier ->
                    SchedulesOverview(
                        modifier,
                        schedules,
                        onScheduleClick = { navActions.navigateToScheduleDetails(it.identifier) },
                        onAddButtonClick = { navActions.navigateToScheduleCreation() }
                    )
                }
            )
        }
        composable(ArjunaDestinations.SCHEDULE_DETAILS) { backStackEntry ->
            val websites by schedulesViewModel.websites.collectAsState(initial = emptySet())
            val scheduleId = backStackEntry.arguments
                ?.getUUID(DestinationArgs.SCHEDULE_ID)
                ?.let { Schedule.Id(it) }
            if (scheduleId != null) {
                val schedule: Schedule? by schedulesViewModel.findScheduleById(scheduleId)
                    .collectAsState(initial = null)
                schedule?.let {
                    val state =
                        ScheduleDetailsState(
                            it.websites + websites,
                            it.name,
                            it.websites,
                            it.from,
                            it.to,
                            it.onDays
                        )
                    ScheduleDetails(state) {
                        schedulesViewModel.save(state.writeTo(schedule))
                        navActions.navigateToHome()
                    }
                }
            } else {
                val state = ScheduleDetailsState(websites)
                ScheduleDetails(state) {
                    schedulesViewModel.save(state.writeTo())
                    navActions.navigateToHome()
                }
            }
        }
        composable(ArjunaDestinations.WARN) { backStackEntry ->
            val websiteDomainName = backStackEntry.arguments
                ?.getString(DestinationArgs.WEBSITE_DOMAIN)
                ?: ""
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(Modifier.align(Alignment.Center)) {
                    Text(
                        "Website you've tried to visit was blocked by you!",
                        Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        websiteDomainName,
                        Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

object ArjunaDestinations {
    const val HOME = "/${Screens.HOME}"
    const val SCHEDULE_DETAILS =
        "/${Screens.SCHEDULES}/{${DestinationArgs.SCHEDULE_ID}}"
    const val WARN = "/${Screens.WARN}/{${DestinationArgs.WEBSITE_DOMAIN}}"

}

object DestinationArgs {
    const val SCHEDULE_ID = "SCHEDULE_ID"
    const val WEBSITE_DOMAIN = "WEBSITE_DOMAIN"
}

object Screens {
    const val HOME = "home"
    const val SCHEDULES = "schedules"
    const val WARN = "warn"
}

class NavActions(private val navController: NavController) {

    fun navigateToScheduleDetails(id: Schedule.Id) {
        navController.navigate(
            ArjunaDestinations.SCHEDULE_DETAILS.replace(
                "{${DestinationArgs.SCHEDULE_ID}}",
                id.uuid.toString()
            )
        )
    }

    fun navigateToScheduleCreation() {
        navController.navigate(ArjunaDestinations.SCHEDULE_DETAILS)
    }

    fun navigateToHome() {
        navController.navigate(ArjunaDestinations.HOME)
    }

    fun navigateToWarn(websiteDomain: String) {
        navController.navigate(
            ArjunaDestinations.WARN.replace(
                "{${DestinationArgs.WEBSITE_DOMAIN}}",
                websiteDomain
            )
        )
    }
}

private fun Bundle.getUUID(arg: String): UUID? = try {
    UUID.fromString(this.getString(arg))
} catch (_: Exception) {
    null
}

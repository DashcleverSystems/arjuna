package io.arjuna

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.arjuna.appcheck.Warn
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.view.SchedulesOverview
import io.arjuna.websites.Website
import io.arjuna.websites.Websites

private val HOME_ELEMENTS_BASE_MODIFIER = Modifier
    .fillMaxWidth(0.8f)
    .padding(5.dp)

@Composable
fun HomeComposable(
    isAppAllowedToBlock: Boolean,
    canOperateFromBackground: Boolean,
    ifCanNotOperateFromBackground: () -> Unit,
    websites: Set<Website>,
    onWebsiteRemove: (Website) -> Unit,
    onWebsiteAdd: (Website) -> Unit,
    schedules: Set<Schedule>,
    navActions: NavActions,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isAppAllowedToBlock.not()) {
            Warn(
                HOME_ELEMENTS_BASE_MODIFIER.align(Alignment.CenterHorizontally),
                "Please go to accessibility settings and allow Arjuna"
            )
        }
        if (canOperateFromBackground.not()) {
            Warn(
                HOME_ELEMENTS_BASE_MODIFIER
                    .align(Alignment.CenterHorizontally)
                    .clickable { ifCanNotOperateFromBackground() },
                """
                 Please allow Arjuna to operate from background. 
                 This requires to allow Arjuna to open new windows while running in the background   
                """.trimIndent()
            )
        }
        Websites(
            HOME_ELEMENTS_BASE_MODIFIER.align(Alignment.CenterHorizontally),
            websites = websites,
            onWebsiteRemove = { onWebsiteRemove(it) },
            onWebsiteAdd = { onWebsiteAdd(it) }
        )
        SchedulesOverview(
            HOME_ELEMENTS_BASE_MODIFIER.align(Alignment.CenterHorizontally),
            schedules,
            onScheduleClick = { navActions.navigateToScheduleDetails(it.identifier) },
            onAddButtonClick = { navActions.navigateToScheduleCreation() }
        )
    }
}
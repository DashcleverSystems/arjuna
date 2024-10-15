package io.arjuna.schedule.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.arjuna.composables.OutlinedCard
import io.arjuna.composables.OptionalContent
import io.arjuna.schedule.domain.Schedule

@Composable
fun SchedulesOverview(
    modifier: Modifier,
    schedules: Set<Schedule>,
    onScheduleClick: (Schedule) -> Unit,
    onAddButtonClick: () -> Unit
) {
    ElevatedCard(modifier) {
        Text(
            "Schedules",
            Modifier.padding(4.dp),
            fontWeight = FontWeight.Bold
        )
        OptionalContent(schedules,
            onEmptyContent = {
                OutlinedCard(Modifier.align(Alignment.CenterHorizontally)) {
                    ScheduleNameText("Nothing to protect from yet!")
                }
            },
            elementComposable = {
                ScheduleDetails(
                    Modifier.align(Alignment.CenterHorizontally),
                    it,
                    onScheduleClick
                )
            }
        )
        SmallFloatingActionButton(
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .padding(end = 20.dp, bottom = 10.dp)
                .align(Alignment.End),
            onClick = onAddButtonClick
        ) {
            Icon(Icons.Sharp.Add, "Adds website to block")
        }
    }
}

@Composable
private fun ScheduleDetails(modifier: Modifier, schedule: Schedule, onClick: (Schedule) -> Unit) {
    OutlinedCard(modifier.clickable { onClick.invoke(schedule) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            ScheduleNameText(schedule.name)
        }
    }
}

@Composable
private fun ScheduleNameText(scheduleName: String) {
    Text(
        scheduleName,
        Modifier
            .padding(6.dp)
            .fillMaxWidth(0.7f)
    )
}

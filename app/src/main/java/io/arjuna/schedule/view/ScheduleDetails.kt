package io.arjuna.schedule.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import io.arjuna.blockedwebsites.BlockedWebsite
import io.arjuna.composables.MainElevatedCardModifier
import io.arjuna.composables.OutlinedCard
import io.arjuna.composables.OutlinedCards
import io.arjuna.schedule.domain.Hour
import io.arjuna.schedule.domain.Minute
import io.arjuna.schedule.domain.Schedule
import io.arjuna.schedule.domain.Time
import io.arjuna.schedule.domain.Weekday
import io.arjuna.schedule.domain.with

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetails(
    state: ScheduleDetailsState = ScheduleDetailsState(),
    onClose: () -> Unit = {}
) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            state.name,
            { state.name = it },
            MainElevatedCardModifier,
            label = { Text("Schedule name") }
        )

        ElevatedCard(MainElevatedCardModifier) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Websites", Modifier.padding(6.dp), fontWeight = FontWeight.Bold)
                OutlinedCards(
                    elements = state.websites,
                    elementComposable = { website ->
                        val isLocked = state.selectedWebsites.any { it == website.identifier }
                        WebsiteName(
                            website,
                            isLocked,
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .clickable(
                                    onClick = {
                                        if (isLocked) state.selectedWebsites -= website.identifier
                                        else state.selectedWebsites += website.identifier
                                    }
                                )
                        )
                    }
                )
            }
        }
        val fromTimePickerState =
            TimePickerState(
                initialHour = state.from.hour.value,
                initialMinute = state.from.minute.value,
                is24Hour = true
            )
        val toTimePickerState =
            TimePickerState(
                initialHour = state.to.hour.value,
                initialMinute = state.to.minute.value,
                is24Hour = true
            )
        ElevatedCard(MainElevatedCardModifier) {
            Column(Modifier.fillMaxWidth()) {
                Text("Lock time", Modifier.padding(6.dp), fontWeight = FontWeight.Bold)
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    "From", Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                TimeInput(
                    fromTimePickerState,
                    Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally)
                        .onFocusChanged {
                            state.from =
                                fromTimePickerState.hour.Hour() with fromTimePickerState.minute.Minute()
                        }
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    "To", Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                TimeInput(
                    toTimePickerState,
                    Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally)
                        .onFocusChanged {
                            state.to =
                                toTimePickerState.hour.Hour() with toTimePickerState.minute.Minute()
                        }
                )
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    "Days", Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                MultiChoiceSegmentedButtonRow(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(4.dp)
                ) {
                    Weekday.entries.forEachIndexed { index, day ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = Weekday.entries.size
                            ),
                            onCheckedChange = {
                                if (it) state.selectedDays += day
                                else state.selectedDays -= day
                            },
                            checked = day in state.selectedDays,
                        ) {
                            Text(
                                day.abbreviation,
                                fontSize = 1.5.em
                            )
                        }
                    }
                }
            }
        }
        SmallFloatingActionButton(
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .align(Alignment.CenterHorizontally),
            onClick = onClose
        ) {
            Icon(Icons.Sharp.Done, "Adds website to block")
        }
    }
}

@Composable
private fun WebsiteName(website: BlockedWebsite, isSelected: Boolean, modifier: Modifier) {
    val colors = if (isSelected)
        CardDefaults.outlinedCardColors()
            .copy(containerColor = MaterialTheme.colorScheme.secondary)
    else
        CardDefaults.outlinedCardColors()
    OutlinedCard(modifier, colors) {
        if (isSelected) {
            Text(
                website.mainDomain,
                Modifier
                    .padding(6.dp)
                    .fillMaxWidth(0.7f),
            )
        } else {
            Text(
                website.mainDomain,
                Modifier
                    .padding(6.dp)
                    .fillMaxWidth(0.7f),
            )
        }
    }
}

private val Weekday.abbreviation: String
    get() = when (this) {
        Weekday.MONDAY -> "MN"
        Weekday.TUESDAY -> "TE"
        Weekday.WEDNESDAY -> "WE"
        Weekday.THURSDAY -> "TH"
        Weekday.FRIDAY -> "FR"
        Weekday.SATURDAY -> "ST"
        Weekday.SUNDAY -> "SN"
    }

class ScheduleDetailsState(
    val websites: Set<BlockedWebsite> = emptySet(),
    initialName: String = "Schedule lock",
    initialSelectedWebsites: Set<BlockedWebsite.Id> = emptySet(),
    initialFrom: Time = Hour(7) with Minute(0),
    initialTo: Time = Hour(17) with Minute(0),
    initialSelectedDays: Set<Weekday> = emptySet()
) {
    var name by mutableStateOf(initialName)
    var selectedWebsites by mutableStateOf(initialSelectedWebsites)
    var from by mutableStateOf(initialFrom)
    var to by mutableStateOf(initialTo)
    var selectedDays by mutableStateOf(initialSelectedDays)

    fun writeTo(schedule: Schedule? = null): Schedule = schedule?.apply {
        this.name = this@ScheduleDetailsState.name
        this.websites = this@ScheduleDetailsState.selectedWebsites
        this.from = this@ScheduleDetailsState.from
        this.to = this@ScheduleDetailsState.to
        this.onDays = this@ScheduleDetailsState.selectedDays
    } ?: Schedule(
        name = this.name,
        websites = this.selectedWebsites,
        from = this.from,
        to = this.to,
        onDays = this.selectedDays
    )
}
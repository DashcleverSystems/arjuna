package io.arjuna

import io.arjuna.schedule.domain.Hour
import io.arjuna.schedule.domain.Minute
import io.arjuna.schedule.domain.Time
import io.arjuna.schedule.domain.isWithin
import io.arjuna.schedule.domain.with
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.LocalTime

@RunWith(Parameterized::class)
internal class LocalTimeWithinDurationExtensionsTest(
    private val time: LocalTime,
    private val duration: Pair<Time, Time>,
    private val isTimeWithinDuration: Boolean
) {

    @Test
    fun test() {
        // when
        val result = time isWithin duration

        // then
        assertEquals(result, isTimeWithinDuration)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(
            name = "{0} isWithin should be: {3}, given duration: {1}"
        )
        fun params(): Iterable<Array<Any>> {
            val duration = (8.Hour() with 0.Minute()) to (10.Hour() with 0.Minute())
            return arrayListOf(
                arrayOf(
                    LocalTime.of(8, 25),
                    duration,
                    true
                ),
                arrayOf(
                    LocalTime.of(8, 0),
                    duration,
                    true
                ),
                arrayOf(
                    LocalTime.of(7, 59),
                    duration,
                    false
                ),
                arrayOf(
                    LocalTime.of(10, 59),
                    duration,
                    false
                ),
                arrayOf(
                    LocalTime.of(10, 0),
                    duration,
                    true
                )
            )
        }

    }
}

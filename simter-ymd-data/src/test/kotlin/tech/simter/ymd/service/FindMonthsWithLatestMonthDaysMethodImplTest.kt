package tech.simter.ymd.service

import com.nhaarman.mockito_kotlin.any
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.dao.YmdDao
import java.time.Month

@SpringJUnitConfig(YmdServiceImpl::class)
@MockBean(YmdDao::class)
class FindMonthsWithLatestMonthDaysMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing because empty months`() {
    // mock data
    val type = randomString()
    val year = randomInt(1900, 3000)
    `when`(dao.findMonths(type, year)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(
      service.findMonthsWithLatestMonthDays(type, year)
    ).verifyComplete()
    verify(dao).findMonths(type, year)
    verify(dao, times(0)).findDays(any(), any(), any())
  }

  @Test
  fun `Found with latest month days`() {
    // mock data
    val type = randomString()
    val year = randomInt(1900, 3000)
    val latestMonth = Month.DECEMBER
    val latestMonthDays = (latestMonth.maxLength() downTo 1).map { it }
    val months = (latestMonth.value downTo 1).map { it }
    `when`(dao.findMonths(type, year)).thenReturn(Flux.fromIterable(months))
    `when`(dao.findDays(type, year, latestMonth.value)).thenReturn(Flux.fromIterable(latestMonthDays))

    // invoke and verify
    StepVerifier.create(
      service.findMonthsWithLatestMonthDays(type, year).collectList()
    ).consumeNextWith {
      assertEquals(months.size, it.size)
      it.forEachIndexed { monthIndex, actualMonth ->
        val expectedMonth = months[monthIndex]
        assertEquals(expectedMonth, actualMonth.month)
        if (monthIndex == 0) { // with latest days
          assertEquals(latestMonthDays.size, actualMonth.days!!.size)
          actualMonth.days!!.forEachIndexed { dayIndex, actualDay ->
            assertEquals(latestMonthDays[dayIndex], actualDay)
          }
        } else assertNull(actualMonth.days) // no days
      }
    }.verifyComplete()
    verify(dao).findMonths(type, year)
    verify(dao).findDays(type, year, latestMonth.value)
  }

  @Test
  fun `Found with months but no latest month days`() {
    // mock data
    val type = randomString()
    val year = randomInt(1900, 3000)
    val latestMonth = Month.DECEMBER
    val months = (latestMonth.value downTo 1).map { it }
    `when`(dao.findMonths(type, year)).thenReturn(Flux.fromIterable(months))
    `when`(dao.findDays(type, year, latestMonth.value)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(
      service.findMonthsWithLatestMonthDays(type, year).collectList()
    ).consumeNextWith {
      assertEquals(months.size, it.size)
      it.forEachIndexed { monthIndex, actualMonth ->
        val expectedMonth = months[monthIndex]
        assertEquals(expectedMonth, actualMonth.month)
        if (monthIndex == 0) {              // with latest days
          assertNull(actualMonth.days)
        } else assertNull(actualMonth.days) // no days
      }
    }.verifyComplete()
    verify(dao).findMonths(type, year)
    verify(dao).findDays(type, year, latestMonth.value)
  }
}
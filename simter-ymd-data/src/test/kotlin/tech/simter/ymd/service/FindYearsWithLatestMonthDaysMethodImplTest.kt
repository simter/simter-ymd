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
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.dao.YmdDao
import java.time.Month
import java.time.Year

@SpringJUnitConfig(YmdServiceImpl::class)
@MockBean(YmdDao::class)
class FindYearsWithLatestMonthDaysMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing because empty years`() {
    // mock data
    val type = randomString()
    `when`(dao.findYears(type)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(service.findYearsWithLatestMonthDays(type)).verifyComplete()
    verify(dao).findYears(type)
    verify(dao, times(0)).findMonths(any(), any())
  }

  @Test
  fun `Found with latest year months and latest month days`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val latestMonth = Month.DECEMBER
    val latestYearMonths = (latestMonth.value downTo 1).map { it }
    val latestMonthDays = (latestMonth.maxLength() downTo 1).map { it }
    val years = (latestYear.value downTo 2000).map { it }
    `when`(dao.findYears(type)).thenReturn(Flux.fromIterable(years))
    `when`(dao.findMonths(type, latestYear.value)).thenReturn(Flux.fromIterable(latestYearMonths))
    `when`(dao.findDays(type, latestYear.value, latestMonth.value)).thenReturn(Flux.fromIterable(latestMonthDays))

    // invoke and verify
    StepVerifier.create(
      service.findYearsWithLatestMonthDays(type).collectList()
    ).consumeNextWith {
      assertEquals(years.size, it.size)
      it.forEachIndexed { yearIndex, actualYear ->
        val expectedYear = years[yearIndex]
        assertEquals(expectedYear, actualYear.year)
        if (yearIndex == 0) {                // with latest year months
          assertEquals(latestYearMonths.size, actualYear.months!!.size)
          actualYear.months!!.forEachIndexed { monthIndex, actualMonth ->
            assertEquals(latestYearMonths[monthIndex], actualMonth.month)
            if (monthIndex == 0) {                 // with latest month days
              assertEquals(latestMonthDays.size, actualMonth.days!!.size)
              actualMonth.days!!.forEachIndexed { dayIndex, actualDay ->
                assertEquals(latestMonthDays[dayIndex], actualDay)
              }
            } else assertNull(actualMonth.days)    // no days
          }
        } else assertNull(actualYear.months) // no months
      }
    }.verifyComplete()
    verify(dao).findYears(type)
    verify(dao).findMonths(type, latestYear.value)
    verify(dao).findDays(type, latestYear.value, latestMonth.value)
  }

  @Test
  fun `Found with latest year months but no latest month days`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val latestMonth = Month.DECEMBER
    val latestYearMonths = (latestMonth.value downTo 1).map { it }
    val years = (latestYear.value downTo 2000).map { it }
    `when`(dao.findYears(type)).thenReturn(Flux.fromIterable(years))
    `when`(dao.findMonths(type, latestYear.value)).thenReturn(Flux.fromIterable(latestYearMonths))
    `when`(dao.findDays(type, latestYear.value, latestMonth.value)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(
      service.findYearsWithLatestMonthDays(type).collectList()
    ).consumeNextWith {
      assertEquals(years.size, it.size)
      it.forEachIndexed { yearIndex, actualYear ->
        val expectedYear = years[yearIndex]
        assertEquals(expectedYear, actualYear.year)
        if (yearIndex == 0) {                // with latest year months
          assertEquals(latestYearMonths.size, actualYear.months!!.size)
          actualYear.months!!.forEachIndexed { monthIndex, actualMonth ->
            assertEquals(latestYearMonths[monthIndex], actualMonth.month)
            assertNull(actualMonth.days)     // no days
          }
        } else assertNull(actualYear.months) // no months
      }
    }.verifyComplete()
    verify(dao).findYears(type)
    verify(dao).findMonths(type, latestYear.value)
    verify(dao).findDays(type, latestYear.value, latestMonth.value)
  }

  @Test
  fun `Found with years but no latest year months`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val years = (latestYear.value downTo 2000).map { it }
    `when`(dao.findYears(type)).thenReturn(Flux.fromIterable(years))
    `when`(dao.findMonths(type, latestYear.value)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(
      service.findYearsWithLatestMonthDays(type).collectList()
    ).consumeNextWith {
      assertEquals(years.size, it.size)
      it.forEachIndexed { yearIndex, actualYear ->
        assertEquals(years[yearIndex], actualYear.year)
        assertNull(actualYear.months) // no months
      }
    }.verifyComplete()
    verify(dao).findYears(type)
    verify(dao).findMonths(type, latestYear.value)
  }
}
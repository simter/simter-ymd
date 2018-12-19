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
class FindYearsWithLatestYearMonthsMethodImplTest @Autowired constructor(
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing because empty years`() {
    // mock data
    val type = randomString()
    `when`(dao.findYears(type)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(service.findYearsWithLatestYearMonths(type)).verifyComplete()
    verify(dao).findYears(type)
    verify(dao, times(0)).findMonths(any(), any())
  }

  @Test
  fun `Found with latest year months`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val latestYearMonths = (12 downTo 1).map { Month.of(it) }
    val years = (latestYear.value downTo 2000).map { Year.of(it) }
    `when`(dao.findYears(type)).thenReturn(Flux.fromIterable(years))
    `when`(dao.findMonths(type, latestYear)).thenReturn(Flux.fromIterable(latestYearMonths))

    // invoke and verify
    StepVerifier.create(
      service.findYearsWithLatestYearMonths(type).collectList()
    ).consumeNextWith {
      assertEquals(years.size, it.size)
      it.forEachIndexed { yearIndex, actualYear ->
        val expectedYear = years[yearIndex]
        assertEquals(expectedYear.value, actualYear.year)
        if (yearIndex == 0) {               // with latest months
          assertEquals(latestYearMonths.size, actualYear.months!!.size)
          actualYear.months!!.forEachIndexed { monthIndex, actualMonth ->
            assertEquals(latestYearMonths[monthIndex].value, actualMonth)
          }
        } else assertNull(actualYear.months) // no months
      }
    }.verifyComplete()
    verify(dao).findYears(type)
    verify(dao).findMonths(type, latestYear)
  }

  @Test
  fun `Found with years but no latest year months`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val years = (latestYear.value downTo 2000).map { Year.of(it) }
    `when`(dao.findYears(type)).thenReturn(Flux.fromIterable(years))
    `when`(dao.findMonths(type, latestYear)).thenReturn(Flux.empty())

    // invoke and verify
    StepVerifier.create(
      service.findYearsWithLatestYearMonths(type).collectList()
    ).consumeNextWith {
      assertEquals(years.size, it.size)
      it.forEachIndexed { yearIndex, actualYear ->
        assertEquals(years[yearIndex].value, actualYear.year)
        assertNull(actualYear.months) // no months
      }
    }.verifyComplete()
    verify(dao).findYears(type)
    verify(dao).findMonths(type, latestYear)
  }
}
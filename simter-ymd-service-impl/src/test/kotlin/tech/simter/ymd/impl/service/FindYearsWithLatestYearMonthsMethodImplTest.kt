package tech.simter.ymd.impl.service

import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.OPERATION_READ
import tech.simter.ymd.UnitTestConfiguration
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService
import java.time.Year

/**
 * Test [YmdServiceImpl.findYearsWithLatestYearMonths].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindYearsWithLatestYearMonthsMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing because empty years`() {
    // mock data
    val type = randomString()
    every { dao.findYears(type) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findYearsWithLatestYearMonths(type).test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findYears(type)
    }
    verify(exactly = 0) { dao.findMonths(any(), any()) }
  }

  @Test
  fun `Found with latest year months`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val latestYearMonths = (12 downTo 1).map { it }
    val years = (latestYear.value downTo 2000).map { it }
    every { dao.findYears(type) } returns Flux.fromIterable(years)
    every { dao.findMonths(type, latestYear.value) } returns Flux.fromIterable(latestYearMonths)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findYearsWithLatestYearMonths(type).collectList()
      .test().consumeNextWith {
        assertEquals(years.size, it.size)
        it.forEachIndexed { yearIndex, actualYear ->
          val expectedYear = years[yearIndex]
          assertEquals(expectedYear, actualYear.year)
          if (yearIndex == 0) {               // with latest months
            assertEquals(latestYearMonths.size, actualYear.months!!.size)
            actualYear.months!!.forEachIndexed { monthIndex, actualMonth ->
              assertEquals(latestYearMonths[monthIndex], actualMonth)
            }
          } else assertNull(actualYear.months) // no months
        }
      }.verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findYears(type)
      dao.findMonths(type, latestYear.value)
    }
  }

  @Test
  fun `Found with years but no latest year months`() {
    // mock data
    val type = randomString()
    val latestYear = Year.of(2010)
    val years = (latestYear.value downTo 2000).map { it }
    every { dao.findYears(type) } returns Flux.fromIterable(years)
    every { dao.findMonths(type, latestYear.value) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findYearsWithLatestYearMonths(type).collectList()
      .test().consumeNextWith {
        assertEquals(years.size, it.size)
        it.forEachIndexed { yearIndex, actualYear ->
          assertEquals(years[yearIndex], actualYear.year)
          assertNull(actualYear.months) // no months
        }
      }.verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findYears(type)
      dao.findMonths(type, latestYear.value)
    }
  }
}
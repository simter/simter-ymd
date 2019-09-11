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
import reactor.test.test
import tech.simter.reactive.security.ModuleAuthorizer
import tech.simter.util.RandomUtils.randomInt
import tech.simter.util.RandomUtils.randomString
import tech.simter.ymd.OPERATION_READ
import tech.simter.ymd.UnitTestConfiguration
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.core.YmdService
import java.time.Month

/**
 * Test [YmdServiceImpl.findMonthsWithLatestMonthDays].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
class FindMonthsWithLatestMonthDaysMethodImplTest @Autowired constructor(
  private val moduleAuthorizer: ModuleAuthorizer,
  private val dao: YmdDao,
  private val service: YmdService
) {
  @Test
  fun `Found nothing because empty months`() {
    // mock data
    val type = randomString()
    val year = randomInt(1900, 3000)
    every { dao.findMonths(type, year) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findMonthsWithLatestMonthDays(type, year)
      .test().verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findMonths(type, year)
    }
    verify(exactly = 0) { dao.findDays(any(), any(), any()) }
  }

  @Test
  fun `Found with latest month days`() {
    // mock data
    val type = randomString()
    val year = randomInt(1900, 3000)
    val latestMonth = Month.DECEMBER
    val latestMonthDays = (latestMonth.maxLength() downTo 1).map { it }
    val months = (latestMonth.value downTo 1).map { it }
    every { dao.findMonths(type, year) } returns Flux.fromIterable(months)
    every { dao.findDays(type, year, latestMonth.value) } returns Flux.fromIterable(latestMonthDays)
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findMonthsWithLatestMonthDays(type, year).collectList()
      .test().consumeNextWith {
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
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findMonths(type, year)
      dao.findDays(type, year, latestMonth.value)
    }
  }

  @Test
  fun `Found with months but no latest month days`() {
    // mock data
    val type = randomString()
    val year = randomInt(1900, 3000)
    val latestMonth = Month.DECEMBER
    val months = (latestMonth.value downTo 1).map { it }
    every { dao.findMonths(type, year) } returns Flux.fromIterable(months)
    every { dao.findDays(type, year, latestMonth.value) } returns Flux.empty()
    every { moduleAuthorizer.verifyHasPermission(OPERATION_READ) } returns Mono.empty()

    // invoke and verify
    service.findMonthsWithLatestMonthDays(type, year).collectList()
      .test().consumeNextWith {
        assertEquals(months.size, it.size)
        it.forEachIndexed { monthIndex, actualMonth ->
          val expectedMonth = months[monthIndex]
          assertEquals(expectedMonth, actualMonth.month)
          if (monthIndex == 0) {              // with latest days
            assertNull(actualMonth.days)
          } else assertNull(actualMonth.days) // no days
        }
      }.verifyComplete()
    verify(exactly = 1) {
      moduleAuthorizer.verifyHasPermission(OPERATION_READ)
      dao.findMonths(type, year)
      dao.findDays(type, year, latestMonth.value)
    }
  }
}
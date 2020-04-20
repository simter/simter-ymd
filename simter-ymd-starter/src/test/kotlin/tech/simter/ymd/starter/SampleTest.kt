package tech.simter.ymd.starter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

/**
 * @author RJ
 */
class SampleTest {
  private val logger = LoggerFactory.getLogger(SampleTest::class.java)

  @Test
  fun test() {
    logger.debug("test log config")
    assertThat(1 + 1).isEqualTo(2)
  }
}
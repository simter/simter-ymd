package tech.simter.ymd.impl.dao.r2dbc

import io.r2dbc.spi.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.ImmutableYmd
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.clean
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.create].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@DataR2dbcTest
class CreateMethodImplTest @Autowired constructor(
  private val client: DatabaseClient,
  private val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    clean(client = client)
  }

  @Test
  fun `Create nothing`() {
    dao.create().test().verifyComplete()
    client.sql("select count(*) from $TABLE_YMD")
      .map { row -> row.get(0, Long::class.javaObjectType) }
      .one()
      .test()
      .expectNext(0).verifyComplete()
  }

  @Test
  fun `Create one`() {
    // init data
    val ymd = randomYmd()

    // save it
    dao.create(ymd).test().verifyComplete()

    // verify saved
    client.sql("select * from $TABLE_YMD where t = :t")
      .bind("t", ymd.type)
      .map(this::rowMapper4Ymd)
      .one()
      .test()
      .assertNext { assertThat(ymd).usingRecursiveComparison().isEqualTo(it) }
      .verifyComplete()
  }

  @Test
  fun `Create two`() {
    // init data
    val ymd1 = randomYmd(year = 2000)
    val ymd2 = randomYmd(year = 2001)

    // save them
    dao.create(ymd1, ymd2).test().verifyComplete()

    // verify saved
    client.sql("select * from $TABLE_YMD order by y asc")
      .map(this::rowMapper4Ymd)
      .all()
      .test()
      .assertNext { assertThat(ymd1).usingRecursiveComparison().isEqualTo(it) }
      .assertNext { assertThat(ymd2).usingRecursiveComparison().isEqualTo(it) }
      .verifyComplete()
  }

  private fun rowMapper4Ymd(row: Row): Ymd {
    return ImmutableYmd(
      type = row.get("t", String::class.java)!!,
      year = row.get("y", Short::class.javaObjectType)!!.toInt(),
      month = row.get("m", Short::class.javaObjectType)!!.toInt(),
      day = row.get("d", Short::class.javaObjectType)!!.toInt()
    )
  }
}
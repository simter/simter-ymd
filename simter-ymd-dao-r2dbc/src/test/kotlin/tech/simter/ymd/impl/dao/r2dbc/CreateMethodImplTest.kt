package tech.simter.ymd.impl.dao.r2dbc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.isEquals
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.kotlin.test.test
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.r2dbc.TestHelper.clean
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.create].
 *
 * @author RJ
 */
@SpringBootTest(classes = [UnitTestConfiguration::class])
@ExtendWith(SpringExtension::class)
class CreateMethodImplTest @Autowired constructor(
  private val client: DatabaseClient,
  val dao: YmdDao
) {
  @BeforeEach
  fun clean() {
    clean(client = client)
  }

  @Test
  fun `Create nothing`() {
    dao.create().test().verifyComplete()
    client.execute("select count(*) from $TABLE_YMD")
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
    client.select()
      .from(TABLE_YMD)
      .matching(where("t").isEquals(ymd.type))
      .`as`(YmdPo::class.java)
      .fetch()
      .one()
      .map { it as Ymd }
      .test()
      .assertNext { assertThat(ymd).isEqualToComparingFieldByField(it) }
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
    client.select()
      .from(TABLE_YMD)
      .orderBy(Sort.Order.asc("y"))
      .`as`(YmdPo::class.java)
      .fetch()
      .all()
      .map { it as Ymd }
      .test()
      .assertNext { assertThat(ymd1).isEqualToComparingFieldByField(it) }
      .assertNext { assertThat(ymd2).isEqualToComparingFieldByField(it) }
      .verifyComplete()
  }
}
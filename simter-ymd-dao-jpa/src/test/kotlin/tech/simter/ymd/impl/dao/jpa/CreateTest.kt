package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.po.YmdPo
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Test [YmdDaoImpl.create].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class CreateTest @Autowired constructor(
  val rem: TestEntityManager,
  val dao: YmdDao
) {
  @Test
  fun `Create nothing`() {
    val expected = rem.querySingle { em ->
      em.createQuery("select count(t) from YmdPo t", Long::class.javaObjectType)
    }.get()
    dao.create().test().verifyComplete()
    assertEquals(expected, rem.querySingle { em ->
      em.createQuery("select count(t) from YmdPo t", Long::class.javaObjectType)
    }.get())
  }

  @Test
  fun `Create one`() {
    // init data
    val ymd = randomYmd()
    val po = YmdPo.from(ymd)

    // invoke and verify
    dao.create(ymd).test().verifyComplete()
    assertEquals(po, rem.find(YmdPo::class.java, po.id).get())
  }

  @Test
  fun `Create two`() {
    // init data
    val ymd1 = randomYmd()
    val po1 = YmdPo.from(ymd1)
    val ymd2 = randomYmd()
    val po2 = YmdPo.from(ymd2)

    // invoke and verify
    dao.create(ymd1, ymd2).test().verifyComplete()
    assertEquals(po1, rem.find(YmdPo::class.java, po1.id).get())
    assertEquals(po2, rem.find(YmdPo::class.java, po2.id).get())
  }
}
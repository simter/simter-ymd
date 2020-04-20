package tech.simter.ymd.impl.dao.jpa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import reactor.kotlin.test.test
import tech.simter.reactive.test.jpa.ReactiveDataJpaTest
import tech.simter.reactive.test.jpa.TestEntityManager
import tech.simter.ymd.core.YmdDao
import tech.simter.ymd.impl.dao.jpa.TestHelper.randomYmd
import tech.simter.ymd.impl.dao.jpa.po.YmdPo

/**
 * Test [YmdDaoImpl.create].
 *
 * @author RJ
 */
@SpringJUnitConfig(UnitTestConfiguration::class)
@ReactiveDataJpaTest
class CreateMethodImplTest @Autowired constructor(
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
    val po = randomYmd()

    // invoke and verify
    dao.create(po).test().verifyComplete()
    assertEquals(po, rem.find(YmdPo::class.java, po.id).get())
  }

  @Test
  fun `Create two`() {
    // init data
    val po1 = randomYmd()
    val po2 = randomYmd()

    // invoke and verify
    dao.create(po1, po2).test().verifyComplete()
    assertEquals(po1, rem.find(YmdPo::class.java, po1.id).get())
    assertEquals(po2, rem.find(YmdPo::class.java, po2.id).get())
  }
}
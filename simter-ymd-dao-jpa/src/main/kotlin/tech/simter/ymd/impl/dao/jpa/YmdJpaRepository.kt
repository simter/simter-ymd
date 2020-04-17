package tech.simter.ymd.impl.dao.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import tech.simter.ymd.impl.dao.jpa.po.YmdPo

/**
 * The block JPA-DAO Repository. See [CrudRepository], [PagingAndSortingRepository] and [SimpleJpaRepository].
 *
 * @author RJ
 */
interface YmdJpaRepository : JpaRepository<YmdPo, String> {
  @Query("select distinct t.year from YmdPo t" +
    " where t.type = ?1" +
    " order by t.year desc")
  fun findYears(type: String): List<Int>

  @Query("select distinct t.month from YmdPo t" +
    " where t.type = ?1 and t.year = ?2" +
    " order by t.month desc")
  fun findMonths(type: String, year: Int): List<Int>

  @Query("select distinct t.day from YmdPo t" +
    " where t.type = ?1 and t.year = ?2 and t.month = ?3" +
    " order by t.day desc")
  fun findDays(type: String, year: Int, month: Int): List<Int>
}
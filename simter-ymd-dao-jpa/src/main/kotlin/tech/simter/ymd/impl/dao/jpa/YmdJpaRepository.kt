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
  @Query("select distinct year from YmdPo where type = ?1 order by year desc")
  fun findYears(type: String): List<Int>

  @Query("select distinct month from YmdPo where type = ?1 and year = ?2 order by month desc")
  fun findMonths(type: String, year: Int): List<Int>

  @Query("select distinct day from YmdPo where type = ?1 and  year = ?2 and month = ?3 order by day desc")
  fun findDays(type: String, year: Int, month: Int): List<Int>
}
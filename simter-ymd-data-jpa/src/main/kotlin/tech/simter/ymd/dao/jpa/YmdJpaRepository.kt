package tech.simter.ymd.dao.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import tech.simter.ymd.po.Ymd
import tech.simter.ymd.po.YmdPK
import java.util.stream.Stream

/**
 * The block JPA-DAO Repository. See [CrudRepository], [PagingAndSortingRepository] and [SimpleJpaRepository].
 *
 * @author RJ
 */
interface YmdJpaRepository : JpaRepository<Ymd, YmdPK> {
  @Query("select year from Ymd where type = ?1 and month = 0 and day = 0 order by year desc")
  fun findYears(type: String): List<Int>

  @Query("select month from Ymd where type = ?1 and year = ?2 order by month desc")
  fun findMonths(type: String, year: Int): List<Int>

  @Query("select day from Ymd where type = ?1 and  year = ?2 and month = ?3 order by day desc")
  fun findDays(type: String, year: Int, month: Int): List<Int>
}
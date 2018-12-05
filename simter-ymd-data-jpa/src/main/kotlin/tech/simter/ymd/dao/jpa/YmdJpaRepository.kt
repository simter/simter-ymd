package tech.simter.ymd.dao.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import tech.simter.ymd.po.Ymd
import tech.simter.ymd.po.YmdPK

/**
 * The block JPA-DAO Repository. See [CrudRepository], [PagingAndSortingRepository] and [SimpleJpaRepository].
 *
 * @author RJ
 */
interface YmdJpaRepository : JpaRepository<Ymd, YmdPK>
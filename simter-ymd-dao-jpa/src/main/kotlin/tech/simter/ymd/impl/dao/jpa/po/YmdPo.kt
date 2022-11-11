package tech.simter.ymd.impl.dao.jpa.po

import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.Ymd.Companion.TABLE
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * The date of massive data.
 *
 * @author RJ
 */
@Entity
@Table(name = TABLE)
data class YmdPo(
  @Column(nullable = false, name = "t", length = 50)
  val type: String,
  @Column(nullable = false, name = "y")
  val year: Int,
  @Column(nullable = false, name = "m")
  val month: Int = 0,
  @Column(nullable = false, name = "d")
  val day: Int = 0,
  /** Only use as jpa entity id */
  @Id
  val id: String = Ymd.uid(type, year, month, day)
) {
  companion object {
    fun from(ymd: Ymd): YmdPo {
      return YmdPo(
        type = ymd.type,
        year = ymd.year,
        month = ymd.month,
        day = ymd.day
      )
    }
  }
}
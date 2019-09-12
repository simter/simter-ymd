package tech.simter.ymd.impl.dao.jpa.po

import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd
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
@Table(name = TABLE_YMD)
data class YmdPo(
  @Column(nullable = false, name = "t", length = 50)
  override val type: String,
  @Column(nullable = false, name = "y")
  override val year: Int,
  @Column(nullable = false, name = "m")
  override val month: Int = 0,
  @Column(nullable = false, name = "d")
  override val day: Int = 0,
  /** Only use as jpa entity id */
  @Id
  val id: String = Ymd.uid(type, year, month, day)
) : Ymd {
  companion object {
    fun from(ymd: Ymd): YmdPo {
      return if (ymd is YmdPo) ymd
      else YmdPo(
        type = ymd.type,
        year = ymd.year,
        month = ymd.month,
        day = ymd.day
      )
    }
  }
}
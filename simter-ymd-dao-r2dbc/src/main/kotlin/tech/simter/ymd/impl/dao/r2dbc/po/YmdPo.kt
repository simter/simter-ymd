package tech.simter.ymd.impl.dao.r2dbc.po

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd

/**
 * The entity implementation of [Ymd].
 *
 * @author RJ
 */
@Table(TABLE_YMD)
data class YmdPo(
  override val type: String,
  override val year: Int,
  override val month: Int = 0,
  override val day: Int = 0,
  /** Only use as spring-data entity id */
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
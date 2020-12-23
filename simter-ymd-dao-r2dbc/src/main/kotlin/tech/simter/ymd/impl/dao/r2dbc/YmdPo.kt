package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
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
  @Column("t")
  override val type: String,
  @Column("y")
  override val year: Int,
  @Column("m")
  override val month: Int = 0,
  @Column("d")
  override val day: Int = 0,
  /** Only use as spring-data entity id */
  @Id @JvmField
  val id: String = Ymd.uid(type, year, month, day)
) : Ymd, Persistable<String> {
  override fun getId(): String {
    return this.id
  }

  override fun isNew(): Boolean {
    return true
  }
}
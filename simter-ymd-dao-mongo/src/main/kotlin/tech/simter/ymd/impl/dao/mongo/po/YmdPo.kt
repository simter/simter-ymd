package tech.simter.ymd.impl.dao.mongo.po

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd

/**
 * The document implementation of [Ymd].
 *
 * @author RJ
 */
@Document(collection = TABLE_YMD)
data class YmdPo(
  @Field("t")
  override val type: String,
  @Field("y")
  override val year: Int,
  @Field("m")
  override val month: Int = 0,
  @Field("d")
  override val day: Int = 0,
  /** Only use as mongo document id */
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
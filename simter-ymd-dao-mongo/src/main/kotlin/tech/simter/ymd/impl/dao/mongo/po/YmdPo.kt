package tech.simter.ymd.impl.dao.mongo.po

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.core.Ymd.Companion.TABLE

/**
 * The document implementation of [Ymd].
 *
 * @author RJ
 */
@Document(collection = TABLE)
data class YmdPo(
  @Field("t") val type: String,
  @Field("y") val year: Int,
  @Field("m") val month: Int = 0,
  @Field("d") val day: Int = 0,
  /** Only use as mongo document id */
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
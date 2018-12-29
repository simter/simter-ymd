package tech.simter.ymd.po

import tech.simter.ymd.TABLE_NAME
import javax.persistence.Column

/**
 * The date of massive data.
 *
 * @author RJ
 */
// for jpa
@javax.persistence.Entity
@javax.persistence.Table(name = TABLE_NAME)
// for jdbc or r2dbc
@org.springframework.data.relational.core.mapping.Table(TABLE_NAME)
// for mongodb
@org.springframework.data.mongodb.core.mapping.Document(collection = TABLE_NAME)
data class Ymd(
  /** The belong type */
  @Column(nullable = false, length = 50) val type: String,

  /** 4 digits year, such as 2018 */
  @Column(nullable = false) val year: Int,

  /** month from 1 to 12. 0 means ignored */
  @Column(nullable = false) val month: Int = 0,

  /** day from 1 to 31. 0 means ignored */
  @Column(nullable = false) val day: Int = 0,

  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  val id: String = uid(type, year, month, day)
) {
  @get:javax.persistence.Transient
  @get:org.springframework.data.annotation.Transient
  val uid: String
    get() = uid(type, year, month, day)

  companion object {
    fun uid(type: String, year: Int, month: Int, day: Int): String {
      return "$type:$year:$month:$day"
    }
  }
}
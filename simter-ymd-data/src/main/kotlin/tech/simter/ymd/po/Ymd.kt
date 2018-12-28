package tech.simter.ymd.po

import tech.simter.ymd.TABLE_NAME
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY

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
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @GeneratedValue(strategy = IDENTITY)
  var id: Int? = null,

  /** The belong type */
  @Column(nullable = false, length = 50) val type: String,

  /** 4 digits year, such as 2018 */
  @Column(nullable = false) val year: Int,

  /** month from 1 to 12. 0 means ignored */
  @Column(nullable = false) val month: Int = 0,

  /** day from 1 to 31. 0 means ignored */
  @Column(nullable = false) val day: Int = 0
)
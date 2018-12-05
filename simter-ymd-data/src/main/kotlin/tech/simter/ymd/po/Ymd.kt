package tech.simter.ymd.po

import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.IdClass
import javax.persistence.Table

/**
 * The date of massive data.
 *
 * @author RJ
 */
@Entity
@Table(name = "st_ymd")
@Document(collection = "st_ymd")
@IdClass(YmdPK::class)
data class Ymd(
  /** The belong type */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false) val type: String,

  /** 4 digits year, such as 2018 */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false) val year: Int,

  /** month from 1 to 12. 0 means ignored */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false) val month: Int = 0,

  /** day from 1 to 31. 0 means ignored */
  @javax.persistence.Id
  @org.springframework.data.annotation.Id
  @Column(nullable = false) val day: Int = 0
)
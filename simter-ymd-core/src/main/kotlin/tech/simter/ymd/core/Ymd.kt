package tech.simter.ymd.core

import kotlinx.serialization.Serializable

/**
 * The date of massive data.
 *
 * [type], [year], [month] and [day] should be unique.
 *
 * @author RJ
 */
@Serializable
data class Ymd(
  /** The belonged type */
  val type: String,
  /** 4 digits year, such as 2018 */
  val year: Int,
  /** month from 1 to 12. 0 means ignored */
  val month: Int = 0,
  /** day from 1 to 31. 0 means ignored */
  val day: Int = 0,
) {
  companion object {
    /**
     * Generate a unique id of the [Ymd] instance.
     *
     * It has a format '$type:$year:$month:$day'.
     */
    fun uid(type: String, year: Int, month: Int, day: Int): String {
      return "$type:$year:$month:$day"
    }

    /** The authorizer key */
    const val AUTHORIZER_KEY = "${tech.simter.ymd.AUTHORIZER_KEY}.ymd"

    /** The [Ymd] table name */
    const val TABLE = "st_ymd"

    /** The operation name */
    const val OPERATION_READ = "READ"
    const val OPERATION_EDIT = "EDIT"
    const val OPERATION_DELETE = "DELETE"
  }
}
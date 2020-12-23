package tech.simter.ymd.core

/**
 * The date of massive data.
 *
 * [type], [year], [month] and [day] should be unique.
 *
 * @author RJ
 */
interface Ymd {
  /** The belong type */
  val type: String

  /** 4 digits year, such as 2018 */
  val year: Int

  /** month from 1 to 12. 0 means ignored */
  val month: Int

  /** day from 1 to 31. 0 means ignored */
  val day: Int

  /** An inner immutable [Ymd] implementation */
  private data class Impl(
    override val type: String,
    override val year: Int,
    override val month: Int,
    override val day: Int
  ) : Ymd

  companion object {
    /** Create an immutable [Ymd] instance */
    fun of(type: String, year: Int, month: Int, day: Int): Ymd {
      return Impl(type, year, month, day)
    }

    /**
     * Generate a unique id of the [Ymd] instance.
     *
     * It has a format '$type:$year:$month:$day'.
     */
    fun uid(type: String, year: Int, month: Int, day: Int): String {
      return "$type:$year:$month:$day"
    }
  }
}
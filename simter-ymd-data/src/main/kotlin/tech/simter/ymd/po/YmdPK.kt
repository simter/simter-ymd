package tech.simter.ymd.po

import java.io.Serializable

/**
 * The id of [Ymd].
 *
 * @author RJ
 */
data class YmdPK(
  val type: String = "",
  val year: Int = 0,
  val month: Int = 0,
  val day: Int = 0
) : Serializable
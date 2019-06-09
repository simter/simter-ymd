package tech.simter.ymd.impl

import tech.simter.ymd.core.Ymd

/**
 * The immutable implementation of [Ymd].
 *
 * @author RJ
 */
data class ImmutableYmd(
  override val type: String,
  override val year: Int,
  override val month: Int,
  override val day: Int
) : Ymd
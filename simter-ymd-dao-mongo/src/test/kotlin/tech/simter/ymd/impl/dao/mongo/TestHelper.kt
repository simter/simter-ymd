package tech.simter.ymd.impl.dao.mongo

import tech.simter.util.RandomUtils.nextId
import tech.simter.ymd.core.Ymd

/**
 * Provide public method for test.
 *
 * @author RJ
 */
object TestHelper {
  /** Generate a next [Ymd] id */
  fun nextId(): String {
    return "id${nextId("id")}"
  }
}
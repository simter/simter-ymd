package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.data.r2dbc.core.DatabaseClient
import tech.simter.ymd.TABLE_YMD
import tech.simter.ymd.core.Ymd
import tech.simter.ymd.test.TestHelper.randomYmd

/**
 * Provide public method of test
 *
 * @author RJ
 */
object TestHelper {
  /** delete all file store data from database */
  fun clean(client: DatabaseClient) {
    client.delete()
      .from(TABLE_YMD)
      .fetch()
      .rowsUpdated()
      .block()!!
  }

  /** insert one file store instance to database */
  fun insert(
    client: DatabaseClient,
    ymd: Ymd = randomYmd()
  ): Ymd {
    return client.insert()
      .into(TABLE_YMD)
      .value("t", ymd.type)
      .value("y", ymd.year)
      .value("m", ymd.month)
      .value("d", ymd.day)
      .value("id", Ymd.uid(
        type = ymd.type,
        year = ymd.year,
        month = ymd.month,
        day = ymd.day
      ))
      .fetch()
      .rowsUpdated()
      .map { ymd }
      .block()!!
  }
}
package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.r2dbc.core.DatabaseClient
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
    client.sql("delete from $TABLE_YMD")
      .fetch()
      .rowsUpdated()
      .block()!!
  }

  /** insert one file store instance to database */
  fun insert(
    client: DatabaseClient,
    ymd: Ymd = randomYmd()
  ): Ymd {
    return client.sql("insert into $TABLE_YMD(t, y, m, d, id) values (:t, :y, :m, :d, :id)")
      .bind("t", ymd.type)
      .bind("y", ymd.year)
      .bind("m", ymd.month)
      .bind("d", ymd.day)
      .bind("id", Ymd.uid(
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
package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import tech.simter.ymd.core.Ymd.Companion.TABLE

/**
 * Provide public method of test
 *
 * @author RJ
 */
@Component
class TestHelper @Autowired constructor(
  private val databaseClient: DatabaseClient,
) {
  /** delete all ymd store data from database */
  fun clean(): Mono<Void> {
    return databaseClient.sql("delete from $TABLE").then()
  }
}
package tech.simter.ymd.dao.reactive.mongo

import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import tech.simter.ymd.po.Ymd

/**
 * See interfaces [ReactiveSortingRepository], [ReactiveQueryByExampleExecutor], [ReactiveCrudRepository], [ReactiveMongoOperations].
 *
 * See implements [ReactiveMongoTemplate], [SimpleReactiveMongoRepository].
 * See [14.3.3. MongoDB JSON-based Query Methods and Field Restriction](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongodb.repositories.queries.json-based)
 *
 * @author RJ
 */
interface YmdRepository : ReactiveCrudRepository<Ymd, String>, YmdRepositoryByTemplate {
  /*  Because spring-data-mongo do not support distinct in @Query,
      so need to use a custom impl by ReactiveMongoTemplate through YmdRepositoryByTemplate.
      See [DATAMONGO-1985 : Support distinct queries through query derivation](https://jira.spring.io/browse/DATAMONGO-1985)

  @Query(value = "{ 'type' : ?0 }", fields = "{ 'year' : 1}", sort = "{ year : -1 }")
  fun findYears(type: String): Flux<Int>

  @Query(value = "{ 'type' : ?0, 'year' : ?1 }", fields = "{ 'month' : 1}", sort = "{ month : -1 }")
  fun findMonths(type: String, year: Int): Flux<Int>

  @Query(value = "{ 'type' : ?0, 'year' : ?1, 'month' : ?2 }", fields = "{ 'day' : 1}", sort = "{ day : -1 }")
  fun findDays(type: String, year: Int, month: Int): Flux<Int>
  */
}
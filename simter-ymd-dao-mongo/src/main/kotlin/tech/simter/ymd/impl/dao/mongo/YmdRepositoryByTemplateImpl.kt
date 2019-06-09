package tech.simter.ymd.impl.dao.mongo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import tech.simter.ymd.impl.dao.mongo.po.YmdPo

/**
 * @author RJ
 */
@Repository
class YmdRepositoryByTemplateImpl @Autowired constructor(
  private val template: ReactiveMongoTemplate
) : YmdRepositoryByTemplate {
  val intDescComparator: Comparator<Int> = Comparator { o1: Int, o2: Int ->
    if (o1 > o2) -1 else if (o1 < o2) 1 else 0
  }

  override fun findYears(type: String): Flux<Int> {
    return template.query(YmdPo::class.java)
      .distinct("year").`as`(Int::class.java) // this working with projection but make mongo sort failed
      .matching(query(where("type").`is`(type)).with(Sort.by(DESC, "year")))
      .all()
      .sort(intDescComparator) // custom sort
  }

  override fun findMonths(type: String, year: Int): Flux<Int> {
    return template.query(YmdPo::class.java)
      .distinct("month").`as`(Int::class.java)
      .matching(
        query(where("type").`is`(type).and("year").`is`(year))
          .with(Sort.by(DESC, "month"))
      ).all()
      .sort(intDescComparator) // custom sort
  }

  override fun findDays(type: String, year: Int, month: Int): Flux<Int> {
    return template.query(YmdPo::class.java)
      .distinct("day").`as`(Int::class.java)
      .matching(
        query(where("type").`is`(type).and("year").`is`(year).and("month").`is`(month))
          .with(Sort.by(DESC, "day"))
      ).all()
      .sort(intDescComparator) // custom sort
  }
}
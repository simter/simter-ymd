package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository

/**
 * The reactive repository.
 *
 * @author RJ
 */
interface YmdRepository : R2dbcRepository<YmdPo, String>
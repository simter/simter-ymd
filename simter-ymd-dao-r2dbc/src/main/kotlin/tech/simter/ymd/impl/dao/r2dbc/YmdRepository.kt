package tech.simter.ymd.impl.dao.r2dbc

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.r2dbc.core.DatabaseClient
import tech.simter.ymd.core.Ymd

/**
 * The reactive repository.
 *
 * No usage and just for [DatabaseClient] initialization.
 *
 * @author RJ
 */
interface YmdRepository : R2dbcRepository<Ymd, String>
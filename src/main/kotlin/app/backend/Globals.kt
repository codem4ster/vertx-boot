package app.backend

import app.backend.core.elastic.Client
import org.slf4j.Logger

object Globals {
  lateinit var elasticClient: Client
  lateinit var logger: Logger
}

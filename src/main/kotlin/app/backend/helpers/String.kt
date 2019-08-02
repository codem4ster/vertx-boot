package app.backend.helpers

import java.util.*

fun String.toUUID() = UUID.fromString(this)

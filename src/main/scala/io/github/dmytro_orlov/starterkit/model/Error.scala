package io.github.dmytro_orlov.starterkit.model

sealed abstract class Error
case class UnexpectedError(cause: Throwable) extends Error
case class DBError(cause: Exception) extends Error
case class NotFoundError(message: String) extends Error

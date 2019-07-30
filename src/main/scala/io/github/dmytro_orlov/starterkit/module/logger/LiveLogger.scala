package io.github.dmytro_orlov.starterkit.module.logger

import com.typesafe.scalalogging.StrictLogging
import io.github.dmytro_orlov.starterkit.model.Error
import zio._

trait LiveLogger extends Logger {

  val logger: Logger.Service = new Logger.Service with StrictLogging {
    def error(message: => String): ZIO[Any, Error, Unit] = UIO(logger.error(message))

    def warn(message: => String): ZIO[Any, Error, Unit] = UIO(logger.warn(message))

    def info(message: => String): ZIO[Any, Error, Unit] = UIO(logger.info(message))

    def debug(message: => String): ZIO[Any, Error, Unit] = UIO(logger.debug(message))

    def trace(message: => String): ZIO[Any, Error, Unit] = UIO(logger.trace(message))

    def error(t: Throwable)(message: => String): ZIO[Any, Error, Unit] = UIO(logger.error(message, t))

    def warn(t: Throwable)(message: => String): ZIO[Any, Error, Unit] = UIO(logger.warn(message, t))

    def info(t: Throwable)(message: => String): ZIO[Any, Error, Unit] = UIO(logger.info(message, t))

    def debug(t: Throwable)(message: => String): ZIO[Any, Error, Unit] = UIO(logger.debug(message, t))

    def trace(t: Throwable)(message: => String): ZIO[Any, Error, Unit] = UIO(logger.trace(message, t))
  }
}

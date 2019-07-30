package io.github.dmytro_orlov.starterkit.module.db

import io.github.dmytro_orlov.starterkit.model.Error
import io.github.dmytro_orlov.starterkit.model.database.User
import zio.ZIO

trait UserRepository {

  val repository: UserRepository.Service
}

object UserRepository {

  trait Service {

    def get(id: Long): ZIO[Any, Error, User]

    def create(user: User): ZIO[Any, Error, User]

    def delete(id: Long): ZIO[Any, Error, Unit]
  }
}

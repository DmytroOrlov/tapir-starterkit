package io.github.dmytro_orlov.starterkit.module

import io.github.dmytro_orlov.starterkit.model.Error
import io.github.dmytro_orlov.starterkit.model.database.User
import zio.ZIO

package object db {

  def get(id: Long): ZIO[UserRepository, Error, User] =
    ZIO.accessM(_.repository.get(id))

  def create(user: User): ZIO[UserRepository, Error, User] =
    ZIO.accessM(_.repository.create(user))

  def delete(id: Long): ZIO[UserRepository, Error, Unit] =
    ZIO.accessM(_.repository.delete(id))
}

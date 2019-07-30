package io.github.dmytro_orlov.starterkit

import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import io.github.dmytro_orlov.starterkit.implicits.Circe._
import io.github.dmytro_orlov.starterkit.model.database.User
import io.github.dmytro_orlov.starterkit.module.db.{InMemoryUserRepository, UserRepository}
import io.github.dmytro_orlov.starterkit.module.logger.{ConsoleLogger, Logger}
import io.github.dmytro_orlov.starterkit.route.UserRoute
import io.circe.generic.auto._
import org.http4s.implicits._
import org.http4s.{Method, Request, Status}
import org.scalatest.FlatSpec
import zio.interop.catz._
import zio.{DefaultRuntime, Ref, ZIO}

class UserRouteSpec extends FlatSpec with HTTPSpec with DefaultRuntime with RandomDataGenerator {
  private val user: User = random[User]

  type Env = UserRepository with Logger
  private val userRoute: UserRoute[Env] = new UserRoute[Env]
  private val app = userRoute.getRoutes.orNotFound
  private val getEnv: ZIO[Any, Nothing, UserRepository with Logger] =
    for {
      store <- Ref.make(Map[Long, User]())
      repo = InMemoryUserRepository(store)
      env = new UserRepository with Logger {
        val repository: InMemoryUserRepository = repo
        val logger = ConsoleLogger
      }
    } yield env

  "UserRoute" should "create a user" in {
    val payload: Request[ZIO[Env, Throwable, ?]] = request(Method.POST, "/user").withEntity(user)

    runWithEnv(check(app.run(payload), Status.Created, Some(user)))
  }

  it should "get a user" in {
    val postPayload: Request[ZIO[Env, Throwable, ?]] = request(Method.POST, "/user").withEntity(user)
    val getPayload: Request[ZIO[Env, Throwable, ?]] = request(Method.GET, s"/user/${user.id}")

    runWithEnv(check(app.run(postPayload) *> app.run(getPayload), Status.Ok, Some(user)))
  }

  it should "delete a user" in {
    val postPayload: Request[ZIO[Env, Throwable, ?]] = request(Method.POST, "/user").withEntity(user)
    val getPayload: Request[ZIO[Env, Throwable, ?]] = request(Method.GET, s"/user/${user.id}")
    val deletePayload: Request[ZIO[Env, Throwable, ?]] = request(Method.DELETE, s"/user/${user.id}")

    runWithEnv(check(app.run(postPayload) *> app.run(deletePayload) *> app.run(getPayload), Status.NotFound))
  }

  private def runWithEnv[E, A](task: ZIO[Env, E, A]): A = {
    val result: ZIO[Any, E, A] = for {
      env <- getEnv
      r <- task.provide(env)
    } yield {
      r
    }

    unsafeRun(result)
  }
}

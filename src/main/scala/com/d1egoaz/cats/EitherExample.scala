package com.d1egoaz.cats

import cats.data.{EitherT, OptionT}
import cats.implicits._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object EitherExample extends App {

  abstract class ValidationError
  case class MyError(error: String) extends ValidationError

  type MyValidation = Either[ValidationError, String]

  val lastnameO: MyValidation = Right("Doe")
  val firstnameF: Future[MyValidation] = Future.successful(Right("Jane"))
  val firstnameFError: Future[MyValidation] = Future.successful(Left(MyError("error validating name")))

  // Some(Left(MyError(Jane)))
  val ot: OptionT[Future, MyValidation] = for {
    a <- OptionT.liftF(firstnameF)
    b <- OptionT.liftF(firstnameFError)
  } yield b
  val result = ot.value
  val r = Await.result(result, 5.second)
  println(s" r1: $r")
  // r1: Some(Left(MyError(error validating name)))

  val ot2: EitherT[Future, ValidationError, String] = for {
    a <- EitherT(firstnameF)
    b <- EitherT(firstnameFError)
  } yield b
  val result2 = ot2.value // Future(Some("Hello Jane Doe"))
  val r2 = Await.result(result2, 5.second)
  println(s" r2: $r2")
  // r2: Left(MyError(error validating name))

  // val greetingFO: Future[Option[String]] = Future.successful(Some("Hello"))
  // val firstnameF: Future[String] = Future.successful("Jane")
  // val lastnameO: Option[String] = Some("Doe")

  // val ot: OptionT[Future, String] = for {
  //   g <- OptionT(greetingFO)
  //   f <- OptionT.liftF(firstnameF)
  //   l <- OptionT.fromOption[Future](lastnameO)
  // } yield s"$g $f $l"

  // val result: Future[Option[String]] = ot.value // Future(Some("Hello Jane Doe"))
}

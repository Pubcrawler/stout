package io.pubcrawler.stout.util

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import io.pubcrawler.stout.models.Result
import org.scalatra.{AsyncResult, ScalatraContext}

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}


trait UtilAsync extends LazyLogging with ScalatraContext {

  protected implicit def executor: ExecutionContext = ActorSystem().dispatcher

  private def uuid() = java.util.UUID.randomUUID.toString

  def resultOption(result: Future[Option[Any]], ifNotFoundMessage: String = "not found"): AsyncResult = {
    val prom = Promise[Result]()
    result onComplete {
      case Success(a) => a match {
        case Some(data) => prom.complete(Try(Result(200, data)))
        case None => prom.complete(Try(Result(404, ifNotFoundMessage)))
      }
      case Failure(e) =>
        val ref = uuid()
        logger.error(s"{$ref}: ${e.getMessage}", e)
        prom.complete(Try(Result(500, s"We're deeply sorry. We're sorry. Sorry! Ref: {$ref}")))
    }
    new AsyncResult() {
      override val is: Future[Result] = prom.future
    }
  }

  def resultSeq(result: Future[Seq[Any]]): AsyncResult = {
    val prom = Promise[Result]()
    result onComplete {
      case Success(a) => a match {
        case data => prom.complete(Try(Result(200, data)))
      }
      case Failure(e) =>
        val ref = uuid()
        logger.error(s"{$ref}: ${e.getMessage}", e)
        prom.complete(Try(Result(500, s"We're deeply sorry. We're sorry. Sorry! Ref: {$ref}")))
    }
    new AsyncResult() {
      override val is: Future[Result] = prom.future
    }
  }
}

object UtilAsync

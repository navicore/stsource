package onextent.akka.naviblob.akka

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.stream.scaladsl.Source
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Await, Future}

/**
  * Convenience entry point api. Users instantiate this and wire it into their streams.
  */
object NaviLake {

  def apply(connector: ActorRef)(
      implicit system: ActorSystem,
      to: Timeout): Source[String, NotUsed] =
    Source.fromGraph(new NaviLake(connector))

}

final case class Pull()
final case class NoMore()

/**
  * Entry point api. Users instantiate this and wire it into their streams.
  */
class NaviLake(connector: ActorRef)(
    implicit system: ActorSystem,
    to: Timeout)
    extends GraphStage[SourceShape[String]]
    with LazyLogging {

  val out: Outlet[String] = Outlet[String]("NaviLakeSource")

  override val shape: SourceShape[String] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      setHandler(
        out,
        new OutHandler {
          override def onPull(): Unit = {
            val f: Future[Any] = connector ask Pull()
            Await.result(f, to.duration) match {
              case data: String => push(out, data)
              case _: NoMore =>
                logger.info(
                  "blob stream is finished. all blobs have been read.")
                completeStage()
              case e => logger.warn(s"pull error: $e", e)
            }
          }
        }
      )
    }

}

package navicore.akka.stsource

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
object StSource {

  def apply() (
      implicit system: ActorSystem, cfg: StConfig,
      to: Timeout): Source[String, NotUsed] =
    Source.fromGraph(new StSource())

}

final case class Pull()
final case class NoMore()

/**
  * Entry point api. Users instantiate this and wire it into their streams.
  */
class StSource (
    implicit system: ActorSystem, cfg: StConfig,
    to: Timeout)
    extends GraphStage[SourceShape[String]]
    with LazyLogging {

  val connector: ActorRef = system.actorOf(StConnector.props)

  val out: Outlet[String] = Outlet[String]("StSource")

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
                  "ST stream is finished. all data have been read.")
                completeStage()
              case e => logger.warn(s"pull error: $e", e)
            }
          }
        }
      )
    }

}

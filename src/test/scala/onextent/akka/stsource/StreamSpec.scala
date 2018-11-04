package onextent.akka.stsource

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout
import org.scalatest._

import scala.concurrent.Future
import scala.concurrent.duration._

class StreamSpec extends FlatSpec with Matchers {

  implicit val actorSystem: ActorSystem = ActorSystem("spec")
  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(actorSystem))

  def requestDuration: Duration = {
    val t = "120 seconds"
    Duration(t)
  }
  implicit def requestTimeout: Timeout = {
    val d = requestDuration
    FiniteDuration(d.length, d.unit)
  }

  var count = 0
  val consumer: Sink[String, Future[Done]] = Sink.foreach(m => {
    count += 1
    println(s"$count sunk $m")
  })

  ignore should "read stuff" in {}

}

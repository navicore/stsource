package onextent.akka.stsource

import java.net.URL

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

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
    if (count > 10000) throw new Exception
  })

  "source" should "publish stuff" in {

    val stgUrl: URL = getClass.getResource("/iotjson.stg")
    implicit val cfg: StConfig =
      StConfig(100,
               stgUrl,
               Map(
                 ("type", List("observation", "error", "heartbeat")),
                 ("deviceId", List("d1", "d2", "d3", "d4")),
                 ("module", List("temp1", "temp2", "waterLvl1")),
                 ("value", List("24.0", "20.8", "19.0", "68.1"))
               ))

    val connector: ActorRef = actorSystem.actorOf(StConnector.props)

    val src = StSource(connector)
    val r: Future[Done] = src.runWith(consumer)

    Await.result(r, 10 seconds)

  }

}

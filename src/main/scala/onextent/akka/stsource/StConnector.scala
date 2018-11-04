package onextent.akka.stsource

import akka.actor.{Actor, Props}
import com.typesafe.scalalogging.LazyLogging

object StConnector extends LazyLogging {

  val name: String = "StConnector"

  def props[T](implicit config: StConfig) = Props(new StConnector())
}

class StConnector(implicit config: StConfig)
    extends Actor
    with LazyLogging {

  val readerIterator: Iterator[String] = new StReader().read()

  override def receive: Receive = {

    case _: Pull =>
      if (readerIterator.hasNext) {
        // read one from the current file
        sender() ! readerIterator.next()
      } else {
        sender() ! NoMore()
      }

    case x => logger.error(s"I don't know how to handle ${x.getClass.getName}")

  }

}

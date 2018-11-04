package onextent.akka.stsource

import scala.util.Random

object MkStream {

  def apply[T](seed: Long, valid: T*): Stream[T] = {
    val r = scala.util.Random
    r.setSeed(seed) // not random, reproducible
    val source: List[T] = valid.toList
    def getV: T = source(r.nextInt(source.length))
    lazy val results: Stream[T] = getV #:: getV #:: results.zip(source.tail).map {_ => getV}
    results
  }

  val random: Random.type = scala.util.Random

  def apply[T](valid: T*): Stream[T] = {
    val source: List[T] = valid.toList
    def getV: T = source(random.nextInt(source.length))
    lazy val results: Stream[T] = getV #:: getV #:: results.zip(source.tail).map {_ => getV}
    results
  }

}

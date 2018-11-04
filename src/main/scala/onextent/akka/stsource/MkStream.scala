package onextent.akka.stsource

import scala.util.Random

/**
  * `val numbers = MkStream(506, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten").toIterator``
  * `for (i <- 1 to 10)``
  * `  println(s"iter saw ${numbers.next()}")`
  */
object MkStream {

  // random per seed - reproducible
  def apply[T](seed: Long, valid: T*): Stream[T] = {
    val r = new Random()
    r.setSeed(seed) // not random, reproducible
    val source: List[T] = valid.toList
    def get: T = source(r.nextInt(source.length))
    lazy val results: Stream[T] = get #:: get #:: results.zip(source.tail).map {_ => get}
    results
  }

  // random each call
  def apply[T](valid: T*): Stream[T] = {
    val r = new Random()
    val source: List[T] = valid.toList
    def get: T = source(r.nextInt(source.length))
    lazy val results: Stream[T] = get #:: get #:: results.zip(source.tail).map {_ => get}
    results
  }

}

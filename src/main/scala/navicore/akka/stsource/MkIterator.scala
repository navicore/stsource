package navicore.akka.stsource

import scala.util.Random

/**
  * {{{
  * val numbers = MkIterator(506, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten")`
  * for (i <- 1 to 10)`
  *   println(s"iter saw \${numbers.next()}")
  * }}}
  */
object MkIterator {

  // random per seed - reproducible
  def applyRandom[T](r: Random, source: List[T]): Iterator[T] = {
    class TI(r: Random, source: List[T]) extends Iterator[T] {
      def get: T = source(r.nextInt(source.length))
      override def hasNext: Boolean = true
      override def next(): T = get
    }
    new TI(r, source)
  }

  // random
  def apply[T](valid: T*): Iterator[T] = {
    val r: Random = new Random()
    applyRandom[T](r, valid.toList)
  }

  // random per seed - reproducible
  def apply[T](seed: Long, valid: T*): Iterator[T] = {
    val r: Random = new Random()
    r.setSeed(seed) // not random, reproducible
    applyRandom[T](r, valid.toList)
  }

}

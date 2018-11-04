package onextent.akka.stsource

import java.io.{BufferedReader, InputStreamReader, StringBufferInputStream}

class StReader()(implicit cfg: StConfig) {

  def read(): Iterator[String] = {

    val gis= new StringBufferInputStream("""{"ha": "ha"}""")
    val decoder = new InputStreamReader(gis, "UTF8")

    val b = new BufferedReader(decoder)

    class LineIterator(b: BufferedReader) extends Iterator[String] {

      override def hasNext: Boolean = b.ready()

      override def next(): String = b.readLine()
    }

    new LineIterator(b)

  }

}

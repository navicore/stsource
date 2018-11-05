package navicore.akka.stsource

import org.stringtemplate.v4.{ST, STGroupFile}

class StReader()(implicit cfg: StConfig) {

  def read(): Iterator[String] = {

    class LineIterator(implicit cfg: StConfig) extends Iterator[String] {

      val group = new STGroupFile(cfg.stgUrl, "UTF8", '<', '>')
      val streams: Map[String, Iterator[String]] =
        cfg.validSets.map(
          s =>
            (s._1,
             if (cfg.reproducible) MkIterator(1234, s._2: _*)
             else MkIterator(s._2: _*)))

      var count = 0

      override def hasNext: Boolean = cfg.size == 0 || count < cfg.size

      override def next(): String = {
        count = count + 1
        val st: ST = group.getInstanceOf("decl")
        streams.foreach(s => st.add(s._1, s._2.next()))
        st.render()
      }

    }

    new LineIterator()

  }

}

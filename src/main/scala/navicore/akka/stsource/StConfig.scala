package navicore.akka.stsource

import java.net.URL

/**
  * size == 0 means infinite
  */
case class StConfig (size: Int, stgUrl: URL, validSets: Map[String, List[String]], reproducible: Boolean = true)

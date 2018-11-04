package onextent.akka.stsource

import java.net.URL

import com.typesafe.scalalogging.LazyLogging
import org.scalatest._
import org.stringtemplate.v4.{ST, STGroupFile}

class StSpec extends FlatSpec with Matchers with LazyLogging {

  "hello" should "have world" in {

    val hello = new ST("Hello, <name>")

    hello.add("name", "World")

    assert("Hello, World" == hello.render())

  }

  "group" should "have template" in {

    val gfile: URL = getClass.getResource("/test.stg")
    val group = new STGroupFile(gfile, "UTF8", '<', '>')

    val st = group.getInstanceOf("decl")
    st.add("type", "int")
    st.add("name", "x")
    st.add("value", 0)
    val result = st.render

    assert("int x = 0;" == result)

  }

  "stream" should "have words" in {

    val r = scala.util.Random
    r.setSeed(5566) // not random
    val sourceWords = List("one!", "two!", "three", "four")

    def getWord: String = sourceWords(r.nextInt(sourceWords.length))

    lazy val words: Stream[String] = getWord #:: getWord #:: words.zip(words.tail).map { _ => getWord }

    words take 5 foreach println

  }

  "mkIterator" should "have lots of laziness" in {

    val numbers = MkIterator(506, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten")
    for (_ <- 1 to 10)
      println(s"iter saw ${numbers.next()}")

    val s = MkIterator(506, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten")
    for (_ <- 1 to 100)
      println(s"infinite saw ${s.next()}")

  }

  "iot" should "have type" in {

    val deviceIds = MkIterator(103, "11-000202", "11-000203", "22-000100")
    val types = MkIterator(206, "observation", "error", "heartbeat")

    val gfile: URL = getClass.getResource("/iotjson.stg")
    val group = new STGroupFile(gfile, "UTF8", '<', '>')

    val st = group.getInstanceOf("decl")

    st.add("type", types.next())
    st.add("deviceId", deviceIds.next())

    val result = st.render
    println(s"result:\n$result")

    val expected = """{"type": "observation", "deviceId": "11-000202"}"""

    assert(expected == result)

  }

}

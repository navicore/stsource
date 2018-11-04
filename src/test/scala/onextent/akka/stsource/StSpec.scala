package onextent.akka.stsource

import java.net.URL

import com.typesafe.scalalogging.LazyLogging
import org.scalatest._
import org.stringtemplate.v4.{ST, STGroupFile}

class StSpec extends FlatSpec with Matchers with LazyLogging {

  "hello" should "world" in {

    val hello = new ST("Hello, <name>")

    hello.add("name", "World")

    assert("Hello, World" == hello.render())

  }

  "group" should "file" in {

    val gfile: URL = getClass.getResource("/test.stg")
    val group = new STGroupFile(gfile, "UTF8", '<', '>')

    val st = group.getInstanceOf("decl")
    st.add("type", "int")
    st.add("name", "x")
    st.add("value", 0)
    val result = st.render

    assert("int x = 0;" == result)

  }

}

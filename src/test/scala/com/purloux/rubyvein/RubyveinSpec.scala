package com.purloux.scala.rubyvein.test
import com.purloux.scala.rubyvein._
import org.scalatest.FlatSpec
import scala.io.Source

class RubyveinSpec extends FlatSpec {
  import TestRubyvein._
  val simpleSource = "def simple; puts \"Simple Ruby Script\" end"

  "The loadScript function" should "load ruby scripts directly from source" in {
     val source = simpleSource
     val helloScript = testvein.loadScript(source)

     assert(helloScript.source === simpleSource)
   }

  it should "load ruby scripts from input streams" in {
    val source = getClass.getResourceAsStream("/test_script_simple.rb")
    val streamScript = testvein.loadScript(source)

    assert(streamScript.source === simpleSource)
  }

  it should "load ruby scripts from scala.io.Source" in {
    val source = Source.fromURL(getClass.getResource("/test_script_simple.rb"))
    val sourceScript = testvein.loadScript(source)

    assert(sourceScript.source === simpleSource)
  }

  "The loadScriptFromPath" should "load ruby scripts from a given path" in {
    val path = "/test_script_simple.rb"
    val pathScript = testvein.loadScriptFromPath(path)

    assert(pathScript.source === simpleSource)
  }
}
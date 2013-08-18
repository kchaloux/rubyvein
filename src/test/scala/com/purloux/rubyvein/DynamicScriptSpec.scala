package com.purloux.scala.rubyvein.test
import com.purloux.scala.rubyvein._
import com.purloux.scala.rubyvein.DynamicScript
import org.scalatest.FlatSpec
import scala.io.Source
import scala.reflect._

class DynamicScriptSpec extends FlatSpec {
  import TestRubyvein._
  lazy val script = testvein.loadScriptFromPath("/test_script.rb")

  "A Function identifier" should "be valid for the exact name of the Ruby function" in {
    val result = script.say_hello [String]
    assert(result === "hello")
  }

  it should "be valid for matching scala-style camelCase identifier" in {
    val result = script.sayHello [String]
    assert(result === "hello")
  }

  "A Function invocation" should "return the type specified by the type parameter given" in {
    val result = script.say_hello [String]
    assert(result.getClass == classOf [String])
  }
}
package com.purloux.scala.rubyvein
import org.jruby.runtime.builtin.IRubyObject
import org.jruby.RubyHash
import org.jruby.RubyArray

object RubyConversions {
  
  /** Return a camelCased string as a ruby-style
   *  snake_cased string. Used to translate scala
   *  style identifiers into ruby style identifiers
   *  
   *  @param id scala-style identifier to translate 
   */
  def scalaIdToRuby(id : String) = 
    id.split("(?<!^)(?=[A-Z])").map(_.toLowerCase).mkString("_")
  
  
  def seqToRuby(vein : Rubyvein, seq : Seq[Any]): RubyArray = {
    val array = vein.runtime.newArray()
    seq.foreach { x => array.add(x) }
    return array
  }
  
  def mapToRuby(vein : Rubyvein, map : Map[Any, Any]): RubyHash = {
      throw new Exception
  }
}
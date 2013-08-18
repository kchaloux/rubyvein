package com.purloux.scala.rubyvein
import scala.language.dynamics
import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import org.jruby.runtime.builtin.IRubyObject
import com.purloux.scala.rubyvein.RubyConversions._

/** Defines the behavior and contract for a
 *  JRuby object that can call unparameterized
 *  functions and methods
 */
trait DynamicSelectable extends Dynamic {
  val vein : Rubyvein
  val receiver : IRubyObject
  
  /** Invokes a method with no return value on
   *  the receiver contained within
   *  
   *  @param id identifier of the method to invoke 
   */
  def callMethod(id : String): Unit =
    vein.callMethod(id)(receiver)
    
  /** Returns the result of a method invoked from a
   *  ruby object with the given identifier
   *  
   *  @param id identifier of the function to invoke 
   */
  def callFunction[T] (id : String): T = 
    vein.callFunction[T](id)(receiver)
    
  /** Returns the invocation of a ruby method called
   *  with the given identifier
   *  
   *  @param id identifier of the method to invoke
   *  @param callFn function used to call method or function from ruby with id
   */
  private def invokeMethod[T](id : String)(callFn : String => T): T = 
    try {
      callFn(id)
    } catch {
      case _ : Throwable => try { callFn(scalaIdToRuby(id)) }
    }
  
  /** Dynamically invokes a method or function
   *  without arguments from the given identifier
   *  
   *  @param id identifier of the method to invoke 
   */ 
  def selectDynamic[T : TypeTag](id : String) = (typeOf[T] match {
    case t if t =:= typeOf[Nothing] => invokeMethod(id)(callMethod(_))
    case _ => invokeMethod(id)(callFunction[T](_))
  }).asInstanceOf[T]
}
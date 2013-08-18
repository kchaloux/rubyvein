package com.purloux.scala.rubyvein
import scala.language.dynamics
import scala.reflect.runtime.universe.TypeTag
import scala.reflect.runtime.universe.typeOf
import org.jruby.runtime.builtin.IRubyObject
import com.purloux.scala.rubyvein.RubyConversions._

/** Defines the behavior and contract for a
 *  JRuby object that can call parameterized functions
 *  and methods
 */
trait DynamicApplicable extends Dynamic {
  val vein : Rubyvein
  val receiver : IRubyObject
  
  /** Invokes a method with no return value on
   *  the receiver contained within 
   *
   *  @param id identifier of the method to invoke
   *  @param args arguments to pass to the method  
   */
  def callMethod(id : String, args : AnyRef*): Unit =
    vein.callMethod(id, args:_*)(receiver)
    
  /** Returns the result of a method invoked from a 
   *  ruby object with the given identifier and arguments  
   * 
   *  @param id identifier of the function to invoke
   *  @param args arguments to pass to the function
   */
  def callFunction[T] (id : String, args : AnyRef*): T = 
    vein.callFunction[T](id, args:_*)(receiver)
 
  /** Returns the invocation of a ruby function called
   *  with the given identifier and list of arguments
   *  
   *  @param id identifier of the function to invoke
   *  @param args arguments to pass to ruby function
   *  @param callFn function used to call function from ruby with id and arguments
   */
  private def invoke[T](id : String, args : AnyRef*)(callFn : (String, AnyRef*) => T): T =
    try {
      callFn(id, args:_*)
    } catch {
      case _ : Throwable => try { callFn(scalaIdToRuby(id), args:_*) }
    }
  
  /** Dynamically invokes a method or function
   *  with arguments from the given identifier
   *  
   *  @param id identifier of the function to invoke
   *  @param args arguments to pass to the function
   */
  def applyDynamic[T : TypeTag](id : String)(args : AnyRef*) = (typeOf[T] match {
    case t if t =:= typeOf[Nothing] => invoke(id, args:_*)((i, a) => callMethod(i, a:_*))
    case _ => invoke(id, args:_*)((i, a) => callFunction[T](i, a:_*))
  }).asInstanceOf[T]
}
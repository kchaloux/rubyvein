package com.purloux.scala.rubyvein
import org.jruby.runtime.builtin.IRubyObject
import org.jruby.RubyModule

/** Defines the behavior of an entity that can load 
 *  Ruby modules from an active VM contained with the
 *  supplied Rubyvein 
 */
trait DynamicModuleLoader {
  val vein : Rubyvein
  
  /** Returns a RubyModule with the given module name
   *  currently specified within the scope of the VM
   *  
   *  @param moduleName name of the module to load 
   */
  def getModule(moduleName : String) : RubyModule
  
  /** Returns an invocable Ruby module of the given name 
   *  
   *  @param moduleName name of the module to load 
   */
  def loadModule(moduleName : String): DynamicModule =
    new DynamicModule(vein, getModule(moduleName))
}
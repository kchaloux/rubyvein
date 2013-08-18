package com.purloux.scala.rubyvein
import org.jruby.runtime.builtin.IRubyObject
import org.jruby.RubyModule

/** Provides an invocable container for a Ruby Module 
 *  
 *  @param vein Rubyvein with Ruby runtime 
 *  @param moduleName name of the module to load 
 */
class DynamicModule(val vein : Rubyvein, val module : RubyModule) 
    extends DynamicApplicable
    with DynamicSelectable
    with DynamicModuleLoader
{
  val source = ""
  val receiver = module
  
  /** Returns a RubyModule internal to this module
   *  
   *  @param moduleName name of the module to load 
   */
  def getModule(moduleName : String) = 
    module.getConstant(moduleName).asInstanceOf[RubyModule]
}
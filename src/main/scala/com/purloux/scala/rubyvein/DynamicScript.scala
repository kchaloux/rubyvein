package com.purloux.scala.rubyvein

/** Provides an invocable container for raw
 *  ruby script sources, containing invocable
 *  functions and methods 
 *  
 *  @param vein Rubyvein with Ruby runtime 
 *  @param source ruby source text used to evaluate script 
 */
class DynamicScript(val vein : Rubyvein, val source : String) 
    extends DynamicApplicable
    with DynamicSelectable
    with DynamicModuleLoader 
{
  val receiver = vein.eval(source)
  
  /** Returns a RubyModule with the given module name
   *  currently specified within the scope of the VM
   *  
   *  @param moduleName name of the module to load 
   */
  def getModule(moduleName : String) = 
    vein.getModule(moduleName)
}
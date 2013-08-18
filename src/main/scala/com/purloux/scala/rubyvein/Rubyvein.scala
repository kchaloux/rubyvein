package com.purloux.scala.rubyvein
import org.jruby.Ruby
import org.jruby.javasupport.JavaEmbedUtils
import org.jruby.runtime.ThreadContext
import org.jruby.runtime.builtin.IRubyObject
import com.purloux.scala.utils.AutoResource
import scala.collection.JavaConverters._

/** Represents a Ruby runtime, evaler and threading context 
 *  through which invocable Ruby entities are interacted 
 *  
 *  @param runtime instance of a JRuby runtime
 */
class Rubyvein(val runtime : Ruby) 
    extends DynamicModuleLoader
{
  val vein = this
  val evaler = JavaEmbedUtils.newRuntimeAdapter()
  val threadContext = ThreadContext.newContext(runtime)
  
  /** Returns a new Rubyvein initialized from a Seq of load paths
   *  
   *  @param loadpaths paths to load for Ruby VM 
   */
  def this(loadpaths : Seq[String]) = this(JavaEmbedUtils.initialize(loadpaths.asJava))
  
  /** Returns a new Rubyvein initialized with a new Ruby VM */
  def this() = this(JavaEmbedUtils.initialize(new java.util.ArrayList()))
  
  /** Return the result of an evaluated ruby script
   *  represented as a string
   *  
   *  @param source ruby code to evaluate
   */
  def eval(source : String) = evaler.eval(runtime, source)
  
  /** Returns the result of the invocation of a method
   *  or function on the provided IRubyObject receiver
   *  with the given method identifier and arguments list
   *  
   *  @param receiver ruby object to invoke method or function on
   *  @param id identifier of the method or function to invoke
   *  @param args arguments to send to the method or function
   */
  private def invoke(receiver : IRubyObject, id : String, args : AnyRef*): AnyRef =
    JavaEmbedUtils.invokeMethod(runtime, receiver, id, args.toArray, classOf[Any])
  
  /** Invokes a method returning no value on a ruby object
   *  
   *  @param id identifier of the method
   *  @param receiver ruby object to send message to 
   */
  def callMethod(id : String)(receiver : IRubyObject) = 
    invoke(receiver, id)
    
  /** Invokes a method returning no value on a ruby object 
   *  
   *  @param id identifier of the method
   *  @param args arguments to send to the method
   *  @param receiver ruby object to send message to
   */
  def callMethod(id : String, args : AnyRef*)(receiver : IRubyObject) =
    invoke(receiver, id, args:_*)
    
  /** Returns the result of a function invoked on a ruby object
   *  
   *  @param id identifier of the function
   *  @param receiver ruby object to send message to
   */
  def callFunction[T](id : String)(receiver : IRubyObject) =
    invoke(receiver, id).asInstanceOf[T]
  
  /** Returns the result of a function invoked on a ruby object
   *  
   *  @param id identifier of the function
   *  @param args arguments to send to the function
   *  @param recevier ruby object to send message to 
   */
  def callFunction[T](id : String, args : AnyRef*)(receiver : IRubyObject) =
    invoke(receiver, id, args:_*).asInstanceOf[T]
  
  /** Returns a new invocable Ruby script from given ruby
   *  source represented as a string
   *  
   *  @param source ruby source code represented as a string 
   */
  def loadScript(source : String): DynamicScript = new DynamicScript(this, source)
  
  /** Returns a new invocable Ruby script from an input
   *  stream referencing a ruby script file 
   *  
   *  @param stream input stream to a ruby script
   */
  def loadScript(stream : java.io.InputStream): DynamicScript = {
    AutoResource.withResource(stream) { (is) =>
      AutoResource.withResource(new scala.io.BufferedSource(is)) { (bs) => 
        new DynamicScript(this, bs.mkString)
      }
    }
  }

  /** Returns a new invocable Ruby script from a
   *  scala.io.Source object referencing a Ruby file
   *
   *  @param source scala.io.Source reference to a Ruby script
   */
  def loadScript(source : scala.io.Source): DynamicScript = {
    AutoResource.withResource(source) { (src) =>
      new DynamicScript(this, src.mkString)
    }
  }

  /** Returns a new invocable Ruby script loaded from
   *  the resource at the given path, as found by getClass.getResource
   *
   *  @param path relative path to ruby script
   */
  def loadScriptFromPath(path : String): DynamicScript = {
    loadScript(io.Source.fromURL(getClass.getResource(path)))
  }
  
  /** Returns a RubyModule with the given module name
   *  currently specified within the scope of the VM
   *  
   *  @param moduleName name of the module to load 
   */
  def getModule(moduleName : String) =
    runtime.getModule(moduleName)
}
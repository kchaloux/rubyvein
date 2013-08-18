import sbt._
import Keys._

object RubyveinBuild extends Build {
  
  object Dependencies {
    lazy val scalaUtils = RootProject(uri("git://github.com/kchaloux/scala-utils.git#master"))
  }

  lazy val substitutions = Project("rubyvein", file("."))
    .dependsOn(Dependencies.scalaUtils)
}


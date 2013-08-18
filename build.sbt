name := "rubyvein"

version := "0.1.1"

scalaVersion := "2.10.0"

libraryDependencies <++= (scalaVersion)(sv =>
  Seq(
    "org.scala-lang" % "scala-reflect" % sv,
    "org.scala-lang" % "scala-compiler" % sv,
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
    "org.jruby" % "jruby-complete" % "1.7.4"
  )
)
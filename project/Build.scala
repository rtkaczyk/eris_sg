import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    name := "ErisSG",
    version := "1.0.1",
    versionCode := 0,
    scalaVersion := "2.9.2",
    platformName in Android := "android-8"
  )

  val proguardSettings = Seq (
    useProguard in Android := true
  )

  lazy val fullAndroidSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    proguardSettings ++
    AndroidManifestGenerator.settings ++
    Seq (
      libraryDependencies += "rtkaczyk.eris" % "api" % "1.0.0"
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    "ErisSG",
    file("."),
    settings = General.fullAndroidSettings
  )
}

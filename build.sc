// -*- mode: scala -*-

import mill._, os._, scalalib._, publish._
import scala.util.Properties

object meta {

  val crossVersions = Seq("3.1.1")

  implicit val wd: Path = pwd

  def nonEmpty(s: String): Option[String] = s.trim match {
    case v if v.isEmpty => None
    case v              => Some(v)
  }

  val MILL_VERSION = Properties.propOrNull("MILL_VERSION")
  val versionFromEnv = Properties.propOrNone("PUBLISH_VERSION")
  val gitSha = nonEmpty(
    proc("git", "rev-parse", "--short", "HEAD").call().out.trim
  )
  val gitTag = nonEmpty(
    proc("git", "tag", "-l", "-n0", "--points-at", "HEAD").call().out.trim
  )
  val publishVersion =
    (versionFromEnv orElse gitTag orElse gitSha).getOrElse("latest")

}

object katan extends Cross[Katan](meta.crossVersions: _*)
class Katan(val crossScalaVersion: String)
    extends CrossScalaModule
    with PublishModule { self =>
  def publishVersion = meta.publishVersion

  def artifactName = "katan"

  def pomSettings = PomSettings(
    description = "A ZIO based Kanren relational programming environment.",
    organization = "com.github.vic",
    url = "https://github.com/vic/katan",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github("vic", "katan"),
    developers = Seq(
      Developer("vic", "Victor Borja", "https://github.com/vic")
    )
  )

  def ivyDeps = Agg(
    ivy"dev.zio::zio:2.0.0-RC2",
    ivy"dev.zio::zio-prelude:1.0.0-RC10"
  )

  object test extends Tests {
    def testFramework = "zio.test.sbt.ZTestFramework";
    def ivyDeps = Agg(
      ivy"dev.zio::zio-test-sbt:2.0.0-RC2"
    )
  }

  // def compileIvyDeps = Agg(
  //  ivy"com.lihaoyi::mill-scalalib:${meta.MILL_VERSION}"
  // )

  // object tests extends Tests with TestModule.Utest {
  //  def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.11") ++ self.compileIvyDeps()
  // }
}

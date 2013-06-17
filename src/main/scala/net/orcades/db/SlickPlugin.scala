package net.orcades.db

import sbt._
import Keys._
import sbt.Plugin

class SlickPlugin extends Plugin with SlickKeys with SlickTasks {
  override lazy val settings = Seq(
    slickModelDirectory <<= baseDirectory { base => base / "app" / "models" },
    slick <<= slickGenerateTask
    )
}
package net.orcades.db

import sbt.TaskKey
import sbt.SettingKey

trait SlickKeys {
  val id = "slick"
  val slickModelDirectory = SettingKey[java.io.File](id + "-directory")
  val slick = TaskKey[Unit](id + "-generate-daos", "Generate slick daos")
}
package net.orcades.db

import sbt.Keys._
import sbt.ConfigKey.configurationToKey
import sbt.Scoped.t2ToTable2
import sbt.State.stateOps
//import scala.slick.session.Database

import net.orcades.db.EntitiesMapper._

trait SlickTasks extends SlickKeys {

  lazy val slickGenerateTask = (state, slickModelDirectory) map { (state, slickDir) =>

//    Database.forURL("jdbc:postgresql:jug", "test", "test", driver = "org.postgresql.Driver") withSession {
//      buildAll
//    }

  }

}
package net.orcades.db

import com.typesafe.config.ConfigFactory
import java.sql.DriverManager
import net.orcades.db.EntitiesMapper._
import sbt._
import sbt.ConfigKey.configurationToKey
import sbt.Keys._
import sbt.Scoped.t2ToTable2
import sbt.State.stateOps

trait SlickTasks extends SlickKeys {

  lazy val slickGenerateTask = (state, baseDirectory, slickModelDirectory) map { (state, base, model) =>

    val appConf = base / "conf" / "application.conf"
    
    val conf = ConfigFactory.parseFile(appConf)
    
    val driver = conf.getString("db.default.driver")
    val user = conf.getString("db.default.user")
    val url = conf.getString("db.default.url")
    val pwd = conf.getString("db.default.password")
    
    Class.forName(driver)

    val conn = DriverManager.getConnection(url, user, pwd)

    buildAll(conn, model)

  }

}
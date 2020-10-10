name := "ml"

version := "0.1"

scalaVersion := "2.12.12"
val sparkVersion = "2.4.2"
val scalatestVersion = "3.2.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-mllib" % sparkVersion
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % sparkVersion

libraryDependencies += "org.scalactic" %% "scalactic" % scalatestVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test"
libraryDependencies += "com.typesafe" % "config" % "1.4.0"
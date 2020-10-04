name := "wizzard_ml"

version := "0.1"

scalaVersion := "2.12.12"
val sparkVersion = "2.4.3"

libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-mllib" % sparkVersion

name := "reactive-api"

version := "1.0"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" % "play_2.10" % "2.2.1",
  "com.typesafe.play" % "play-cache_2.10" % "2.2.1",
  "com.typesafe.play" % "play-exceptions" % "2.2.1",
  "com.typesafe.play" % "play-functional_2.10" % "2.2.1",
  "com.typesafe.play" % "play-iteratees_2.10" % "2.2.1",
  "com.typesafe.play" % "play-test_2.10" % "2.2.1"
)

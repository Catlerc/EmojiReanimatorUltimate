ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val ScalaJsReactVer = "2.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "EmojiReanimator",

    libraryDependencies ++= Seq(

      // Optionally include scalajs-react Callback classes
      // (Note: these need to come before "core-bundle-cats_effect")
      "com.github.japgolly.scalajs-react" %%% "callback" % ScalaJsReactVer,
      "com.github.japgolly.scalajs-react" %%% "callback-ext-cats" % ScalaJsReactVer,
      "com.github.japgolly.scalajs-react" %%% "callback-ext-cats_effect" % ScalaJsReactVer,

      // Mandatory
      "com.github.japgolly.scalajs-react" %%% "core-bundle-cats_effect" % ScalaJsReactVer,

      // Optional utils exclusive to scalajs-react
      "com.github.japgolly.scalajs-react" %%% "extra" % ScalaJsReactVer,

      // Optional extensions to `core` & `extra` for Monocle
      "com.github.japgolly.scalajs-react" %%% "extra-ext-monocle2" % ScalaJsReactVer,
      "com.github.japgolly.scalajs-react" %%% "extra-ext-monocle3" % ScalaJsReactVer,

      // For unit tests
      "com.github.japgolly.scalajs-react" %%% "test" % ScalaJsReactVer % Test,
    ),


    Compile / fastOptJS / artifactPath := baseDirectory.value / "src/index.js",
    Compile / fastOptJS / scalaJSLinkerConfig ~= {
      _.withSourceMap(false)
    },

    Compile / fullOptJS / artifactPath := baseDirectory.value / "src/index.js",
    Compile / fullOptJS / scalaJSLinkerConfig ~= {
      _.withSourceMap(false)
    },
    jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv(),


    scalaJSUseMainModuleInitializer := true,

    Compile / npmDependencies ++= Seq(
      "react" -> "17.0.2",
      "react-dom" -> "17.0.2")


  ).enablePlugins(ScalaJSPlugin).enablePlugins(ScalaJSBundlerPlugin)



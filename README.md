[![Build Status](https://travis-ci.org/navicore/navilake.svg?branch=master)](https://travis-ci.org/navicore/navilake)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1901174b92304a8d98ce2d8b64f4d9dc)](https://www.codacy.com/app/navicore/navilake?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=navicore/navilake&amp;utm_campaign=Badge_Grade)

# Read Azure Data Lake Storage into Akka Streams

Replay historical data-at-rest into an
existing code base that had been designed for streaming.

## Current Storage Sources
1.  GZip files of UTF8 `\n` delimited strings
2.  Other storage implementations TBD

Uses the [adslapi].

## USAGE

update your `build.sbt` dependencies with:

```scala
// https://mvnrepository.com/artifact/tech.navicore/navilake
libraryDependencies += "tech.navicore" %% "navilake" % "0.9.0"
```

This example reads gzip data from Azure Data Lake.

Create a config, a connector, and a source via the example below.

```scala
    val consumer = ... // some Sink
    ...
    ...
    ...
    // credentials and location
    implicit val cfg: LakeConfig = LakeConfig(ACCOUNTFQDN, CLIENTID, AUTHEP, CLIENTKEY, Some(PATH))
    val connector: ActorRef = actorSystem.actorOf(GzipConnector.props)
    val src = NaviLake(connector)
    ...
    ...
    ...
    src.runWith(consumer)
    ...
    ...
    ...
```

## OPS

### publish local

```console
sbt +publishLocalSigned
```

### publish to nexus staging

```console
export GPG_TTY=$(tty)
sbt +publishSigned
sbt sonatypeReleaseAll
```

---

[adslapi]:https://docs.microsoft.com/en-us/azure/data-lake-store/data-lake-store-get-started-java-sdk#read-a-file

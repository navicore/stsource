[![Build Status](https://travis-ci.org/navicore/stsource.svg?branch=master)](https://travis-ci.org/navicore/stsource)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/958b74b9d6d1441d8314d2468383dda7)](https://app.codacy.com/app/navicore/stsource?utm_source=github.com&utm_medium=referral&utm_content=navicore/stsource&utm_campaign=Badge_Grade_Settings)

A StringTemplate4 Akka Streams source producing template driven test data
------

See [stringtemplate4].

# UNDER CONSTRUCTION

# UNDER CONSTRUCTION

# UNDER CONSTRUCTION


## USAGE

update your `build.sbt` dependencies with:

```scala
// https://mvnrepository.com/artifact/tech.navicore/stsource
libraryDependencies += "tech.navicore" %% "stsource" % "TBD"
```

Create a config, a connector, and a source via the example below.

```scala
    val consumer = ... // some Sink
    ...
    ...
    ...
    implicit val cfg: StConfig = StConfig()
    val connector: ActorRef = actorSystem.actorOf(StConnector.props)
    val src = StSource(connector)
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

[stringtemplate4]:https://github.com/antlr/stringtemplate4

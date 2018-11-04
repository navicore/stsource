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

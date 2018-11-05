[![Build Status](https://travis-ci.org/navicore/stsource.svg?branch=master)](https://travis-ci.org/navicore/stsource)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/958b74b9d6d1441d8314d2468383dda7)](https://app.codacy.com/app/navicore/stsource?utm_source=github.com&utm_medium=referral&utm_content=navicore/stsource&utm_campaign=Badge_Grade_Settings)

A StringTemplate4 Akka Streams source producing template driven test data
------

See [stringtemplate4].

## USAGE

update your `build.sbt` dependencies with:

```scala
// https://mvnrepository.com/artifact/tech.navicore/stsource
libraryDependencies += "tech.navicore" %% "stsource" % "1.0.0"
```

Use any ST group file - assuming `UTF8` and '<' '>' delimiters.  For example, `/iotjson.stg`:
```
decl(type, deviceId, module, value) ::= "{<initType(type)>, <initDeviceId(deviceId)>, <initModule(module)>, <initValue(value)>}"
initType(v) ::= "<if(v)>\"type\": \"<v>\"<endif>"
initDeviceId(v) ::= "<if(v)>\"deviceId\": \"<v>\"<endif>"
initModule(v) ::= "<if(v)>\"module\": \"<v>\"<endif>"
initValue(v) ::= "<if(v)>\"value\": <v><endif>"
```

Create a config and a source as in the example below.

```scala
    import navicore.akka.stsource._
    ...
    ...
    ...
    val consumer = ... // some Sink that prints `String` input to console
    ...
    ...
    ...
    val stgUrl: URL = getClass.getResource("/iotjson.stg")
    implicit val cfg: StConfig =
      StConfig(100,                        // 100 lines, use 0 for infinite
               stgUrl,
               Map(
                 ("type", List("observation", "error", "heartbeat")),
                 ("deviceId", List("d1", "d2", "d3", "d4")),
                 ("module", List("temp1", "temp2", "waterLvl1")),
                 ("value", List("24.0", "20.8", "19.0", "68.1"))
               ))
    val src = StSource()
    ...
    ...
    ...
    src.runWith(consumer)
    ...
    ...
    ...
```

Above produces 100 lines like below:

```json
{"type": "error", "deviceId": "d4", "module": "temp2", "value": 68.1}
{"type": "heartbeat", "deviceId": "d2", "module": "waterLvl1", "value": 20.8}
{"type": "error", "deviceId": "d3", "module": "temp2", "value": 19.0}
{"type": "heartbeat", "deviceId": "d3", "module": "waterLvl1", "value": 19.0}
{"type": "observation", "deviceId": "d1", "module": "temp1", "value": 24.0}
{"type": "error", "deviceId": "d4", "module": "temp2", "value": 68.1}
{"type": "observation", "deviceId": "d1", "module": "temp1", "value": 24.0}
{"type": "heartbeat", "deviceId": "d4", "module": "waterLvl1", "value": 68.1}
{"type": "error", "deviceId": "d2", "module": "temp2", "value": 20.8}
{"type": "heartbeat", "deviceId": "d3", "module": "waterLvl1", "value": 19.0}
{"type": "error", "deviceId": "d3", "module": "temp2", "value": 19.0}
{"type": "heartbeat", "deviceId": "d2", "module": "waterLvl1", "value": 20.8}
{"type": "error", "deviceId": "d4", "module": "temp2", "value": 68.1}
{"type": "error", "deviceId": "d1", "module": "temp2", "value": 24.0}
{"type": "observation", "deviceId": "d3", "module": "temp1", "value": 19.0}
{"type": "heartbeat", "deviceId": "d4", "module": "waterLvl1", "value": 68.1}
{"type": "error", "deviceId": "d1", "module": "temp2", "value": 24.0}
```

## TODO

1. support stg file paths in addition to URL.
2. support abstract stg file sources instead of ST's current requirement of a file path or URL - might require a fork of StringTemplate4.

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

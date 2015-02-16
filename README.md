# yetu play authenticator

A Scala/Playframework library using [Mohiva Silhouette](https://github.com/mohiva/play-silhouette) to "log in with yetu".

This small library is intended to help you get started building an app that authorizes with and fits into the yetu ecosystem.

This library is based on, and takes code from [play-silhouette-seed](https://github.com/mohiva/play-silhouette-seed) (Thanks!)
It uses OAuth2

## How to use this library from your Scala/Play application:

### get a pair of clientId / clientSecret from yetu to authorize your app:

TODO fill this section?

### in your `project/plugins.sbt` file add:

```
resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")

```

### in your `build.sbt` add:

```
bintrayResolverSettings

resolvers += bintray.Opts.resolver.mavenRepo("yetu")

//substitute <VERSION> here with the latest stable release
libraryDependencies += "com.yetu" %% "yetu-play-authenticator" % "<VERSION>"

```

### in your `conf/routes` add:


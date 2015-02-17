# yetu play authenticator

A Scala / Play Framework library using [Mohiva Silhouette](https://github.com/mohiva/play-silhouette) to "log in with yetu".

This library is intended to help you get started building an app that authorizes with and fits into the yetu ecosystem.

This library is based on, and takes code from [play-silhouette-seed](https://github.com/mohiva/play-silhouette-seed) (Thanks!)
It uses OAuth2

## Usage for a Scala / Play Framework application:

##### get a pair of clientId / clientSecret from yetu to authorize your app:

Currently please contact dev-support@yetu.com to obtain OAuth2 credentials.

For more information on how to develop yetu apps, please see [this page](https://github.com/yetu/app-development-workflow/wiki/How-to-develop-Apps-for-the-yetu-platform%3F)


##### in your `project/plugins.sbt` file add:

```
resolvers += Resolver.url(
  "bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")

```

##### in your `build.sbt` add:

```
bintrayResolverSettings

resolvers += bintray.Opts.resolver.mavenRepo("yetu")

//substitute <VERSION> here with the latest stable release
libraryDependencies += "com.yetu" %% "yetu-play-authenticator" % "<VERSION>"

```

##### in your `conf/routes` add:

```
# Silhouette / Authentication routes
GET         /signOut                       @com.yetu.play.authenticator.controllers.ApplicationController.signOut
GET         /authenticate/:provider        @com.yetu.play.authenticator.controllers.SocialAuthController.authenticate(provider)
```

## Example app:

For more information, also have a look at the [youtube-html-app](https://github.com/yetu/youtube-html-app) to see how this library can be used.

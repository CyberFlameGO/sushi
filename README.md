# 🍣 sushi
> Simple Ktor server to handle GitHub Issues to YouTrack, usually for YouTrack Standalone.

## Why?
There is not really a definite way to handle GitHub Issues towards YouTrack Standalone. This is basically
a webhook that listens on issue events (created, closed, comment add, etc) to post them onto YouTrack.

## Installation
You only really need **Java 16** or higher to run sushi since it's a stateless microservice, it doesn't persist
anything.

I provide a Docker image if you wish to run sushi on Docker.

```sh
# Docker installation
$ docker pull noelware/sushi && docker run -d -p 3333:3333 \
  -v /path/to/config.json:/app/sushi/config.json \
  noelware/sushi
 
# Git repository installation
$ git clone https://github.com/Noelware/sushi && cd sushi
$ ./gradlew build
$ java -jar ./build/libs/sushi.jar
```

## License
**sushi** is released under the **GPL-3.0** License.

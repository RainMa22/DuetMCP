# DuetMCP

The AI MCP server with no AI code contributions(for now).

> This repo is powered by pure hubris and stupidity,


## Running/Building/Distributing

DuetMCP requires JDK 21(or later).

> See `app/build.gradle` for the libraries used by this project.

For running the MCP from the cloned repository:

```bash
./gradlew run
```

> see [CONFIGURATION.md](./CONFIGURATION.md) for more configuration options for running DuetMCP

For building and distributing(fat jar via ShadowJar plugin) from the cloned repository:

```bash
./gradlew shadowJar
```

the built jar will be `./app/generated/libs/apps-all.jar`

Running the built jar would be:

```bash
java -jar <path_to_apps-all.jar>
```

> see [CONFIGURATION.md](./CONFIGURATION.md) for more configuration options for running DuetMCP


## Adding Your Own Plugins to DuetMCP

Coming soon
# java-discord-rpc

This library contains Java bindings for [Discord's official Game SDK](https://discord.com/developers/docs/developer-tools/game-sdk) using JNA.

## Documentation

You can see the official discord documentation in the [API Documentation](https://discord.com/developers/docs/developer-tools/game-sdk).

## Setup

In the following please replace `%VERSION%` with the version listed above.
Latest version: ``2024-3.1``

### Gradle

**Repository**

```gradle
repositories {
    mavenCentral()
    jcenter()
    maven { url = 'https://jitpack.io' }
}
```

**Artifact**

```gradle
dependencies {
    implementation 'com.github.cavespiderclient:java-discord-rpc:%VERSION%'
}
```

### Compile Yourself

1. Install git and JDK 8+
2. `git clone https://github.com/cavespiderclient/java-discord-rpc`
3. `cd java-discord-rpc`
4. `./gradlew build` or on windows `gradlew build`
5. Get the jar from `build/libs` with the name `java-discord-rpc-%VERSION%-all.jar`

## Examples

### Basics

The library can be used just like the SDK. This means you can almost copy the exact code used in the official documentation.

```java
import cavespider.discord.rpc.DiscordGameSDK.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("You must specify an application ID in the arguments!");
            System.exit(-1);
        }
        DiscordCore core = new DiscordCore(args[0], DiscordCreateFlags.Default);
        DiscordActivityManager activityManager = core.getActivityManager();
        DiscordActivity activity = new DiscordActivity();
        activity.setDetails("Details here");
        activity.setState("State here");
        activity.timestamps().setStart(System.currentTimeMillis() / 1000);
        activity.timestamps().setEnd(activity.timestamps().getStart() + 20);
        activity.party().size().setCurrentSize(1);
        activity.party().size().setMaxSize(4);
        activityManager.updateActivity(activity, (result) -> {
            if (result != DiscordResult.Ok) {
                System.err.println("Failed to update activity: " + result);
            }
        });

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                core.runCallbacks();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    core.close();
                    break;
                }
            }
        }, "RPC-Callback-Handler");
        t.start();
    }
}
```

## License

java-discord-rpc is licensed under the Apache 2.0 License.

## Contributing

Find something that is lacking? Fork the project and pull request!

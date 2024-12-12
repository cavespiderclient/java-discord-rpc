/*
 * Copyright 2016 - 2019 Florian Spieß and the java-discord-rpc contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("Main")
package club.minnced.discord.rpc.example

import com.discord.GameSDK.*
import com.discord.GameSDK.DiscordActivityManager.*
import com.discord.GameSDK.DiscordUserManager.*
import java.io.File
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    println("Initializing...")
    // if .application-id doesn't exist we hard fail here
    val applicationId = File(".application-id").readText()
    val steamId: String? = with (File(".steam-id")) {
        if (exists())
            readText()
        else
            null
    }

    println("Starting RPC...")
    val core = DiscordCore(applicationId, DiscordCreateFlags.Default)
    val activityManager = core.getActivityManager()
    val activity = DiscordActivity().apply {
        details = "Undisclosed"
        state = "Free 4 All"
        timestamps().start = System.currentTimeMillis() / 1000
        largeImageKey = "map-desert"
        largeImageText = "Desert"
        smallImageKey = "char-robert"
        smallImageText = "Robert"
    }

    activityManager.updateActivity(activity) { result ->
        if (result != DiscordResult.Ok) {
            println("Failed to update activity: $result")
        }
    }

    thread(name="RPC-Callback-Handler") {
        while (!Thread.currentThread().isInterrupted) {
            core.runCallbacks()
            Thread.sleep(2000)
        }
    }
}

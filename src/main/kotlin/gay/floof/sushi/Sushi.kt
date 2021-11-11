/**
 * 🍣 sushi: simple github to youtrack proxy.
 * Copyright (c) 2021 Noelware <cutie@floofy.dev>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package gay.floof.sushi

import gay.floof.sushi.data.SushiConfig
import gay.floof.sushi.kotlin.logging
import gay.floof.sushi.types.OkResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.lang.management.ManagementFactory
import java.security.Security

class Sushi {
    private lateinit var server: NettyApplicationEngine
    private val logger by logging<Sushi>()
    private val config = SushiConfig.load()

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    fun launch() {
        val runtime = Runtime.getRuntime()
        val os = ManagementFactory.getOperatingSystemMXBean()
        val dedi = System.getProperty("winterfox.dedi", "")

        logger.info("Launching Sushi server... displaying runtime information:")
        logger.info("========================")
        logger.info("|> Free / Total Memory: ${runtime.freeMemory() / 1024 / 1024}MB/${runtime.totalMemory() / 1024 / 1024}MB")
        logger.info("|> Operating System:    ${os.name} (${os.version}; x${os.arch})")
        logger.info("|> Sushi Info:          ${SushiInfo.VERSION} (commit: ${SushiInfo.COMMIT}; ${SushiInfo.BUILD_DATE})")

        if (dedi.isNullOrEmpty()) logger.info("|> Dedi Node: $dedi")
        logger.info("========================")

        server = embeddedServer(Netty, port = config.port, host = config.host) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(CallLogging)

            install(Routing) {
                get("/") {
                    call.respond(HttpStatusCode.OK, OkResponse(ok = true))
                }

                post("/") {
                    call.respond(HttpStatusCode.OK, OkResponse(ok = true))
                }
            }
        }

        server.addShutdownHook {
            logger.warn("Shutting down Sushi server...")
            server.stop(1L, 1000L)
        }

        server.start(wait = true)
    }
}

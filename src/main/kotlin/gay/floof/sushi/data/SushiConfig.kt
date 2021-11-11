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

package gay.floof.sushi.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class SushiConfig(
    @SerialName("youtrack_uri")
    val youtrackUrl: String,
    val secret: String,
    val host: String = "0.0.0.0",
    val port: Int = 3333
) {
    companion object {
        fun load(): SushiConfig = Json.decodeFromString(serializer(), File("./config.json").readText())
    }
}

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

import java.util.*

object SushiInfo {
    val BUILD_DATE: String
    val VERSION: String
    val COMMIT: String

    init {
        val stream = this::class.java.getResourceAsStream("/build-info.properties") ?: error("Unable to retrieve build info props.")
        val props = Properties().apply { load(stream) }

        BUILD_DATE = props.getProperty("app.build.date", "unknown")
        VERSION = props.getProperty("app.version", "0.0.0")
        COMMIT = props.getProperty("app.commit", "unknown")
    }
}

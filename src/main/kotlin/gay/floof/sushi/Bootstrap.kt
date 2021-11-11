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

import gay.floof.sushi.kotlin.logging
import kotlinx.coroutines.runBlocking

object Bootstrap {
    private val logger by logging<Bootstrap>()

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("                _     _")
        logger.info("  ___ _   _ ___| |__ (_)")
        logger.info(" / __| | | / __| '_ \\| |")
        logger.info(" \\__ \\ |_| \\__ \\ | | | |")
        logger.info(" |___/\\__,_|___/_| |_|_|")
        logger.info("=================================")

        val sushi = Sushi()
        runBlocking {
            sushi.launch()
        }
    }
}

package tests.asserting

import mcfilelib.generic.Screenshot
import neatlin.div
import neatlin.toi8
import java.nio.file.Path
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

fun screenshots(screenshots: Path) {
    val time = measureTimeMillis {
        val screenshot1 = Screenshot(screenshots/"2021-03-02_15.59.30.png")
        assertTrue { screenshot1.time.year == 2021L && screenshot1.time.month == 3.toi8() && screenshot1.time.day == 2.toi8() && screenshot1.time.hour == 15.toi8() && screenshot1.time.minute == 59.toi8() && screenshot1.time.second == 30.toi8() }
        val screenshot2 = Screenshot(screenshots/"2021-03-02_15.59.43.png")
        assertTrue { screenshot2.time.year == 2021L && screenshot2.time.month == 3.toi8() && screenshot2.time.day == 2.toi8() && screenshot2.time.hour == 15.toi8() && screenshot2.time.minute == 59.toi8() && screenshot2.time.second == 43.toi8() }
    }
    println("Screenshots test finished in ${time}ms")
}
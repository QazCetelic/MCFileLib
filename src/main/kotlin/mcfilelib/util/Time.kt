package mcfilelib.util

/**
 * Simple class to store time for screenshots
 */
class Time (
    val year: Long,
    val month: Byte,
    val day: Byte,
    val hour: Byte,
    val minute: Byte,
    val second: Byte
) {
    /**
     * Returns "YEAR-MONTH-DAY HOUR:MINUTE:SECOND"
     */
    override fun toString() = "$year-$month-${day} $hour:$minute:$second"
}
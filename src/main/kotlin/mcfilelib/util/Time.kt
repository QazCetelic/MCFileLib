package mcfilelib.util

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

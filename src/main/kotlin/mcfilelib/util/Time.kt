package mcfilelib.util

class Time (
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Byte,
    val minute: Byte,
    val second: Byte
) {
    /**
     * Returns "YEAR-MONTH-DAY_HOUR.MINUTE.SECOND"
     * It uses different characters in between because that's done by Minecraft itself, the string should be the same as the file name without extension.
     */
    override fun toString() = "$year-$month-${day}_$hour.$minute.$second"
}

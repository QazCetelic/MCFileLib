package classes.used

data class Time (
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Byte,
    val minute: Byte,
    val second: Byte
) {
    override fun toString() = "$year-$month-${day}_$hour.$minute.$second"
}

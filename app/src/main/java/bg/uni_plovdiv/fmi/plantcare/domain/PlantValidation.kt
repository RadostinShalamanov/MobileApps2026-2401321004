package bg.uni_plovdiv.fmi.plantcare.domain

object PlantValidation {
    const val MIN_INTERVAL = 1
    const val MAX_INTERVAL = 365

    fun isValidName(name: String?): Boolean = !name.isNullOrBlank()


    fun parseInterval(text: String?): Int? =
        text?.trim()?.toIntOrNull()?.takeIf { it in MIN_INTERVAL..MAX_INTERVAL }
}
package io.github.gdrfgdrf.cutebedwars.commons

import io.github.gdrfgdrf.cutebedwars.abstracts.commons.IConstants
import io.github.gdrfgdrf.multimodulemediator.annotation.ServiceImpl

@ServiceImpl("constants")
object Constants : IConstants {
    const val BASE_FOLDER = "cute-bedwars/"
    const val CONFIG_FILE_NAME = "config.json"

    const val ANOTHER_CONFIG_PATH = BASE_FOLDER + "configs/"

    const val LANGUAGE_FOLDER = BASE_FOLDER + "languages"
    const val DATABASE_IMPL_DESCRIPTION_FILE_NAME = "database-impl.json"
    const val CUSTOM_DATABASE_IMPL_FOLDER_NAME = BASE_FOLDER + "custom-database-impl/"

    const val AREA_FOLDER = BASE_FOLDER + "areas/"

    const val GLOBAL_TIMEOUT = 30000L

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String): T {
        val declaredField = Constants::class.java.getDeclaredField(key)
        declaredField.isAccessible = true
        return declaredField.get(this) as T
    }
}
package io.github.gdrfgdrf.cutebedwars.works

import io.github.gdrfgdrf.cutebedwars.beans.Config
import io.github.gdrfgdrf.cutebedwars.commons.common.Constants
import io.github.gdrfgdrf.cutebedwars.commons.extension.logInfo
import io.github.gdrfgdrf.cutebedwars.database.Database
import io.github.gdrfgdrf.cutebedwars.holders.javaPluginHolder
import io.github.gdrfgdrf.cutebedwars.locale.CustomClassLoader
import io.github.gdrfgdrf.cutebedwars.locale.collect.CommandDescriptionLanguage
import io.github.gdrfgdrf.cuteframework.CuteFramework
import io.github.gdrfgdrf.cuteframework.config.ConfigManager
import io.github.gdrfgdrf.cuteframework.locale.LanguageLoader
import io.github.gdrfgdrf.cuteframework.minecraftplugin.CuteFrameworkSupport
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object Loader {
    fun load(javaPlugin: JavaPlugin) {
        createFolders()

        CuteFrameworkSupport.load(javaPlugin)
        javaPluginHolder().set(javaPlugin)

        loadConfig()
        loadLanguage()

        Database.getInstance().initialize()
    }

    private fun createFolders() {
        val baseFolder = File(Constants.BASE_FOLDER)
        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }
    }

    private fun loadConfig() {
        "Loading the configuration file".logInfo()

        Config.INSTANCE = ConfigManager.getInstance().load(
            Constants.OWNER,
            Constants.CONFIG_FILE_NAME,
            Config::class.java
        )
    }

    private fun loadLanguage() {
        "Loading the language".logInfo()

        LanguageLoader.getInstance().load(
            Loader::class.java.classLoader,
            "io.github.gdrfgdrf.cutebedwars.locale.collect",
            "io.github.gdrfgdrf.cutebedwars.locale.language",
            Constants.OWNER,
            Config.INSTANCE.language,
        )
    }
}
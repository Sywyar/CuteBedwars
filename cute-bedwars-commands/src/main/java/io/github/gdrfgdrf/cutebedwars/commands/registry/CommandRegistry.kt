package io.github.gdrfgdrf.cutebedwars.commands.registry

import io.github.gdrfgdrf.cutebedwars.abstracts.commands.CommandRegistry
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.ICommands
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.IPermissionGroups
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.IPermissions
import io.github.gdrfgdrf.cutebedwars.commands.RootCommand
import io.github.gdrfgdrf.cutebedwars.commands.manager.SubCommandManager
import io.github.gdrfgdrf.cutebedwars.holders.javaPluginHolder
import io.github.gdrfgdrf.cutebedwars.utils.extension.logInfo
import io.github.gdrfgdrf.multimodulemediator.annotation.ServiceImpl
import org.bukkit.Bukkit

@ServiceImpl("command_registry")
object CommandRegistry : CommandRegistry {
    override fun registerCommands() {
        "Registering the root command ${ICommands.valueOf("ROOT").string()}".logInfo()

        javaPluginHolder().get().getCommand(ICommands.valueOf("ROOT").string()).executor = RootCommand
        javaPluginHolder().get().getCommand("cutebedwars").executor = RootCommand

        SubCommandManager.scanAndRegister()

        val userPermission = IPermissionGroups.valueOf("USER").get()
        val administratorPermission = IPermissionGroups.valueOf("ADMIN").get()

        IPermissions.values().forEach { permissions ->
            if (permissions !is IPermissions) {
                return
            }

            if (permissions.needOps()) {
                permissions.putToGroup(administratorPermission)
                return@forEach
            }
            permissions.putToGroup(userPermission)
        }

        Bukkit.getPluginManager().addPermission(userPermission)
        Bukkit.getPluginManager().addPermission(administratorPermission)
    }
}
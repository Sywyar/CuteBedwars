package io.github.gdrfgdrf.cutebedwars.commands.registry

import io.github.gdrfgdrf.cutebedwars.abstracts.commands.CommandRegistry
import io.github.gdrfgdrf.cutebedwars.commands.RootCommand
import io.github.gdrfgdrf.cutebedwars.commands.manager.SubCommandManager
import io.github.gdrfgdrf.cutebedwars.commons.enums.CommandPermissions
import io.github.gdrfgdrf.cutebedwars.commons.enums.Commands
import io.github.gdrfgdrf.cutebedwars.commons.extension.logInfo
import io.github.gdrfgdrf.cutebedwars.holders.javaPluginHolder
import org.bukkit.Bukkit

object CommandRegistry : CommandRegistry() {
    override fun registerCommands() {
        "Registering the root command ${Commands.ROOT.string}".logInfo()

        javaPluginHolder().get().getCommand(Commands.ROOT.string).executor = RootCommand
        javaPluginHolder().get().getCommand("cutebedwars").executor = RootCommand

        SubCommandManager.scanAndRegister()

        val userPermission = CommandPermissions.Groups.USER.get()
        val administratorPermission = CommandPermissions.Groups.ADMIN.get()

        SubCommandManager.forEach { _, subCommand ->
            val permissions = subCommand.command.permissions

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
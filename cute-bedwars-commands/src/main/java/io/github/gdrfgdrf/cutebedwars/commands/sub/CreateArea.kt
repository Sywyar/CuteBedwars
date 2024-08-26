package io.github.gdrfgdrf.cutebedwars.commands.sub

import io.github.gdrfgdrf.cutebedwars.abstracts.enums.ICommands
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.IRequestTypes
import io.github.gdrfgdrf.cutebedwars.abstracts.game.management.IManagers
import io.github.gdrfgdrf.cutebedwars.abstracts.requests.IRequests
import io.github.gdrfgdrf.cutebedwars.commands.base.SubCommand
import io.github.gdrfgdrf.cutebedwars.languages.collect.CommandDescriptionLanguage
import io.github.gdrfgdrf.cutebedwars.languages.collect.CommandSyntaxLanguage
import io.github.gdrfgdrf.cutebedwars.languages.collect.CommonLanguage
import io.github.gdrfgdrf.cutebedwars.locale.localizationScope
import io.github.gdrfgdrf.cuteframework.locale.LanguageString
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

object CreateArea : SubCommand(
    command = ICommands.valueOf("CREATE_AREA")
){
    override fun syntax(): LanguageString? = CommandSyntaxLanguage.CREATE_AREA
    override fun description(): LanguageString? = CommandDescriptionLanguage.CREATE_AREA

    override fun run(sender: CommandSender, args: Array<String>, pageSchemeIndex: Int) {
        localizationScope(sender) {
            val areaName = args[0]
            val managers = IManagers.get()
            val requests = IRequests.get()

            val sameNameArea = managers.get(areaName)
            if (sameNameArea != null && sameNameArea.size >= 1) {
                val pair = requests.auto(type = IRequestTypes.valueOf("CREATE_AREA"), sender = sender)
                val new = pair.first
                val request = pair.second

                if (new) {
                    message(CommonLanguage.DUPLICATE_AREA_NAME_WARNING)
                        .format(areaName, TimeUnit.SECONDS.convert(request.timeout(), request.timeUnit()))
                        .send()
                    return@localizationScope
                }
            }
            requests.removeForAuto(type = IRequestTypes.valueOf("CREATE_AREA"), sender = sender)

            message(CommonLanguage.CREATING_AREA)
                .format(areaName)
                .send()

            val areaManager = managers.createArea(areaName)
            if (managers.get(areaManager.area().id) != null) {
                message(CommonLanguage.DUPLICATE_AREA_ID_ERROR)
                    .format(areaManager.area().id)
                    .send()
                return@localizationScope
            }

            managers.register(areaManager)

            message(CommonLanguage.CREATE_AREA_FINISHED)
                .format(areaName, areaManager.area().id)
                .send()
        }
    }
}
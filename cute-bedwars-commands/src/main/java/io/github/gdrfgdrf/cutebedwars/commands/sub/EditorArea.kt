package io.github.gdrfgdrf.cutebedwars.commands.sub

import io.github.gdrfgdrf.cutebedwars.abstracts.enums.ICommands
import io.github.gdrfgdrf.cutebedwars.abstracts.editing.IEditors
import io.github.gdrfgdrf.cutebedwars.abstracts.commands.AbstractSubCommand
import io.github.gdrfgdrf.cutebedwars.abstracts.commands.IParamCombination
import io.github.gdrfgdrf.cutebedwars.abstracts.utils.uuid
import io.github.gdrfgdrf.cutebedwars.commands.finder.BetterAreaFinder
import io.github.gdrfgdrf.cutebedwars.languages.collect.CommandDescriptionLanguage
import io.github.gdrfgdrf.cutebedwars.languages.collect.CommandSyntaxLanguage
import io.github.gdrfgdrf.cutebedwars.languages.collect.EditorLanguage
import io.github.gdrfgdrf.cutebedwars.abstracts.locale.localizationScope
import io.github.gdrfgdrf.cuteframework.locale.LanguageString
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EditorArea : AbstractSubCommand(
    command = ICommands.valueOf("EDITOR_AREA")
) {
    override fun syntax(): LanguageString? = CommandSyntaxLanguage.EDITOR_AREA
    override fun description(): LanguageString? = CommandDescriptionLanguage.EDITOR_AREA

    override fun run(sender: CommandSender, args: Array<String>, paramCombination: IParamCombination) {
        localizationScope(sender) {
            val findType = paramCombination.findType()
            val identifier = paramCombination.areaIdentifier()
            val uuid = sender.uuid()

            val areaManager = BetterAreaFinder.find(sender, findType!!, identifier) ?: return@localizationScope
            val editor = IEditors.instance().get(uuid)
            if (editor != null) {
                message(EditorLanguage.ALREADY_IN_EDITING_MODE)
                    .send()
                return@localizationScope
            }

            message(EditorLanguage.LOADING_AREA_EDITOR)
                .send()

            runCatching {
                val areaEditor = IEditors.instance().createAreaEditor(uuid, areaManager.context())
                IEditors.instance().put(areaEditor)

                message(EditorLanguage.EDITOR_LOAD_FINISHED)
                    .send()
            }.onFailure {
                it.printStackTrace()
                message(EditorLanguage.EDITOR_LOAD_ERROR)
                    .send()
            }
        }
    }
}
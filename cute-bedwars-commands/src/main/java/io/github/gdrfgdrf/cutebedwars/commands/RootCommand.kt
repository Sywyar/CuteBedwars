package io.github.gdrfgdrf.cutebedwars.commands

import io.github.gdrfgdrf.cutebedwars.abstracts.commands.AbstractSubCommand
import io.github.gdrfgdrf.cutebedwars.abstracts.commands.IParamScheme
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.ICommandNodes
import io.github.gdrfgdrf.cutebedwars.abstracts.enums.ICommands
import io.github.gdrfgdrf.cutebedwars.commands.common.ParamCombination
import io.github.gdrfgdrf.cutebedwars.commands.manager.SubCommandManager
import io.github.gdrfgdrf.cutebedwars.languages.collect.CommandLanguage
import io.github.gdrfgdrf.cutebedwars.abstracts.locale.localizationScope
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object RootCommand : TabExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>,
    ): Boolean {
        if (!"cbw".equals(label, true) && !"cutebedwars".equals(label, true)) {
            return true
        }

        if (args.isEmpty()) {
            val helpCommand = SubCommandManager.get("help") ?: return true
            execute(sender, args, helpCommand)
            return true
        }
        val pair = analyzeArgs(args)
        val subCommand: AbstractSubCommand? = pair.first
        val params = pair.second

        if (subCommand != null) {
            execute(sender, params.toTypedArray(), subCommand)
            return true
        }

        localizationScope(sender) {
            message(CommandLanguage.NOT_FOUND)
                .send()
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>,
    ): MutableList<String> {
        val result = arrayListOf<String>()

        if (args.size == 1) {
            val commandNodes = arrayListOf<String>().apply {
                ICommandNodes.allDisplayOnRootTab().forEach {
                    add(it.string)
                }
                ICommands.allDisplayOnRootTab().forEach {
                    if (!contains(it.string)) {
                        add(it.string)
                    }
                }
            }

            result.addAll(commandNodes)
        } else {
            if (args.size > 1) {
                val analyzeResult = analyzeArgs(args)
                if (analyzeResult.first != null) {
                    val subCommand = analyzeResult.first
                    val paramList = arrayListOf<String>().apply {
                        addAll(analyzeResult.second)
                        removeAt(0)
                    }

                    val paramSchemes = subCommand!!.command.paramSchemes

                    if (!paramSchemes.isNullOrEmpty()) {
                        val providedLength = paramList.size

                        val filterResult = CopyOnWriteArrayList<IParamScheme>()
                        val result2 = arrayListOf<IParamScheme>()

                        paramSchemes.forEach {
                            val length = it.length()
                            if (length >= providedLength) {
                                filterResult.add(it)
                            }
                        }

                        paramList.forEachIndexed { index, realParam ->
                            filterResult.forEach {
                                if (realParam.isEmpty()) {
                                    result2.add(it)
                                    return@forEach
                                }

                                val list = it.params
                                val param = list[index]

                                if (param.validate(sender, paramList.toTypedArray(), index, realParam)) {
                                    result2.add(it)
                                } else {
                                    filterResult.remove(it)
                                }
                            }
                        }

                        result2.forEach { paramScheme ->
                            val params = paramScheme.params
                            val param = params[paramList.size - 1]
                            val paramProvideTab = param.tab(sender, paramList.toTypedArray())
                            if (paramProvideTab.isNotEmpty()) {
                                return paramProvideTab
                            }
                        }
                    }

                    return subCommand.tab(sender, paramList.toTypedArray())
                }
                if (args.contains("args")) {
                    // unreachable
                    return arrayListOf()
                }

                val nodeString = if (args.size == 2) {
                    args[0]
                } else {
                    args[args.size - 2]
                }

                val node = ICommandNodes.find(nodeString)
                val children = arrayListOf<String>().apply {
                    node ?: return@apply

                    ICommandNodes.getChild(node).forEach {
                        add(it.string)
                    }
                    ICommands.getChild(node).forEach {
                        if (!contains(it.string)) {
                            add(it.string)
                        }
                    }
                }
                if (children.isNotEmpty()) {
                    result.addAll(children)
                } else {
                    val deeperNodeString = if (args.size == 2) {
                        args[0]
                    } else {
                        args[args.size - 3]
                    }

                    var needAddArgs = false

                    val deeperNode = ICommandNodes.find(deeperNodeString)?.let { deeperNode ->
                        if (deeperNode.parent == ICommandNodes.valueOf("ARGS")) {
                            needAddArgs = true
                        }
                    }
                    if (deeperNode == null) {
                        ICommands.find(deeperNodeString, ICommandNodes.valueOf("ARGS"))?.let {
                            needAddArgs = true
                        }
                    }

                    if (needAddArgs) {
                        result.add("args")
                    }
                }

                return result
            }
        }

        return result
    }

    @Suppress("UNCHECKED_CAST")
    private fun execute(sender: CommandSender, args: Array<String>, subCommand: AbstractSubCommand) {
        localizationScope(sender) {
            if (subCommand.hasPermission(sender)) {
                if (!subCommand.command.onlyPlayer || sender is Player) {
                    if (args.isEmpty()) {
                        subCommand.run(sender, args, ParamCombination.empty(args))
                        return@localizationScope
                    }

                    val argsRange = subCommand.command.argsRange
                    val newArray = arrayOfNulls<String>(args.size - 1)
                    System.arraycopy(args, 1, newArray, 0, args.size - 1)

                    if (newArray.isEmpty() && subCommand.command.allowEmptyParam) {
                        subCommand.run(sender, args, ParamCombination.empty(args))
                        return@localizationScope
                    }

                    if (argsRange.contains(newArray.size)) {
                        if (argsRange.last == Int.MAX_VALUE) {
                            subCommand.run(sender, newArray as Array<String>, ParamCombination.empty(args))
                            return@localizationScope
                        }

                        var validateResult: Map.Entry<IParamScheme, Int>? = null

                        val providedLength = newArray.size
                        val paramSchemes = ConcurrentHashMap<IParamScheme, Int>()

                        for ((index, it) in subCommand.command.paramSchemes!!.withIndex()) {
                            val length = it.length()
                            if (length == providedLength) {
                                paramSchemes[it] = index
                            }
                        }

                        newArray.forEachIndexed { index, realParam ->
                            realParam ?: return@localizationScope

                            paramSchemes.forEach { (paramScheme, index2) ->
                                val list = paramScheme.params
                                val param = list[index]

                                if (param.validate(sender, newArray as Array<String>, index, realParam)) {
                                    if (index >= list.size - 1) {
                                        validateResult = object : Map.Entry<IParamScheme, Int> {
                                            override val key: IParamScheme
                                                get() = paramScheme
                                            override val value: Int
                                                get() = index2
                                        }
                                    }
                                } else {
                                    paramSchemes.remove(paramScheme)
                                }
                            }
                        }

                        if (validateResult != null) {
                            subCommand.run(
                                sender,
                                newArray as Array<String>,
                                ParamCombination.create(args, validateResult!!.value, validateResult!!.key)
                            )
                            return@localizationScope
                        }
                    }

                    if (subCommand.syntax != null) {
                        message(CommandLanguage.SYNTAX_ERROR)
                            .format0(subCommand.syntax!!.operate().string)
                            .send()
                    } else {
                        message(CommandLanguage.SYNTAX_ERROR)
                            .format0("null")
                            .send()
                    }
                } else {
                    message(CommandLanguage.ONLY_PLAYER)
                        .send()
                }
            } else {
                message(CommandLanguage.NO_PERMISSION)
                    .send()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun analyzeArgs(args: Array<String>): Pair<AbstractSubCommand?, List<String>> {
        var subCommand: AbstractSubCommand? = null
        val params = arrayListOf<String>()

        if (!args.contains("args")) {
            val nodeString = if (args.size == 1) {
                "cbw"
            } else {
                args[args.size - 2]
            }
            val commandPart = args[args.size - 1]

            val node = ICommandNodes.find(nodeString)
            if (node != null) {
                params.add(commandPart)
                subCommand = SubCommandManager.get(commandPart, node)
            }
        } else {
            val argsSplitIndex = args.indexOf("args")
            if (argsSplitIndex < args.size - 1) {
                val nodePart = arrayOfNulls<String>(argsSplitIndex)
                val paramPart = arrayOfNulls<String>(args.size - argsSplitIndex - 1)

                System.arraycopy(args, 0, nodePart, 0, argsSplitIndex)
                System.arraycopy(args, argsSplitIndex + 1, paramPart, 0, args.size - argsSplitIndex - 1)

                val nodeString = if (nodePart.size == 1) {
                    "cbw"
                } else {
                    nodePart[nodePart.size - 2]!!
                }

                val node = ICommandNodes.find(nodeString)
                val commandPart = nodePart[nodePart.size - 1]!!

                if (node != null) {
                    params.add("args")
                    params.addAll(paramPart as Array<String>)
                    subCommand = SubCommandManager.get(commandPart, node)

                    if (subCommand == null) {
                        subCommand = SubCommandManager.get(commandPart, ICommandNodes.valueOf("ARGS"))
                    }
                }
            }
        }

        return Pair(subCommand, params)
    }
}
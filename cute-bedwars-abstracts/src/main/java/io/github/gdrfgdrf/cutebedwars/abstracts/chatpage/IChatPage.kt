package io.github.gdrfgdrf.cutebedwars.abstracts.chatpage

import io.github.gdrfgdrf.cutebedwars.abstracts.locale.ITranslationAgent
import io.github.gdrfgdrf.multimodulemediator.annotation.Service

@Service("chat_page", singleton = false)
interface IChatPage {
    var enableDefaultTopAndBottom: Boolean
    val size: Int
    var changeable: Boolean

    fun send(index: Int)
    fun addPage(loader: (Int) -> List<ITranslationAgent>)
    fun lineCountEveryPages(): Int
    fun lineCountEveryPages(lineCount: Int)
}
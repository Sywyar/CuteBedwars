package io.github.gdrfgdrf.cutebedwars.database.impl.mysql.service.impl

import io.github.gdrfgdrf.cutebedwars.beans.AbstractPlayerData
import io.github.gdrfgdrf.cutebedwars.database.impl.mysql.mapper.PlayerDataMapper
import io.github.gdrfgdrf.cutebedwars.database.impl.mysql.service.IIPlayerService
import io.github.gdrfgdrf.cutebedwars.database.impl.sqlite.beans.PlayerData
import io.github.gdrfgdrf.cutebedwars.database.impl.mysql.service.base.BetterServiceImpl
import java.util.*

class PlayerServiceImpl : BetterServiceImpl<PlayerDataMapper, PlayerData>(), IIPlayerService {
    override fun insert(playerData: AbstractPlayerData): Int {
        return super.insert(playerData as PlayerData)
    }

    override fun exist(uuid: UUID): Boolean {
        return mapper?.selectByUuid(uuid) != null
    }

    override fun selectByUuid(uuid: UUID): PlayerData? {
        return mapper?.selectByUuid(uuid)
    }
}
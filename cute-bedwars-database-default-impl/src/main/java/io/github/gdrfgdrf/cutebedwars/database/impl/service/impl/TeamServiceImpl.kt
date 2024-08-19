package io.github.gdrfgdrf.cutebedwars.database.impl.service.impl

import io.github.gdrfgdrf.cutebedwars.beans.game.AbstractTeam
import io.github.gdrfgdrf.cutebedwars.database.impl.beans.game.Team
import io.github.gdrfgdrf.cutebedwars.database.impl.mapper.TeamMapper
import io.github.gdrfgdrf.cutebedwars.database.impl.service.IITeamService
import io.github.gdrfgdrf.cutebedwars.database.impl.service.base.BetterServiceImpl
import io.github.gdrfgdrf.cuteframework.bean.annotation.Component
import io.github.gdrfgdrf.cuteframework.bean.annotation.Order

@Order(2)
@Component(name = "ITeamService")
class TeamServiceImpl : BetterServiceImpl<TeamMapper, Team>(), IITeamService {
    override fun insert(team: AbstractTeam): Int {
        val result = mapper?.insert(team as Team) ?: return -1
        return result
    }
}
package io.github.gdrfgdrf.cutebedwars.database.service;

import io.github.gdrfgdrf.cutebedwars.beans.game.AbstractGame;
import io.github.gdrfgdrf.cutebedwars.database.Database;
import io.github.gdrfgdrf.cutebedwars.database.base.IService;

/**
 * @author gdrfgdrf
 */
public interface IGameService extends IService {
    int insert(AbstractGame game);

    static IGameService get() {
        return Database.get().getService(IGameService.class);
    }
}

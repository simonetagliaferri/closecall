package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.model.domain.Player;

public class PlayerMapper {

    private PlayerMapper() {}

    public static PlayerBean toBean(Player player) {
        return new PlayerBean(player.getUsername(), player.getEmail());
    }

    public static Player fromBean(PlayerBean playerBean) {
        return new Player(playerBean.getUsername(), playerBean.getEmail());
    }
}

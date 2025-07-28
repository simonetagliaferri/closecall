package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.model.domain.Player;

public class PlayerMapper {

    private PlayerMapper() {}

    public static PlayerBean toBean(Player player) {
        if (player == null) return null;
        return new PlayerBean(player.getUsername(), player.getEmail());
    }

    public static Player fromBean(PlayerBean playerBean) {
        if (playerBean == null) return null;
        return new Player(playerBean.getUsername(), playerBean.getEmail());
    }
}

package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.model.domain.Host;

public class HostMapper {

    private HostMapper() {}

    public static HostBean toBean(Host host) {
        return new HostBean(host.getUsername(), host.getEmail());
    }

    public static Host fromBean(HostBean hostBean) {
        return new Host(hostBean.getUsername());
    }
}

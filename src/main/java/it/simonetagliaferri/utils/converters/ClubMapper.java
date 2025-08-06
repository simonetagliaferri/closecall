package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.model.domain.Club;


public class ClubMapper {

    private ClubMapper() {}

    public static Club fromBean(ClubBean clubBean) {
        Club club = new Club(clubBean.getName());
        club.updateAddress(clubBean.getStreet(), clubBean.getNumber(), clubBean.getCity(), clubBean.getState(), clubBean.getZip(), clubBean.getCountry());
        club.updateContacts(clubBean.getPhone());
        if (clubBean.getOwner() != null) {
            club.setOwner(HostMapper.fromBean(clubBean.getOwner()));
        }
        return club;
    }

    public static ClubBean toBean(Club club) {
        ClubBean clubBean = new ClubBean();
        clubBean.setName(club.getName());
        clubBean.setStreet(club.getStreet());
        clubBean.setNumber(club.getNumber());
        clubBean.setCity(club.getCity());
        clubBean.setState(club.getState());
        clubBean.setZip(club.getZip());
        clubBean.setCountry(club.getCountry());
        clubBean.setPhone(club.getPhone());
        clubBean.setOwner(HostMapper.toBean(club.getOwner()));
        return clubBean;
    }

}

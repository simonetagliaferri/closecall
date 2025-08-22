package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.domain.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User fromBean(UserBean bean) {
        if (bean == null) return null;
        if (bean.getEmail() == null) return new User(bean.getUsername());
        return new User(bean.getUsername(), bean.getEmail(), bean.getPassword(), bean.getRole());
    }
}

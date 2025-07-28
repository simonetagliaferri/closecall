package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.PasswordUtils;

public class UserMapper {

    public static User fromBeanAndHashPassword(UserBean bean) {
        return new User(bean.getUsername(), bean.getEmail(), PasswordUtils.sha256Hex(bean.getPassword()), bean.getRole());
    }
}

package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.User;

public interface LoginDAO {
    User findByUsername(String username);

    User signup(User user);
}

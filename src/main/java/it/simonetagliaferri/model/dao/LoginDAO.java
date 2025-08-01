package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.User;

public interface LoginDAO {
    User findByUsername(String username);

    void signup(User user);

    User findByEmail(String email);
}

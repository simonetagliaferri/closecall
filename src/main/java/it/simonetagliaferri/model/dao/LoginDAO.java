package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.User;

public interface LoginDAO {
    User login(User user);
    User signup(User user);
}

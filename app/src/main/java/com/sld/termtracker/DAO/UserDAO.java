package com.sld.termtracker.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.sld.termtracker.Entities.User;
@Dao
public interface UserDAO {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username")
    User getUserByUsername(String username);

    @Query("DELETE FROM users")
    void deleteAll();
}

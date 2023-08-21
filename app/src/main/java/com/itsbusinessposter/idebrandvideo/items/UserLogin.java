package com.itsbusinessposter.idebrandvideo.items;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(tableName = "user_login", primaryKeys = "user_id")
public class UserLogin {

    @NonNull
    public final String user_id;

    public final Boolean login;

    @Embedded(prefix = "user_")
    public final UserItem user;

    public UserLogin(@NonNull String user_id, Boolean login, UserItem user) {
        this.user_id = user_id;
        this.login = login;
        this.user = user;
    }
}

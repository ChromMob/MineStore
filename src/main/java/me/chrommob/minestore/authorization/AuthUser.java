package me.chrommob.minestore.authorization;

import lombok.Getter;
import me.chrommob.minestore.data.Config;

@Getter
public class AuthUser {
    public AuthUser(String name, String auth_id, int id, int index){
        this.name = name;
        this.auth_id = auth_id;
        this.id = id;
        this.index = index;
    }
    private final String name;
    private final String auth_id;
    private final int id;
    private final int index;
    private final long endTime = (long) (System.currentTimeMillis() + (1000 * 60 * Config.getAuthDelay()));
}

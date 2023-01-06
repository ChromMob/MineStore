package me.chrommob.minestore.mysql.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
public class UserManager {
    private final Map<UUID, User> profiles = new ConcurrentHashMap<>();
    public void createProfile(UUID uuid, String name) {
        User profile = new User(uuid, name);
        profiles.put(uuid, profile);
    }

    public User getProfile(UUID uuid) {
        if (profiles.get(uuid) != null) {
            return profiles.get(uuid);
        } else {
            return null;
        }
    }

    public void removeProfile(UUID uuid) {
        profiles.remove(uuid);
    }

    public Map<UUID, User> getAll(){
        return profiles;
    }

    public void updateAll() {
        try {
            for (User profile : profiles.values()) {
                profile.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

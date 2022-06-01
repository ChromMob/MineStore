package me.chrommob.minestore.websocket.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SocketObjects {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("command")
    @Expose
    private String command;

    @SerializedName("is_online_required")
    @Expose
    private boolean playerOnlineNeeded;
}

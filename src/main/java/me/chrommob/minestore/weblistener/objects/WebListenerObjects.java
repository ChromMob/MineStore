package me.chrommob.minestore.weblistener.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class WebListenerObjects {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("command")
    @Expose
    private String command;

    @SerializedName("is_online_required")
    @Expose
    private boolean playerOnlineNeeded;
}

package me.chrommob.minestore.placeholders.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class LastDonator {
    @SerializedName("user")
    @Expose
    private String name;

    @SerializedName("amount")
    @Expose
    private double amount;

    @SerializedName("date")
    @Expose
    private String date;
}

package me.chrommob.minestore.placeholders.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class DonationGoal {
    @SerializedName("goal")
    @Expose
    private double goal;

    @SerializedName("goal_sum")
    @Expose
    private double goalSum;
}

package me.chrommob.minestore.data;

import lombok.Getter;
import lombok.Setter;
import me.chrommob.minestore.gui.objects.ListObject;

import java.util.HashMap;
import java.util.List;

public class GuiData {
    @Setter
    @Getter
    private static List<ListObject> data;
    @Getter
    @Setter
    private static HashMap<String, String> subcategory = new HashMap<>();

    public static void createSub() {
        for (ListObject datum : data) {
            if (datum.getSubcategories() != null) {
                for (int j = 0; j < datum.getSubcategories().size(); j++) {
                    subcategory.put(datum.getName().toLowerCase(), datum.getSubcategories().get(j).getName());
                }
            }
        }
    }
}

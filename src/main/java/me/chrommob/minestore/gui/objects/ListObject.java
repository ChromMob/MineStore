package me.chrommob.minestore.gui.objects;

import lombok.Getter;

import java.util.List;


@Getter
public class ListObject {
    private int id;
    private String name;
    private String url;
    private String gui_item_id;
    private List<SubCategoriesType> subcategories;
    private List<PackagesType> packages;
}

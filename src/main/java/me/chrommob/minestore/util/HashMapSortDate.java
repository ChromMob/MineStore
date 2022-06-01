package me.chrommob.minestore.util;

import java.util.*;

public class HashMapSortDate {
    public static HashMap<Integer, Date>
    sortByValue(HashMap<Integer, Date> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Date>> list
                = new LinkedList<Map.Entry<Integer, Date>>(
                hm.entrySet());

        // Sort the list using lambda expression
        Collections.sort(
                list,
                (i2,
                 i1) -> i1.getValue().compareTo(i2.getValue()));

        // put data from sorted list to hashmap
        HashMap<Integer, Date> temp
                = new LinkedHashMap<Integer, Date>();
        for (Map.Entry<Integer, Date> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
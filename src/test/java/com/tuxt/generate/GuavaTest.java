package com.tuxt.generate;

import com.google.common.collect.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

public class GuavaTest {

    @Test
    public void test1() {
        Table<String, String, Integer> table = HashBasedTable.create();
        //存放元素
        table.put("Hydra", "Jan", 20);
        table.put("Hydra", "Feb", 28);

        table.put("Trunks", "Jan", 28);
        table.put("Trunks", "Feb", 16);

        //取出元素
        Integer dayCount = table.get("Hydra", "Feb");
        Assertions.assertEquals(28, dayCount);
    }

    @Test
    public void test2() {
        // 创建一个 BiMap 实例
        BiMap<String, Integer> biMap = HashBiMap.create();

        // 将键值对添加到 BiMap 中
        biMap.put("one", 1);
        biMap.put("two", 2);
        biMap.put("three", 3);

        // 从键获取值
        Integer valueForOne = biMap.get("one");
        System.out.println("The value for 'one': " + valueForOne);

        // 从值获取键
        String keyForTwo = biMap.inverse().get(2);
        System.out.println("The key for 2: " + keyForTwo);

        // 检查 BiMap 中是否包含某个键
        boolean containsKey = biMap.containsKey("two");
        System.out.println("Contains 'two': " + containsKey);

        // 检查 BiMap 中是否包含某个值
        boolean containsValue = biMap.containsValue(2);
        System.out.println("Contains value 2: " + containsValue);

        // 获取所有键的集合
        System.out.println("Keys: " + biMap.keySet());

        // 获取所有值的集合
        System.out.println("Values: " + biMap.values());

        // 移除一个键值对
        biMap.remove("two");
        System.out.println("After removing 'two': " + biMap);
    }

    @Test
    public void test3(){
        Multimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.put("day",1);
        multimap.put("day",2);
        multimap.put("day",8);
        multimap.put("month",3);
        System.out.println(multimap.toString());
        Collection<Integer> day = multimap.get("day");
        Map<String, Collection<Integer>> map = multimap.asMap();
        for (String key : map.keySet()) {
            System.out.println(key+" : "+map.get(key));
        }
    }

    @Test
    public void test4(){
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closedOpen(0,60),"fail");
        rangeMap.put(Range.closed(60,90),"satisfactory");
        rangeMap.put(Range.openClosed(90,100),"excellent");

        System.out.println(rangeMap.get(59));
        System.out.println(rangeMap.get(60));
        System.out.println(rangeMap.get(90));
        System.out.println(rangeMap.get(91));
    }
}

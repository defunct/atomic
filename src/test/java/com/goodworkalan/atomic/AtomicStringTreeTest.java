package com.goodworkalan.atomic;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

public class AtomicStringTreeTest {
    private static String ALPHABET = "abcdefghijklmnopqrstuvqxyz";
    
    @Test
    public void putBlank() {
        CharacterTree<String> tree = new CharacterTree<String>();
        tree.put("com.", "a");
        tree.put("com.yoyodyne.", "b");
        tree.put("org.eclipse.", "c");
        tree.put("org.apache.", "d");
        assertEquals(tree.get("com."), "a");
        assertEquals(tree.get("com.yoyodyne."), "b");
        assertEquals(tree.get("org.eclipse."), "c");
        assertEquals(tree.get("org.apache."), "d");
    }
    
    @Test
    public void gather() {
        CharacterTree<String> tree = new CharacterTree<String>();
        tree.put("com.", "a");
        tree.put("com.yoyodyne.", "b");
        assertEquals(tree.getLast("com.yoyodyne.utility."), "b");
    }
    
    @Test
    public void foo() {
        Random random = new Random();
        String[] words = new String[10000];
        for (int i = 0, stop = words.length; i < stop; i++) {
            StringBuilder string = new StringBuilder();
            int length = random.nextInt(10) + 10;
            for (int j = 0; j < length; j++) {
                string.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
            }
            words[i] = string.toString();
        }
        Map<String, String> map = new ConcurrentHashMap<String, String>();
        long start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            map.put(words[i], words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            map.put(words[i], words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            map.get(words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
        CharacterTree<String> tree = new CharacterTree<String>();
        start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            tree.put(words[i], words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            tree.put(words[i], words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            tree.get(words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
        map = new ConcurrentHashMap<String, String>();
        start = System.currentTimeMillis();
        for (int i = 0, stop = words.length; i < stop; i++) {
            map.put(words[i], words[i]);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}

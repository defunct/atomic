package com.goodworkalan.atomic;

import com.goodworkalan.atomic.CharacterTree;

import junit.framework.TestCase;

public class CharacterTreeTestCase
extends TestCase
{
    public void testCharacterNode()
    {
        final CharacterTree.ObjectFactory factory = new CharacterTree.ObjectFactory()
        {
            public Object newObject(char character)
            {
                if (character != '.')
                {
                    return null;
                }
                return new Character(character);
            }
        };

        CharacterTree tree = new CharacterTree(factory);

        tree.put("us.");
        
        assertTrue(tree.contains("us"));
        assertTrue(tree.contains("us."));
        assertFalse(tree.contains("us.a"));
        assertFalse(tree.contains("ca."));
        
        assertNull(tree.get("us"));
        assertNull(tree.get("ca.a"));
        assertNull(tree.get("fr."));
        assertNotNull(tree.get("us."));
        
        tree.put("ca.");
        
        assertTrue(tree.contains("ca."));
        assertTrue(tree.contains("us."));
        
        tree.put("com.");
        tree.put("com.agtrz.");
        tree.put("uk.");
        tree.put("fr.");
        tree.put("jp.");
        tree.put("com.agtrz.swag.CharacterTree" + '.');
        
        assertNotNull("com.agtrz.swag.");

        assertTrue(tree.contains("com.agtrz."));
        
        assertTrue(tree.contains("com.agtrz"));
        assertTrue(tree.contains("com.agtrz.swag.CharacterTree."));
        assertFalse(tree.contains("com.agtrz.swag.Foo."));
    }
}


/* vim: set et sw=4 ts=4 ai tw=70: */
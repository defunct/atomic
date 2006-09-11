/*
 * Copyright The Engine Room, LLC 2005. All Right Reserved.
 */
package com.agtrz.swag.chtree;

import java.util.ArrayList;
import java.util.List;

public class CharacterTree
{
    private final ObjectFactory factory;
    
    private final Node rootNode;
    
    public CharacterTree(ObjectFactory factory)
    {
        this.factory = factory;
        this.rootNode = new Node((char) 0, null);
    }

    /** Put ought to return the object constructed by the object factory? */
    public void put(CharSequence charSequence)
    {
        rootNode.add(charSequence, 0, factory);
    }
    
    public boolean contains(CharSequence charSequence)
    {
        ContainsVisitor containsVisitor = new ContainsVisitor();
        rootNode.visit(charSequence, 0, containsVisitor);
        return !containsVisitor.failed;
    }

    public boolean contains2(String charSequence)
    {
        Node current = rootNode;
        int index = 0;
        int length = charSequence.length();
        while (current != null && index < length)
        {
            int character = charSequence.charAt(index);
            if (character < current.character)
            {
                current = current.left;
            }
            else if (character > current.character)
            {
                current = current.right;
            }
            else
            {
                index++;
            }
        }
        return current != null && index == length;
    }

    public void partialVisit(String charSequence, Visitor visitor)
    {
        Node current = rootNode;
        int index = 0;
        int length = charSequence.length();
        while (current != null && index < length)
        {
            char character = charSequence.charAt(index);
            if (character < current.character)
            {
                current = current.left;
            }
            else if (character > current.character)
            {
                current = current.right;
            }
            else
            {
                visitor.visit(charSequence, index, current.object);
                current = current.center;
                index++;
            }
        }
    }

    public void visit2(String charSequence, Visitor visitor)
    {
        Node current = rootNode;
        int index = 0;
        int length = charSequence.length();
        while (current != null && index < length)
        {
            char character = charSequence.charAt(index);
            if (character < current.character)
            {
                current = current.left;
            }
            else if (character > current.character)
            {
                current = current.right;
            }
            else
            {
                visitor.visit(charSequence, index, current.object);
                current = current.center;
                index++;
            }
        }

        if (current == null || index != length)
        {
            visitor.failed(charSequence, index);
        }
    }

    public Object get(CharSequence charSequence)
    {
        GetVisitor getVisitor = new GetVisitor();
        rootNode.visit(charSequence, 0, getVisitor);
        return getVisitor.object;
    }
    
    public List getAll(CharSequence charSequence)
    {
        AccumulateVisitor accumulateVisitor = new AccumulateVisitor(null);
        rootNode.visit(charSequence, 0, accumulateVisitor);
        return accumulateVisitor.listOfObjects;
    }
    
    public List getAll(CharSequence charSequence, String forCharacters)
    {
        AccumulateVisitor accumulateVisitor = new AccumulateVisitor(forCharacters);
        rootNode.visit(charSequence, 0, accumulateVisitor);
        return accumulateVisitor.listOfObjects;
    }
    
    public interface Visitor
    {
        public void visit(CharSequence charSequence, int index, Object object);
        
        public void failed(CharSequence charSequence, int index);
    }

    public interface ObjectFactory
    {
        public Object newObject(char character);
    }

    private final static  class Node
    {
        private final char character;
        
        private final Object object;
        
        private Node left;
        
        private Node center;
        
        private Node right;
        
        public Node(char character, Object object)
        {
            this.character = character;
            this.object = object;
        }
        
        public void setLeft(Node left)
        {
            this.left = left;
        }
        
        public void setRight(Node right)
        {
            this.right = right;
        }
        
        public void add(CharSequence charSequence, int index, CharacterTree.ObjectFactory factory)
        {
            char compare = charSequence.charAt(index);
            if (compare < character)
            {
                if (left == null)
                {
                    left = new Node(compare, factory.newObject(compare));
                }
                left.add(charSequence, index, factory);
            }
            else if (compare > character)
            {
                if (right == null)
                {
                    right = new Node(compare, factory.newObject(compare));
                }
                right.add(charSequence, index, factory);
            }
            else if (index + 1 < charSequence.length())
            {
                if (center == null)
                {
                    char next = charSequence.charAt(index + 1);
                    center = new Node(next, factory.newObject(next));
                }
                center.add(charSequence, index + 1, factory);
            }
        }
    
        public boolean contains(CharSequence charSequence, int index)
        {
            char compare = charSequence.charAt(index);
    
            if (compare < character)
            {
                if (left != null)
                {
                    return left.contains(charSequence, index);
                }
                return false;
            }
            else if (compare > character)
            {
                if (right != null)
                {
                    return right.contains(charSequence, index);
                }
                return false;
            }
            else if (index < charSequence.length())
            {
                if (center != null)
                {
                    return center.contains(charSequence, index + 1);
                }
            }
            return true;
        }
        
        public void visit(CharSequence charSequence, int index, CharacterTree.Visitor visitor)
        {
            char compare = charSequence.charAt(index);
            
            if (compare < character)
            {
                if (left == null)
                {
                    visitor.failed(charSequence, index);
                }
                else
                {
                    left.visit(charSequence, index, visitor);
                }
            }
            else if (compare > character)
            {
                if (right == null)
                {
                    visitor.failed(charSequence, index);
                }
                else
                {
                    right.visit(charSequence, index, visitor);
                }
            }
            else
            {
                visitor.visit(charSequence, index, object);
                if (index + 1 < charSequence.length())
                {
                    if (center == null)
                    {
                        visitor.failed(charSequence, index + 1);
                    }
                    else
                    {
                        center.visit(charSequence, index + 1, visitor);
                    }
                }
            }
        }
    }
  
    private final static class ContainsVisitor
    implements Visitor
    {
        public boolean failed;
        
        public void visit(CharSequence charSequence, int index, Object object)
        {
        }
        
        public void failed(CharSequence charSequence, int index)
        {
            failed = true;
        }
    }
    
    private final static class GetVisitor
    implements Visitor
    {
        public Object object;
        
        public void visit(CharSequence charSequence, int index, Object object)
        {
            this.object = object;
        }
        
        public void failed(CharSequence charSequence, int index)
        {
            this.object = null;
        }
    }
    
    private final static class AccumulateVisitor
    implements Visitor
    {
        public List listOfObjects = new ArrayList();
        
        private final String match;
        
        public AccumulateVisitor(String match)
        {
            this.match = match;
        }
        
        public void visit(CharSequence charSequence, int index, Object object)
        {
            char character = charSequence.charAt(index);
            if (match == null || match.indexOf(character) != -1)
            {
                listOfObjects.add(object);
            }
        }
    
        public void failed(CharSequence charSequence, int index)
        {
            this.listOfObjects = null;
        }
    }
}

/* vim: set et sw=4 ts=4 ai tw=68: */
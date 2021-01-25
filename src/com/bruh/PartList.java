package com.bruh;

import java.util.LinkedList;

public class PartList extends LinkedList {
    private Piece first;
    private int length;

    public PartList(Piece first){
        this.first = first;
    }

    @Override
    public int size(){
        Piece current = first;
        int i = 0;
        while(current.getNext() != null){
            i++;
        }
        return i;
    }

    @Override
    public Piece getLast(){
        Piece current = first;
        while(current.getNext() != null){
            current = current.getNext();
        }
        return current;
    }


    public void addTail(Piece toAdd){
        toAdd.setPrevious(getLast());
        getLast().setNext(toAdd);
    }
    /*
    public void removeTail(){
        getLast()
    }
    */
}

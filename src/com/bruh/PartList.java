package com.bruh;

import java.util.LinkedList;

public class PartList extends LinkedList {
    private Piece first;

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
    public Piece get(int index){
        Piece current = first;
        for(int i = 1; i <= index; i++){
            current = current.getNext();
        }
        if(isEmpty()){
            return current;
        }
        else{
            return null;
        }
    }

    public Piece getFirst(){
        return first;
    }

    @Override
    public Piece getLast(){
        Piece current = first;
        while(current.getNext() != null){
            current = current.getNext();
        }
        if(isEmpty()){
            return current;
        }
        else{
            return first;
        }
    }


    public Piece before(int index){
        Piece current = get(index);
        return current.getPrevious();
    }

    public Piece after(int index){
        Piece current = get(index);
        return current.getNext();
    }
    public void set(int index, Piece toSet){
        Piece current = get(index);
        current.getPrevious().setNext(toSet);
        toSet.setPrevious(current.getPrevious());
        toSet.setNext(current.getNext());
        current.getNext().setPrevious(toSet);
        current.setNext(null);
        current.setPrevious(null);
    }

    public void addTail(Piece toAdd){
        if(!isEmpty()){
            toAdd.setPrevious(getLast());
            getLast().setNext(toAdd);
        }
        else{
            set(0, toAdd);
        }
    }

    @Override
    public boolean isEmpty(){
        if(size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public void addHead(Piece toAdd){
        toAdd.setNext(first);
        first.setPrevious(toAdd);
        first = toAdd;
    }

    public void removeTail(){
        Piece secondLast = getLast().getPrevious();
        getLast().setPrevious(null);
        secondLast.setNext(null);
    }
    public void removeHead(){
        first = first.getNext();
        first.getPrevious().setNext(null);
        first.setPrevious(null);
    }

    public void insertBefore(int index, Piece toAdd){
        Piece current = first;
        for(int i = 1; i <= index; i++){
            current = current.getNext();
        }
        current = current.getPrevious();
        current.getNext().setPrevious(toAdd);
        toAdd.setNext(current.getNext());
        toAdd.setPrevious(current);
        current.setNext(toAdd);
    }

    public void insertAfter(int index, Piece toAdd){
        Piece current = first;
        for(int i = 1; i <= index; i++){
            current = current.getNext();
        }
        current = current.getNext();
        toAdd.setNext(current);
        toAdd.setPrevious(current.getPrevious());
        current.getPrevious().setNext(toAdd);
        current.setPrevious(toAdd);
    }
}

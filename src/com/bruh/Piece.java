package com.bruh;

public class Piece {
    private String pieceName;
    private Material currentMat;
    private double volumeTaken;
    private double weight;
    private double partPrice;
    private double sheetMetalThickness;
    private double length;
    private double width;
    private double height;

    public Piece(String pieceName, double price, double weight){
        this.pieceName = pieceName;
        this.length = length;
        this.width = width;
        this.height = height;
        volumeTaken = this.length * this.width * this.height;
        this.weight = weight;
        partPrice = price;
    }

    public Piece(double length, double width, double height, Material mat, double sheetMetalThickness){
        this.currentMat = mat;
        this.length = length;
        this.width = width;
        this.height = height;
        this.sheetMetalThickness = sheetMetalThickness;
        pieceName = mat.getName() + " chassis piece";
        volumeTaken = ((this.length * this.width * this.sheetMetalThickness) * 2) + (this.length * (this.height-2*this.sheetMetalThickness) * this.sheetMetalThickness);
        volumeTaken *= (8.0/9.0);
        weight = currentMat.getDensity() * volumeTaken;
        partPrice = currentMat.getPrice() * weight;
    }

    public void setSheetMetalThickness(double thickness){
        sheetMetalThickness = thickness;
    }

    public void setPartPrice(double partPrice) {
        this.partPrice = partPrice;
    }

    public double getSheetMetalThickness(){ return sheetMetalThickness; }

    public double getWeight(){ return weight; }

    public double getPrice(){ return partPrice; }

    public String toString(){
        return(pieceName);
    }
}

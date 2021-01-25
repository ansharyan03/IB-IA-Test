package com.bruh;

public class Material {
    private final double density;
    private final double pricePerUnitMass;
    private String name;
    public Material(String name, double density, double pricePerUnitMass){
        this.density = density;
        this.name = name;
        this.pricePerUnitMass = pricePerUnitMass;
    }
    public String getName() { return name; }
    public double getDensity(){
        return density;
    }
    public double getPrice() { return pricePerUnitMass; }
}

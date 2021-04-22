package com.example.gascalc;

public class MediaGas {
    //Campos
    private int M_ID;
    private double M_dist;
    private double M_gas;
    private double M_preco;
    private double M_media;

    //Construtores
    public MediaGas() {}

    public MediaGas(int ID, double dist, double gas, double preco, double media)
    {
        M_ID = ID;
        M_dist = dist;
        M_gas = gas;
        M_preco = preco;
        M_media = media;
    }

    //Propiedades
    public void setID(int ID)
    {
        M_ID = ID;
    }

    public int getID()
    {
        return M_ID;
    }

    public void setDistancia(double dist)
    {
        M_dist = dist;
    }

    public double getDistancia()
    {
        return M_dist;
    }

    public void setGas(double gas)
    {
        M_gas = gas;
    }

    public double getGas()
    {
        return M_gas;
    }

    public void setPreco(double preco)
    {
        M_preco = preco;
    }

    public double getPreco()
    {
        return M_preco;
    }

    public void setMedia(double media)
    {
        M_media = media;
    }

    public double getMedia()
    {
        return M_media;
    }
}

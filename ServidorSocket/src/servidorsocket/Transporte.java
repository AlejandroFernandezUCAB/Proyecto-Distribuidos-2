/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.util.ArrayList;

/**
 *
 * @author pedro
 */
public class Transporte {
    public int _id;
    public ArrayList<Paquete> _paquetes;

    public Transporte(int _id, ArrayList<Paquete> _paquetes) {
        this._id = _id;
        this._paquetes = _paquetes;
    }

    public Transporte() {
        
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public ArrayList<Paquete> getPaquetes() {
        return _paquetes;
    }

    public void setPaquetes(ArrayList<Paquete> _paquetes) {
        this._paquetes = _paquetes;
    }
    
    
}

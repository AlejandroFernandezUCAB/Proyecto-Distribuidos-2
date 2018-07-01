/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anillo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;

/**
 *
 * @author pedro
 */
public class Paquete {
    public int _carga;
    public int _tiempo;
    public int _nodoDestino;
    
    public Paquete() {
        this._carga = ThreadLocalRandom.current().nextInt(0,1000);
        this._tiempo = 0;
        
        this._nodoDestino = ThreadLocalRandom.current().nextInt(0,4);
    }
    
    
}

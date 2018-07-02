import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;

public class Paquete {
    public int _carga;
    public int _tiempo;
    public int _nodoDestino;
    
    public Paquete() {
        this._carga = ThreadLocalRandom.current().nextInt(0,1000);
        this._tiempo = 5;       
        // por ahora le pondre 4
        this._nodoDestino = ThreadLocalRandom.current().nextInt(0,3);
    }
    
    
}

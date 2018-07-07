import java.util.List;
import java.util.ArrayList;

// Implementing the remote interface 
public class ImplEstadisticas implements Estadisticas {  
   
    int _paquetesNoSubidosAlPrimerIntento;

    public ImplEstadisticas(){
        this._paquetesNoSubidosAlPrimerIntento = 0;
    }

    public void PaqueteNoSubioAlPrimerIntento(){
        this._paquetesNoSubidosAlPrimerIntento++;
    }

    // Mostrar metrica/estadistica
    public int PaquetesNoSubidosAlPrimerIntento(){
        return this._paquetesNoSubidosAlPrimerIntento;
    }
 } 
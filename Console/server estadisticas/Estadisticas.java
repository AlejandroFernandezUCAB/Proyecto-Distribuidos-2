import java.rmi.Remote; 
import java.rmi.RemoteException;  
import java.util.List;

// Creating Remote interface for our application 
public interface Estadisticas extends Remote { 
        /*
            Las estadísticas pueden ser consultadas desde la cónsola del servidor de estadísticas
            mediante un menú. La actualización de los datos se puede hacer con polling o trap y 
            deben justificarlo en el informe. Los datos a reportar son:
            ◦ Tiempo promedio que tardan los paquetes de un almacén en llegar a destino 
            (cada paquete debe conservar su tiempo de emisión desde el almacén).
            ◦ Cantidad de paquetes que no suben en su primer intento al transporte. 
            ◦ Porcentaje de tiempo en que el transporte circula con su carga máxima
            ◦ Porcentaje de paquetes que entran al primer intento en el transporte y 
                salen al primer intento del transporte.
        */ 

    // Cantidad de paquetes que no suben en su primer intento al transporte. 
            // recolectar datos
    void PaqueteNoSubioAlPrimerIntento() throws RemoteException;
} 
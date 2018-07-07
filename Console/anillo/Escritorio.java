
/**
    Clase tipo hilo para el proceso de paquetes, ademas guarda en un archivo carga util y paquetes
 */
public class Escritorio extends Thread{
    
    public Paquete _paquete;
    
    public Escritorio( Paquete _paquete){

        this._paquete = _paquete;

    }

    public void run(){
        try {
            ActiveWorkers instancia = ActiveWorkers.getInstance();
            sleep(10000);
            //Procedo a guardar en el archivo el paquete
            Archivo archivo = new Archivo( String.valueOf(_paquete._carga) + " " +String.valueOf(_paquete._tiempo) );
            archivo.crearArchivo("Estadisticas.txt");
            //Matando el hilos
            instancia.removeWorker();
            interrupt();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
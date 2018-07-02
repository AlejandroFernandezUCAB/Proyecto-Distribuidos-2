import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Archivo
 */
public class Archivo {

    public String _creacion;

    public Archivo( String _creacion){

        this._creacion = _creacion + "\n";

    }
    
    public void crearArchivo(){
        try{
            File f = new File("Estadisticas.txt");
            if (f.exists() && !f.isDirectory()){
                FileWriter fw = new FileWriter("Estadisticas.txt", true);
                fw.write( _creacion );
                fw.close();
            }else{
                FileWriter fw = new FileWriter("Estadisticas.txt");
                fw.write( _creacion );
                fw.close();

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
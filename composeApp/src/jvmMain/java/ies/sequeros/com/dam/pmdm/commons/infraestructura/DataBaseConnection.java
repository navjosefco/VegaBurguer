package ies.sequeros.com.dam.pmdm.commons.infraestructura;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseConnection {
    private String config_path;
    private String connection_string;
    private Connection conexion;

    //El constructor vacio es necesario para que se pueda crear un objeto de esta clase 
    public DataBaseConnection() {
    }
    public void open() throws Exception {
        //Creamos el objeto FileReader para leer el archivo app.properties
        FileReader fr = null;

        //Leemos el archivo app.properties que se encuentra en la carpeta composeApp/src/jvmMain/resources
        //"user.dir" es la ruta del directorio actual (Donde se encuentra el proyecto) 
        // this.getConfig_path() es la ruta del archivo app.properties
        //con esto lo 
        File f =new File(System.getProperty("user.dir")+
                this.getConfig_path());

        fr = new FileReader(f);

        Properties props = new Properties();

        try {

            props.load(fr);

        } catch (IOException ex) {

           ex.printStackTrace();
        }

        //Leemos los datos de la base de datos que son usuario y contraseña del archivo app.properties
        String user = props.getProperty("database.user");
        String password = props.getProperty("database.password");
        //Leemos la url de la base de datos del archivo app.properties
        this.connection_string = props.getProperty("database.path");

        //El objeto conexion es el que se encarga de la conexion con la base de datos y le pasamos la url, usuario y contraseña
        //que son los datos que leimos del archivo app.properties
        this.conexion =
                DriverManager.getConnection(this.connection_string, user, password);
    }

    public Connection getConnection() {
        return this.conexion;
    }
    public void close() throws SQLException {

        /*Cambiamos este codigo porque en mysql no es necesario pasarle la instruccion shutdown=true ya que 
        //es un servidor de base de datos y no es necesario cerrarla
        //antes estaba como:
        //conexion.close(); - cerrar la conexion
        //DriverManager.getConnection(this.connection_string+";shutdown=true"); - cerrar el servidor
        //conexion = null; - cerrar la conexion
        */
        if (conexion != null && !conexion.isClosed()) {
             conexion.close();
        }
        conexion = null;
    }
    public String getConfig_path() {
        return config_path;
    }
    public void setConfig_path(String config_path) {
        this.config_path = config_path;
    }
}
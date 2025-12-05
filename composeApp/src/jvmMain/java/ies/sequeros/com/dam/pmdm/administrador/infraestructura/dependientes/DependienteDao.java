package ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes;


import ies.sequeros.com.dam.pmdm.administrador.modelo.Dependiente;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//Importo la libreria para encriptar la contraseña
import org.jasypt.util.password.StrongPasswordEncryptor;

public class DependienteDao implements IDao<Dependiente> {

    private DataBaseConnection conn;

    private final String table_name = "dependiente";
    
    private final String selectall = "select * from " + table_name;

    private final String selectbyid = "select * from " + table_name 
                                        + " where id=?";

    private final String findbyname = "select * from " + table_name 
                                        + " where name=?";

    private final String deletebyid = "delete from " + table_name 
                                        + " where id=?";

    private final String insert = "INSERT INTO " + table_name 
                                    + " (id, name, email, password, image_path, enabled, is_admin) " 
                                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private final String update = "UPDATE " + table_name 
                                    + " SET name = ?, email = ?, password = ?, image_path = ?, enabled = ?, is_admin = ? " 
                                    + "WHERE id = ?";
    
    private final String updatePassword = "UPDATE " + table_name 
                                    + " SET password = ?" 
                                    + " WHERE id = ?";
    
    //Creo un cosntructor vacio para que pueda ser instanciado
    public DependienteDao() {
    }

    //Metodo para obtener la conexion
    public DataBaseConnection getConn() {
        return this.conn;

    }

    //Metodo para establecer la conexion
    public void setConn(final DataBaseConnection conn) {
        this.conn = conn;
    }


    //Sobreescribo el metodo getById 
    @Override
    public Dependiente getById(final String id) {

        Dependiente sp = null;// = new Dependiente();

        try {
            //Preparo la sentencia sql
            final PreparedStatement pst = conn.getConnection().prepareStatement(selectbyid);
            
            //Le doy la posicion del parametro y el valor
            pst.setString(1, id);
            
            //Ejecuto la sentencia pero el resultado esta en formato registro
            final ResultSet rs = pst.executeQuery();
            
            //Recorro el resultado
            while (rs.next()) {
                //Paso de registro a objeto 
                sp = registerToObject(rs);
            }

            //Cierro la sentencia
            pst.close();
            
            //Registo el log
            Logger logger = Logger.getLogger(DependienteDao.class.getName());
            logger.info("Ejecutando SQL: " + selectbyid + " | Parametros: [id=" + id + "]");
            
            //Devuelvo el resultado
            return sp;

        } catch (final SQLException ex) {

            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        
        //Si no, devuelvo null
        return sp;
    }

    public Dependiente findByName(final String name) {

        Dependiente sp = null;// = new Dependiente();
        
        try {

            final PreparedStatement pst = conn.getConnection().prepareStatement(findbyname);
            
            pst.setString(1, name);
            
            final ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {

                sp = registerToObject(rs);
            }

            pst.close();
            Logger logger = Logger.getLogger(DependienteDao.class.getName());
            logger.info("Ejecutando SQL: " + findbyname + " | Parametros: [name=" + name + "]");

            return sp;

        } catch (final SQLException ex) {

            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        return sp;
    }

    @Override
    public List<Dependiente> getAll() {
        //Como la sentencia contiene varias filas me creo un array para meter los resultados
        final ArrayList<Dependiente> scl = new ArrayList<>();

        Dependiente tempo;

        PreparedStatement pst = null;

        try {

            try {

                pst = conn.getConnection().prepareStatement(selectall);

            } catch (final SQLException ex) {
                
                Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE, null, ex);
            }

            final ResultSet rs = pst.executeQuery();

            //recorro el resultset y voy metiendo los resultados en el array llamado tempo
            while (rs.next()) {

                tempo = registerToObject(rs);
                scl.add(tempo);
            }

            pst.close();
            Logger logger = Logger.getLogger(DependienteDao.class.getName());
            logger.info("Ejecutando SQL: " + selectall+ " | Parametros: ");

        } catch (final SQLException ex) {

            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return scl;
    }

    @Override
    public void update(final Dependiente item) {

        try {

            final PreparedStatement pst = conn.getConnection().prepareStatement(update);
            
            //Inicializo el objeto StrongPasswordEncryptor
            StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
            //Le paso la contraseña y la encripto
            String Password = encryptor.encryptPassword(item.getPassword());

            //Le paso los valores a la sentencia para actualizar
            pst.setString(1, item.getName());
            pst.setString(2, item.getEmail());
            pst.setString(3, Password); //Le paso la contraseña encriptada
            pst.setString(4, item.getImagePath());
            pst.setBoolean(5, item.getEnabled());
            pst.setBoolean(6, item.isAdmin());
            pst.setString(7, item.getId());
            pst.executeUpdate();
            pst.close();

            Logger logger = Logger.getLogger(DependienteDao.class.getName());

            logger.info(() ->
                    "Ejecutando SQL: " + update +
                            " | Params: [1]=" + item.getName() +
                            ", [2]=" + item.getEmail() +
                            ", [3]=" + item.getPassword() +
                            ", [4]=" + item.getImagePath() +
                            ", [5]=" + item.getEnabled() +
                            ", [6]=" + item.isAdmin() +
                            ", [7]=" + item.getId() +
                            "]"
            );

        } catch (final SQLException ex) {

            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(final Dependiente item) {
        try {

            final PreparedStatement pst = conn.getConnection().prepareStatement(deletebyid);
            pst.setString(1, item.getId());
            pst.executeUpdate();
            pst.close();

            Logger logger = Logger.getLogger(DependienteDao.class.getName());
            logger.info("Ejecutando SQL: " + deletebyid + " | Parametros: [id=" + item.getId() + "]");

        } catch (final SQLException ex) {
            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @Override
    public void insert(final Dependiente item) {

        final PreparedStatement pst;
        try {
           pst = conn.getConnection().prepareStatement(insert,
                    Statement.RETURN_GENERATED_KEYS);
            
            StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
            String Password = encryptor.encryptPassword(item.getPassword());

            pst.setString(1, item.getId());
            pst.setString(2, item.getName());
            pst.setString(3, item.getEmail());
            pst.setString(4, Password);
            pst.setString(5,item.getImagePath());
            pst.setBoolean(6, item.getEnabled());
            pst.setBoolean(7, item.isAdmin());

            pst.executeUpdate();
            pst.close();
            Logger logger = Logger.getLogger(DependienteDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + insert +
                            " | Params: [1]=" + item.getId() +
                            ", [2]="+ item.getName() +
                            ", [3]=" + item.getEmail() +
                            ", [4]=" + item.getPassword() +
                            ", [5]=" + item.getImagePath() +
                            ", [6]=" + item.getEnabled() +
                            ", [7]=" + item.isAdmin() +

                            "]"
            );

        } catch (final SQLException ex) {
            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public void cambiarContrasenya(String id, String oldPass, String newPass) throws Exception {
        Dependiente dependiente = getById(id);
        if (dependiente == null) {
            throw new Exception("Usuario no encontrado");
        }

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        if (!encryptor.checkPassword(oldPass, dependiente.getPassword())) {
            throw new Exception("La contraseña actual es incorrecta");
        }

        String encryptedNewPass = encryptor.encryptPassword(newPass);

        try (PreparedStatement pst = conn.getConnection().prepareStatement(updatePassword)) {
            pst.setString(1, encryptedNewPass);
            pst.setString(2, id);
            pst.executeUpdate();
            
            Logger.getLogger(DependienteDao.class.getName()).info("Contraseña actualizada para el usuario: " + id);
        } catch (SQLException ex) {
            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Error al actualizar la contraseña en la base de datos");
        }
    }

    //pasar de registro a objeeto
    private Dependiente registerToObject(final ResultSet r) {

        //Me creo un objeto de tipo dependiente
        Dependiente sc =null;

        try {

            //Le paso los valores a la sentencia para que me cree un objeto de tipo dependiente
            sc=new Dependiente(

                    r.getString("ID"),
                    r.getString("NAME"),
                    r.getString("EMAIL"),
                    r.getString("PASSWORD"),
                    r.getString("IMAGE_PATH") != null ? r.getString("IMAGE_PATH") : "",
                    r.getBoolean("ENABLED"),
                    r.getBoolean("IS_ADMIN"));

            //Devuelvo el objeto dependiente
            return sc;

        } catch (final SQLException ex) {

            Logger.getLogger(DependienteDao.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        //Si no devuelvo null
        return sc;
    }
}

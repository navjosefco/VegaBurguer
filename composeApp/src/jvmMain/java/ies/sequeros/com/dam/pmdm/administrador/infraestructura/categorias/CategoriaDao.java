package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
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

public class CategoriaDao implements IDao<Categoria> {

    //Conexion a la base de datos
    private DataBaseConnection conn;

    /*
     * Sentencias SQL
     */
    //Nombre de la tabla con la que voy a trabajar
    private final String table_name = "categoria";

    //Traer todas las categorias
    private final String selectAll =
        "SELECT * FROM " + table_name;

    //Traer categoria por el id
    private final String selectById =
        "SELECT * FROM " + table_name
        + " WHERE id = ?";

    //Traer categorias por nombre
    private final String selectByName =
        "SELECT * FROM " + table_name
        + " WHERE name = ?";

    //Insertar una categoria
    private final String insert =
        "INSERT INTO " + table_name
        + " (id, name, image_path, enabled)"
        + " VALUES (?, ?, ?, ?);";

    //Actualizar una categoria
    private final String update =
        "UPDATE " + table_name
        + " SET name = ?, image_path = ?, enabled = ?"
        + " WHERE id = ?;";

    //Eliminar la categoria
    private final String delete =
        "DELETE FROM " + table_name
        + " WHERE id = ?;";

    //Constructor vacio para poder instanciar la clase
    public CategoriaDao(){}

    //Metodo para obtener la conexion
    public DataBaseConnection getConn(){
        return this.conn;
    }

    //Metodo para establecer la conexion
    public void setConn(final DataBaseConnection conn) {
        this.conn = conn;
    }

    /* Pasar de registro SQL a objeto Categoria */
    private Categoria registerToObject(final ResultSet rs){

        Categoria categoria = null;

        try{
            categoria = new Categoria(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("image_path") != null ? rs.getString("image_path") : "",
                rs.getBoolean("enabled")
            );

            return categoria;

        } catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categoria;
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: selectAll
     */
    @Override
    public List<Categoria> getAll(){

        //Me creo un array de Categorias
        final ArrayList<Categoria> categoriasList = new ArrayList<>();

        //Me creo una categoria temporal para ir metiendo los resultados unitarios
        Categoria categoriaTemp;

        //Me creo un PreparedStatement para ejecutar la sentencia
        PreparedStatement pst = null;

        //El segundo try es para establecer la conexion
        //El primer try ejecuta la sentencia y agrega todos los resultados a la lista
        try{

            try{
                pst = conn.getConnection().prepareStatement(selectAll);

            } catch(final SQLException ex ){
                Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
            }

            final ResultSet rs = pst.executeQuery();

            //Al ser varios resultados agrego uno a uno con un while
            while(rs.next()){

                //Utilizo mi categoria temporal para almacenar los datos y trasnformados
                categoriaTemp = registerToObject(rs);
                //Agrego la categoria temporal a la lista
                categoriasList.add(categoriaTemp);
            }

            pst.close();
            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + selectAll + " | Parametros: ");

        } catch(final SQLException ex ){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return categoriasList;
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: selectById
     */
    @Override
    public Categoria getById(final String id){

        Categoria sp = null;

        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(selectById);

            pst.setString(1, id);

            final ResultSet rs = pst.executeQuery();

            while(rs.next()){
                sp = registerToObject(rs);
            }

            pst.close();

            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + selectById + " | Parametros: [id=" + id + "]");

        } catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sp;
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: insert
     */
    @Override
    public void insert(final Categoria categoria){

        final PreparedStatement pst;

        try{
            pst = conn.getConnection().prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

            //id, name, image_path, enabled
            pst.setString(1, categoria.getId());
            pst.setString(2, categoria.getName());
            pst.setString(3, categoria.getImage_path());
            pst.setBoolean(4, categoria.getEnabled());

            pst.executeUpdate();

            pst.close();

            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info(() ->
                        "Ejecutando SQL: " + insert +
                            " | Parametros: [id=" + categoria.getId() + "]"
                            + " [name=" + categoria.getName() + "]"
                            + " [image_path=" + categoria.getImage_path() + "]"
                            + " [enabled=" + categoria.getEnabled() + "]"
            );

        } catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: Update
     */
    @Override
    public void update(final Categoria categoria){

        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(update);

            pst.setString(1, categoria.getName());
            pst.setString(2, categoria.getImage_path());
            pst.setBoolean(3, categoria.getEnabled());
            pst.setString(4, categoria.getId());

            pst.executeUpdate();

            pst.close();

            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + update +
                            " | Parametros: [id=" + categoria.getId() + "]"
                            + " [name=" + categoria.getName() + "]"
                            + " [image_path=" + categoria.getImage_path() + "]"
                            + " [enabled=" + categoria.getEnabled() + "]"
            );

        } catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: Delete
     */
    @Override
    public void delete(final Categoria categoria){

        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(delete);

            pst.setString(1, categoria.getId());

            pst.executeUpdate();

            pst.close();

            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + delete +
                            " | Parametros: [id=" + categoria.getId() + "]"
            );

        } catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Implementacion del método específico getByName
     */
    public Categoria getByName(final String name){

        Categoria sp = null;

        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(selectByName);

            pst.setString(1, name);

            final ResultSet rs = pst.executeQuery();

            while(rs.next()){
                sp = registerToObject(rs);
            }

            pst.close();

            Logger logger = Logger.getLogger(CategoriaDao.class.getName());
            logger.info("Ejecutando SQL: " + selectByName + " | Parametros: [name=" + name + "]");

        } catch(final SQLException ex){
            Logger.getLogger(CategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sp;
    }
}

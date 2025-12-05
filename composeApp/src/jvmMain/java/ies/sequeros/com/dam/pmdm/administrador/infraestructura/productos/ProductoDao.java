package ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto;
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

public class ProductoDao implements IDao<Producto>{
    
    //Conexion a la base de datos
    private DataBaseConnection conn;

    /*
     * Sentencias SQL
     */

    //Nombre de la tabla con la que voy a trabajar
    private final String table_name = "producto";

    //Traer todos los productos
    private final String selectAll = 
        "SELECT * FROM " + table_name;

    //Traer producto por el id
    private final String selectById = 
        "SELECT * FROM " + table_name
        + " WHERE id = ?";

    //Traer productos por categoria
    private final String selectByCat = 
        "SELECT * FROM " + table_name
        + " WHERE categoria_id = ?";

    //Traer productos por nombre
    private final String selectByName = 
        "SELECT * FROM " + table_name
        + " WHERE name = ?";

    //Insertar un producto
    private final String insert = 
        "INSERT INTO " + table_name 
            + " (id, categoria_id, name, description, price, image_path, enabled)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?);";

    //Actualizar un producto
    private final String update = 
        "UPDATE " + table_name
        + " SET categoria_id = ?, name = ?, description = ?, price = ?, image_path = ?, enabled =?"
        + " WHERE id = ?;";

    //Eliminar el producto
    private final String delete =
        "DELETE FROM " + table_name
        + " WHERE id = ?;";

    //Constructo vacio para poder instanciar la clase
    public ProductoDao(){}

    //Metodo para obtener la conexion
    public DataBaseConnection getConn(){
        return this.conn;
    }
    
    //Metodo para establecer la conexion
    public void setConn(final DataBaseConnection conn) {
        this.conn = conn;
    }


    /* Pasar de registro SQL a objeto Producto*/
    private Producto registerToObject(final ResultSet rs){

        Producto product = null;

        try{

            product = new Producto(

                rs.getString("id"),
                rs.getString("categoria_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("image_path"),
                rs.getBoolean("enabled")
            );

            return product;

        }catch(final SQLException ex){

            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return product;
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: selectAll
     */
    @Override
    public List<Producto> getAll(){

        //Me creo un array de Productos
        final ArrayList<Producto> productosList = new ArrayList<>();

        //Me creo un producto temporal para ir metiendo los resultados unitarios
        Producto productTemp;

        //Me creo un PreparedStatement para ejecutar la sentencia
        PreparedStatement pst = null;

        //El segundo try es para establecer la conexion
        //El primer try ejecuta la sentencia y agrega todos los resultados a la lista
        try{
            
            try{

                pst = conn.getConnection().prepareStatement(selectAll);

            }  catch(final SQLException ex ){

                Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
            }

            final ResultSet rs = pst.executeQuery();

            //Al ser varios resultados agrego uno a uno con un while
            while(rs.next()){

                //Utilizo mi producto temporal para almacenar los datos y trasnformados
                productTemp = registerToObject(rs);
                //Agrego el producto temporal a la lista
                productosList.add(productTemp);
            }

            pst.close();
            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info("Ejecutando SQL: " + selectAll + " | Parametros: ");

        }catch(final SQLException ex ){

            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productosList;
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: selectById
     */
    @Override
    public Producto getById(final String id){

        Producto sp = null;

        try{

            final PreparedStatement pst = conn.getConnection().prepareStatement(selectById);

            pst.setString(1, id);

            final ResultSet rs = pst.executeQuery();

            while(rs.next()){

                sp = registerToObject(rs);
            }

            pst.close();

            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info("Ejecutando SQL: " + selectById + " | Parametros: [id=" + id + "]");

        }catch(final SQLException ex){
            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sp;
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: selectByCat
     */
    public List<Producto> getByCategoria(final String idCategoria){
        //Me creo un array de productos
        final ArrayList<Producto> productosList = new ArrayList<>();

        //Producto Temporal
        Producto productTemp;

        PreparedStatement pst = null;

        try{
            try{

                pst = conn.getConnection().prepareStatement(selectByCat);
                pst.setString(1, idCategoria);

            }catch(final SQLException ex){

                Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
            }

            final ResultSet rs = pst.executeQuery();

            while(rs.next()){

                productTemp = registerToObject(rs);
                productosList.add(productTemp);
            }

            pst.close();

            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info("Ejecutando SQL: " + selectById + " | Parametros: [id=" + idCategoria + "]");

        }catch(final SQLException ex){
            
            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productosList;
        
    }

    
    /*
     * Implementacion de la interface IDao
     * Sentencia: insert
     */
    @Override
    public void insert(final Producto producto){

        final PreparedStatement pst;

        try{
            pst = conn.getConnection().prepareStatement(insert,  Statement.RETURN_GENERATED_KEYS);

            //id, categoria_id, name, description, price, image_path, enabled
            pst.setString(1, producto.getId());
            pst.setString(2, producto.getCategoria_id());
            pst.setString(3, producto.getName());
            pst.setString(4, producto.getDescription());
            pst.setDouble(5, producto.getPrice());
            pst.setString(6, producto.getImage_path());
            pst.setBoolean(7, producto.getEnabled());

            pst.executeUpdate();

            pst.close();

            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info(() ->
                        "Ejecutando SQL: " + insert +
                            " | Parametros: [id=" + producto.getId() + "]"
                            + " [categoria_id=" + producto.getCategoria_id() + "]"
                            + " [name=" + producto.getName() + "]"
                            + " [description=" + producto.getDescription() + "]"
                            + " [price=" + producto.getPrice() + "]"
                            + " [image_path=" + producto.getImage_path() + "]"
                            + " [enabled=" + producto.getEnabled() + "]"
        );

        }catch(final SQLException ex){
            
            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: Update
     */
    @Override
    public void update(final Producto producto){

        try{
            final PreparedStatement pst = conn.getConnection().prepareStatement(update);

            pst.setString(1, producto.getId());
            pst.setString(2, producto.getCategoria_id());
            pst.setString(3, producto.getName());
            pst.setString(4, producto.getDescription());
            pst.setDouble(5, producto.getPrice());
            pst.setString(6, producto.getImage_path());
            pst.setBoolean(7, producto.getEnabled());

            pst.executeUpdate();

            pst.close();

            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + insert +
                            " | Parametros: [id=" + producto.getId() + "]"
                            + " [categoria_id=" + producto.getCategoria_id() + "]"
                            + " [name=" + producto.getName() + "]"
                            + " [description=" + producto.getDescription() + "]"
                            + " [price=" + producto.getPrice() + "]"
                            + " [image_path=" + producto.getImage_path() + "]"
                            + " [enabled=" + producto.getEnabled() + "]"
            );


        }catch(final SQLException ex){

            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /*
     * Implementacion de la interface IDao
     * Sentencia: Delete
     */
    @Override
    public void delete(final Producto producto){

        try{

            final PreparedStatement pst = conn.getConnection().prepareStatement(delete);

            pst.setString(1, producto.getId());

            pst.executeUpdate();

            pst.close();

            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info(() ->
                    "Ejecutando SQL: " + delete +
                            " | Parametros: [id=" + producto.getId() + "]"
            );

        }catch(final SQLException ex){

            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Implementacion de la interface IDao
     * Sentencia: selectByName
     */
    public Producto getByName(final String name){

        Producto sp = null;

        try{

            final PreparedStatement pst = conn.getConnection().prepareStatement(selectByName);

            pst.setString(1, name);

            final ResultSet rs = pst.executeQuery();

            while(rs.next()){

                sp = registerToObject(rs);
            }

            pst.close();

            Logger logger = Logger.getLogger(ProductoDao.class.getName());
            logger.info("Ejecutando SQL: " + selectByName + " | Parametros: [name=" + name + "]");

        }catch(final SQLException ex){
            Logger.getLogger(ProductoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sp;
    }
}

/* 
interface IProductoRepositorio {
    
    *suspend fun getAll(): list<Producto>
    *suspend fun getById(id: String): Producto?
    *suspend fun getByCategoria(idCategoria: String): list<Producto>
    *suspend fun insert(producto: Producto)
    *suspend fun update(producto: Producto)
    *suspend fun delete(producto:Producto)

    public T getById(String id);
    public List<T> getAll();
    public void update(T item);
    public void delete(T item);
    public void insert(T item);*/
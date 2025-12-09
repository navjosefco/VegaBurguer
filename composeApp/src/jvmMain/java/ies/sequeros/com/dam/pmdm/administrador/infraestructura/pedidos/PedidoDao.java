package ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.IDao;

public class PedidoDao implements IDao<Pedido> {

    //DataBaseConnection: declaro la variable con la que voy a interactuar con la base de datos 
    private DataBaseConnection conn;

    private final String table_name = "pedido";

    //Sentencias SQL -PEDIDO-


    private final String INSERT_PEDIDO =
            "INSERT INTO "+table_name
                +" (id, status, customer_name, total_price, dependiente_id)"
                +" VALUES (?, ?, ?, ?, ?)";

    private final String UPDATE_PEDIDO =
            "UPDATE "+table_name
                +" SET status = ?, customer_name = ?, total_price = ?, dependiente_id = ?"
                +" WHERE id = ?";

    private final String DELETE_PEDIDO =
            "DELETE FROM "+table_name+
                    " WHERE id = ?";

    private final String SELECT_ALL_PEDIDO =
            "SELECT * FROM "+table_name;

    private final String SELECT_BY_ID_PEDIDO =
            "SELECT * FROM "+table_name+
                    " WHERE id = ?";


    //Sentencias SQL -LINEAPEDIDO-

    private final String INSERT_LINEA =
            "INSERT INTO linea_"+table_name
                    +" (id, pedido_id, producto_id, quantity, unit_price)"
                    +" VALUES (?, ?, ?, ?, ?)";

    private final String DELETE_LINEA =
            "DELETE FROM linea_"+table_name
                    +" WHERE pedido_id = ?";

    private final String SELECT_LINEA_PEDIDO =
            "SELECT * FROM linea_"+table_name
                    +" WHERE pedido_id = ?";

    /*Me creo el constructor vacio para que pueda ser instanciado */

    public PedidoDao(){}
 
    /*Me creo el metodo setConnectio para poder llamar la conexion a la base de datos */
    public void setConnection(DataBaseConnection conn) {this.conn = conn;}


    //Metodos para pasar de registro a Objeto

    private LineaPedido lineaToObject(final ResultSet rs){

        LineaPedido lineaObjeto = null;

        try{

            lineaObjeto = new LineaPedido(

                    rs.getString("id"),
                    rs.getString("pedido_id"),
                    rs.getString("producto_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price")
            );

        }catch (final SQLException ex){

            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);

        }
        return lineaObjeto;
    }

    /*
    * Lo mejor es crear una lista dentro del patron DAO de objetos LineaPedido para 
    * evitar tener informacion redundante en la memoria.
    * Convierte una fila del ResultSet (datos del Pedido) en un objeto Pedido.
    * Requiere que se le pase la lista de 'lineas' previamente obtenidas,
    * ya que el objeto Pedido es inmutable y necesita sus líneas al construirse.
    * */
    private Pedido pedidoToObject(final ResultSet rs, List<LineaPedido> lineas){

        Pedido pedidoObjeto = null;

        try{
            pedidoObjeto = new Pedido(

                rs.getString("id"),
                rs.getString("status"),
                rs.getString("customer_name"),
                rs.getDouble("total_price"),
                rs.getString("dependiente_id"),
                lineas);

        }catch (final SQLException ex){

            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedidoObjeto;

    }


    private List<LineaPedido> getLineasPedido(String pedidoid){

        ArrayList<LineaPedido> listaLineas = new ArrayList<>();

        try(PreparedStatement pst =
            conn.getConnection().prepareStatement(SELECT_LINEA_PEDIDO)){

            pst.setString(1, pedidoid);

            final ResultSet rs = pst.executeQuery();

            while(rs.next()) listaLineas.add(lineaToObject(rs));

            pst.close();

            Logger logger = Logger.getLogger(PedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + SELECT_LINEA_PEDIDO + " | Parametros: [id=" + pedidoid + "]");

        }catch (final SQLException ex){

            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaLineas;
    }

    @Override
    public List<Pedido> getAll(){

        ArrayList<Pedido> listaPedidos = new ArrayList<>();

        try(PreparedStatement pst =
            conn.getConnection().prepareStatement(SELECT_ALL_PEDIDO)){

            final ResultSet rs = pst.executeQuery();

            while(rs.next()){

                String pedidoid = rs.getString("id");
                List<LineaPedido> lineas = getLineasPedido(pedidoid);
                listaPedidos.add(pedidoToObject(rs, lineas));
            }

            pst.close();

            Logger logger = Logger.getLogger(PedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + SELECT_ALL_PEDIDO + " | Parametros: ");

        }catch (final SQLException ex){

            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, "Error al obtener lineas", ex);
        }

        return listaPedidos;
    }

    @Override
    public Pedido getById(String id){

        Pedido pedido = null;

        try(PreparedStatement pst =
            conn.getConnection().prepareStatement(SELECT_BY_ID_PEDIDO)){

            pst.setString(1, id);

            final ResultSet rs = pst.executeQuery();

            if(rs.next()){

                List<LineaPedido> lineasCoca = getLineasPedido(id);
                pedido = pedidoToObject(rs, lineasCoca);
            }

            pst.close();
            Logger logger = Logger.getLogger(PedidoDao.class.getName());
            logger.info("Ejecutando SQL: " + SELECT_BY_ID_PEDIDO + " | Parametros: [id=" + id + "]");

        }catch (final SQLException ex){

            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedido;
    }

    /*
    * Para este insert, al utilizar dos tablas es mejor hacer una transacción
    * Ya que un pedido tiene varias lineas de pedidso y tengo que ingresar en dos
    * tablas distintas
    */
    @Override
    public void insert(Pedido pedido){

        Connection c = conn.getConnection();

        try{

            c.setAutoCommit(false);

            //Insertamos datos del pedido
            try(PreparedStatement pst =
                c.prepareStatement(INSERT_PEDIDO, PreparedStatement.RETURN_GENERATED_KEYS)){

                pst.setString(1, pedido.getId());
                pst.setString(2, pedido.getStatus());
                pst.setString(3, pedido.getCustomerName());
                pst.setDouble(4, pedido.getTotalPrice());
                pst.setString(5, pedido.getDependienteId());

                pst.executeUpdate();

                Logger logger = Logger.getLogger(PedidoDao.class.getName());
                logger.info(() ->
                    "Ejecutando SQL: " + INSERT_PEDIDO +
                        " | Params: [1]=" + pedido.getId() +
                        ", [2]=" + pedido.getStatus() +
                        ", [3]=" + pedido.getCustomerName() +
                        ", [4]=" + pedido.getTotalPrice() +
                        ", [5]=" + pedido.getDependienteId() +
                        "]"
                );
            }

            //Insertamos ahora las lineas
            try(PreparedStatement pstLinea =
                c.prepareStatement(INSERT_LINEA, PreparedStatement.RETURN_GENERATED_KEYS)){

                for(LineaPedido linea : pedido.getLineas()){

                    pstLinea.setString(1, linea.getId());
                    pstLinea.setString(2, pedido.getId());
                    pstLinea.setString(3, linea.getProductoId());
                    pstLinea.setInt(4, linea.getQuantity());
                    pstLinea.setDouble(5, linea.getUnitPrice());

                    pstLinea.addBatch();
                }

                pstLinea.executeBatch();

                Logger logger = Logger.getLogger(PedidoDao.class.getName());
                logger.info(() ->
                    "Ejecutando SQL: " + INSERT_LINEA +
                        " | Lineas insertadas: " + pedido.getLineas().size()
                );
            }

            c.commit();
            Logger logger = Logger.getLogger(PedidoDao.class.getName());
            logger.info("Pedido y lineas insertadas correctamente");

        }catch (final SQLException ex){

            try { c.rollback(); } catch (SQLException e) {}
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, "Error al insertar pedido y lienas de pedido", ex);

        }finally {

            try {
                c.setAutoCommit(true);
                //c.close();
            } catch (SQLException e) {}
        }
    }

    @Override
    public void update(Pedido pedido){

        Connection c = conn.getConnection();

        try {

            c.setAutoCommit(false);

            try (PreparedStatement pst = c.prepareStatement(UPDATE_PEDIDO)) {

                pst.setString(1, pedido.getStatus());
                pst.setString(2, pedido.getCustomerName());
                pst.setDouble(3, pedido.getTotalPrice());
                pst.setString(4, pedido.getDependienteId());
                pst.setString(5, pedido.getId());

                pst.executeUpdate();

                Logger logger = Logger.getLogger(PedidoDao.class.getName());
                logger.info(() ->
                    "Ejecutando SQL: " + UPDATE_PEDIDO +
                        " | Params: [1]=" + pedido.getStatus() +
                        ", [2]=" + pedido.getCustomerName() +
                        ", [3]=" + pedido.getTotalPrice() +
                        ", [4]=" + pedido.getDependienteId() +
                        ", [5]=" + pedido.getId() +
                        "]"
                );
            }

            try(PreparedStatement pstDeleteLine = c.prepareStatement(DELETE_LINEA)){

                pstDeleteLine.setString(1, pedido.getId());
                pstDeleteLine.executeUpdate();
            }

            try(PreparedStatement pstLinea =
                c.prepareStatement(INSERT_LINEA, PreparedStatement.RETURN_GENERATED_KEYS)){

                for (LineaPedido linea : pedido.getLineas()) {

                    pstLinea.setString(1, linea.getId());
                    pstLinea.setString(2, pedido.getId());
                    pstLinea.setString(3, linea.getProductoId());
                    pstLinea.setInt(4, linea.getQuantity());
                    pstLinea.setDouble(5, linea.getUnitPrice());

                    pstLinea.addBatch();
                }

                pstLinea.executeBatch();

                Logger logger = Logger.getLogger(PedidoDao.class.getName());
                logger.info(() ->
                    "Ejecutando SQL: " + INSERT_LINEA +
                        " | Lineas actualizadas: " + pedido.getLineas().size()
                );

            }

            c.commit();

        }catch (SQLException ex) {

            try { c.rollback(); } catch (SQLException e) {}
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, "Error actualizando pedido", ex);

        } finally {

            try {
                c.setAutoCommit(true);
               // c.close();
            } catch (SQLException e) {}
        }
    }

    @Override
    public void delete(Pedido pedido){

        Connection c = conn.getConnection();

        try {

            c.setAutoCommit(false);

            /*Tenemos que borrar primero las lineas del pedido*/
            try (PreparedStatement pstLineasPedido = c.prepareStatement(DELETE_LINEA)){

                pstLineasPedido.setString(1, pedido.getId());
                pstLineasPedido.executeUpdate();

                Logger logger = Logger.getLogger(PedidoDao.class.getName());
                logger.info("Ejecutando SQL: " + DELETE_LINEA
                        + " | Parametros: [id=" + pedido.getId()
                        + "]+ Lineas borradas: " + pedido.getLineas().size() + "]");
            }

            /*Ahora si borramos el pedido*/

            try (PreparedStatement pstPedido = c.prepareStatement(DELETE_PEDIDO)){

                pstPedido.setString(1, pedido.getId());
                pstPedido.executeUpdate();

                Logger logger = Logger.getLogger(PedidoDao.class.getName());
                logger.info("Ejecutando SQL: " + DELETE_PEDIDO + " | Parametros: [id=" + pedido.getId() + "]");

            }

            c.commit();

            Logger logger = Logger.getLogger(PedidoDao.class.getName());
            logger.info("Pedido borrado correctamente");


        }catch (SQLException ex) {

            try { c.rollback(); } catch (SQLException e) {}
            Logger.getLogger(PedidoDao.class.getName()).log(Level.SEVERE, "Error borrando pedido", ex);

        } finally {

            try {
                c.setAutoCommit(true);
                //c.close();
            } catch (SQLException e) {}
        }
        
    }

}
/*
public interface IDao<T> {
    *public T getById(String id);
    *public List<T> getAll();
    *public void update(T item);
    public void delete(T item);
    public void insert(T item);

}


val id: String,
    val status: String,
    val customerName: String?,
    val totalPrice: Double,
    val dependienteId: String?,


 */
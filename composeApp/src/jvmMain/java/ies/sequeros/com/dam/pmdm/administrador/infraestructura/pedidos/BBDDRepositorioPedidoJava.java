package ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos;

import java.util.List;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

public class BBDDRepositorioPedidoJava {

    private final DataBaseConnection db;
    private PedidoDao dao;

    public BBDDRepositorioPedidoJava(DataBaseConnection db){

        this.db = db;
        this.dao = new PedidoDao();
        this.dao.setConnection(this.db);
    }

    public void add(Pedido pedido){

        this.dao.insert(pedido);
    }

    public boolean remove(Pedido pedido){

        this.dao.delete(pedido);
        return true;
    }

    public boolean update(Pedido pedido){

        this.dao.update(pedido);
        return true;
    }

    public List<Pedido> getAll(){

        return this.dao.getAll();
    }

    public Pedido getById(String id){

        return this.dao.getById(id);
    }

}

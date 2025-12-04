package ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos;

import java.util.List;

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

//MeEsta clase sirve como puente entre la base de datos y la capa de negocio en Java
public class BBDDRepositorioProductoJava {

    private final DataBaseConnection db;

    private ProductoDao dao;

    public BBDDRepositorioProductoJava(DataBaseConnection db){
        //Llama al constructor de la superclase
        super();
        this.db = db;
        dao = new ProductoDao();
        dao.setConn(this.db);
    }

    public void add(Producto prod){

        this.dao.insert(prod);
    }

    public boolean remove(Producto prod){

        this.dao.delete(prod);
        return true;
    }

    public boolean remove(String id){

        var item = this.dao.getById(id);

        if (item != null){

            this.remove(item);
            return true;
        }

        return false;
    }

    public boolean update(Producto prod){

        this.dao.update(prod);
        return true;
    }

    public List<Producto> getAll(){

        return this.dao.getAll();
    }

    public Producto findById(String id){

        return this.dao.getById(id);
    }

    public List<Producto> getByCat(String id){

        return this.dao.getByCategoria(id);
    }
}

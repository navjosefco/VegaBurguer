package ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias;

import java.util.List;
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria;
import ies.sequeros.com.dam.pmdm.commons.infraestructura.DataBaseConnection;

//Esta clase sirve como puente entre la base de datos y la capa de negocio en Java
public class BBDDRepositorioCategoriaJava {

    private final DataBaseConnection db;

    private CategoriaDao dao;

    public BBDDRepositorioCategoriaJava(DataBaseConnection db){
        //Llama al constructor de la superclase
        super();
        this.db = db;
        dao = new CategoriaDao();
        dao.setConn(this.db);
    }

    public void add(Categoria categoria){
        this.dao.insert(categoria);
    }

    public boolean remove(Categoria categoria){
        this.dao.delete(categoria);
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

    public boolean update(Categoria categoria){
        this.dao.update(categoria);
        return true;
    }

    public List<Categoria> getAll(){
        return this.dao.getAll();
    }

    public Categoria findById(String id){
        return this.dao.getById(id);
    }
    
    // Nuevo método delegado para buscar por nombre
    public Categoria findByName(String name){
        return this.dao.getByName(name);
    }
}

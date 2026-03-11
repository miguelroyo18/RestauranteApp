package es.unizar.eina.T201_comidas.ui.pedidos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.T201_comidas.database.raciones.Racion;
import es.unizar.eina.T201_comidas.database.Repository;
import es.unizar.eina.T201_comidas.database.pedidos.Pedido;

/**
 * ViewModel para manejar y gestionar datos relacionados con pedidos en la aplicación.
 */
public class PedidoViewModel extends AndroidViewModel {

    private Repository mRepository;
    private final LiveData<List<Pedido>> mAllPedidos;
    private LiveData<List<Racion>> mAllRaciones;


    /**
     * Constructor para inicializar el ViewModel.
     *
     * @param application Aplicación actual.
     */
    public PedidoViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllPedidos = mRepository.getAllPedidos();
        mAllRaciones = mRepository.getAllRaciones();
    }

    /**
     * Obtiene LiveData que contiene la lista de todos los pedidos.
     *
     * @return LiveData de la lista de pedidos.
     */
    LiveData<List<Pedido>> getAllPedidos() {
        return mAllPedidos;
    }
    /**
     * Obtiene LiveData que contiene la lista de todas las raciones.
     *
     * @return LiveData de la lista de raciones.
     */
    LiveData<List<Racion>> getAllRaciones() {
        return mAllRaciones;
    }

    /**
     * Obtiene LiveData que contiene la lista ordenada de todos los pedidos.
     *
     * @param filter El filtro para ordenar
     */
    LiveData<List<Pedido>> getOrderedPedidos(String filter) {
        return mRepository.getOrderedPedidos(filter);
    }

    /**
     * Obtiene LiveData que contiene la lista ordenada y filtrada de los pedidos.
     *
     * @param filter El filtro para ordenar
     * @param estado El estado para filtrar
     */
    LiveData<List<Pedido>> getOrderedPedidos(String filter, String estado) {
        return mRepository.getOrderedPedidos(filter, estado);
    }

    /**
     * Inserta un nuevo pedido en la base de datos.
     *
     * @param pedido Objeto Pedido a insertar.
     */
    public long insert(Pedido pedido) {
        return mRepository.insertPedido(pedido);
    }

    /**
     * Actualiza un pedido existente en la base de datos.
     *
     * @param pedido Objeto Pedido a actualizar.
     */
    public void update(Pedido pedido) {
        mRepository.updatePedido(pedido);
    }

    /**
     * Elimina un pedido de la base de datos.
     *
     * @param pedido Objeto Pedido a eliminar.
     */
    public void delete(Pedido pedido) {
        mRepository.deletePedido(pedido);
    }

    /**
     * Inserta una ración en la base de datos.
     *
     * @param racion La ración a insertar.
     */
    public void insertRacion(Racion racion) {
        mRepository.insertRacion(racion);
    }

    /**
     * Actualiza una ración en la base de datos.
     *
     * @param racion La ración a actualizar.
     */
    public void updateRacion(Racion racion) {
        mRepository.updateRacion(racion);
    }

    /**
     * Elimina una ración en la base de datos.
     *
     * @param racion La ración a eliminar.
     */
    public void deleteRacion(Racion racion) {
        mRepository.deleteRacion(racion);
    }

    /**
     * Elimina todas las raciones en la base de datos asociadas a un pedido
     *
     * @param id El id del pedido.
     */
    public void deleteAllPlatosOfPedido(int id) {
        mRepository.deleteAllPlatosOfPedido(id);
    }

    /**
     * Obtiene una lista de raciones asociadas a un pedido de la base de datos.
     *
     * @param id El id del pedido.
     */
    public List<Racion> getAllRaciones(int id) {
        List<Racion> allRaciones = mAllRaciones.getValue();
        List racionesDe = new ArrayList<Racion>();
        if (allRaciones != null) {
            for (Racion racion : allRaciones) {
                if (racion.getPedidoID() == id) {
                    racionesDe.add(racion);

                }
            }
        }
        return racionesDe;
    }
}

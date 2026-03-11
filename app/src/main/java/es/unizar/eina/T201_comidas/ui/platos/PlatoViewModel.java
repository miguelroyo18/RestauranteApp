package es.unizar.eina.T201_comidas.ui.platos;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import es.unizar.eina.T201_comidas.database.platos.Plato;
import es.unizar.eina.T201_comidas.database.Repository;

/**
 * ViewModel para la gestión de datos de la entidad Plato.
 */
public class PlatoViewModel extends AndroidViewModel {
    private Repository mRepository;
    private final LiveData<List<Plato>> mAllPlatos;

    /**
     * Constructor de la clase.
     *
     * @param application Instancia de la aplicación.
     */
    public PlatoViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllPlatos = mRepository.getAllPlatos();
    }

    /**
     * Obtiene la lista observable de todos los platos.
     *
     * @return Lista observable de platos.
     */
    public LiveData<List<Plato>> getAllPlatos() {
        return mAllPlatos;
    }

    public LiveData<List<Plato>> getOrderedPlatos(String order) {
        return mRepository.getOrderedPlatos(order);
    }

    /**
     * Inserta un nuevo plato en la base de datos.
     *
     * @param plato Plato a insertar.
     */
    public void insert(Plato plato) {
        mRepository.insertPlato(plato);
    }

    /**
     * Actualiza un plato existente en la base de datos.
     *
     * @param plato Plato a actualizar.
     */
    public void update(Plato plato) {
        mRepository.updatePlato(plato);
    }

    /**
     * Elimina un plato de la base de datos.
     *
     * @param plato Plato a eliminar.
     */
    public void delete(Plato plato) {
        mRepository.deletePlato(plato);
        mRepository.deleteAllPedidosWithAPlato(plato);
    }
}

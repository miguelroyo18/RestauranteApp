package es.unizar.eina.T201_comidas.database;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import es.unizar.eina.T201_comidas.database.raciones.Racion;
import es.unizar.eina.T201_comidas.database.pedidos.Pedido;
import es.unizar.eina.T201_comidas.database.pedidos.PedidoDao;
import es.unizar.eina.T201_comidas.database.platos.Plato;
import es.unizar.eina.T201_comidas.database.platos.PlatoDao;

/**
 * Repositorio único de la aplicación RestauranteApp, empleado para comunicar la interfaz
 * de usuario con la base de datos.
 */
public class Repository {

    private PlatoDao mPlatoDao;
    private PedidoDao mPedidoDao;
    private LiveData<List<Pedido>> mAllPedidos;

    private final long TIMEOUT = 15000;

    /**
     * Constructor del repositorio
     * @param application El contexto de la aplicación
     */
    public Repository(Application application) {
        RestauranteRoomDatabase db = RestauranteRoomDatabase.getDatabase(application);
        mPlatoDao = db.platoDao();
        mPedidoDao = db.pedidoDao();
        mAllPedidos = mPedidoDao.getOrderedPedidos("nombreCliente");
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Obtiene todos los platos almacenados en la base de datos.
     * @return Una lista LiveData de todos los platos.
     */
    public LiveData<List<Plato>> getAllPlatos() {
        return mPlatoDao.getOrderedPlatos("nombre");
    }

    private ExecutorService executorService;


    /**
     * Obtiene una lista de todos los platos almacenados en la base de datos ordenados según
     * un filtro.
     * @param filter El filtro a aplicar
     * @return Una lista LiveData de todos los platos ordenada por filter.
     */
    public LiveData<List<Plato>> getOrderedPlatos(String filter) {
        return mPlatoDao.getOrderedPlatos(filter);
    }

    /**
     * Obtiene un plato con un identificador concreto
     * @param id El id del plato
     * @return El plato
     */
    public Plato getPlato(int id) {
        return mPlatoDao.getPlato(id);
    }

    /**
     * Inserta un plato en la base de datos.
     * @param plato El plato a insertar.
     * @return Un valor long que representa el identificador del plato creado.
     */
    public long insertPlato(Plato plato) {
        AtomicLong result = new AtomicLong();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPlatoDao.insertPlato(plato));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
            result.set(-1);
        }
        return result.get();
    }

    /**
     * Modifica un plato en la base de datos.
     * @param plato El plato a ser modificado.
     * @return Un valor entero con el número de filas modificadas.
     */
    public int updatePlato(Plato plato) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPlatoDao.updatePlato(plato));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina un plato de la base de datos.
     * @param plato El plato a ser eliminado.
     * @return Un valor int con el número de filas eliminadas.
     */
    public int deletePlato(Plato plato) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPlatoDao.deletePlato(plato));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina todos los pedidos que contienen un plato
     * @param plato El plato cuyos pedidos se quiere eliminar
     * @return El número de pedidos eliminados.
     */
    public int deleteAllPedidosWithAPlato(Plato plato) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.deleteAllPedidosWithAPlato(plato.getId()));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Obtiene todos los pedidos almacenados en la base de datos.
     * @return Una lista LiveData de todos los pedidos.
     */
    public LiveData<List<Pedido>> getAllPedidos() {
        return mAllPedidos;
    }

    /**
     * Obtiene todos los pedidos almacenados en la base de datos ordenados según un filtro.
     * @param filter El filtro por el que se ordena.
     * @return Una lista LiveData de todos los pedidos.
     */
    public LiveData<List<Pedido>> getOrderedPedidos(String filter) {
        return mPedidoDao.getOrderedPedidos(filter);
    }

    /**
     * Obtiene todos los pedidos almacenados en la base de datos filtrados por su estado y ordenados
     * por un filtro
     * @param filter El filtro por el que se ordena.
     * @param estado El estado por el que se filtra.
     * @return Una lista LiveData de todos los pedidos.
     */
    public LiveData<List<Pedido>> getOrderedPedidos(String filter, String estado) {
        return mPedidoDao.getOrderedPedidos(filter, estado);
    }

    /**
     * Inserta un pedido en la base de datos.
     * @param pedido El pedido a ser insertado.
     * @return Un valor long con el identificador del pedido creado.
     */
    public long insertPedido(Pedido pedido) { // !!
        AtomicLong result = new AtomicLong();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.insert(pedido));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
        /*
        Callable<Long> tarea = () -> mPedidoDao.insert(pedido);
        Future<Long> future = executorService.submit(tarea);

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
        */
    }

    /**
     * Modifica un pedido en la base de datos.
     * @param pedido El pedido a ser actualizado.
     * @return Un valor int con el número de filas modificadas.
     */
    public int updatePedido(Pedido pedido) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.updatePedido(pedido));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina un pedido de la base de datos.
     * @param pedido El pedido a ser eliminado.
     * @return Un valor int con el número de filas eliminadas.
     */
    public int deletePedido(Pedido pedido) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.deletePedido(pedido));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Obtiene todos los platos asociados a un pedido con un identificador concreto
     */
    public LiveData<List<Racion>> getAllRaciones() {
        return mPedidoDao.getRaciones();
    }

    /**
     * Inserta una ración en la base de datos.
     * @param racion La ración a insertar.
     * @return El id de la ración insertada.
     */
    public long insertRacion(Racion racion) {
        AtomicLong result = new AtomicLong();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.insertPlatoInPedido(racion));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Actualiza una ración en la base de datos.
     * @param racion La ración a actualizar.
     * @return El número de raciones actualizadas.
     */
    public int updateRacion(Racion racion) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.updatePlatoInPedido(racion));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina una ración en la base de datos.
     * @param racion La ración a eliminar.
     * @return El número de raciones eliminadas.
     */
    public int deleteRacion(Racion racion) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.deletePlatoInPedido(racion));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }

    /**
     * Elimina todas las raciones asociadas a un pedido en la base de datos.
     * @param id El id del pedido
     * @return El número de raciones eliminadas.
     */
    public int deleteAllPlatosOfPedido(int id) {
        AtomicInteger result = new AtomicInteger();
        Semaphore resource = new Semaphore(0);
        RestauranteRoomDatabase.databaseWriteExecutor.execute(() -> {
            result.set(mPedidoDao.deleteAllPlatosOfPedido(id));
            resource.release();
        });
        try {
            resource.tryAcquire(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.d("Repository", "InterruptedException" + e.getMessage());
        }
        return result.get();
    }
}

package es.unizar.eina.T201_comidas.database.pedidos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unizar.eina.T201_comidas.database.platos.Plato;
import es.unizar.eina.T201_comidas.database.raciones.Racion;

/**
 * Definición de un Data Access Object para Pedido.
 */
@Dao
public interface PedidoDao {

    /**
     * Inserta un pedido en la base de datos.
     * @param pedido El pedido a insertar.
     * @return El nuevo identificador del pedido insertado.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Pedido pedido);

    /**
     * Actualiza un pedido existente en la base de datos.
     * @param pedido El pedido a actualizar.
     * @return El número de pedidos actualizados.
     */
    @Update
    int updatePedido(Pedido pedido);

    /**
     * Elimina un pedido de la base de datos.
     * @param pedido El pedido a ser eliminado.
     * @return El número de pedidos eliminados
     */
    @Delete
    int deletePedido(Pedido pedido);

    /**
     * Elimina todos los pedidos de la base de datos.
     */
    @Query("DELETE FROM pedidosTabla")
    void deleteAll();

    /**
     * Obtiene todos los pedidos ordenados según un criterio indicado en el filtro
     * @return Una lista LiveData de pedidos ordenada
     */
    @Query("SELECT * FROM pedidosTabla ORDER BY "
            + "CASE WHEN :filter = 'nombreCliente' THEN nombreCliente END ASC, "
            + "CASE WHEN :filter = 'numeroMovilCliente' THEN numeroMovilCliente END ASC, "
            + "CASE WHEN :filter = 'fechayhora' THEN fecha END ASC, "
            + "CASE WHEN :filter = 'fechayhora' THEN hora END ASC")
    LiveData<List<Pedido>> getOrderedPedidos(String filter);

    /**
     * Obtiene todos los pedidos ordenados según un criterio indicado en el filtro y filtrados
     * por un estado en concreto
     * @param filter El filtro para ordenar en la consulta
     * @param estado El estado por el que se quiere filtrar
     * @return Una lista LiveData de pedidos ordenada y filtrada
     */
    @Query("SELECT * FROM pedidosTabla " +
            "WHERE estado = :estado " +
            "ORDER BY "
            + "CASE WHEN :filter = 'nombreCliente' THEN nombreCliente END ASC, "
            + "CASE WHEN :filter = 'numeroMovilCliente' THEN numeroMovilCliente END ASC, "
            + "CASE WHEN :filter = 'fechayhora' THEN fecha END ASC, "
            + "CASE WHEN :filter = 'fechayhora' THEN hora END ASC")
    LiveData<List<Pedido>> getOrderedPedidos(String filter, String estado);

    /**
     * Elimina todos los pedidos que contienen un plato
     * @param idPlato El identificador del plato
     * @return El número de pedidos eliminados.
     */
    @Query("DELETE FROM pedidosTabla WHERE id IN (SELECT pedidoID FROM racionesTabla WHERE platoID = :idPlato)")
    int deleteAllPedidosWithAPlato(int idPlato);

    /**
     * Obtiene todos las raciones asociadas al pedido con un identificador concreto
     * @return Una lista de raciones
     */
    @Query("SELECT * FROM racionesTabla")
    LiveData<List<Racion>> getRaciones();

    /**
     * Inserta una ración (clase asociación) en la base de datos.
     * @param racion La ración insertar.
     * @return El nuevo identificador de la ración insertada.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertPlatoInPedido(Racion racion);

    /**
     * Actualiza una ración existente en la base de datos.
     * @param racion La ración a actualizar.
     * @return El número de raciones actualizadas.
     */
    @Update
    int updatePlatoInPedido(Racion racion);

    /**
     * Elimina una ración existente en la base de datos.
     * @param racion La ración a eliminar.
     * @return El número de raciones eliminadas.
     */
    @Delete
    int deletePlatoInPedido(Racion racion);

    /**
     * Elimina todas las raciones existente en la base de datos vinculadas a un pedido
     * @param id El identificador del pedido cuyas raciones se quiere eliminar
     * @return El número de raciones eliminadas.
     */
    @Query("DELETE FROM racionesTabla WHERE pedidoID = :id")
    int deleteAllPlatosOfPedido(int id);
}

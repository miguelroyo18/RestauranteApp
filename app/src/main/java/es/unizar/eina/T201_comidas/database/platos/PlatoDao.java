package es.unizar.eina.T201_comidas.database.platos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Definición de un Data Access Object para Plato.
 */
@Dao
public interface PlatoDao {

    /**
     * Inserta un plato en la base de datos.
     * @param plato El plato a añadir.
     * @return El número de platos añadidos
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertPlato(Plato plato);

    /**
     * Actualiza un plato de la base de datos.
     * @param plato El plato a ser actualizado.
     * @return El número de platos actualizados
     */
    @Update
    int updatePlato(Plato plato);

    /**
     * Elimina un plato de la base de datos.
     * @param plato El plato a ser eliminado.
     * @return El número de platos eliminados
     */
    @Delete
    int deletePlato(Plato plato);

    /**
     * Elimina todos los platos de la base de datos.
     */
    @Query("DELETE FROM platosTabla")
    void deleteAll();

    /**
     * Obtiene todos los platos ordenados por el filtro indicado.
     * @param filter El filtro que se desea aplicar sobre los datos de la base de datos.
     * @return Una lista LiveData de platos ordenados.
     */
    @Query("SELECT * FROM platosTabla ORDER BY "
            + "CASE WHEN :filter = 'nombre' THEN nombre END ASC, "
            + "CASE WHEN :filter = 'categoria' THEN CASE WHEN categoria = 'Primero' THEN 1 WHEN categoria = 'Segundo' THEN 2 WHEN categoria = 'Postre' THEN 3 ELSE 4 END END ASC, "
            + "CASE WHEN :filter = 'catnom' THEN CASE WHEN categoria = 'Primero' THEN 1 WHEN categoria = 'Segundo' THEN 2 WHEN categoria = 'Postre' THEN 3 ELSE 4 END END ASC, "
            + "CASE WHEN :filter = 'catnom' THEN nombre END ASC")
    LiveData<List<Plato>> getOrderedPlatos(String filter);

    /**
     * Devuelve el plato con un identificador concreto
     * @param id El id del plato
     * @return El plato
     */
    @Query("SELECT * FROM platosTabla WHERE id = :id")
    Plato getPlato(int id);
}


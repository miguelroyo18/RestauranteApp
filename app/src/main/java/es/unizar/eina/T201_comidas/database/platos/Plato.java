package es.unizar.eina.T201_comidas.database.platos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa un plato.
 * */
@Entity(tableName = "platosTabla")
public class Plato {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "ingredientes")
    private String ingredientes;

    @ColumnInfo(name = "categoria")
    private String categoria;

    @ColumnInfo(name = "precio")
    private float precio;

    /**
     * Constructor de la clase Plato.
     * @param nombre    El nombre del plato.
     * @param ingredientes      Los descripción e ingredientes del plato.
     * @param categoria La categoría del plato.
     * @param precio    El precio de una ración del plato.
     */
    public Plato(@NonNull String nombre, String ingredientes, String categoria, float precio) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.categoria = categoria;
        this.precio = precio;
    }

    /**
     * Devuelve el identificador numérico del plato.
     * @return El identificador del plato.
     */
    public int getId(){
        return this.id;
    }

    /**
     * Permite modificar el identificador del plato.
     * @param id El nuevo identificador del plato.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del plato.
     * @return El nombre del plato.
     */
    public String getNombre(){
        return this.nombre;
    }

    /**
     * Devuelve la descripción e ingredientes del plato.
     * @return La descripción e ingredientes del plato.
     */
    public String getIngredientes(){
        return this.ingredientes;
    }

    /**
     * Devuelve el precio del plato.
     * @return El precio del plato.
     */
    public float getPrecio() {return this.precio; }

    /**
     * Devuelve la categoría del plato.
     * @return La categoría del plato.
     */
    public String getCategoria() {return this.categoria; }
}

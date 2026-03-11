package es.unizar.eina.T201_comidas.database.raciones;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

/**
 * Entidad que representa una asociación entre un pedido y un número de raciones de un plato
 */
@Entity(tableName = "racionesTabla", primaryKeys = {"pedidoID", "platoID"})
public class Racion implements Serializable {
    @ColumnInfo(name = "pedidoID")
    private int pedidoID;
    @ColumnInfo(name = "platoID")
    private int platoID;

    @NonNull
    @ColumnInfo(name = "raciones")
    private int raciones;

    /** 
     * Constructor de la clase Ración.
     * @param pedidoID ID del pedido.
     * @param platoID  ID del plato.
     * @param raciones Las raciones del plato
     */
    public Racion(@NonNull int pedidoID, @NonNull int platoID, @NonNull int raciones) {
        this.pedidoID = pedidoID;
        this.platoID = platoID;
        this.raciones = raciones;
    }

    /**
     * Devuelve el id del pedido asociado a la ración
     * @return El id
     */
    public int getPedidoID() {return pedidoID;}

    /**
     * Devuelve el id del plato asociado a la ración
     * @return El id
     */
    public int getPlatoID() {return platoID;}

    /**
     * Devuelve el número de raciones del plato asociadas al pedido
     * @return El número de raciones
     */
    public int getRaciones() {return raciones;}
}

package es.unizar.eina.T201_comidas.database.pedidos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidad que representa un pedido.
 * */
@Entity(tableName = "pedidosTabla")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    @ColumnInfo(name = "numeroMovilCliente")
    private String numeroMovilCliente;

    @ColumnInfo(name = "estado")
    private String estado;

    @ColumnInfo(name = "precioTotal")
    private float precioTotal;

    @ColumnInfo(name = "fecha")
    private String fecha;

    @ColumnInfo(name = "hora")
    private String hora;

    /**
     * Constructor de la clase Pedido.
     * @param nombreCliente       Nombre del cliente.
     * @param numeroMovilCliente  Número de móvil del cliente.
     * @param estado              Estado del pedido.
     * @param precioTotal              Precio del pedido.
     * @param fecha               Fecha del pedido.
     * @param hora                Hora del pedido.
     */
    public Pedido(@NonNull String nombreCliente, String numeroMovilCliente, String estado, float precioTotal
                 , String fecha, String hora) {
        this.nombreCliente = nombreCliente;
        this.numeroMovilCliente = numeroMovilCliente;
        this.estado = estado;
        this.precioTotal = precioTotal;
        this.fecha = fecha;
        this.hora = hora;
    }

    /**
     * Devuelve el identificador numérico del pedido.
     * @return El identificador del pedido.
     */
    public int getId(){
        return this.id;
    }

    /**
     * Permite cambiar el identificador del pedido.
     * @param id El nuevo identificador del pedido.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del cliente del pedido.
     * @return El nombre del cliente.
     */
    public String getNombreCliente(){
        return this.nombreCliente;
    }

    /**
     * Devuelve el número de móvil del cliente.
     * @return El número de móvil del cliente.
     */
    public String getNumeroMovilCliente(){
        return this.numeroMovilCliente;
    }

    /**
     * Devuelve el precio del pedido.
     * @return El precio del pedido.
     */
    public float getPrecioTotal() {return this.precioTotal; }

    /**
     * Devuelve el estado del pedido.
     * @return El estado del pedido.
     */
    public String getEstado() {return this.estado; }

    /**
     * Devuelve la fecha del pedido.
     * @return La fecha del pedido.
     */
    public String getFecha() {return this.fecha; }

    /**
     * Devuelve la hora del pedido.
     * @return La hora del pedido.
     */
    public String getHora() {return this.hora; }

}

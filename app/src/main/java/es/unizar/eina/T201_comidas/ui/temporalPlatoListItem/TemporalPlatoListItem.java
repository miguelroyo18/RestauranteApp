package es.unizar.eina.T201_comidas.ui.temporalPlatoListItem;

import java.io.Serializable;

/**
 * Clase correspondiente a una ración temporal utilizada internamente durante la creación y edición
 * de pedidos.
 */
public class TemporalPlatoListItem implements Serializable {
    private int platoID;
    private int raciones;
    private String name;
    private Float precio;

    /**
     * Constructor de la ración temporal.
     * @param platoID El id del plato de la ración.
     * @param raciones Las raciones del plato.
     */
    public TemporalPlatoListItem(int platoID, int raciones, String name, Float precio) {
        this.platoID = platoID;
        this.raciones = raciones;
        this.name = name;
        this.precio = precio;
    }

    /**
     * @return El id del plato
     */
    public int getPlatoID() {return platoID;}

    /**
     * @return El número de raciones
     */
    public int getRaciones() {return raciones;}

    /**
     * @return El número de raciones
     */
    public String getName() {return name;}
    /**
     * @return El número de raciones
     */
    public Float getPrecio() {return precio;}

    /**
     * Incrementa en uno el número de raciones del plato
     */
    public void increaseRaciones() {raciones++;}

    /** 
     * Decrementa en uno el número de raciones del plato
     */
    public void decreaseRaciones() {raciones--;}

}

package es.unizar.eina.T201_comidas.ui.pedidos;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T201_comidas.database.pedidos.Pedido;

/**
 * Adapter para la lista de pedidos en la interfaz de usuario.
 */
public class PedidoListAdapter extends ListAdapter<Pedido, PedidoViewHolder> {
    private int position;

    /**
     * Constructor del adaptador.
     *
     * @param diffCallback El objeto DiffUtil.ItemCallback para comparar elementos en la lista.
     */
    public PedidoListAdapter(@NonNull DiffUtil.ItemCallback<Pedido> diffCallback) {
        super(diffCallback);
    }

    /**
     * Crea y devuelve una instancia de PedidoViewHolder.
     *
     * @param parent   El grupo al que se va a añadir la nueva vista.
     * @param viewType El tipo de la nueva vista.
     * @return Una instancia de PedidoViewHolder.
     */
    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PedidoViewHolder.create(parent);
    }

    /**
     * Vincula los datos del pedido actual al ViewHolder y establece un onLongClickListener.
     *
     * @param holder   El ViewHolder que debe ser actualizado.
     * @param position La posición del elemento en la lista de datos.
     */
    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {

        Pedido current = getItem(position);
        holder.bind(current.getNombreCliente(), current.getEstado(), String.valueOf(current.getPrecioTotal()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    /**
     * Obtiene la posición actual en la lista.
     *
     * @return La posición actual en la lista.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Establece la posición actual en la lista.
     *
     * @param position La posición a establecer.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Obtiene el pedido actual en la posición especificada.
     *
     * @return El pedido actual.
     */
    public Pedido getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Clase interna que implementa la lógica de comparación de elementos para el adaptador.
     */
    static class PedidoDiff extends DiffUtil.ItemCallback<Pedido> {

        /**
         * Comprueba si dos elementos son iguales (tienen el mismo identificador).
         *
         * @param oldItem El antiguo objeto Pedido.
         * @param newItem El nuevo objeto Pedido.
         * @return true si los elementos son los mismos, false de lo contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Comprueba si el contenido de dos elementos es el mismo.
         *
         * @param oldItem El antiguo objeto Pedido.
         * @param newItem El nuevo objeto Pedido.
         * @return true si el contenido es el mismo, false de lo contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            return oldItem.getNombreCliente().equals(newItem.getNombreCliente()) &&
                   oldItem.getEstado().equals(newItem.getEstado()) &&
                   oldItem.getPrecioTotal() == newItem.getPrecioTotal();
        }
    }
}

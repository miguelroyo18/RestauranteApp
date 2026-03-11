package es.unizar.eina.T201_comidas.ui.platos;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T201_comidas.database.platos.Plato;

/**
 * Adaptador personalizado para la lista de platos.
 */
public class PlatoSelectAdapter extends ListAdapter<Plato, PlatoViewHolder> {
    private OnItemClickListener itemClickListener;

    /**
     * Constructor del adaptador.
     *
     * @param diffCallback Callback para comparar elementos de la lista.
     */
    public PlatoSelectAdapter(@NonNull DiffUtil.ItemCallback<Plato> diffCallback) {
        super(diffCallback);
    }

    /**
     * Crea y devuelve un nuevo ViewHolder para un elemento de la lista.
     *
     * @param parent   El grupo al que se añadirá el nuevo View.
     * @param viewType Tipo de vista.
     * @return Nuevo ViewHolder.
     */
    @Override
    public PlatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PlatoViewHolder.create(parent);
    }

    /**
     * Vincula los datos del plato actual al ViewHolder y establece un listener de pulsación larga.
     *
     * @param holder   El ViewHolder.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato current = getItem(position);
        holder.bind(current.getNombre(), current.getCategoria(), String.valueOf(current.getPrecio()));

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(current);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(Plato selectedPlato);
    }

    /**
     * Función para gestionar los clicks del usuario en la pantalla de selección
     */
    public void setOnItemClickListener(PlatoSelectAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Clase interna para comparar platos en la lista.
     */
    static class PlatoDiff extends DiffUtil.ItemCallback<Plato> {

        /**
         * Compara si los elementos son los mismos.
         *
         * @param oldItem Elemento antiguo.
         * @param newItem Elemento nuevo.
         * @return true si los elementos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Compara si los contenidos de los elementos son iguales (nombre, ingredientes, categoría
         * y precio coinciden).
         *
         * @param oldItem Elemento antiguo.
         * @param newItem Elemento nuevo.
         * @return true si los contenidos son iguales, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            // We are just worried about differences in visual representation, i.e. changes in the title
            return oldItem.getNombre().equals(newItem.getNombre()) && oldItem.getIngredientes().equals(newItem.getIngredientes()) && oldItem.getCategoria().equals(newItem.getCategoria()) && oldItem.getPrecio() == newItem.getPrecio();
        }

    }
}

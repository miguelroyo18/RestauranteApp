package es.unizar.eina.T201_comidas.ui.platos;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.T201_comidas.R;
import es.unizar.eina.T201_comidas.database.platos.Plato;

/**
 * Adaptador personalizado para la lista de platos.
 */
public class PlatoListAdapter extends ListAdapter<Plato, PlatoViewHolder> {
    private int position;

    /**
     * Constructor del adaptador.
     *
     * @param diffCallback Callback para comparar elementos de la lista.
     */
    public PlatoListAdapter(@NonNull DiffUtil.ItemCallback<Plato> diffCallback) {
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
     * Obtiene la posición actual en la lista.
     *
     * @return La posición actual.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Establece la posición actual en la lista.
     *
     * @param position La nueva posición.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Obtiene el plato actual en la posición actual.
     *
     * @return El plato actual.
     */
    public Plato getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Vincula los datos del plato actual al ViewHolder y establece un listener de pulsación larga.
     *
     * @param holder   El ViewHolder.
     * @param position La posición del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(PlatoViewHolder holder, int position) {

        Plato current = getItem(position);
        holder.bind(current.getNombre(), current.getCategoria(), String.valueOf(current.getPrecio()));

        holder.itemView.setOnCreateContextMenuListener((new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, PlatosList.DELETE_ID, Menu.NONE, R.string.menu_delete);
                menu.add(Menu.NONE, PlatosList.EDIT_ID, Menu.NONE, R.string.menu_edit);
            }
        }));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }

        });
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
            return oldItem.getNombre().equals(newItem.getNombre()) && oldItem.getIngredientes().equals(newItem.getIngredientes()) && oldItem.getCategoria().equals(newItem.getCategoria()) && oldItem.getPrecio() == newItem.getPrecio();
        }
    }
}

package es.unizar.eina.T201_comidas.ui.platos;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import es.unizar.eina.T201_comidas.R;

/**
 * ViewHolder para elementos de la lista de platos.
 */
public class PlatoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {
    private final TextView mPlatoItemView;
    private final TextView mPlatoPriceView;
    private final TextView mPlatoCategoryView;

    /**
     * Constructor del ViewHolder.
     *
     * @param itemView Vista del elemento de la lista.
     */
    private PlatoViewHolder(View itemView) {
        super(itemView);
        mPlatoItemView = itemView.findViewById(R.id.textPlatoView);
        mPlatoPriceView = itemView.findViewById(R.id.priceView);
        mPlatoCategoryView = itemView.findViewById(R.id.categoryView);

        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Método estático para crear una instancia de PlatoViewHolder.
     *
     * @param parent Grupo al que se añadirá el nuevo View.
     * @return Nuevo PlatoViewHolder.
     */
    static PlatoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    /**
     * Metodo para la creación del menú contextual.
     *
     * @param menu     Menú contextual a crear.
     * @param v        Vista que recibirá el menú contextual.
     * @param menuInfo Información sobre el menú contextual.
     */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {}

    @Override
    public void onClick(View v) {}

    /**
     * Vincula datos al ViewHolder.
     *
     * @param text     Texto a mostrar en la vista de texto.
     * @param category Categoría del plato a mostrar.
     * @param price    Precio del plato a mostrar.
     */
    public void bind(String text, String category, String price) {
        mPlatoItemView.setText(text);
        mPlatoCategoryView.setText(category.substring(0, 1).toUpperCase(Locale.ROOT) + category.substring(1).toLowerCase());
        mPlatoPriceView.setText(price + " €");
    }
}

package es.unizar.eina.T201_comidas.ui.pedidos;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import es.unizar.eina.T201_comidas.R;

/**
 * ViewHolder para pedidos.
 */
class PedidoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView mPedidoItemView;
    private final TextView mPedidoPriceView;
    private final TextView mStateView;

    private PedidoViewHolder(View itemView) {
        super(itemView);
        mPedidoItemView = itemView.findViewById(R.id.textPedidoView);
        mPedidoPriceView = itemView.findViewById(R.id.priceView);
        mStateView = itemView.findViewById(R.id.stateView);

        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Vincula los datos del pedido actual con la vista.
     *
     * @param text Texto a mostrar en la vista.
     */
    public void bind(String text, String state, String price) {
        mPedidoItemView.setText(text);
        mStateView.setText(state.substring(0, 1).toUpperCase(Locale.ROOT) + state.substring(1).toLowerCase());
        mPedidoPriceView.setText(price + " €");
    }

    /**
     * Crea un nuevo ViewHolder para un elemento de la lista.
     *
     * @param parent El ViewGroup al que pertenece la vista.
     * @return Un nuevo PedidoViewHolder que contiene la vista de un elemento de la lista.
     */
    static PedidoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    /**
     * Muestra el menú contextual al realizar una pulsación larga en el elemento de la lista.
     *
     * @param menu     Menú contextual que se mostrará.
     * @param v        Vista que se ha pulsado.
     * @param menuInfo Información sobre el menú contextual.
     */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, PedidosList.DELETE_ID, Menu.NONE, R.string.menu_delete);
        menu.add(Menu.NONE, PedidosList.EDIT_ID, Menu.NONE, R.string.menu_edit);
        menu.add(Menu.NONE, PedidosList.SEND_ID, Menu.NONE, R.string.menu_send);
    }


}

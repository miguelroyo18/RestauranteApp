package es.unizar.eina.T201_comidas.ui.temporalPlatoListItem;

import android.app.Application;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador personalizado de la lista de raciones temporales
 */
public class TempPlatoAdapter extends RecyclerView.Adapter<TempPlatoViewHolder> {

    Context context;
    Application application;
    List<TemporalPlatoListItem> platos;
    OnIncreaseRacionClickListener mListener;
    OnDecreaseRacionClickListener dListener;
    OnDeletePlatoClickListener rListener;

    /**
     * Constructor del adaptador
     * @param context El contexto de la aplicación
     * @param temporalPlatoListItems La lista temporal de raciones
     */
    public TempPlatoAdapter(Context context, List<TemporalPlatoListItem> temporalPlatoListItems, Application application) {
        this.context = context;
        this.platos = temporalPlatoListItems;
        this.application = application;
    }

    @NonNull
    @Override
    public TempPlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TempPlatoViewHolder viewHolder = TempPlatoViewHolder.create(parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TempPlatoViewHolder holder, int position) {
        TemporalPlatoListItem item = platos.get(position);
        holder.bind(item, mListener, dListener, rListener);
    }

    // Listeners

    /**
     * Listener de incremento de ración
     */
    public interface OnIncreaseRacionClickListener {
        void onIncreaseRacionClick(int position);
    }

    /**
     * Listener de decremento de ración
     */
    public interface OnDecreaseRacionClickListener {
        void onDecreaseRacionClick(int position);
    }

    /**
     * Listener de eliminación de plato
     */
    public interface OnDeletePlatoClickListener {
        void onDeletePlatoClick(int position);
    }

    public void setOnIncreaseRacionClickListener(OnIncreaseRacionClickListener listener) {
        this.mListener = listener;
    }

    public void setOnDecreaseRacionClickListener(OnDecreaseRacionClickListener listener) {
        this.dListener = listener;
    }

    public void setOnDeletePlatoClickListener(OnDeletePlatoClickListener listener) {
        this.rListener = listener;
    }

    /**
     * Función que devuelve el tamaño de la lista de platos
     * @return El tamaño de la lista de platos
     */
    @Override
    public int getItemCount() {
        return platos.size();
    }

    /** 
     * Incrementa en uno las raciones de un plato de la lista.
     * @param position La posición del plato en la lista.
     */
    public void increaseRacion(int position) {
        TemporalPlatoListItem temp = platos.get(position);
        temp.increaseRaciones();
        notifyItemChanged(position);
    }

    /** 
     * Decrementa en uno las raciones de un plato de la lista.
     * @param position La posición del plato en la lista.
     */
    public void decreaseRacion(int position) {
        TemporalPlatoListItem temp = platos.get(position);
        if (temp.getRaciones() == 1) {
            deletePlato(position);
        }
        else {
            temp.decreaseRaciones();
            notifyItemChanged(position);
        }
    }

    /**
     * Elimina un plato de la lista
     * @param position La posición del plato en la lista
     */
    public void deletePlato(int position) {
        platos.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Función que calcula el precio total de pedido a partir de la lista temporal de platos
     */
    public float calculatePrice() {
        float precio = 0;
        if (!platos.isEmpty()) {
            for (TemporalPlatoListItem temp : platos) {
                precio += temp.getPrecio() * temp.getRaciones();
            }
        }
        return precio;
    }
}

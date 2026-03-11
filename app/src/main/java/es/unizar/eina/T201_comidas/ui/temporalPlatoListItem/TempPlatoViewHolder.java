package es.unizar.eina.T201_comidas.ui.temporalPlatoListItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T201_comidas.R;

/**
 * ViewHolder de la lista temporal de raciones
 */
public class TempPlatoViewHolder extends RecyclerView.ViewHolder {

    TextView nombrePlato;
    TextView raciones;

    Button increaseRacionButton;
    Button decreaseRacionButton;
    Button deletePlatoButton;
    TempPlatoAdapter.OnIncreaseRacionClickListener mListener;
    TempPlatoAdapter.OnDecreaseRacionClickListener dListener;
    TempPlatoAdapter.OnDeletePlatoClickListener rListener;

    /**
     * Constructor del ViewHolder
     * @param view La vista
     */
    public TempPlatoViewHolder(@NonNull View view) {
        super(view);

        nombrePlato = view.findViewById(R.id.nombrePlatoListItem);
        raciones = view.findViewById(R.id.racionesPlatoListItem);

        increaseRacionButton = itemView.findViewById(R.id.buttonIncreaseRacion);
        increaseRacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onIncreaseRacionClick(position);
                }
            }
        });

        decreaseRacionButton = itemView.findViewById(R.id.buttonDecreaseRacion);
        decreaseRacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && dListener != null) {
                    dListener.onDecreaseRacionClick(position);
                }
            }
        });

        deletePlatoButton = itemView.findViewById(R.id.buttonDeletePlato);
        deletePlatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && rListener != null) {
                    rListener.onDeletePlatoClick(position);
                }
            }
        });
    }

    /**
     * Método estático para crear una instacia de TempPlatoViewHolder.
     * @param parent Grupo al que se añadirá el nuevo view.
     * @return Nuevo TempPlatoViewHolder
     */
    static TempPlatoViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plato_item_in_pedido, parent, false);
        return new TempPlatoViewHolder(view);
    }

    /** 
     * Vincula datos al ViewHolder.
     * @param item El elemento de la lista.
     * @param listener El listener de aumento de raciones.
     * @param dlistener El listener de decremento de raciones.
     * @param rlistener El listener de eliminación de ración.
     */
    public void bind(TemporalPlatoListItem item, TempPlatoAdapter.OnIncreaseRacionClickListener listener, TempPlatoAdapter.OnDecreaseRacionClickListener dlistener, TempPlatoAdapter.OnDeletePlatoClickListener rlistener) {
        nombrePlato.setText(item.getName());
        raciones.setText(Integer.toString(item.getRaciones()));

        increaseRacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onIncreaseRacionClick(getAdapterPosition());
                }
            }
        });

        decreaseRacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dlistener != null) {
                    dlistener.onDecreaseRacionClick(getAdapterPosition());
                }
            }
        });

        deletePlatoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rlistener != null) {
                    rlistener.onDeletePlatoClick(getAdapterPosition());
                }
            }
        });
    }
}

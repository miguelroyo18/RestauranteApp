package es.unizar.eina.T201_comidas.ui.platos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.T201_comidas.R;

/**
 * Pantalla utilizada para la selección de un plato durante la edición de pedidos
 */
public class PlatoSelect extends AppCompatActivity {
    private PlatoViewModel mPlatoViewModel;
    public static final String PLATO_SELECTED = "plato";
    public static final String PLATO_SELECTED_NAME = "nombre";
    public static final String PLATO_SELECTED_PRICE = "precio";

    RecyclerView mRecyclerView;
    PlatoSelectAdapter mAdapter;

    Button mCancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plato_select_menu);

        mRecyclerView = findViewById(R.id.recyclerviewPedidoListPlatos);
        mAdapter = new PlatoSelectAdapter(new PlatoSelectAdapter.PlatoDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mPlatoViewModel.getAllPlatos().observe(this, platos -> {
            mAdapter.submitList(platos);
        });

        mCancelButton = findViewById(R.id.buttonCancel);
        mCancelButton.setOnClickListener(view -> {
            cancelInput();
        });

        mAdapter.setOnItemClickListener((selectedPlato) -> {
            Intent replyIntent = new Intent();
            replyIntent.putExtra(PlatoSelect.PLATO_SELECTED, selectedPlato.getId());
            replyIntent.putExtra(PlatoSelect.PLATO_SELECTED_NAME, selectedPlato.getNombre());
            replyIntent.putExtra(PlatoSelect.PLATO_SELECTED_PRICE, selectedPlato.getPrecio());
            setResult(RESULT_OK, replyIntent);
            finish();
        });
    }

    /**
     * Redefinición de método para que pulsar la tecla hacia atrás cancele la selección de plato.
     */
    @Override
    public void onBackPressed() { cancelInput(); }

    /**
     * Método que cancela la entrada.
     */
    private void cancelInput() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}

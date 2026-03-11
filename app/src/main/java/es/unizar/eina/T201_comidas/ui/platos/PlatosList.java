package es.unizar.eina.T201_comidas.ui.platos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.widget.PopupMenu;
import android.view.MenuInflater;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import es.unizar.eina.T201_comidas.database.platos.Plato;
import es.unizar.eina.T201_comidas.R;
import es.unizar.eina.T201_comidas.ui.Pruebas;
import es.unizar.eina.T201_comidas.ui.pedidos.PedidosList;

/** 
 * Pantalla principal de la aplicación T201_comidas 
 */
public class PlatosList extends AppCompatActivity {

    public static final int ACTIVITY_CREATE = 1;
    public static final int ACTIVITY_EDIT = 2;
    static final int DELETE_ID = Menu.FIRST;
    static final int EDIT_ID = Menu.FIRST + 1;

    private PlatoViewModel mPlatoViewModel;
    RecyclerView mRecyclerView;

    PlatoListAdapter mAdapter;

    FloatingActionButton mFab;
    FloatingActionButton mFabPruebas;
    Button mSort;

    TabLayout mTab;
    private int tabIndexToSelect;

    private String sortBy;

    private int pruebasCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plato_list);

        mRecyclerView = findViewById(R.id.recyclerviewPedidoListPlatos);
        mAdapter = new PlatoListAdapter(new PlatoListAdapter.PlatoDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mTab = findViewById(R.id.tabs);

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        switchToPedidos();
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });

        tabIndexToSelect = 0;

        mPlatoViewModel = new ViewModelProvider(this).get(PlatoViewModel.class);
        mPlatoViewModel.getAllPlatos().observe(this, platos -> {
            mAdapter.submitList(platos);
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {
            createPlato();
        });

        mFabPruebas = findViewById(R.id.fabPruebs);
        mFabPruebas.setOnClickListener(view -> {
            goToTestMenu();
        });

        mSort = findViewById(R.id.buttonSortPlato);
        // sortBy = "Nombre";

        registerForContextMenu(mRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabLayout.Tab tabToSelect = mTab.getTabAt(tabIndexToSelect);
        if (tabToSelect != null) {
            tabToSelect.select();
        }
    }

    /**
     * Maneja el resultado de las actividades iniciadas para obtener resultados.
     *
     * @param requestCode Código de solicitud original.
     * @param resultCode  Código de resultado devuelto por la actividad.
     * @param data        Datos devueltos por la actividad.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        if (resultCode != RESULT_CANCELED) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_allowed,
                        Toast.LENGTH_LONG).show();
            } else {
                switch (requestCode) {
                    case ACTIVITY_CREATE:
                        Plato newPlato = new Plato(extras.getString(PlatoEdit.PLATO_NOMBRE)
                                , extras.getString(PlatoEdit.PLATO_INGREDIENTES)
                                , extras.getString(PlatoEdit.PLATO_CATEGORIA)
                                , Float.parseFloat(extras.getString(PlatoEdit.PLATO_PRECIO)));
                        mPlatoViewModel.insert(newPlato);
                        break;
                    case ACTIVITY_EDIT:

                        int id = extras.getInt(PlatoEdit.PLATO_ID);
                        Plato updatedPlato = new Plato(extras.getString(PlatoEdit.PLATO_NOMBRE)
                                , extras.getString(PlatoEdit.PLATO_INGREDIENTES)
                                , extras.getString(PlatoEdit.PLATO_CATEGORIA)
                                , Float.parseFloat(extras.getString(PlatoEdit.PLATO_PRECIO)));
                        updatedPlato.setId(id);
                        mPlatoViewModel.update(updatedPlato);
                        break;
                }
            }
        }
    }

    /**
     * Método que se ocupa de la selección de elementos en el menú contextual.
     *
     * @param item Elemento del menú contextual seleccionado.
     * @return true si se maneja el evento, false en caso contrario.
     */
    public boolean onContextItemSelected(MenuItem item) {
        Plato current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.deleting_plato) + " (" + current.getNombre() + ")",
                        Toast.LENGTH_LONG).show();
                mPlatoViewModel.delete(current);
                return true;
            case EDIT_ID:
                editPlato(current);
                return true;
        }
        return super.onContextItemSelected(item);
    }


    /**
     * Muestra un menú de clasificación para los platos.
     *
     * @param view La vista que activa el menú.
     */
    public void showSortMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.sort_menu_plato, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort_by_nombre_plato) {
                sortBy = "nombre";
            } else if (item.getItemId() == R.id.sort_by_nom_cat_plato) {
                sortBy = "catnom";
            } else if (item.getItemId() == R.id.sort_by_cat_plato) {
                sortBy = "categoria";
            }
            mPlatoViewModel.getOrderedPlatos(sortBy).observe(this, sortedPlatos -> {
                mAdapter.submitList(sortedPlatos);
            });
            updateButtonText((String) item.getTitle());
            return true;
        });

        popupMenu.show();
    }

    /**
     * Actualiza el texto del botón de ordenado.
     *
     * @param newText El nuevo texto que se mostrará en el botón.
     */
    private void updateButtonText(String newText) {
        mSort.setText(newText);
    }

    /**
     * Cambia a la actividad de lista de pedidos.
     */
    private void switchToPedidos() {
        Intent intent = new Intent(this, PedidosList.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * Inicia la actividad para crear un nuevo plato.
     */
    private void createPlato() {
        Intent intent = new Intent(this, PlatoEdit.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

    /**
     * Inicia la actividad para editar un plato existente.
     *
     * @param current Plato que se va a editar.
     */
    private void editPlato(Plato current) {
        Intent intent = new Intent(this, PlatoEdit.class);
        intent.putExtra(PlatoEdit.PLATO_NOMBRE, current.getNombre());
        intent.putExtra(PlatoEdit.PLATO_INGREDIENTES, current.getIngredientes());
        intent.putExtra(PlatoEdit.PLATO_CATEGORIA, current.getCategoria());
        intent.putExtra(PlatoEdit.PLATO_PRECIO, Float.toString(current.getPrecio()));
        intent.putExtra(PlatoEdit.PLATO_ID, current.getId());
        startActivityForResult(intent, ACTIVITY_EDIT);
    }

    /**
     * Lanza el menú de pruebas de la aplicación
     */
    private void goToTestMenu() {
        Intent intent = new Intent(this, Pruebas.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (++pruebasCount == 5) {
            Intent intent = new Intent(this, Pruebas.class);
            startActivity(intent);
            pruebasCount = 0;
        }
    }
}

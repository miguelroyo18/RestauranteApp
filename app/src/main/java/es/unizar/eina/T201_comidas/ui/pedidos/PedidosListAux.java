package es.unizar.eina.T201_comidas.ui.pedidos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.List;

import es.unizar.eina.T201_comidas.R;
import es.unizar.eina.T201_comidas.database.pedidos.Pedido;
import es.unizar.eina.T201_comidas.database.raciones.Racion;
import es.unizar.eina.T201_comidas.ui.Pruebas;
import es.unizar.eina.T201_comidas.ui.platos.PlatosList;
import es.unizar.eina.T201_comidas.ui.temporalPlatoListItem.TemporalPlatoListItem;
import es.unizar.eina.send.SendAbstractionImpl;

/**
 * Pantalla principal de la aplicación T201_comidas.
 */
public class PedidosListAux extends AppCompatActivity {
    private PedidoViewModel mPedidoViewModel;

    public static final int ACTIVITY_CREATE = 1;

    public static final int ACTIVITY_EDIT = 2;

    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;

    static final int SEND_ID = Menu.FIRST + 3;

    private String sortBy;

    RecyclerView mRecyclerView;

    PedidoListAdapter mAdapter;

    FloatingActionButton mFab;
    FloatingActionButton mFabPruebas;

    ToggleButton buttonSolicitado;
    ToggleButton buttonPreparado;
    ToggleButton buttonRecogido;

    Button mSort;

    private String estado = "";
    private String order = "nombreCliente";
    private String currentEstado = "";

    private SendAbstractionImpl mSendImplementor;

    TabLayout mTab;
    private int pruebasCount = 0;

    private List<TemporalPlatoListItem> temporalPlatoListItems1;
    private List<Racion> temporalPlatoListItems2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_list);

        mRecyclerView = findViewById(R.id.recyclerviewPedidoListPlatos);
        mAdapter = new PedidoListAdapter(new PedidoListAdapter.PedidoDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mSendImplementor = new SendAbstractionImpl(this, "SMS");

        mTab = findViewById(R.id.tabs);

        int tabIndexToSelect = 1;
        TabLayout.Tab tabToSelect = mTab.getTabAt(tabIndexToSelect);

        if (tabToSelect != null) {
            tabToSelect.select();
        }

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab select
                switch (tab.getPosition()) {
                    case 0:
                        switchToPlatos();
                        finish();
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselect
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselect
            }
        });

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        currentEstado = sharedPreferences.getString("filter", "");

        order = sharedPreferences.getString("order", "");
        if (!("nombreCliente".equals(order) || "numeroMovilCliente".equals(order) || "fechayhora".equals(order))) {
            order = "nombreCliente";
        }

        mPedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);

        mPedidoViewModel.getOrderedPedidos(order, currentEstado).observe(this, pedidos -> {
              mAdapter.submitList(pedidos);
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> {
            createPedido();
        });

        mFabPruebas = findViewById(R.id.fabPruebs);
        mFabPruebas.setOnClickListener(view -> {
            goToTestMenu();
        });

        buttonSolicitado = findViewById(R.id.toggleButtonSolicitado);

        buttonSolicitado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonSolicitado.setBackgroundResource(R.drawable.rounded_button);
                buttonPreparado.setChecked(false);
                buttonRecogido.setChecked(false);
                estado = "Solicitado";
            } else {
                estado = "";
                buttonSolicitado.setBackgroundResource(R.drawable.rounded_border_button);
            }
            order = sharedPreferences.getString("order", "");
            filterAndSortPedidos(order, estado);
            saveFilterState(estado);
        });

        buttonPreparado = findViewById(R.id.toggleButtonPreparado);

        buttonPreparado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonPreparado.setBackgroundResource(R.drawable.rounded_button);
                buttonSolicitado.setChecked(false);
                buttonRecogido.setChecked(false);
                estado = "Preparado";
            } else {
                estado = "";
                buttonPreparado.setBackgroundResource(R.drawable.rounded_border_button);
            }
            order = sharedPreferences.getString("order", "");
            filterAndSortPedidos(order, estado);
            saveFilterState(estado);
        });

        buttonRecogido = findViewById(R.id.toggleButtonRecogido);

        buttonRecogido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                buttonRecogido.setBackgroundResource(R.drawable.rounded_button);
                buttonPreparado.setChecked(false);
                buttonSolicitado.setChecked(false);
                estado = "Recogido";
            } else {
                estado = "";
                buttonRecogido.setBackgroundResource(R.drawable.rounded_border_button);
            }
            order = sharedPreferences.getString("order", "");
            filterAndSortPedidos(order, estado);
            saveFilterState(estado);
        });

        registerForContextMenu(mRecyclerView);

        if (getString(R.string.estado_solicitado).equals(currentEstado)) {
            buttonSolicitado.setChecked(true);
        } else if (getString(R.string.estado_preparado).equals(currentEstado)) {
            buttonPreparado.setChecked(true);
        } else if (getString(R.string.estado_recogido).equals(currentEstado)) {
            buttonRecogido.setChecked(true);
        }
        else {
            filterAndSortPedidos(order, "");
            saveFilterState("");
        }

        mSort = findViewById(R.id.buttonSortPedido);

        String title;
        if (order.equals("nombreCliente")) {
            title = getString(R.string.sort_by_nombre_cliente);
        }
        else if (order.equals("numeroMovilCliente")) {
            title = getString(R.string.sort_by_num_movil);
        }
        else {
            title = getString(R.string.sort_by_fecha_hora);
        }
        updateButtonText(title);
    }


    @Override
    public void onBackPressed() {
        if (++pruebasCount == 5) {
            Intent intent = new Intent(this, Pruebas.class);
            startActivity(intent);
            pruebasCount = 0;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return ;
        }

        Bundle extras = data.getExtras();
        if (resultCode == RESULT_OK) { // En cualquier otro caso no se hace nada
            switch (requestCode) {
                case ACTIVITY_CREATE:
                    Pedido newPedido = new Pedido(extras.getString(PedidoEdit.PEDIDO_NOMBRE)
                            , extras.getString(PedidoEdit.PEDIDO_NUMERO)
                            , extras.getString(PedidoEdit.PEDIDO_ESTADO)
                            , Float.parseFloat(extras.getString(PedidoEdit.PEDIDO_PRECIO))
                            , extras.getString(PedidoEdit.PEDIDO_DATE)
                            , extras.getString(PedidoEdit.PEDIDO_TIME));
                    long insertedID = mPedidoViewModel.insert(newPedido);
                    temporalPlatoListItems1 = (List<TemporalPlatoListItem>) extras.getSerializable(PedidoEdit.PEDIDO_LIST_PLATOS);

                    for (TemporalPlatoListItem temp : temporalPlatoListItems1) {
                        Racion racion = new Racion((int) (long) insertedID, temp.getPlatoID(), temp.getRaciones()); // Arreglar
                        mPedidoViewModel.insertRacion(racion);
                    }

                    break;
                case ACTIVITY_EDIT:

                    int id = Integer.parseInt(extras.getString(PedidoEdit.PEDIDO_ID));
                    Pedido updatedPedido = new Pedido(extras.getString(PedidoEdit.PEDIDO_NOMBRE)
                            , extras.getString(PedidoEdit.PEDIDO_NUMERO)
                            , extras.getString(PedidoEdit.PEDIDO_ESTADO)
                            , Float.parseFloat(extras.getString(PedidoEdit.PEDIDO_PRECIO))
                            , extras.getString(PedidoEdit.PEDIDO_DATE)
                            , extras.getString(PedidoEdit.PEDIDO_TIME));
                    updatedPedido.setId(id); // Is this the source of errors?
                    mPedidoViewModel.update(updatedPedido);
                    temporalPlatoListItems1 = (List<TemporalPlatoListItem>) extras.getSerializable(PedidoEdit.PEDIDO_LIST_PLATOS);
                    generateTemporalPlatoList(id, new DataReadyCallback() {
                        @Override
                        public void onDataReady(List<Racion> dataList) {
                            temporalPlatoListItems2 = dataList;
                        }
                    });

                    for (Racion temp2 : temporalPlatoListItems2) {
                        boolean isInList = false;
                        for (TemporalPlatoListItem temp1 : temporalPlatoListItems1) {
                            if (temp2.getPlatoID() == temp1.getPlatoID()) {
                                isInList = true;
                                Racion racion = new Racion(id, temp1.getPlatoID(), temp1.getRaciones());
                                mPedidoViewModel.updateRacion(racion);
                                break;
                            }
                        }
                        if (!isInList) {
                            Racion racion = new Racion(id, temp2.getPlatoID(), temp2.getRaciones());
                            mPedidoViewModel.deleteRacion(racion);
                        }
                    }
                    for (TemporalPlatoListItem temp1 : temporalPlatoListItems1) {
                        boolean isInList = false;
                        for (Racion temp2 : temporalPlatoListItems2) {
                            if (temp2.getPlatoID() == temp1.getPlatoID()) {
                                isInList = true;
                            }
                            break;
                        }
                        if (!isInList) {
                            Racion racion = new Racion(id, temp1.getPlatoID(), temp1.getRaciones());
                            mPedidoViewModel.insertRacion(racion);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * Método llamado cuando se selecciona un elemento en el menú contextual.
     * @param item Elemento del menú contextual seleccionado.
     * @return true si se maneja el evento, false de lo contrario.
     */
    public boolean onContextItemSelected(MenuItem item) {
        Pedido current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        R.string.deleting_pedido + current.getNombreCliente(), // pasar a string
                        Toast.LENGTH_LONG).show();
                mPedidoViewModel.delete(current);
                mPedidoViewModel.deleteAllPlatosOfPedido(current.getId());
                return true;
            case EDIT_ID:
                editPedido(current);
                return true;
            case SEND_ID:
                mSendImplementor.send(current.getNumeroMovilCliente(),
                        buildMensaje(current));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Cambia a la actividad que muestra la lista de platos.
     */
    private void switchToPlatos() {
        Intent intent = new Intent(this, PlatosList.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        overridePendingTransition(0, 0);
    }

    /**
     * Comunica al adaptador el estado por el que se quiere filtrar y el filtro por el que ordenar
     * los pedidos.
     * @param filter Filtro por el que ordenar.
     * @param estado Estado por el que filtrar.
     */
    public void filterAndSortPedidos(String filter, String estado) {
        if (estado.isEmpty()) {
            mPedidoViewModel.getOrderedPedidos(filter).observe(this, sortedPedidos -> {
                mAdapter.submitList(sortedPedidos);
            });
        }
        else {
            mPedidoViewModel.getOrderedPedidos(filter, estado).observe(this, filteredPedidos -> {
                mAdapter.submitList(filteredPedidos);
            });
        }
    }

    /**
     * Almacena el estado por el que se está filtrando para que se conserve al cambiar de actividad
     * @param filter El estado
     */
    private void saveFilterState(String filter) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!filter.isEmpty()) {
            editor.putString("filter", filter);
        } else {
            editor.remove("filter");
        }

        editor.apply();
    }

    /**
     * Almacena el filtro por el que se está ordenando para que se conserve al cambiar de actividad
     * @param order El estado
     */
    private void saveOrder(String order) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!order.isEmpty()) {
            editor.putString("order", order);
        } else {
            editor.remove("order");
        }

        editor.apply();
    }

    /**
     * Inicia una nueva actividad para crear un nuevo pedido.
     */
    private void createPedido() {
        Intent intent = new Intent(this, PedidoEdit.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }

    /**
     * Inicia una nueva actividad para editar el pedido actual.
     * @param current Pedido actual que se va a editar.
     */
    private void editPedido(Pedido current) {
    generateTemporalPlatoList(current.getId(), new DataReadyCallback() {
        @Override
        public void onDataReady(List<Racion> dataList) {
            Intent intent = new Intent(PedidosListAux.this, PedidoEdit.class);
            intent.putExtra(PedidoEdit.PEDIDO_NOMBRE, current.getNombreCliente());
            intent.putExtra(PedidoEdit.PEDIDO_NUMERO, current.getNumeroMovilCliente());
            intent.putExtra(PedidoEdit.PEDIDO_PRECIO, current.getPrecioTotal());
            intent.putExtra(PedidoEdit.PEDIDO_ESTADO, current.getEstado());
            intent.putExtra(PedidoEdit.PEDIDO_DATE, current.getFecha());
            intent.putExtra(PedidoEdit.PEDIDO_TIME, current.getHora());
            intent.putExtra(PedidoEdit.PEDIDO_ID, Integer.toString(current.getId()));
            intent.putExtra(PedidoEdit.PEDIDO_LIST_PLATOS2, (Serializable) dataList);
            startActivityForResult(intent, ACTIVITY_EDIT);
        }
        });
    }

    public interface DataReadyCallback {
        void onDataReady(List<Racion> dataList);
    }

    private void generateTemporalPlatoList(int id, DataReadyCallback callback) {
        mPedidoViewModel.getAllRaciones().observe(this, racions -> {
            List<Racion> dataList = mPedidoViewModel.getAllRaciones(id);
            callback.onDataReady(dataList);
        });
    }

    /**
     * Muestra el menú de selección de filtro para ordenar los pedidos
     * @param view La vista de la aplicación
     */
    public void showSortMenu(View view) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        currentEstado = sharedPreferences.getString("filter", "");

        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.sort_menu_pedido, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort_by_nombre_cliente) {
                sortBy = "nombreCliente";
            } else if (item.getItemId() == R.id.sort_by_num_movil) {
                sortBy = "numeroMovilCliente";
            } else if (item.getItemId() == R.id.sort_by_fecha_hora) {
                sortBy = "fechayhora";
            }
            saveOrder(sortBy);
            filterAndSortPedidos(sortBy, currentEstado);
            updateButtonText((String) item.getTitle());
            return true;
        });

        popupMenu.show();
    }

    /**
     * Actualiza el texto del botón de orden
     * @param newText El nuevo texto
     */
    private void updateButtonText(String newText) {
        mSort.setText(newText);
    }

    /**
     * Devuelve un mensaje personalizado para el cliente del pedido
     * @return El mensaje
     */
    public String buildMensaje(Pedido pedido) {
        return "Estimado " + pedido.getNombreCliente() + ", su pedido se encuentra con estado " + estado +
                " será entregado con fecha " + pedido.getFecha().toString() + " a las " + pedido.getHora().toString() + "." +
                " Tendrá un coste total de " + pedido.getPrecioTotal() + " euros.";
    }

    private void goToTestMenu() {
        Intent intent = new Intent(this, Pruebas.class);
        startActivity(intent);
    }
}

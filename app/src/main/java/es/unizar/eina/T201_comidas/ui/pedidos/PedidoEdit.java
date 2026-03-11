package es.unizar.eina.T201_comidas.ui.pedidos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.unizar.eina.T201_comidas.R;
import es.unizar.eina.T201_comidas.database.Repository;
import es.unizar.eina.T201_comidas.database.platos.Plato;
import es.unizar.eina.T201_comidas.database.raciones.Racion;
import es.unizar.eina.T201_comidas.ui.platos.PlatoSelect;
import es.unizar.eina.T201_comidas.ui.temporalPlatoListItem.TempPlatoAdapter;
import es.unizar.eina.T201_comidas.ui.temporalPlatoListItem.TemporalPlatoListItem;

/** 
 * Pantalla utilizada para la creación o edición de un pedido 
 */
public class PedidoEdit extends AppCompatActivity {

    public static final String PEDIDO_ID = "id";
    public static final String PEDIDO_NOMBRE = "Nombre del cliente";
    public static final String PEDIDO_NUMERO = "Numero movil";

    public static final String PEDIDO_ESTADO = "estado";
    public static final String PEDIDO_PRECIO = "precio";
    public static final String PEDIDO_DATE = "fecha";
    public static final String PEDIDO_TIME = "hora";
    public static final String PEDIDO_LIST_PLATOS = "Lista de platos del pedido";
    public static final String PEDIDO_LIST_PLATOS2 = "Lista de platos del pedido 2";

    private String estado1;
    private String estado2;
    private String estado3;

    private EditText mNombreText;
    private EditText mNumberText;
    private TextView mPrecioText;
    private TextView mDateText;
    private TextView mTimeText;
    private TextView mIdPedidoText;

    private Spinner mSpinner;

    private Integer mRowId;

    private String estado;

    Button mSaveButton;
    Button mCancelButton;
    Button mDateButton;
    Button mTimeButton;
    Button mAddPlatosButton;

    private List<TemporalPlatoListItem> temporalPlatoListItems;
    TempPlatoAdapter mAdapter;
    RecyclerView mRecyclerView;
    private Repository mRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedido_edit);

        mRepository = new Repository(getApplication());

        estado1 = getString(R.string.estado_solicitado);
        estado2 = getString(R.string.estado_preparado);
        estado3 = getString(R.string.estado_recogido);

        mNombreText = findViewById(R.id.editPedidoNombre);
        mNumberText = findViewById(R.id.editNumeroTelefono);
        mPrecioText = findViewById(R.id.precioTextView);
        mDateText = findViewById(R.id.buttonDate);
        mTimeText = findViewById(R.id.buttonTime);
        mIdPedidoText = findViewById(R.id.idPedido);

        mSpinner = (Spinner) findViewById(R.id.spinnerEstado);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    switch (pos) {
                        case 0:
                            estado = estado1;
                            break;
                        case 1:
                            estado = estado2;
                            break;
                        case 2:
                            estado = estado3;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
        });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.estado, R.layout.dropdown_menu_item);
        mSpinner.setAdapter(adapter);

        temporalPlatoListItems = new ArrayList<TemporalPlatoListItem>();

        mSaveButton = findViewById(R.id.buttonSave);
        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (mNombreText.getText().toString().isEmpty() || mNumberText.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.empty_not_allowed, Toast.LENGTH_SHORT).show();
            }
            else if (mDateText.getText().toString().equals(R.string.time) ||
                     mTimeText.getText().toString().equals(R.string.date)) {
                Toast.makeText(this, R.string.empty_not_allowed, Toast.LENGTH_SHORT).show();
            }
            else if (!isValidDate(mDateText.getText().toString(), mTimeText.getText().toString(), estado)) {
                Toast.makeText(this, R.string.incorrect_datetime, Toast.LENGTH_SHORT).show();
            }
            else {
                replyIntent.putExtra(PedidoEdit.PEDIDO_NOMBRE, mNombreText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_NUMERO, mNumberText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_DATE, mDateText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_TIME, mTimeText.getText().toString());
                replyIntent.putExtra(PedidoEdit.PEDIDO_PRECIO, Float.toString(mAdapter.calculatePrice()));
                replyIntent.putExtra(PedidoEdit.PEDIDO_ESTADO, estado);
                replyIntent.putExtra(PedidoEdit.PEDIDO_LIST_PLATOS, (Serializable) temporalPlatoListItems);

                if (mRowId!=null) {
                    replyIntent.putExtra(PedidoEdit.PEDIDO_ID, Integer.toString(mRowId.intValue()));
                }
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        mCancelButton = findViewById(R.id.buttonCancel);
        mCancelButton.setOnClickListener(view -> {
            cancelInput();
        });

        mDateButton = findViewById(R.id.buttonDate);
        mDateButton.setOnClickListener(view -> {
            openDateDialog();
        });


        mTimeButton = findViewById(R.id.buttonTime);
        mTimeButton.setOnClickListener(view -> {
            openTimePicker();
        });

        mAddPlatosButton = findViewById(R.id.addPlatoButton);
        mAddPlatosButton.setOnClickListener(view -> {
            selectPlato();
        });


        mAddPlatosButton = findViewById(R.id.addPlatoButton);
        mAddPlatosButton.setOnClickListener(view -> {
            selectPlato();
        });

        mRecyclerView = findViewById(R.id.recyclerPlatosInPedido);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mAdapter = new TempPlatoAdapter(getApplicationContext(), temporalPlatoListItems, getApplication());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnIncreaseRacionClickListener(position -> {
            mAdapter.increaseRacion(position);
            calculatePrice();
        });

        mAdapter.setOnDecreaseRacionClickListener(position -> {
            mAdapter.decreaseRacion(position);
            calculatePrice();
        });

        mAdapter.setOnDeletePlatoClickListener(position -> {
            mAdapter.deletePlato(position);
            calculatePrice();
        });

        populateFields();
        mAdapter.notifyDataSetChanged();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                int id = extras.getInt(PlatoSelect.PLATO_SELECTED);
                String name = extras.getString(PlatoSelect.PLATO_SELECTED_NAME);
                Float precio = extras.getFloat(PlatoSelect.PLATO_SELECTED_PRICE);
                boolean added = false;
                // Busca entre todos los elementos de la lista al plato que tenga identificador numérico <<id>>
                for (TemporalPlatoListItem temp : temporalPlatoListItems) {
                    if (temp.getPlatoID() == id) {
                        added = true;
                        temp.increaseRaciones();
                        break;
                    }
                }
                if (!added) {
                    temporalPlatoListItems.add(new TemporalPlatoListItem(id, 1, name, precio));
                }
                mAdapter.notifyDataSetChanged();
                calculatePrice();
            }
        }
    }

    /**
     * Lanza la actividad de selección de plato
     */
    private void selectPlato() {
        Intent intent = new Intent(this, PlatoSelect.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Lanza en segundo plano una operación para obtener los datos de platos de una ración
     * @param temp La ración
     */
    private void fetchPlatosInBackground(Racion temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPlatoTask().execute(temp.getPlatoID(), temp.getRaciones());
            }
        });
    }

    /**
     * Rellena los campos de la actividad con los valores recibidos en los extras del Intent.
     */
    private void populateFields () {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreText.setText(extras.getString(PedidoEdit.PEDIDO_NOMBRE));
            mNumberText.setText(extras.getString(PedidoEdit.PEDIDO_NUMERO));
            mDateText.setText(extras.getString(PedidoEdit.PEDIDO_DATE));
            mTimeText.setText(extras.getString(PedidoEdit.PEDIDO_TIME));
            mIdPedidoText.setText(getString(R.string.pre_id_pedido) + extras.getString(PedidoEdit.PEDIDO_ID));
            switch (extras.getString(PedidoEdit.PEDIDO_ESTADO)) {
                case "Solicitado":
                    mSpinner.setSelection(0);
                    break;
                case "Preparado":
                    mSpinner.setSelection(1);
                    break;
                case "Recogido":
                    mSpinner.setSelection(2);
                    break;
            }

            mRowId = Integer.parseInt(extras.getString(PedidoEdit.PEDIDO_ID));
            List<Racion> tempList = (List<Racion>) extras.getSerializable(PedidoEdit.PEDIDO_LIST_PLATOS2);
            for (Racion temp : tempList) {
                fetchPlatosInBackground(temp);
            }
        }
        else {
            mIdPedidoText.setText(getString(R.string.new_pedido));
        }
        calculatePrice();
    }

    /**
     * Abre el diálogo para seleccionar la fecha.
     */
    private void openDateDialog() {
        // Obtiene la fecha actual para establecerla como predeterminada en el diálogo
        Calendar calendar = Calendar.getInstance();
        int añoActual = calendar.get(Calendar.YEAR);
        int mesActual = calendar.get(Calendar.MONTH);
        int diaActual = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Actualiza el campo de texto con la fecha seleccionada
                mDateText.setText(String.format(Locale.getDefault(), "%04d.%02d.%02d", year, month + 1, dayOfMonth));
                updateButtonDate(year, month, dayOfMonth);
            }
        }, añoActual, mesActual, diaActual);


        dialog.show();
    }

    /**
     * Abre el diálogo para seleccionar la hora.
     */
    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            mTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            updateButtonTextTime(hourOfDay, minute);
        }, horaActual, minutoActual, true);

        timePickerDialog.show();
    }

    /**
     * Actualiza el texto del botón de fecha.
     *
     * @param year  Año seleccionado.
     * @param month Mes seleccionado.
     * @param day   Día del mes seleccionado.
     */
    private void updateButtonDate(int year, int month, int day) {
        String formattedDate = String.format("%02d/%02d/%04d", day, month + 1, year);
        mDateButton.setText(formattedDate);
    }

    /**
     * Actualiza el texto del botón de hora.
     *
     * @param hour   Hora del día seleccionada.
     * @param minute Minuto seleccionado.
     */
    private void updateButtonTextTime(int hour, int minute) {
        String formattedTime = String.format("%02d:%02d", hour, minute);
        mTimeButton.setText(formattedTime);
    }

    /**
     * Función que efectúa una serie de comprobaciones sobre la fecha y hora.
     * @return Si la fecha es o no es válida para la aplicación
     */
    private boolean isValidDate(String date, String time, String estado) {
        String formato = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.getDefault());
        try {
            java.util.Date datetime = sdf.parse(date + " " + time);
            if (datetime.after(new java.util.Date())) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(datetime);
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if (day != Calendar.MONDAY) {
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    if ((hour > 19 || (hour == 19 && minute >= 30)) && hour < 23) {
                        return true;
                    }
                }
            }
            return false;
        } catch (ParseException exception) {
            // No debería ocurrir
            return false;
        }
    }

    /**
     * Función que calcula el precio total de pedido a partir de la lista temporal de platos
     */
    private void calculatePrice() {
        mPrecioText.setText(String.format("%.2f", mAdapter.calculatePrice()));
    }

    /**
     * Clase que implementa una tarea asíncrona para obtener en segundo plato los platos de una ración
     */
    private class GetPlatoTask extends AsyncTask<Object, Void, String> {
       @Override
       protected String doInBackground(Object... params)  {
           int id = (int) params[0];
           int raciones = (int) params[1];
           Plato plato = mRepository.getPlato(id);
           temporalPlatoListItems.add(new TemporalPlatoListItem(id, raciones, plato.getNombre(), plato.getPrecio()));

           return "Finished";

       }

       @Override
       protected void onPostExecute(String result) {
           Log.d("MyApp", "GetPlatoTask onPostExecute");
           runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                calculatePrice();
            }
        });
       }
    }

    /**
     * Redefinición de método para que pulsar la tecla hacia atrás cancele la edición de pedido.
     */
    @Override
    public void onBackPressed() { cancelInput(); }

    /**
     * Método que cancela la entrada actual y cierra la actividad, indicando un resultado cancelado.
     */
    private void cancelInput() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}

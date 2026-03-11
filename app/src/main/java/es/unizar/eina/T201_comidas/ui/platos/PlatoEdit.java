package es.unizar.eina.T201_comidas.ui.platos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import es.unizar.eina.T201_comidas.R;

/** 
 * Pantalla utilizada para la creación o edición de un plato. 
 */
public class PlatoEdit extends AppCompatActivity {
    public static final String PLATO_NOMBRE = "Nombre del plato";
    public static final String PLATO_INGREDIENTES = "Ingredientes";
    public static final String PLATO_ID = "id";
    public static final String PLATO_CATEGORIA = "categoria";
    public static final String PLATO_PRECIO = "precio";

    private EditText mNombrePlato;
    private EditText mIngredientesText;
    private EditText mPrecioPlato;
    private Spinner mSpinner;
    private Integer mRowId;

    private String categoria;
    private String categoria1;
    private String categoria2;
    private String categoria3;

    Button mSaveButton;
    Button mCancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plato_edit);

        categoria1 = getString(R.string.categoria_primero);
        categoria2 = getString(R.string.categoria_segundo);
        categoria3 = getString(R.string.categoria_postre);

        mNombrePlato = findViewById(R.id.nombre);
        mIngredientesText = findViewById(R.id.body);
        mPrecioPlato = findViewById(R.id.editPrecio);
        mSpinner = (Spinner) findViewById(R.id.spinnerCategorias);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    switch (pos) {
                        case 0:
                            categoria = categoria1;
                            break;
                        case 1:
                            categoria = categoria2;
                            break;
                        case 2:
                            categoria = categoria3;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {}
        });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.categorias, R.layout.dropdown_menu_item);
        mSpinner.setAdapter(adapter);

        mSaveButton = findViewById(R.id.buttonSave);
        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (mNombrePlato.getText().toString().isEmpty() || mIngredientesText.getText().toString().isEmpty() || mPrecioPlato.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.empty_not_allowed, Toast.LENGTH_SHORT).show();
            } else {
                replyIntent.putExtra(PlatoEdit.PLATO_NOMBRE, mNombrePlato.getText().toString());
                replyIntent.putExtra(PlatoEdit.PLATO_INGREDIENTES, mIngredientesText.getText().toString());
                replyIntent.putExtra(PlatoEdit.PLATO_CATEGORIA, categoria);
                replyIntent.putExtra(PlatoEdit.PLATO_PRECIO, mPrecioPlato.getText().toString());

                if (mRowId!=null) {
                    replyIntent.putExtra(PlatoEdit.PLATO_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

        mCancelButton = findViewById(R.id.buttonCancel);
        mCancelButton.setOnClickListener(view -> {
            cancelInput();
        });

        populateFields();
    }

    /**
     * Rellena los campos con los datos del plato existente si se está editando.
     */
    private void populateFields () {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombrePlato.setText(extras.getString(PlatoEdit.PLATO_NOMBRE));
            mIngredientesText.setText(extras.getString(PlatoEdit.PLATO_INGREDIENTES));
            mPrecioPlato.setText(extras.getString(PlatoEdit.PLATO_PRECIO));
            switch (extras.getString(PlatoEdit.PLATO_CATEGORIA)) {
                case "Primero":
                    mSpinner.setSelection(0);
                    break;
                case "Segundo":
                    mSpinner.setSelection(1);
                    break;
                case "Postre":
                    mSpinner.setSelection(2);
                    break;
            }
            mRowId = extras.getInt(PlatoEdit.PLATO_ID);
        }
    }

    /**
     * Redefinición de método para que pulsar la tecla hacia atrás cancele la edición de plato.
     */
    public void onBackPressed() { cancelInput(); }

    /**
     * Cancela la entrada y cierra la actividad.
     */
    private void cancelInput() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }
}

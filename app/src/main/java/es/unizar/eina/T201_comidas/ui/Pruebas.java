package es.unizar.eina.T201_comidas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.unizar.eina.T201_comidas.R;
import es.unizar.eina.T201_comidas.UnitTests;

/**
 * Pantalla de pruebas de la aplicación
 */
public class Pruebas extends AppCompatActivity {
    Button mTest1Button;
    Button mTest2Button;
    Button mTest3Button;

    Button mCancelButton;

    UnitTests mUnitTests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_screen);

        mUnitTests = new UnitTests(this.getApplication());

        mTest1Button = findViewById(R.id.buttonCaja);
        mTest1Button.setOnClickListener(view -> {
            mUnitTests.clasesEquivalenciaPlatos();
            mUnitTests.clasesEquivalenciaPedidos();
            Toast.makeText(this, R.string.test_ended_message, Toast.LENGTH_SHORT).show();
        });

        mTest2Button = findViewById(R.id.buttonVolumen);
        mTest2Button.setOnClickListener(view -> {
            mUnitTests.testPlatos(100);
            mUnitTests.testPedidos(2000);
            Toast.makeText(this, R.string.test_ended_message, Toast.LENGTH_SHORT).show();
        });

        mTest3Button = findViewById(R.id.buttonSobrecarga);
        mTest3Button.setOnClickListener(view -> {
            mUnitTests.sobrecargaPlatos();
            Toast.makeText(this, R.string.test_ended_message, Toast.LENGTH_SHORT).show();
        });

        mCancelButton = findViewById(R.id.buttonCancel);
        mCancelButton.setOnClickListener(view -> {
            cancel();
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void cancel() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

}

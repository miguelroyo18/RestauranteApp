package es.unizar.eina.T201_comidas;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.T201_comidas.database.Repository;
import es.unizar.eina.T201_comidas.database.pedidos.Pedido;
import es.unizar.eina.T201_comidas.database.platos.Plato;

public class UnitTests {
    private Repository mRepository;

    public UnitTests(Application application) {
        mRepository = new Repository(application);
    }

    /**
     * Test de clases de equivalencia para la insercción de platos
     */
    public void clasesEquivalenciaPlatos() {
        List<Plato> platosList = new ArrayList<>();
        int i = 0;
        platosList.add(new Plato("Tortilla" + ++i, "Huevos y patatas" ,"Primero" ,12.0f));
        platosList.add(new Plato("Tortilla" + ++i, "Huevos y patatas" ,"Segundo" ,12.0f));
        platosList.add(new Plato("Tortilla" + ++i, "Huevos y patatas" ,"Tercero" ,12.0f));
        platosList.add(new Plato(null, "Huevos y patatas" ,"Primero" ,12.0f));
        platosList.add(new Plato("", "Huevos y patatas" ,"Primero" ,12.0f));
        platosList.add(new Plato("Tortilla" + ++i, null ,"Primero" ,12.0f));
        platosList.add(new Plato("Tortilla" + ++i, "" ,"Primero" ,12.0f));
        platosList.add(new Plato("Tortilla" + ++i, "Huevos y patatas" ,"Entrante" ,12.0f));
        platosList.add(new Plato("Tortilla" + ++i, "Huevos y patatas" ,"Primero" ,-12.0f));

        for (Plato plato : platosList) {
            mRepository.insertPlato(plato);
        }
    }

    /**
     * Test de clases de equivalencia para la insercción de pedidos
     */
    public void clasesEquivalenciaPedidos() {
        List<Pedido> pedidosList = new ArrayList<>();
        int i = 0;
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Solicitado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Preparado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Recogido",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("", "123456789", "Solicitado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido(null, "123456789", "Solicitado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, null, "Solicitado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "", "Solicitado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "francisco", "Solicitado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Reservado",
                12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Solicitado",
                -12.0f, "28/12/2023", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Solicitado",
                12.0f, "01/01/2024", "22:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Solicitado",
                12.0f, "02/01/2024", "23:30"));
        pedidosList.add(new Pedido("Francisco" + ++i, "123456789", "Solicitado",
                12.0f, "27/12/2023", "22:30"));

        for (Pedido pedido : pedidosList) {
            mRepository.insertPedido(pedido);
        }
    }

    /**
     * Test de saturación de platos
     * @param n Número de platos a crear
     */
    public void testPlatos(int n) {
        for (int i = 0; i < n ; i++) {
            mRepository.insertPlato(new Plato("Plato de prueba " + i, "Plato de " +
                    "prueba generado durante los test de la aplicación.", "Primero", 0.0f));
        }
    }

    /**
     * Test de saturación de pedidos
     * @param n Número de pedidos a crear
     */
    public void testPedidos(int n) {
        for (int i = 0; i < n ; i++) {
            mRepository.insertPedido(new Pedido("Pedido de prueba " + i, "123",
                    "Preparado", 0.0f, "01/01/2000", "00:00"));
        }
    }

    /**
     * Test de sobrecarga de la descripción de un plato
     */
    public void sobrecargaPlatos() {
        String desc = "";
        Plato plato;
        int n = 100;
        for (int i = 0; ; i++) {
            for (int j = 0; j < n; j++) {
                desc += "a";
            }
            plato = new Plato("Prueba sobrecarga", desc, "Primero", 0.0f);
            Log.d("Pruebas", "Probando la creación de un plato con una descripción" +
                    "de longitud " + n*i);
            if (mRepository.insertPlato(plato) == -1) {
                break;
            }
            else mRepository.deletePlato(plato);
        }

    }

}

package es.unizar.eina.T201_comidas.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unizar.eina.T201_comidas.database.raciones.Racion;
import es.unizar.eina.T201_comidas.database.pedidos.Pedido;
import es.unizar.eina.T201_comidas.database.pedidos.PedidoDao;
import es.unizar.eina.T201_comidas.database.platos.Plato;
import es.unizar.eina.T201_comidas.database.platos.PlatoDao;

/**
 * Base de datos de Room que contiene las tablas para los objetos Plato, Pedido y Racion.
 */
@Database(entities = {Plato.class, Pedido.class, Racion.class}, version = 1, exportSchema = false)
public abstract class RestauranteRoomDatabase extends RoomDatabase {

    public abstract PlatoDao platoDao();
    public abstract PedidoDao pedidoDao();

    private static volatile RestauranteRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Obtiene una instancia de la base de datos Room. Si la instancia no existe, se crea una nueva.
     * @param context El contexto de la aplicación.
     * @return La instancia de la base de datos.
     */
    public static RestauranteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RestauranteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RestauranteRoomDatabase.class, "note_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Creamos algunos platos de prueba para facilitar la depuración de la aplicación
            databaseWriteExecutor.execute(() -> {
                PlatoDao dao = INSTANCE.platoDao();
                dao.deleteAll();

                Plato plato = new Plato("Pizza", "Masa, tomate y queso", "Primero", 5);
                dao.insertPlato(plato);
                plato = new Plato("Hamburguesa", "Pan, ternera y queso", "Segundo", 3);
                dao.insertPlato(plato);
                plato = new Plato("Flan", "Huevos y azúcar", "Postre", 2);
                dao.insertPlato(plato);
            });
        }
    };

}

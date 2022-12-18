package best.azura.irc.sql.seed;

import best.azura.irc.main.Main;
import best.azura.irc.sql.SQLConnector;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Class used to manage Seeds.
 */
public class SeedManager {

    /**
     * Method called to run all Seeds.
     *
     * @param sqlConnector The SQLConnector.
     */
    public static void runAllSeeds(SQLConnector sqlConnector) {
        Reflections reflections = new Reflections("best.azura.irc");
        Set<Class<? extends Seed>> classes = reflections.getSubTypesOf(Seed.class);
        for (Class<? extends Seed> aClass : classes) {
            try {
                Main.getInstance().getLogger().info("Trying to run Seed {}", aClass.getSimpleName());
                Seed seed = aClass.getDeclaredConstructor().newInstance();

                if (sqlConnector.querySQL("SELECT * FROM Seeds WHERE VERSION=?", seed.getSeedVersion().toString()).hasResults()) {
                    Main.getInstance().getLogger().info("Seed {} already ran.", aClass.getSimpleName());
                    continue;
                }

                seed.run(sqlConnector);
                sqlConnector.querySQL("INSERT INTO Seeds (VERSION, DATE) VALUES (?, ?)", seed.getSeedVersion().toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                Main.getInstance().getLogger().error("Could not run Seed!", e);
            }
        }
    }

}
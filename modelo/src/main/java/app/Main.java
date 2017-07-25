package app;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Main {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1,"org.app.prenengapp");
        schema.enableKeepSectionsByDefault();
        createDataBase(schema);
        DaoGenerator generator = new DaoGenerator();
        generator.generateAll(schema,args[0]);
    }
    public static void createDataBase(Schema schema){
        Entity usuario =schema.addEntity("Reporte");
        usuario.addIdProperty();
        usuario.addStringProperty("imagen");
        usuario.addStringProperty("coordenadas");
        usuario.addStringProperty("titulo");
        usuario.addStringProperty("descripcion");
    }
}

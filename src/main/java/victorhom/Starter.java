package victorhom;

import Database.Database;
import victorhom.view.App;

import java.io.File;
import java.net.URISyntaxException;


public class Starter {
    public static void main(String[] args) throws URISyntaxException {
        Database.dbdir = new File(Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath().getParent() +
                             "/data";
        App.main(args);


    }


}

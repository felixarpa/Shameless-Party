package felixarpa.shamelessapp.domain.model;

import felixarpa.shamelessapp.R;
import io.realm.RealmObject;

public class NGO extends RealmObject {

    public static final String GREENPEACE = "Greenpeace";
    public static final String AMNESTY =  "Amnesty";
    public static final String UNICEF = "UNICEF";
    public static final String MEDECINS_SANS_FRONTIERES = "Médecins Sans Frontières";
    public static final String WWF = "World Wildlife Found";

    public static final int GREENPEACE_INDEX = 0;
    public static final int AMNESTY_INDEX = 1;
    public static final int UNICEF_INDEX = 2;
    public static final int MEDECINS_SANS_FRONTIERES_INDEX = 3;
    public static final int WWF_INDEX = 4;

    public static final int[] LOGO_RES_IDs = { R.mipmap.greenpeace, R.mipmap.amnesty,
            R.mipmap.unicef, R.mipmap.msf, R.mipmap.wwf };

    public static final String[] NAME_ARRAY = { GREENPEACE, AMNESTY, UNICEF,
            MEDECINS_SANS_FRONTIERES, WWF };

    private String name;

    public NGO(String name) {
        this.name = name;
    }

    public NGO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}

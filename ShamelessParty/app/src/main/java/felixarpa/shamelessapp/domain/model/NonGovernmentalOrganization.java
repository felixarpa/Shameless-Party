package felixarpa.shamelessapp.domain.model;

import io.realm.RealmObject;

public class NonGovernmentalOrganization extends RealmObject {

    public static final String GREENPEACE = "Greenpeace";
    public static final String AMNESTY =  "Amnesty";
    public static final String UNICEF = "UNICEF";
    public static final String MEDECINS_SANS_FRONTIERES = "Médecins Sans Frontières";
    public static final String WWF = "World Wildlife Found";

    private String name;

    public NonGovernmentalOrganization(String name) {
        this.name = name;
    }

    public NonGovernmentalOrganization() {
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

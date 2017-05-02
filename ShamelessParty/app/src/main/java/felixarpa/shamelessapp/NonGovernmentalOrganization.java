package felixarpa.shamelessapp;

import io.realm.RealmObject;

public class NonGovernmentalOrganization extends RealmObject {

    private String name;

//    @Ignore
//    private String imageName;


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

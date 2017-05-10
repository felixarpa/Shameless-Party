package felixarpa.shamelessapp.domain.data;

import felixarpa.shamelessapp.domain.model.NonGovernmentalOrganization;
import felixarpa.shamelessapp.domain.model.Party;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmManager {

    private Realm realm;
    private static final RealmManager ourInstance = new RealmManager();

    public static RealmManager getInstance() {
        return ourInstance;
    }

    private RealmManager() {
        realm = Realm.getDefaultInstance();
    }

    public void initialize() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        NonGovernmentalOrganization greenpeace = realm.createObject(NonGovernmentalOrganization.class);
        NonGovernmentalOrganization amnesty = realm.createObject(NonGovernmentalOrganization.class);
        NonGovernmentalOrganization unicef = realm.createObject(NonGovernmentalOrganization.class);
        NonGovernmentalOrganization medecinsSansFrontieres = realm.createObject(NonGovernmentalOrganization.class);
        NonGovernmentalOrganization worldWildlifeFound = realm.createObject(NonGovernmentalOrganization.class);

        greenpeace.setName("Greenpeace");
        amnesty.setName("Amnesty");
        unicef.setName("UNICEF");
        medecinsSansFrontieres.setName("Médecins Sans Frontières");
        worldWildlifeFound.setName("World Wildlife Found");

        // TODO: Image String Base64


        realm.commitTransaction();

    }

    RealmResults<Party> selectAll() {
        return realm.where(Party.class).findAll();
    }

    RealmResults<Party> select() {
        return Realm.getDefaultInstance()
                .where(Party.class)
                .equalTo("finalPayment", -1.0f)
                .findAllSorted("date", Sort.DESCENDING);
    }

    RealmResults<Party> selectDateEqual(long date) {
        return Realm.getDefaultInstance()
                .where(Party.class)
                .equalTo("date", date)
                .findAll();
    }

    void insert(Party party) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(party);
        realm.commitTransaction();
    }


    void update(Party party) {

    }
}

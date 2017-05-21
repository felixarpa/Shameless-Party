package felixarpa.shamelessapp.domain.data;

import felixarpa.shamelessapp.domain.model.Party;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmManager {

    private static final RealmManager ourInstance = new RealmManager();

    public static RealmManager getInstance() {
        return ourInstance;
    }

    private RealmManager() {}

    RealmResults<Party> allParties() {
        return Realm.getDefaultInstance()
                .where(Party.class)
                .findAll();
    }

    void put(Party party) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(party);
        realm.commitTransaction();
    }
}

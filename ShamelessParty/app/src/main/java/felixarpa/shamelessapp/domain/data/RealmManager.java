package felixarpa.shamelessapp.domain.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import felixarpa.shamelessapp.domain.model.NonGovernmentalOrganization;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.utils.C;
import felixarpa.shamelessapp.utils.DateUtil;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmResults;

public class RealmManager {

    private static final RealmManager ourInstance = new RealmManager();

    public static RealmManager getInstance() {
        return ourInstance;
    }

    private RealmManager() {}

    public void initialize(Context applicationContext, Transaction.OnSuccess success, Transaction.OnError error) throws AlreadyInitializedException {
        SharedPreferences sp = applicationContext.getSharedPreferences(C.SPN, Context.MODE_PRIVATE);
        boolean init = sp.getBoolean("realm-init", false);
        if (init) {
            Date initDate = new Date(sp.getLong("realm-date", 0L));
            throw new AlreadyInitializedException(
                    "Realm database has already been initialized:"
                    + DateUtil.getDate(initDate)
            );
        }

        Realm.init(applicationContext);
        Realm.getDefaultInstance().executeTransactionAsync(
                new Transaction() {
                    @Override
                    public void execute(Realm realm) {
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
                    }
                }, success, error
        );
    }

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

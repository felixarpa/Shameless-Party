package felixarpa.shamelessapp.domain.data;

import android.content.SharedPreferences;

import java.util.Date;

import felixarpa.shamelessapp.domain.model.NonGovernmentalOrganization;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.presentation.activity.ShamelessActivity;
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

    public void initialize(ShamelessActivity shamelessActivity, Transaction.OnSuccess success, Transaction.OnError error) throws AlreadyInitializedException {
        SharedPreferences sp = shamelessActivity.getSharedPreferences();
        boolean init = sp.getBoolean(C.INIT_BOOL, false);
        if (!init) {
            Realm.init(shamelessActivity);
            SharedPreferences.Editor editor = shamelessActivity.getEditor();
            editor.putBoolean(C.INIT_BOOL, true);
            editor.putLong(C.INIT_DATE, System.currentTimeMillis());
            Realm.getDefaultInstance().executeTransactionAsync(
                    new Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            NonGovernmentalOrganization greenpeace = realm.createObject(NonGovernmentalOrganization.class);
                            NonGovernmentalOrganization amnesty = realm.createObject(NonGovernmentalOrganization.class);
                            NonGovernmentalOrganization unicef = realm.createObject(NonGovernmentalOrganization.class);
                            NonGovernmentalOrganization medecinsSansFrontieres = realm.createObject(NonGovernmentalOrganization.class);
                            NonGovernmentalOrganization worldWildlifeFound = realm.createObject(NonGovernmentalOrganization.class);

                            greenpeace.setName(NonGovernmentalOrganization.GREENPEACE);
                            amnesty.setName(NonGovernmentalOrganization.AMNESTY);
                            unicef.setName(NonGovernmentalOrganization.UNICEF);
                            medecinsSansFrontieres.setName(NonGovernmentalOrganization.MEDECINS_SANS_FRONTIERES);
                            worldWildlifeFound.setName(NonGovernmentalOrganization.WWF);

                        }
                    }, success, error
            );

        } else {
            Date initDate = new Date(sp.getLong(C.INIT_DATE, 0L));
            throw new AlreadyInitializedException(
                    "Realm database has already been initialized:"
                            + DateUtil.getDate(initDate)
            );
        }
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

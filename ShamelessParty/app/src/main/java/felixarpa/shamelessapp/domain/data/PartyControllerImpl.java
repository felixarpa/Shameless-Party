package felixarpa.shamelessapp.domain.data;

import java.util.ArrayList;
import java.util.Date;

import felixarpa.shamelessapp.domain.controller.AlreadyPartyingException;
import felixarpa.shamelessapp.domain.controller.InvalidAmountException;
import felixarpa.shamelessapp.domain.controller.NoSuchPartyGoingOnException;
import felixarpa.shamelessapp.domain.controller.NoSuchPartyThenException;
import felixarpa.shamelessapp.domain.controller.PartyController;
import felixarpa.shamelessapp.domain.model.NonGovernmentalOrganization;
import felixarpa.shamelessapp.domain.model.Party;
import io.realm.RealmResults;

public class PartyControllerImpl implements PartyController {

    private RealmManager db = RealmManager.getInstance();
    private static final PartyControllerImpl ourInstance = new PartyControllerImpl();

    public static PartyControllerImpl getInstance() {
        return ourInstance;
    }

    private PartyControllerImpl() {
    }

    @Override
    public Party createNewParty(String title, Date limitHour, float moneyAmount, int minutes, NonGovernmentalOrganization ngo)
            throws AlreadyPartyingException, InvalidAmountException {
        int eurosPerMinute = (int) (moneyAmount * minutes);
        if (eurosPerMinute < 1) {
            throw new InvalidAmountException(eurosPerMinute);
        } else {
            try {
                getParty();
                throw new AlreadyPartyingException();
            } catch (NoSuchPartyGoingOnException e) {
                    Party party = new Party(title, limitHour.getTime(), moneyAmount, minutes, ngo);
                    db.put(party);
                    return party;
            }
        }
    }

    @Override
    public void commitParty() throws NoSuchPartyGoingOnException {
        Party party = getParty();
        long currentTime = new Date().getTime();
        int minutes = (int) ((currentTime - party.getHour()) / 60000);
        float finalPayment = minutes * party.getMoneyAmount();
        party.setFinalPayment(finalPayment);

        db.put(party);
    }

    @Override
    public void cancelParty() throws NoSuchPartyGoingOnException {
        Party party = getParty();
        party.cancel();
        db.put(party);
    }

    @Override
    public Party getParty() throws NoSuchPartyGoingOnException {
        ArrayList<Party> parties = getAllParties();
        for (Party party : parties) {
            if (party.isGoingOn()) {
                return party;
            }
        }
        throw new NoSuchPartyGoingOnException();
    }

    @Override
    public ArrayList<Party> getAllParties() {
        RealmResults<Party> dbParties = db.allParties();
        return new ArrayList<>(dbParties);
    }

    @Override
    public Party getPartyAt(Date date) throws NoSuchPartyThenException {
        long now = new Date().getTime();
        ArrayList<Party> parties = getAllParties();
        for (Party party : parties) {
            if (party.getHour() == now) {
                return party;
            }
        }
        throw new NoSuchPartyThenException();
    }
}

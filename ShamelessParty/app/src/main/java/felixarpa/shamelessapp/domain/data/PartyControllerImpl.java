package felixarpa.shamelessapp.domain.data;

import java.util.ArrayList;
import java.util.Date;

import felixarpa.shamelessapp.domain.controller.PartyController;
import felixarpa.shamelessapp.domain.controller.exception.AlreadyPartyingException;
import felixarpa.shamelessapp.domain.controller.exception.InvalidAmountException;
import felixarpa.shamelessapp.domain.controller.exception.InvalidLocationException;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyGoingOnException;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyThenException;
import felixarpa.shamelessapp.domain.controller.exception.PastPartyException;
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
    public Party createNewParty(String title, Date limitHour, float moneyAmount, int minutes,
                                String ngo, double latitude, double longitude)
            throws AlreadyPartyingException, InvalidAmountException, PastPartyException {
        if ((new Date()).getTime() > limitHour.getTime()) {
            throw new PastPartyException();
        }
        int eurosPerMinute = (int) (moneyAmount * minutes);
        if (eurosPerMinute < 1) {
            throw new InvalidAmountException(eurosPerMinute);
        }
        try {
            getParty();
            throw new AlreadyPartyingException();
        } catch (NoSuchPartyGoingOnException e) {
                Party party = new Party(title, limitHour.getTime(), moneyAmount, minutes, ngo,
                        latitude, longitude);
                db.put(party);
                return party;
        }
    }

    @Override
    public void checkNewPartyValues(Date limitHour, float moneyAmount, int minutes, double latitude,
                                    double longitude)
            throws AlreadyPartyingException, InvalidAmountException, PastPartyException,
                    InvalidLocationException {

        if (latitude < Party.LATITUDE_MIN || latitude > Party.LATITUDE_MAX) {
            throw new InvalidLocationException();
        }
        if (longitude < Party.LONGITUDE_MIN || longitude > Party.LONGITUDE_MAX) {
            throw new InvalidLocationException();
        }

        if ((new Date()).getTime() > limitHour.getTime()) {
            throw new PastPartyException();
        }
        int eurosPerMinute = (int) (moneyAmount * minutes);
        if (eurosPerMinute < 1) {
            throw new InvalidAmountException(eurosPerMinute);
        }

        try {
            getParty();
            throw new AlreadyPartyingException();
        } catch (NoSuchPartyGoingOnException ignored) {}
    }

    @Override
    public void commitParty() throws NoSuchPartyGoingOnException {
        Party party = getParty();
        long now = System.currentTimeMillis();
        int minutes = (int) ((now - party.getHour()) / 60000);
        float finalPayment = minutes * party.getMoneyAmount();
        db.postFinalPayment(party, finalPayment);
    }

    @Override
    public void cancelParty() throws NoSuchPartyGoingOnException {
        db.postCancel(getParty());
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
        long now = System.currentTimeMillis();
        ArrayList<Party> parties = getAllParties();
        for (Party party : parties) {
            if (party.getHour() == now) {
                return party;
            }
        }
        throw new NoSuchPartyThenException();
    }
}

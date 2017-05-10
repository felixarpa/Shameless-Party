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

public class PartyControllerImpl implements PartyController {
    private static final PartyControllerImpl ourInstance = new PartyControllerImpl();

    public static PartyControllerImpl getInstance() {
        return ourInstance;
    }

    private PartyControllerImpl() {
    }

    @Override
    public Party createNewParty(String title, Date limitHour, float moneyAmount, int minutes, NonGovernmentalOrganization ngo)
            throws AlreadyPartyingException, InvalidAmountException {
        try {
            getParty();
            throw new AlreadyPartyingException();
        } catch (NoSuchPartyGoingOnException e) {
            int eurosPerMinute = (int) (moneyAmount * minutes);
            if (eurosPerMinute < 1) {
                throw new InvalidAmountException(eurosPerMinute);
            } else {
                Party party = new Party(title, limitHour.getTime(), moneyAmount, minutes, ngo);
                RealmManager.getInstance().insert(party);
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
        RealmManager.getInstance().update(party);
    }

    @Override
    public void cancelParty() throws NoSuchPartyGoingOnException {

    }

    @Override
    public Party getParty() throws NoSuchPartyGoingOnException {
        ArrayList<Party> parties = new ArrayList<>(RealmManager.getInstance().select());
        if (parties.size() == 0) {
            throw new NoSuchPartyGoingOnException();
        } else {
            return parties.get(0);
        }
    }

    @Override
    public ArrayList<Party> getAllParties() {
        return new ArrayList<>(RealmManager.getInstance().selectAll());
    }

    @Override
    public Party getPartyAt(Date date) throws NoSuchPartyThenException {
        long time = date.getTime();
        ArrayList<Party> parties = new ArrayList<>(RealmManager.getInstance().selectDateEqual(time));
        if (parties.size() == 0) {
            throw new NoSuchPartyThenException();
        } else {
            return parties.get(0);
        }
    }
}

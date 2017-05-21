package felixarpa.shamelessapp.domain.controller;

import java.util.ArrayList;
import java.util.Date;

import felixarpa.shamelessapp.domain.controller.exception.AlreadyPartyingException;
import felixarpa.shamelessapp.domain.controller.exception.InvalidAmountException;
import felixarpa.shamelessapp.domain.controller.exception.InvalidLocationException;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyGoingOnException;
import felixarpa.shamelessapp.domain.controller.exception.NoSuchPartyThenException;
import felixarpa.shamelessapp.domain.controller.exception.PastPartyException;
import felixarpa.shamelessapp.domain.model.Party;

public interface PartyController {
    Party createNewParty(String title, Date limitHour, float moneyAmount, int minutes, String ngo,
                         double longitude, double latitude)
            throws AlreadyPartyingException, InvalidAmountException, PastPartyException,
                    InvalidLocationException;

    void checkNewPartyValues(Date limitHour, float moneyAmount, int minutes, double longitude,
                             double latitude)
            throws AlreadyPartyingException, InvalidAmountException, PastPartyException,
                    InvalidLocationException;

    void /* TODO Payment service */ commitParty() throws NoSuchPartyGoingOnException;

    void cancelParty() throws NoSuchPartyGoingOnException;

    Party getParty() throws NoSuchPartyGoingOnException;

    ArrayList<Party> getAllParties();

    Party getPartyAt(Date date) throws NoSuchPartyThenException;
}

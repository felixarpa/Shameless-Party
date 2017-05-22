package felixarpa.shamelessapp.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.data.PartyControllerImpl;
import felixarpa.shamelessapp.domain.model.Party;

public class RankingFragment extends ShamelessFragment {

    private ArrayList<RankingNGO> rankingNGOs;

    public RankingFragment() {
    }

    public static RankingFragment newInstance() {
        return new RankingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        rankingNGOs = new ArrayList<>();
        ArrayList<Party> parties = PartyControllerImpl.getInstance().getAllParties();
        for (int i = 0; i < parties.size(); ++i) {
            Party party = parties.get(i);
            if (party.isGoingOn()) {
                parties.remove(i);
            } else {
                addAmountToNGO(party.getFinalPayment(), party.getNgo());
            }
        }

        return view;
    }

    @Override
    public boolean isCurrentFragment(int id) {
        return id == R.id.navigation_list;
    }

    private RankingNGO addAmountToNGO(float amount, String ngo) {
        for (RankingNGO rankingNGO : rankingNGOs) {
            if (rankingNGO.getNgo().equals(ngo)) {
                rankingNGO.add(amount);
                return rankingNGO;
            }
        }
        RankingNGO rankingNGO = new RankingNGO(ngo, amount);
        rankingNGOs.add(rankingNGO);
        return rankingNGO;
    }

    public class RankingNGO implements Comparator<RankingNGO> {
        private String ngo;
        private float amount;

        public RankingNGO(String ngo, float amount) {
            this.ngo = ngo;
            this.amount = amount;
        }

        public String getNgo() {
            return ngo;
        }

        public float getAmount() {
            return amount;
        }

        public float add(float amountToAdd) {
            this.amount += amountToAdd;
            return amount;
        }

        @Override
        public int compare(RankingNGO rNGO1, RankingNGO rNGO2) {
            return (int) ((rNGO1.amount - rNGO2.amount) * 100);
        }
    }
}

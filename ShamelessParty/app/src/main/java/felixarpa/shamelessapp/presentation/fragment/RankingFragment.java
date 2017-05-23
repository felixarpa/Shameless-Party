package felixarpa.shamelessapp.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.data.PartyControllerImpl;
import felixarpa.shamelessapp.domain.model.Party;
import felixarpa.shamelessapp.utils.C;

public class RankingFragment extends ShamelessFragment {

    private ArrayList<RankingNGO> rankingNGOs;
    private Comparator<RankingNGO> comparator = new Comparator<RankingNGO>() {
        @Override
        public int compare(RankingNGO rNGO1, RankingNGO rNGO2) {
            return (int) ((rNGO2.getAmount() - rNGO1.getAmount()) * 100);
        }
    };

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
            if (!party.isGoingOn() && !party.isCanceled()) {
                addAmountToNGO(party.getFinalPayment(), party.getNgo());
            }
        }
        Collections.sort(rankingNGOs, comparator);

        TextView textView1 = (TextView) view.findViewById(R.id.money_text1);
        TextView textView2 = (TextView) view.findViewById(R.id.money_text2);
        TextView textView3 = (TextView) view.findViewById(R.id.money_text3);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.image3);
        if (rankingNGOs.size() > 0) {
            float amount = rankingNGOs.get(0).getAmount();
            String ngo = rankingNGOs.get(0).getNgo();
            textView1.setText(String.format("%.2f €", amount));
            imageView1.setImageResource(C.getNGOImageRes(ngo));
        }
        if (rankingNGOs.size() > 1) {
            float amount = rankingNGOs.get(1).getAmount();
            String ngo = rankingNGOs.get(1).getNgo();
            textView2.setText(String.format("%.2f €", amount));
            imageView2.setImageResource(C.getNGOImageRes(ngo));
        }
        if (rankingNGOs.size() > 2) {
            float amount = rankingNGOs.get(2).getAmount();
            String ngo = rankingNGOs.get(2).getNgo();
            textView3.setText(String.format("%.2f €", amount));
            imageView3.setImageResource(C.getNGOImageRes(ngo));
        }

        return view;
    }

    @Override
    public boolean isCurrentFragment(int id) {
        return id == R.id.navigation_stats;
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

    public class RankingNGO {
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
    }
}

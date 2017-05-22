package felixarpa.shamelessapp.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.model.NGO;
import felixarpa.shamelessapp.domain.model.Party;

public class PartyRecyclerViewAdapter extends RecyclerView.Adapter<PartyRecyclerViewAdapter.ViewHolder> {

    private final List<Party> parties;

    public PartyRecyclerViewAdapter(List<Party> items) {
        parties = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_party_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Party party = parties.get(position);

        holder.titleTextView.setText(party.getTitle());

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        Date date = new Date(party.getHour());
        calendar.setTime(date);
        holder.dateTextView.setText(dateFormat.format(calendar.getTime()));

        float amount = party.getFinalPayment();
        if (party.isCanceled()) {
            holder.amountTextView.setTextColor(0xffff0000);
            holder.dateTextView.setText(holder.dateTextView.getText() + " (canceled)");
        } else if (amount == 0.0f){
            holder.amountTextView.setTextColor(0xff212121);
        }
        holder.amountTextView.setText(String.format("%.2f €", amount));

        int imageResId = R.mipmap.earth;
        String ngo = party.getNgo();
        if (ngo.equals(NGO.GREENPEACE)) {
            imageResId = R.mipmap.greenpeace;
        } else if (ngo.equals(NGO.AMNESTY)) {
            imageResId = R.mipmap.amnesty;
        } else if (ngo.equals(NGO.UNICEF)) {
            imageResId = R.mipmap.unicef;
        } else if (ngo.equals(NGO.MEDECINS_SANS_FRONTIERES)) {
            imageResId = R.mipmap.msf;
        } else if (ngo.equals(NGO.WWF)) {
            imageResId = R.mipmap.wwf;
        }
        holder.ngoLogoImage.setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return parties.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView amountTextView;
        public ImageView ngoLogoImage;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.titleTextView = (TextView) view.findViewById(R.id.title_text);
            this.dateTextView = (TextView) view.findViewById(R.id.petao_text);
            this.amountTextView = (TextView) view.findViewById(R.id.amount_text);
            this.ngoLogoImage = (ImageView) view.findViewById(R.id.ngo_logo);
        }
    }
}

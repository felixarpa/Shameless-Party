package felixarpa.shamelessapp.utils;

import felixarpa.shamelessapp.R;
import felixarpa.shamelessapp.domain.model.NGO;

public class C {
    public final static String SPN = "ShamelessParty-SharedPreferences";
    public final static String INIT_BOOL = "realm-init";
    public final static String INIT_DATE = "realm-init-date";
    public final static String NTI = "need_to_initialize";
    public final static String CPI = "current_party_id";

    public static int getNGOImageRes(String ngo) {
        int imageResId = R.mipmap.earth;
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
        return imageResId;
    }
}

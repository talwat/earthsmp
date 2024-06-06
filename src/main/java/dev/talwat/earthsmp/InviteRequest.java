package dev.talwat.earthsmp;

import java.util.Calendar;

public class InviteRequest {
    public final String nation;
    public final Calendar time;

    public InviteRequest(String nation) {
        this.nation = nation;
        this.time = Calendar.getInstance();
    }
}


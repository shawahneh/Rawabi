package com.techcamp.aauj.rawabi.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 1/1/2018.
 */

public class Transportation {
    private List<TransportationElement> fromRamallah;
    private List<TransportationElement> fromRawabi;

    public Transportation() {
    }

    public List<TransportationElement> getFromRamallah() {
        return fromRamallah;
    }

    public void setFromRamallah(List<TransportationElement> fromRamallah) {
        this.fromRamallah = fromRamallah;
    }

    public List<TransportationElement> getFromRawabi() {
        return fromRawabi;
    }

    public void setFromRawabi(List<TransportationElement> fromRawabi) {
        this.fromRawabi = fromRawabi;
    }
}

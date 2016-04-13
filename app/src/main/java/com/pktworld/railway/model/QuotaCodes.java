package com.pktworld.railway.model;

/**
 * Created by Prabhat on 27/03/16.
 */
public class QuotaCodes {

    String code;
    String quotaName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public QuotaCodes(String code, String quotaName) {
        this.code = code;
        this.quotaName = quotaName;
    }
}

package com.dior.dior.dto;

public class PayingInfo {
    private String out_trad_no;
    private String memberId;
    private Integer count;

    public String getOut_trad_no() {
        return out_trad_no;
    }

    public void setOut_trad_no(String out_trad_no) {
        this.out_trad_no = out_trad_no;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

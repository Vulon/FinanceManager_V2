package FinanceManager_V2.TransportableDataObjects;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;


@JsonIgnoreProperties
public class TokenData implements Serializable {
    private static final long serialVersionUID = 4741485262466415550L;
    private String access_token;
    private String refresh_token;
    private Date access_token_expire_date;
    private Date refresh_token_expire_date;

    public TokenData(String access_token, String refresh_token, Date access_token_expire_date, Date refresh_token_expire_date) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.access_token_expire_date = access_token_expire_date;
        this.refresh_token_expire_date = refresh_token_expire_date;
    }

    public TokenData() {
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setAccess_token_expire_date(Date access_token_expire_date) {
        this.access_token_expire_date = access_token_expire_date;
    }

    public void setRefresh_token_expire_date(Date refresh_token_expire_date) {
        this.refresh_token_expire_date = refresh_token_expire_date;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public Date getAccess_token_expire_date() {
        return access_token_expire_date;
    }

    public Date getRefresh_token_expire_date() {
        return refresh_token_expire_date;
    }
}

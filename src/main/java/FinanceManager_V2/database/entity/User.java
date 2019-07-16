package FinanceManager_V2.Database.Entity;


import FinanceManager_V2.TransportableDataObjects.TokenData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 8160753068197071922L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "lang")
    private String lang;

    @Column(name = "access_token")
    private String access_token;

    @Column(name = "refresh_token")
    private String refresh_token;

    @Column(name = "access_token_exp")
    private Date access_token_exp;

    @Column(name = "refresh_token_exp")
    private Date refresh_token_exp;

    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_update;





    public User(String email, String password, String lang) {
        this.email = email;
        this.password = password;
        this.lang = lang;
    }

    public User() {
    }

    public void setTokenData(TokenData tokenData){
        access_token = tokenData.getAccess_token();
        refresh_token = tokenData.getRefresh_token();
        access_token_exp = tokenData.getAccess_token_expire_date();
        refresh_token_exp = tokenData.getRefresh_token_expire_date();
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public Date getAccess_token_exp() {
        return access_token_exp;
    }
    public Date getRefresh_token_exp() {
        return refresh_token_exp;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }
}

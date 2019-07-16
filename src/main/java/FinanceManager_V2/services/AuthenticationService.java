package FinanceManager_V2.Services;


import FinanceManager_V2.TransportableDataObjects.TokenData;
import FinanceManager_V2.Database.Repositories.BudgetRepository;
import FinanceManager_V2.Database.Repositories.CategoryRepository;
import FinanceManager_V2.Database.Repositories.TransactionRepository;
import FinanceManager_V2.Database.Repositories.UserRepository;
import FinanceManager_V2.Database.Entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthenticationService {
    public User user;
    public TokenData tokenData;
    private final String url = "http://localhost:8081";

    public static enum ServerResponseCode {
        OK,
        USER_NOT_FOUND,
        INVALID_TOKEN,
        EMAIL_NOT_VERIFIED,
        INTERNAL_SERVER_ERROR,
        CONNECTION_TIMEOUT,
        UNKNOWN,
        ALREADY_REGISTERED
    }

    public synchronized TokenData getTokenData() {
        return tokenData;
    }

    public synchronized void setTokenData(TokenData tokenData) {
        this.tokenData = tokenData;
    }

    UserRepository userRepository;
    TransactionRepository transactionRepository;
    CategoryRepository categoryRepository;
    BudgetRepository budgetRepository;
    Lang lang;

    public AuthenticationService(UserRepository userRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository, BudgetRepository budgetRepository, Lang lang) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        this.lang = lang;
    }

    public ServerResponseCode mapHttpStatus(HttpStatus status){
        if(status == HttpStatus.UNAUTHORIZED){
            return ServerResponseCode.INVALID_TOKEN;
        }else if(status == HttpStatus.NOT_ACCEPTABLE){
            return ServerResponseCode.USER_NOT_FOUND;
        }else if(status == HttpStatus.PRECONDITION_FAILED){
            return ServerResponseCode.EMAIL_NOT_VERIFIED;
        }else if(status == HttpStatus.REQUEST_TIMEOUT){
            return ServerResponseCode.CONNECTION_TIMEOUT;
        }else if(status == HttpStatus.INTERNAL_SERVER_ERROR){
            return ServerResponseCode.INTERNAL_SERVER_ERROR;
        }else if (status == HttpStatus.OK){
            return ServerResponseCode.OK;
        }else if(status == HttpStatus.CONFLICT){
            return ServerResponseCode.ALREADY_REGISTERED;
        }else{
            return ServerResponseCode.UNKNOWN;
        }
    }

    public ServerResponseCode attemptRegister(String email, String password){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/register");
        builder.queryParam("email", email)
                .queryParam("password", password);

        try{
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.toUriString(), null, String.class);
            System.out.println(responseEntity.getBody());
        }catch (HttpStatusCodeException e){
            return mapHttpStatus(e.getStatusCode());
        }

        return mapHttpStatus(HttpStatus.OK);
    }

    public ServerResponseCode attemptLogin(String email, String password){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/login");
        builder.queryParam("email", email)
                .queryParam("password", password);

        System.out.println("Connecting to " + builder.toUriString());
        ResponseEntity<TokenData> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), TokenData.class);
        }catch (HttpStatusCodeException e){
            return mapHttpStatus(e.getStatusCode());
        }
        if(responseEntity.getStatusCode() == HttpStatus.OK){
            user = userRepository.findByEmail(email);

            setTokenData(responseEntity.getBody());

            if(user == null){
                user = new User(email, password, lang.getLanguage().name());
                user.setTokenData(getTokenData());
                userRepository.saveAndFlush(user);
            }else{
                user.setTokenData(getTokenData());
            }
            userRepository.updateTokens(user.getId(), getTokenData().getAccess_token(), getTokenData().getAccess_token_expire_date(),
                    getTokenData().getRefresh_token(), getTokenData().getRefresh_token_expire_date());
            return ServerResponseCode.OK;
        }
        return ServerResponseCode.UNKNOWN;
    }



    public HttpStatus debugClearUsersDatabase(){ //TODO delete debug method
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/clear", String.class);
        return response.getStatusCode();
    }

    public synchronized Long getUserId() {
        return user.getId();
    }

    public synchronized void setUserId(Long userId) {
        this.user.setId(userId);
    }

    public String getUserPassword(){
        return user.getPassword();
    }
    public String getUserEmail(){
        return user.getEmail();
    }
}

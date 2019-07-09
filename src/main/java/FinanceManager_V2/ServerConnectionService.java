package FinanceManager_V2;


import FinanceManager_V2.TransportableDataObjects.TokenData;
import FinanceManager_V2.database.BudgetRepository;
import FinanceManager_V2.database.CategoryRepository;
import FinanceManager_V2.database.TransactionRepository;
import FinanceManager_V2.database.UserRepository;
import FinanceManager_V2.database.entity.User;
import FinanceManager_V2.services.Lang;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServerConnectionService {
    User user;
    TokenData tokenData;
    private final String url = "http://localhost:8081";


    UserRepository userRepository;
    TransactionRepository transactionRepository;
    CategoryRepository categoryRepository;
    BudgetRepository budgetRepository;
    Lang lang;

    public ServerConnectionService(UserRepository userRepository, TransactionRepository transactionRepository, CategoryRepository categoryRepository, BudgetRepository budgetRepository, Lang lang) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
        this.lang = lang;
    }

    public String attemptRegister(String email, String password){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/register");
        builder.queryParam("email", email)
                .queryParam("password", password);
        System.out.println("Connecting to " + builder.toUriString());

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(builder.toUriString(), null, String.class);
        return responseEntity.getBody();
    }

    public HttpStatus attemptLogin(String email, String password){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/login");
        builder.queryParam("email", email)
                .queryParam("password", password);

        System.out.println("Connecting to " + builder.toUriString());

        ResponseEntity<TokenData> responseEntity = restTemplate.getForEntity(builder.toUriString(), TokenData.class);
        if(responseEntity.getStatusCode() == HttpStatus.NOT_FOUND){
            return HttpStatus.NOT_FOUND;
        }else if(responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED){
            return HttpStatus.NOT_MODIFIED;
        }
        if(responseEntity.getStatusCode() == HttpStatus.ACCEPTED){
            user = userRepository.findByEmail(email);
            if(user == null){
                user = new User(email, password, lang.getLanguage().name());
            }
            tokenData = responseEntity.getBody();
            user.setTokenData(tokenData);

            userRepository.updateTokens(user.getId(), tokenData.getAccess_token(), tokenData.getAccess_token_expire_date(),
                    tokenData.getRefresh_token(), tokenData.getRefresh_token_expire_date());
            return HttpStatus.OK;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus debugClearUsersDatabase(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/clear", String.class);
        return response.getStatusCode();
    }
}

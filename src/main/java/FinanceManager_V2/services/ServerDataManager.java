package FinanceManager_V2.Services;


import FinanceManager_V2.TransportableDataObjects.ActionQueue;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@Service
public class ServerDataManager {

    public ServerDataManager(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private AuthenticationService authenticationService;

    public AuthenticationService.ServerResponseCode lastResponseCode;


    public ActionQueue getLastUpdates(String access_token, Date lastUpdate){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authenticationService.url + "/api/get_updates");

        builder.queryParam("access_token", access_token)
                .queryParam("last_update", lastUpdate.getTime());
        ResponseEntity<ActionQueue> responseEntity;
        try{
            System.out.println("Trying to get last updates!!!!!!!");
            responseEntity = restTemplate.getForEntity(builder.toUriString(), ActionQueue.class);
            lastResponseCode = authenticationService.mapHttpStatus(responseEntity.getStatusCode());
        }catch (HttpStatusCodeException e){
            AuthenticationService.ServerResponseCode code = authenticationService.mapHttpStatus(e.getStatusCode());
            lastResponseCode = code;
            return null;
        }catch (Exception e){
            lastResponseCode = AuthenticationService.ServerResponseCode.CONNECTION_TIMEOUT;
            return  null;

        }
        return  responseEntity.getBody();
    }

    public ActionQueue postUpdates(ActionQueue actionQueue, String access_token){
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("SERVER DATA MANAGER is about to post: " + actionQueue);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authenticationService.url + "/api/post_updates");
        builder.queryParam("access_token", access_token);
        ResponseEntity<ActionQueue> responseEntity;
        try{
            responseEntity = restTemplate.postForEntity(builder.toUriString(), actionQueue, ActionQueue.class);
            lastResponseCode = authenticationService.mapHttpStatus(responseEntity.getStatusCode());
        }catch (HttpStatusCodeException e){
            AuthenticationService.ServerResponseCode code = authenticationService.mapHttpStatus(e.getStatusCode());
            lastResponseCode = code;
            return null;
        }catch (Exception e){
            lastResponseCode = AuthenticationService.ServerResponseCode.CONNECTION_TIMEOUT;
            return null;
        }
        return responseEntity.getBody();
    }

}

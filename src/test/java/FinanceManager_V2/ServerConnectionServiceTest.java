package FinanceManager_V2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerConnectionServiceTest {
    static ConfigurableApplicationContext springContext;

    private final String url = "http://localhost:8081";


    static ServerConnectionService  service;
    @BeforeClass
    public static void setUpConnectionTest(){
        springContext = SpringApplication.run(MainApplication.class);
        service = springContext.getBean(ServerConnectionService.class);
        assertEquals(service.debugClearUsersDatabase(), HttpStatus.OK);
    }
    String link1;
    String link3;



    @Test
    @Order(1)
    public void testARegister(){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/register");
        builder.queryParam("email", "test1@testcom")
                .queryParam("password", "123Test123");

        ResponseEntity<String> response1 = restTemplate.postForEntity(builder.toUriString(), null, String.class);
        assertEquals(response1.getStatusCode(), HttpStatus.OK);
        System.out.println("Response 1 link " + response1.getBody());
        ResponseEntity<String> response2 = restTemplate.postForEntity(builder.toUriString(), null, String.class);
        assertEquals(response2.getStatusCode(), HttpStatus.OK);
        builder= UriComponentsBuilder.fromHttpUrl(url + "/register")
        .queryParam("email", "test2@testcom").queryParam("password", "123345");

        ResponseEntity<String> response3 = restTemplate.postForEntity(builder.toUriString(), null, String.class);
        assertEquals(response3.getStatusCode(), HttpStatus.OK);

        try {
            ResponseEntity<String> response4 = restTemplate.postForEntity(url + "/register?email=asdffds", null, String.class);
        }catch (HttpClientErrorException.BadRequest e){
            System.out.println(e.getMessage());
            assertTrue(true);
        }

        link1 = response1.getBody();
        link3 = response3.getBody();
    }


    @Test
    @Order(2)
    public void testBVerify(){
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response1 = restTemplate.getForEntity(url + "/verify?access_token=safkjsdlfkjlfds", String.class);
        }catch (HttpServerErrorException.InternalServerError e){
            System.out.println(e.getMessage());
            assertTrue(true);
        }

        try {
            ResponseEntity<String> response2 = restTemplate.getForEntity(url + "/verify", String.class);
        }catch (HttpClientErrorException.BadRequest e){
            System.out.println(e.getMessage());
            assertTrue(true);
        }

        System.out.println(link1);
        ResponseEntity<String> response3 = restTemplate.getForEntity(link1, String.class);
        assertEquals(response3.getStatusCode(), HttpStatus.OK);

    }

    @Test
    @Order(3)
    public void testCLogin(){
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/login");
        builder.queryParam("email", "test4@testcom")
                .queryParam("password", "123Test123");


        ResponseEntity<String> response1 = restTemplate.getForEntity(builder.toUriString(), String.class);
        assertEquals(response1.getStatusCode(), HttpStatus.NO_CONTENT);

        builder = UriComponentsBuilder.fromHttpUrl(url + "/login");
        builder.queryParam("email", "test1@testcom")
                .queryParam("password", "123test123");
        ResponseEntity<String> response2 = restTemplate.getForEntity(builder.toUriString(), String.class);
        assertEquals(response2.getStatusCode(), HttpStatus.NO_CONTENT);

        builder = UriComponentsBuilder.fromHttpUrl(url + "/login");
        builder.queryParam("email", "test1@testcom")
                .queryParam("password", "123Test123");
        ResponseEntity<String> response3 = restTemplate.getForEntity(builder.toUriString(), String.class);
        assertEquals(response3.getStatusCode(), HttpStatus.ACCEPTED);

        builder = UriComponentsBuilder.fromHttpUrl(url + "/login")
                .queryParam("email", "test2@testcom").queryParam("password", "123345");
        ResponseEntity<String> response4 = restTemplate.getForEntity(builder.toUriString(), String.class);
        assertEquals(response4.getStatusCode(), HttpStatus.CREATED);
    }
    @AfterClass
    public static void clearDatabase(){
        assertEquals(service.debugClearUsersDatabase(), HttpStatus.OK);
    }
}
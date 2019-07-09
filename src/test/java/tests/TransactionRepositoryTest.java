package tests;

import FinanceManager_V2.MainApplication;
import FinanceManager_V2.database.CategoryRepository;
import FinanceManager_V2.database.TransactionRepository;
import FinanceManager_V2.database.entity.Category;
import FinanceManager_V2.database.entity.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionRepositoryTest {
    static TransactionRepository repository;
    static CategoryRepository categoryRepository;
    static ArrayList<Category> categories;
    static ConfigurableApplicationContext springContext;
    @BeforeClass
    public static void setUpRepository(){
        springContext = SpringApplication.run(MainApplication.class);
        repository = springContext.getBean(TransactionRepository.class);

        categoryRepository = springContext.getBean(CategoryRepository.class);

        categories = new ArrayList<>();
        categories.add(RandomDataGenerator.generateCategory(null));
        categories.add(RandomDataGenerator.generateCategory(null));
        categories.add(RandomDataGenerator.generateCategory(null));
        categories.add(RandomDataGenerator.generateCategory(null));
        categoryRepository.saveAll(categories);
        categoryRepository.flush();
    }
    @AfterClass
    public static void clearDatabase(){
        repository.flush();
        repository.deleteByNoteLike("%test");
        categoryRepository.deleteUnused();
        repository = null;
        categoryRepository = null;
        categories = null;
        springContext.close();
    }



    @Test
    public void testInsert(){
        //Check repository content
        ArrayList<Transaction> initialData = new ArrayList<>(repository.findAll());
        for(Transaction tr : initialData){
            System.out.println(tr);
        }


        //repository.deleteAll();
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(RandomDataGenerator.generateTransaction(null, categories));
        transactions.add(RandomDataGenerator.generateTransaction(null, categories));
        transactions.add(RandomDataGenerator.generateTransaction(null, categories));
        transactions.add(RandomDataGenerator.generateTransaction(null, categories));
        transactions.add(RandomDataGenerator.generateTransaction(null, categories));
        repository.saveAll(transactions);
        repository.flush();

        ArrayList<Transaction> testTransactions = new ArrayList<>(repository.findByNoteLike("%test"));
        assertEquals(transactions.size(), testTransactions.size());
        for (int i = 0; i < testTransactions.size(); i++){
            assertEquals(transactions.get(i), testTransactions.get(i));
        }
        //Test offline data saving

        repository.saveAndFlush(RandomDataGenerator.generateTransaction("First Transaction", categories));
        repository.saveAndFlush(RandomDataGenerator.generateTransaction("Second Transaction", categories));


    }



}
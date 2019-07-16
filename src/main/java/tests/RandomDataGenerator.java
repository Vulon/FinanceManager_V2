package tests;

import FinanceManager_V2.Database.Entity.Category;
import FinanceManager_V2.Database.Entity.Transaction;
import FinanceManager_V2.Database.Entity.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class RandomDataGenerator {
    private static String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static String[] colorChars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static String randName(){
        String name = "";
        for(int i = 0; i < 11; i++){
            name += alphabet[(int)(Math.random() * alphabet.length)];
        }
        return name;
    }
    private static String randColor(){
        String color = "#";
        for(int i = 0; i < 6; i++){
            color += colorChars[(int)(Math.random() * colorChars.length)];
        }
        return color;
    }

    public static Category generateCategory(User user, @Nullable String name){
        if(name == null){
            name = randName() + "test";
        }

        return new Category(user.getId(), randColor(), name, (int)(Math.random() * 20), null);
    }


    public static Transaction generateTransaction(User user, @Nullable String name, ArrayList<Category> categories){
        if(name == null){
            name = randName() + "test";
        }
        Category category = categories.get((int)(Math.random() * categories.size()));
        return new Transaction(user.getId(), Math.random() * 20000l,  new Date(), name, category);
    }
}

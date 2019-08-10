package FinanceManager_V2.Services;


import FinanceManager_V2.MainApplication;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

@Service
public class Lang {
    public enum Language{
        ENGLISH,
        RUSSIAN
    }

    public enum TextLine{
        login,
        email,
        password,
        register,
        have_account,
        dont_have_account,
        repeat_password,
        email_incorrect,
        password_incorrect,
        passwords_mismatch,
        connecting_server,
        already_registered,
        check_your_email,
        server_error,
        user_not_found,
        email_not_verified,
        income,
        expense,
        page,
        amount_of_transaction,
        date_of_transaction,
        repeatable_monthly,
        save,
        delete,
        cancel,
        transactions,
        categories,
        budgets,
        logger,
        settings,
        note,
        name,
        parent,
        browse_icon,
        amount_of_budget,
        start_date,
        end_date,
        notify_level,
        type,
        transaction,
        category,
        budget,
        not_repeatable,
        amount_advice,
        parent_advice,
        name_advice,
        icon_advice,
        budget_dates_advice,
        database_update_exception
    }

    private  HashMap<String, String> strings = new HashMap<>();
    private Language language;

    public Lang() {
        language = Language.ENGLISH;
    }

    public void setLanguage(Language language) {
        this.language = language;
        loadLang();
    }

    public Language getLanguage() {
        return language;
    }

    @PostConstruct
    public void loadLang(){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File file = new File(MainApplication.class.getClassLoader().getResource("lang.xml").getFile());
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("language");
            System.out.println("Language name: " + language.name());

            Node node = nodeList.item(0);
            for(int i = 0; i < nodeList.getLength(); i++){
                node = nodeList.item(i);
                Element e = (Element)node;
                System.out.println("FOUND " + e.getAttribute("id"));
                if(e.getAttribute("id").equals(language.name())){
                    System.out.println("BREAKING AT " + e.getAttribute("id"));
                    break;
                }
            }
            strings.clear();
            Element e = (Element)node;
            TextLine[] lines = TextLine.values();
            for(int i = 0; i < lines.length; i++){
                strings.put(lines[i].name(), e.getElementsByTagName(lines[i].name()).item(0).getTextContent());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getTextLine(TextLine textLine){
        return strings.get(textLine.name());
    }

}

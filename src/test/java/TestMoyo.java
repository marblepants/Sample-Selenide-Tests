import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.InvalidStateException;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.fail;

public class TestMoyo {
    @BeforeEach
    void TestSetUp() {
        Configuration.baseUrl = "https://www.moyo.ua/ua/";
        clearBrowserCookies();

        Configuration.browserSize = "1800x900";

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @Test
    void TestCartCounter() {
        //Test if counter in shopping cart updates properly
        int myCounter = 0;
        open("");

        $(MoyoElements.Header.searchField).setValue("macbook").pressEnter();

        //Add 1st result, check if cart has 1 item
        $(MoyoElements.SearchResults.nThResultBuy(1)).click();
        sleep(1000); //code breaks without sleep. performance?
        $(MoyoElements.SearchResults.closeCart).click();
        sleep(200);

        myCounter += 1;
        $(MoyoElements.Header.cartCounter).shouldBe(Condition.exactText(String.valueOf(myCounter)));

        //Add 4 more items, check if cart has 5 items
        for (int i = 2; i<7; i++) {
            $(MoyoElements.SearchResults.nThResultBuy(i)).click();
            sleep(1000); //code breaks without sleep. performance?
            $(MoyoElements.SearchResults.closeCart).click();
            sleep(200);
            myCounter += 1;
        }
        $(MoyoElements.Header.cartCounter).shouldBe(Condition.exactText(String.valueOf(myCounter)));
    }

    @Test
    void TestScrollablePhotos() {
        //Test if images on product page can be properly scrolled one by one

        open("");

        $(MoyoElements.Header.searchField).setValue("airpods").pressEnter();

        //Open 3rd search result
        $(MoyoElements.SearchResults.nThResultLink(3)).click();
        $(MoyoElements.ProductPage.mainImage).click();

        //See if image changes after clicking for the next one
        String imageBefore = $(MoyoElements.ProductPage.galleryImage).getAttribute("src");
        assert imageBefore != null;
        $(MoyoElements.ProductPage.nextImageButton).click();
        $(MoyoElements.ProductPage.galleryImage).shouldNotBe(Condition.attributeMatching("src", imageBefore));
    }


    @Test
    void TestCityStore() {
        ///Test if a given city correctly filters out other cities in store locator

        //Pick a city
        String city = MoyoElements.StoreLocator.storeList.get(4);
        //Make an array of cities excluding the chosen one
        List<String> storeListToCompare = MoyoElements.StoreLocator.storeList;
        storeListToCompare.remove(4);

        open("trade_network.html");
        $(MoyoElements.StoreLocator.GetCityButton(city)).click();

        //technical variable for following iterators
        ElementsCollection results = $$(MoyoElements.StoreLocator.selectedStores);

        //Check if excluded cities make it to the list
        for (SelenideElement element : results) {
            element.shouldNotHave(Condition.matchText(String.join(" | ", storeListToCompare)));
        }
        //Check if every store belongs to the chosen city
        for (SelenideElement element : results) {
            element.shouldHave(Condition.text(city));
        }
    }

    @Test
    void TestCompareProducts() {
        open("");

        //Storing ID of phones we add here
        List<String> addedPhonesId = new ArrayList<>();
        int addedCounter = 0;

        String[] phones = {"oneplus 9 pro", "samsung s22"};
        for (String phone : phones) {
            $(MoyoElements.Header.searchField).setValue(phone).pressEnter();

            //Add first product to compare and save its id
            $(MoyoElements.SearchResults.nThResultLink(1)).click();


            try {
                $(MoyoElements.ProductPage.addToCompare).click();
            } catch (InvalidStateException e) {
                System.out.println("A wild popup appeared! Closing...");
                $(MoyoElements.MainPage.popupCloseBtn).click();
                $(MoyoElements.ProductPage.addToCompare).click();
            }

            addedPhonesId.add($(MoyoElements.ProductPage.itemId).getText());
            System.out.println("New phone added! New ID list:" + addedPhonesId.toString());

            addedCounter += 1;
        }

        $(MoyoElements.Header.compareButton).click(ClickOptions.usingJavaScript()); //usingJavaScript to click through Google popup

        //Check if item count is correct
        $(MoyoElements.CompareList.itemCounter).shouldBe(Condition.exactText(String.valueOf(addedCounter)));

        //List of actual phones in comparison table
        ElementsCollection testedPhones = $$(MoyoElements.CompareList.itemTable);

        //Making sure we have added every phone
        if (addedPhonesId.size() == testedPhones.size()) {

            Iterator<String> expectedList = addedPhonesId.listIterator();
            Iterator<SelenideElement> actualList = testedPhones.listIterator();

            //Check if displayed items are correct
            while (expectedList.hasNext() & actualList.hasNext()) {
                $(actualList.next()).shouldHave(Condition.attribute("data-product_id", String.valueOf(expectedList.next())));
            }
        } else {
            fail("Added products and found products do not match!");
        }
    }

    @Test
    void TestTopProductBar() {
        //Test if a bar with product summary appears when the product page is scrolled

        open("");

        $(MoyoElements.Header.searchField).setValue("airpods").pressEnter();

        //Open any search result
        $(MoyoElements.SearchResults.nThResultLink(3)).click();

        //Make sure top bar is hidden
        $(MoyoElements.ProductPage.productBar).shouldNotBe(Condition.visible);

        //Scroll the page and check if bar appeared
        $(".footer").scrollIntoView(false);
        $(MoyoElements.ProductPage.productBar).shouldBe(Condition.visible);
    }
}

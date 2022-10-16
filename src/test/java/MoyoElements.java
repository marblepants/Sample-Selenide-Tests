import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codeborne.selenide.Selectors.byXpath;
public class MoyoElements {
    public static class MainPage {
        public static String popupCloseBtn = ".popupbanner > .close-btn";
    }

    public static class Header {
        public static String searchField = "#search-input";
        public static By cartButton = byXpath("//div[contains(@class, 'header_content_container')]/div[3]/div[3]");
        public static String searchButton = ".header_search_input_submit";
        public static String cartCounter = ".header_actions_item_count > span";
        public static String compareButton = ".header_actions_compare";
    }
    public static class SearchResults {

        public static String closeCart = ".modal__close";

        public static By nThResultLink(int n) {
            return (byXpath("//div[contains(@class, 'js-products-list')]/div[" + String.valueOf(n) + "]/a"));
        }

        public static By nThResultBuy(int n) {
            return (byXpath("//div[contains(@class, 'js-products-list')]/div[" + String.valueOf(n) + "]//button[contains(@class, 'buy-btn')]"));
    }
    }

    public static class ProductPage {

        public static By mainImage = byXpath("//div[contains(@class, 'product_image-wrap')]//div[contains(@class, 'slick-current')]/a");
        public static String galleryImage = ".slick-active.product-modal_img-block-wrap > div > img";

        public static String nextImageButton = ".product-modal_img-wrap > .slick-next";
        public static String itemId = ".product_id > span";
        public static String addToCompare = ".product_add-to-compare";
        public static String productBar = ".product_fixed_container";
    }

    public static class StoreLocator {

        static String stores = "Київ, Дніпро, Львів, Одеса, Черкаси, Кривий Рiг,  Харків, Ганопіль, Вінниця, Лубни, Самбір, Луцьк, Чернігів, Червоноград, Чернівці, Хмельницкий, Первомайськ, Рівне,  Павлоград, Івано-Франківськ, Кропивницький, Кременчук, Коломия, Бориспіль, Полтава, Кам'янець-Подільський, Запоріжжя, Ужгород, Миколаїв, Суми, Бровари, Кам'янське, Житомир";
        public static String[] storeArray = stores.split(", ");
        public static List<String> storeList = new LinkedList<String>(Arrays.asList(storeArray));


        public static By GetCityButton(String city) {
            return byXpath("//div[contains(@class, 'trade-network__city-list')]/a[contains(text(), '" + city + "')]");
        }

        public static By selectedStores = byXpath("//*[contains(@class, 'stores-information__address-street')]");
    }

    public static class CompareList {
        public static String itemCounter = ".compare-filters-button-count";
        public static By itemTable = byXpath("//th[contains(@class, 'compare-table-header-item')]/div");
    }
}

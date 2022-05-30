package ru.netology.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.By.cssSelector;

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestPositiveRegistration() {
        driver.findElement(cssSelector("[data-test-id = 'name'] input")).sendKeys("Петр Семенов");
        driver.findElement(cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79876543210");
        driver.findElement(cssSelector("[data-test-id = 'agreement']")).click();
        driver.findElement(cssSelector("[type = 'button']")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id = 'order-success']")).getText().trim();
        String expectedMessage = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        assertEquals(expectedMessage, actualMessage, "Успешно");
    }

    @Test
    void shouldTestEnglishCharsInName() {
        driver.findElement(cssSelector("[data-test-id = 'name'] input")).sendKeys("Edward James");
        driver.findElement(cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79876543210");
        driver.findElement(cssSelector("[data-test-id = 'agreement']")).click();
        driver.findElement(cssSelector("[type = 'button']")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id = 'name'].input_invalid .input__sub")).getText().trim();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        assertEquals(expectedMessage, actualMessage, "Потрачено");
    }

    @Test
    void shouldTestPhoneNumbersMoreThan11() {
        driver.findElement(cssSelector("[data-test-id = 'name'] input")).sendKeys("Семен Елизарович");
        driver.findElement(cssSelector("[data-test-id = 'phone'] input")).sendKeys("+798765432109");
        driver.findElement(cssSelector("[data-test-id = 'agreement']")).click();
        driver.findElement(cssSelector("[type = 'button']")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id = 'phone'].input_invalid .input__sub")).getText().trim();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        assertEquals(expectedMessage, actualMessage, "Потрачено");
    }

    @Test
    void shouldTestUncheckedAgreement() {
        driver.findElement(cssSelector("[data-test-id = 'name'] input")).sendKeys("Семен Елизарович");
        driver.findElement(cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79876543210");
        driver.findElement(cssSelector("[type = 'button']")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id = 'agreement'].input_invalid .checkbox__text")).getCssValue("color");
        String expectedMessage = "rgba(255, 92, 92, 1)";
        assertEquals(expectedMessage, actualMessage, "Потрачено");
    }

    @Test
    void shouldTestEmptyNameField() {
        driver.findElement(cssSelector("[data-test-id = 'name'] input")).sendKeys("");
        driver.findElement(cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79876543210");
        driver.findElement(cssSelector("[data-test-id = 'agreement']")).click();
        driver.findElement(cssSelector("[type = 'button']")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id = 'name'].input_invalid .input__sub")).getText().trim();
        String expectedMessage = "Поле обязательно для заполнения";
        assertEquals(expectedMessage, actualMessage, "Потрачено");
    }

    @Test
    void shouldTestEmptyPhoneField() {
        driver.findElement(cssSelector("[data-test-id = 'name'] input")).sendKeys("Мария Никулина");
        driver.findElement(cssSelector("[data-test-id = 'phone'] input")).sendKeys("");
        driver.findElement(cssSelector("[data-test-id = 'agreement']")).click();
        driver.findElement(cssSelector("[type = 'button']")).click();
        String actualMessage = driver.findElement(cssSelector("[data-test-id = 'phone'].input_invalid .input__sub")).getText().trim();
        String expectedMessage = "Поле обязательно для заполнения";
        assertEquals(expectedMessage, actualMessage, "Потрачено");
    }
}

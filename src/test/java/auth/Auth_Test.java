package auth;

import factory.DriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.compress.utils.TimeUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.WaitTools;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Auth_Test {

  private String baseUrl = System.getProperty("base.url");
  private String login = System.getProperty("login");
  private String password = System.getProperty("password");

  private WebDriver driver;
  private WaitTools waitTools;

  @BeforeEach
  public void init() {
    driver = new DriverFactory().create();
    waitTools = new WaitTools(driver);
    driver.get(baseUrl);
  }

  @AfterEach
  public void close() {
    if(driver != null) {
      driver.quit();
    }
  }

  @Test
  public void loginUser() {

    String signInButtonLocator = "//button[text()='Войти']";

    waitTools.waitForCondition(ExpectedConditions.presenceOfElementLocated(By.xpath(signInButtonLocator)));
    waitTools.waitForCondition(ExpectedConditions.elementToBeClickable(By.xpath(signInButtonLocator)));

    WebElement signInButton = driver.findElement(By.xpath(signInButtonLocator));

    String signInPopupSelector = "#__PORTAL__ > div";
    Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.cssSelector(signInPopupSelector)))), "Error");

    signInButton.click();

    WebElement authPopupElement = driver.findElement(By.cssSelector(signInPopupSelector));

    Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.visibilityOf(authPopupElement)), "");

    driver.findElement(By.xpath("//div[./input[@name='email']]")).click();

    WebElement emailInputField = driver.findElement(By.cssSelector("input[name='email']"));
    waitTools.waitForCondition(ExpectedConditions.stalenessOf(emailInputField));
    emailInputField.sendKeys(login);

    WebElement passwordInputField = driver.findElement(By.cssSelector("input[type='password']"));

    driver.findElement(By.xpath("//div[./input[@type='password']]")).click();
    waitTools.waitForCondition(ExpectedConditions.stalenessOf(passwordInputField));
    passwordInputField.sendKeys(password);

    driver.findElement(By.cssSelector("#__PORTAL__ button")).click();

    Assertions.assertTrue(waitTools.waitForCondition(ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.xpath(signInButtonLocator)))));
  }

}

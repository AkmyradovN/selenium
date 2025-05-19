import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
import java.net.MalformedURLException;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    
    private final String VALID_USERNAME = "tomsmith";
    private final String VALID_PASSWORD = "SuperSecretPassword!";
    
    private final By USERNAME_FIELD = By.id("username");
    private final By PASSWORD_FIELD = By.id("password");
    private final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private final By FLASH_MESSAGE = By.id("flash");
    private final By LOGOUT_BUTTON = By.cssSelector(".button.secondary.radius");
    
    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        this.driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, 10);
    }
    
    @Test
    public void testSuccessfulLogin() {
        this.driver.get("http://the-internet.herokuapp.com/login");
        
        login(VALID_USERNAME, VALID_PASSWORD);
        
        WebElement flashMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(FLASH_MESSAGE));
        String messageText = flashMessage.getText();
        assertTrue("Success login message", 
                  messageText.contains("You logged into a secure area!"));
        
        assertTrue("URL should end with /secure", 
                  driver.getCurrentUrl().endsWith("/secure"));
        
        driver.findElement(LOGOUT_BUTTON).click();
        
        flashMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(FLASH_MESSAGE));
        messageText = flashMessage.getText();
        assertTrue("Success logout message", 
                  messageText.contains("You logged out of the secure area!"));
    }
    
    @Test
    public void testFailedLogin() {
        this.driver.get("http://the-internet.herokuapp.com/login");
        
        login(VALID_USERNAME, "WrongPassword");
        
        WebElement flashMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(FLASH_MESSAGE));
        String messageText = flashMessage.getText();
        assertTrue("Error password message", 
                  messageText.contains("Your password is invalid!"));
        
        assertFalse("URL should not end with /secure", 
                   driver.getCurrentUrl().endsWith("/secure"));
    }
    
    private void login(String username, String password) {
        driver.findElement(USERNAME_FIELD).sendKeys(username);
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }
    
    @After
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}
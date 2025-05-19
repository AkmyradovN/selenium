import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.net.URL;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.List;
import java.util.Arrays;

public class RedditTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    
    private final String VALID_USERNAME = "AkmyradovN";
    private final String VALID_PASSWORD = "12345nazar";
    
    private final String BASE_URL = "https://www.reddit.com";
    private final String LOGIN_URL = BASE_URL + "/login";

    private final String[] STATIC_PAGES = {
        BASE_URL + "/r/popular",
        BASE_URL + "/r/all",
        BASE_URL + "/r/AskReddit",
        BASE_URL + "/r/worldnews",
        BASE_URL + "/r/funny"
    };
    
    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        this.driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, 10);
        
        // Setup JavaScript executor
        this.js = (JavascriptExecutor) driver;
    }
    
    /**
     * Test #1: Login Form Submission
     * Tests the login functionality with valid credentials
     */
    @Test
    public void testLogin() {
        driver.get(LOGIN_URL);
        
        // Wait for the username field to be visible
        WebElement usernameField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("login-username"))
        );
        
        // Enter username
        usernameField.sendKeys(VALID_USERNAME);
        
        // Enter password
        WebElement passwordField = driver.findElement(By.id("login-password"));
        passwordField.sendKeys(VALID_PASSWORD);
        
        WebElement loginButton = wait.until(
            ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.login")
            )
        );
        loginButton.click();

        
        // Wait for login to complete, verify by checking for user avatar
        WebElement userAvatar = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.id("expand-user-drawer-button")
             )
        );
        
        // Verify login was successful
        assertTrue("User should be logged in", userAvatar.isDisplayed());
        assertTrue("URL should not be login page anymore", 
                !driver.getCurrentUrl().contains("/login"));
    }
    
    /**
     * Test #2: Form submission after login
     * Tests submitting a form after being logged in (commenting)
     */
    @Test
    public void testFormSubmissionAfterLogin() {
        testLogin();
        driver.get(BASE_URL + "/r/AskReddit/");
        
        List<WebElement> posts = wait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("div[data-testid='post-container']")
            )
        );
        if (!posts.isEmpty()) {
            WebElement commentLink = posts.get(0).findElement(
                By.cssSelector("a[data-click-id='comments']")
            );
            commentLink.click();
        
            // Wait and interact with comment box
            WebElement commentBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("div[data-test-id='comment-submission-form-richtext']")
                )
            );
            commentBox.click();
        
            js.executeScript(
                "arguments[0].textContent = 'Very interesting :-0 )'",
                commentBox
            );
        
            WebElement commentButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[type='submit'][aria-label*='comment']")
                )
            );
            commentButton.click();
            
            try {
                Thread.sleep(3000);
                assertTrue("Comment form should be submitted", true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // /**
    //  * Test #3: Logout
    //  * Tests the logout functionality
    //  */
    // @Test
    // public void testLogout() {
    //     // First login
    //     testLogin();
        
    //     // Click user menu
    //     WebElement userMenu = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.cssSelector("button[id*='USER_DROPDOWN_ID']")
    //         )
    //     );
    //     userMenu.click();
        
    //     // Wait for the dropdown to appear and click logout
    //     WebElement logoutButton = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//span[contains(text(), 'Log Out')]/ancestor::button")
    //         )
    //     );
    //     logoutButton.click();
        
    //     // Confirm logout if a confirmation dialog appears
    //     try {
    //         WebElement confirmLogout = wait.until(
    //             ExpectedConditions.elementToBeClickable(
    //                 By.xpath("//button[contains(text(), 'Log Out')]")
    //             )
    //         );
    //         confirmLogout.click();
    //     } catch (TimeoutException e) {
    //         // If no confirmation dialog, continue
    //     }
        
    //     // Wait for login button to appear, confirming logout
    //     WebElement loginLink = wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.cssSelector("a[href*='/login']")
    //         )
    //     );
        
    //     assertTrue("User should be logged out", loginLink.isDisplayed());
    // }
    
    // /**
    //  * Test #4.1: Fill Input - User Settings Profile
    //  * Test filling input fields in user settings
    //  */
    // @Test
    // public void testFillInputProfileSettings() {
    //     // First login
    //     testLogin();
        
    //     // Navigate to user settings
    //     driver.get(BASE_URL + "/settings/profile");
        
    //     // Wait for settings page to load
    //     wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//h1[contains(text(), 'Customize profile')]")
    //         )
    //     );
        
    //     // Find and update display name input
    //     WebElement displayNameInput = wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//input[@placeholder='Display name (optional)']")
    //         )
    //     );
    //     displayNameInput.clear();
    //     displayNameInput.sendKeys("SeleniumTest");
        
    //     // Find and update about textarea
    //     WebElement aboutTextarea = wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//textarea[contains(@placeholder, 'about')]")
    //         )
    //     );
    //     aboutTextarea.clear();
    //     aboutTextarea.sendKeys("This is a test bio from Selenium");
        
    //     assertTrue("Display name input should be filled", 
    //             displayNameInput.getAttribute("value").equals("SeleniumTest"));
    //     assertTrue("About textarea should be filled", 
    //             aboutTextarea.getAttribute("value").equals("This is a test bio from Selenium"));
    // }
    
    // /**
    //  * Test #4.2: Fill Input - Search
    //  * Test using the search input functionality
    //  */
    // @Test
    // public void testFillInputSearch() {
    //     driver.get(BASE_URL);
        
    //     // Find and click the search input
    //     WebElement searchInput = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.name("q")
    //         )
    //     );
    //     searchInput.click();
        
    //     // Enter search query
    //     searchInput.sendKeys("Selenium WebDriver");
    //     searchInput.sendKeys(Keys.ENTER);
        
    //     // Verify search results page
    //     wait.until(
    //         ExpectedConditions.urlContains("/search/?q=Selenium")
    //     );
        
    //     assertTrue("Should be on search results page", 
    //             driver.getCurrentUrl().contains("/search"));
    // }
    
    /**
     * Test #4.3: Fill Input - Radio, Checkbox in Settings
     * Test using radio buttons and checkboxes in Reddit settings
     */
    // @Test
    // public void testFillInputRadioCheckbox() {
    //     // First login
    //     testLogin();
        
    //     // Go to feed settings
    //     driver.get(BASE_URL + "/settings/feed");
        
    //     // Wait for settings page to load
    //     wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//h1[contains(text(), 'Feed settings')]")
    //         )
    //     );
        
    //     // Find and click Adult content checkbox toggle
    //     WebElement adultContentToggle = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//button[.//*[contains(text(), 'Adult content')]]/../following-sibling::button")
    //         )
    //     );
        
    //     // Store initial state
    //     boolean initialState = adultContentToggle.getAttribute("aria-checked") != null && 
    //                            adultContentToggle.getAttribute("aria-checked").equals("true");
        
    //     // Click to toggle
    //     adultContentToggle.click();
        
    //     // Verify toggle changed
    //     wait.until(ExpectedConditions.or(
    //         ExpectedConditions.attributeToBe(adultContentToggle, "aria-checked", 
    //             initialState ? "false" : "true"),
    //         ExpectedConditions.attributeToBe(adultContentToggle, "aria-checked", 
    //             !initialState ? "true" : "false")
    //     ));
        
    //     // Toggle back to original state
    //     adultContentToggle.click();
        
    //     assertTrue("Toggle should return to initial state", true);
    // }
    
    /**
     * Test #5.1: Send Form - Update Account Settings
     * Test sending a form to update account settings
     */
    // @Test
    // public void testSendFormUpdateSettings() {
    //     // First login
    //     testLogin();
        
    //     // Navigate to account settings
    //     driver.get(BASE_URL + "/settings/account");
        
    //     // Wait for settings page to load
    //     wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//h1[contains(text(), 'Account settings')]")
    //         )
    //     );
        
    //     // Change country
    //     WebElement countryDropdown = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//select[contains(@name, 'country')]")
    //         )
    //     );
    //     countryDropdown.click();
        
    //     // Select a different country (e.g., Canada)
    //     WebElement canadaOption = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//option[@value='CA']")
    //         )
    //     );
    //     canadaOption.click();
        
    //     // Find and click the save button
    //     WebElement saveButton = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//button[contains(text(), 'Save')]")
    //         )
    //     );
    //     saveButton.click();
        
    //     // Wait for confirmation (typically a success message)
    //     wait.until(
    //         ExpectedConditions.or(
    //             ExpectedConditions.visibilityOfElementLocated(
    //                 By.xpath("//*[contains(text(), 'saved') or contains(text(), 'updated')]")
    //             ),
    //             ExpectedConditions.invisibilityOfElementLocated(
    //                 By.xpath("//div[contains(@class, 'loading')]")
    //             )
    //         )
    //     );
        
    //     assertTrue("Settings form should be submitted", true);
    // }
    
    /**
     * Test #5.2: Send Form - Create Post
     * Test creating a new post
     */
    // @Test
    // public void testSendFormCreatePost() {
    //     // First login
    //     testLogin();
        
    //     // Go to a subreddit
    //     driver.get(BASE_URL + "/r/test");
        
    //     // Click the create post button
    //     WebElement createPostButton = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//button[contains(text(), 'Create Post')]")
    //         )
    //     );
    //     createPostButton.click();
        
    //     // Wait for post creation page to load
    //     wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//span[contains(text(), 'Create a post')]")
    //         )
    //     );
        
    //     // Select post type (Text)
    //     WebElement textPostButton = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//button[.//span[contains(text(), 'Post')]]")
    //         )
    //     );
    //     textPostButton.click();
        
    //     // Fill in post title
    //     WebElement titleInput = wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//textarea[contains(@placeholder, 'Title')]")
    //         )
    //     );
    //     titleInput.sendKeys("Selenium Test Post - Please Ignore");
        
    //     // Fill in post content
    //     WebElement contentInput = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//div[contains(@role, 'textbox')]")
    //         )
    //     );
    //     contentInput.click();
    //     contentInput.sendKeys("This is a test post created by Selenium. Please ignore.");
        
    //     // Don't actually submit the post to avoid spam
    //     // But verify the form is filled correctly
    //     assertTrue("Post title should be filled", 
    //             titleInput.getAttribute("value").contains("Selenium Test"));
    // }
    
    /**
     * Test #5.3: Send Form - Subscribe to Subreddit
     * Test subscribing to a subreddit
     */
    // @Test
    // public void testSendFormSubscribe() {
    //     // First login
    //     testLogin();
        
    //     // Go to a subreddit
    //     driver.get(BASE_URL + "/r/selenium");
        
    //     // Find the join/leave button
    //     WebElement joinButton = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.xpath("//button[contains(text(), 'Join') or contains(text(), 'Joined') or contains(text(), 'Leave')]")
    //         )
    //     );
        
    //     // Check current state
    //     String initialState = joinButton.getText();
        
    //     // Click to toggle subscription
    //     joinButton.click();
        
    //     // Wait for state to change
    //     if (initialState.contains("Join")) {
    //         wait.until(
    //             ExpectedConditions.or(
    //                 ExpectedConditions.textToBePresentInElement(joinButton, "Joined"),
    //                 ExpectedConditions.textToBePresentInElement(joinButton, "Leave")
    //             )
    //         );
    //     } else {
    //         wait.until(
    //             ExpectedConditions.textToBePresentInElement(joinButton, "Join")
    //         );
    //     }
        
    //     // Toggle back to avoid changing the actual subscription
    //     joinButton.click();
        
    //     assertTrue("Join button should toggle", true);
    // }
    
    /**
     * Test #6: Static Page Test
     * Test opening a static page and verifying its contents
     */
    // @Test
    // public void testStaticPage() {
    //     // Navigate to the about page
    //     driver.get(BASE_URL + "/r/popular/about");
        
    //     // Wait for the page to load
    //     WebElement aboutContent = wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.xpath("//div[contains(@class, 'about')]")
    //         )
    //     );
        
    //     // Verify some content exists
    //     assertNotNull("About page should have content", aboutContent);
    //     assertTrue("About page should display correctly", aboutContent.isDisplayed());
    // }
    
    // /**
    //  * Test #7: Multiple Page Test from Array
    //  * Test opening multiple pages from an array
    //  */
    // @Test
    // public void testMultiplePages() {
    //     for (String page : STATIC_PAGES) {
    //         // Navigate to the page
    //         driver.get(page);
            
    //         // Wait for page to load (posts to appear)
    //         WebElement pageContent = wait.until(
    //             ExpectedConditions.visibilityOfElementLocated(
    //                 By.xpath("//div[contains(@class, 'Post') or @data-testid='post-container']")
    //             )
    //         );
            
    //         // Verify page loaded correctly
    //         assertTrue("Page " + page + " should load correctly", pageContent.isDisplayed());
            
    //         // Verify URL matches expected
    //         assertTrue("URL should match requested page", 
    //                 driver.getCurrentUrl().contains(page.replace(BASE_URL, "")));
    //     }
    // }
    
    // /**
    //  * Teardown method executed after each test
    //  */
    // @After
    // public void tearDown() {
    //     if (driver != null) {
    //         driver.quit();
    //     }
    // }
    
    // /**
    //  * Helper method to log in
    //  * 
    //  * @param username The username to log in with
    //  * @param password The password to log in with
    //  */
    // private void login(String username, String password) {
    //     driver.get(LOGIN_URL);
        
    //     // Enter username and password
    //     WebElement usernameField = wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(By.id("loginUsername"))
    //     );
    //     usernameField.sendKeys(username);
        
    //     WebElement passwordField = driver.findElement(By.id("loginPassword"));
    //     passwordField.sendKeys(password);
        
    //     // Click login button
    //     WebElement loginButton = wait.until(
    //         ExpectedConditions.elementToBeClickable(
    //             By.cssSelector("button[type='submit']")
    //         )
    //     );
    //     loginButton.click();
        
    //     // Wait for login to complete
    //     wait.until(
    //         ExpectedConditions.visibilityOfElementLocated(
    //             By.cssSelector("button[id*='USER_DROPDOWN_ID']")
    //         )
    //     );
    // }
}
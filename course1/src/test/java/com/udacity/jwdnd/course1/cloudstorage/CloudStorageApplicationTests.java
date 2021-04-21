 package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CloudStorageApplicationTests {
	@Autowired
	EncryptionService encryptionService;
	@Autowired
	CredentialService credentialsService;
	@Autowired
	UserService userService;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	
	@Test
	public void testUnauthorizedUserAccess(){
		driver.get("http://localhost:" + this.port +"/home");
		Assertions.assertEquals("Login",driver.getTitle());
		driver.get("http://localhost:" + this.port +"/uploadFile");
		Assertions.assertEquals("Login",driver.getTitle());
		driver.get("http://localhost:" + this.port +"/credential");
		Assertions.assertEquals("Login",driver.getTitle());
		driver.get("http://localhost:" + this.port +"/note");
		Assertions.assertEquals("Login",driver.getTitle());
	}
	@Test
	public void testUserSignupAndLogin() throws InterruptedException {
		String username = "mm";
		String password = "mm123";
		String firstname = "kosuri";
		String lastname = "matha";
		driver.get("http://localhost:" + this.port+"/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstname,lastname,username,password);
		Assertions.assertEquals("You successfully signed up! Please continue to the login page.",driver.findElement(By.id("success-msg")).getText());
		driver.get("http://localhost:" + this.port+"/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username,password);
		Assertions.assertEquals("Home",driver.getTitle());
		driver.findElement(By.id("logoutbutton")).submit();
		Thread.sleep(5000);
		Assertions.assertNotEquals("Home",driver.getTitle());
		driver.get("http://localhost:" + this.port+"/home");
		Thread.sleep(3000);
		Assertions.assertNotEquals("Home",driver.getTitle());
		Thread.sleep(3000);
	}
	
	@Test
	public void testLogin()throws InterruptedException{
		String username = "mm";
		String password = "mm123";
		String firstname = "kosuri";
		String lastname = "matha";
		driver.get("http://localhost:" + this.port+"/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstname,lastname,username,password);
		Assertions.assertEquals("You successfully signed up! Please continue to the login page.",driver.findElement(By.id("success-msg")).getText());
		driver.get("http://localhost:" + this.port+"/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username,password);
		Assertions.assertEquals("Home",driver.getTitle());
	}
	
	@Test
	  public void testAddNote() {
        driver.get("http://localhost:" + this.port + "/signup");
        SignupPage signUpPage = new SignupPage(driver);
        signUpPage.signup("mm","mm123","matha","kosuri");


        driver.get("http://localhost:" + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("matha", "kosuri");

        driver.get("http://localhost:" + this.port + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createNote();
        driver.get("http://localhost:" + this.port + "/home");
        homePage.clickNoteTab();
        

     //   Assertions.assertEquals(true, homePage.isNoteCreated());
    }
	
	
	@Test
	public void testDeleteNote() {
	      driver.get("http://localhost:" + this.port + "/signup");
	        SignupPage signUpPage = new SignupPage(driver);
	        signUpPage.signup("mm","mm123","matha","kosuri");


	        driver.get("http://localhost:" + this.port + "/login");
	        LoginPage loginPage = new LoginPage(driver);
	        loginPage.login("matha", "kosuri");

	        driver.get("http://localhost:" + this.port + "/home");
	        HomePage homePage = new HomePage(driver);
	        homePage.createNote();
	        driver.get("http://localhost:" + this.port + "/home");
	        homePage.clickNoteTab();
	        homePage.deleteNote();
	        Assertions.assertThrows(NoSuchElementException.class, ()->{
	        	homePage.getNoteTitleText();
	        });
	   
		
	}
	
	@Test
	public void testEditNote() {
	      driver.get("http://localhost:" + this.port + "/signup");
	        SignupPage signUpPage = new SignupPage(driver);
	        signUpPage.signup("mm","mm123","matha","kosuri");


	        driver.get("http://localhost:" + this.port + "/login");
	        LoginPage loginPage = new LoginPage(driver);
	        loginPage.login("matha", "kosuri");

	        driver.get("http://localhost:" + this.port + "/home");
	        HomePage homePage = new HomePage(driver);
	        homePage.createNote();
	        driver.get("http://localhost:" + this.port + "/home");
	        homePage.clickNoteTab();
	        homePage.editNote();
	        driver.get("http://localhost:" + this.port + "/home");
	        homePage.clickNoteTab();
	        String noteTitleText= homePage.getNoteTitleText();
	        System.out.println("the edited note text is"+noteTitleText);
	        Assertions.assertEquals("edittest", homePage.getNoteTitleText());
	        
	       
	}
	
	
	@Test
	public void testAddCredential() throws Exception {
		   driver.get("http://localhost:" + this.port + "/signup");
	        SignupPage signUpPage = new SignupPage(driver);
	        signUpPage.signup("mm","mm123","matha","kosuri");


	        driver.get("http://localhost:" + this.port + "/login");
	        LoginPage loginPage = new LoginPage(driver);
	        loginPage.login("matha", "kosuri");

	        driver.get("http://localhost:" + this.port + "/home");
	        HomePage homePage = new HomePage(driver);
	        homePage.createCredential();
	        driver.get("http://localhost:" + this.port + "/home");
	        homePage.clickCredentialTab();
	      
	       
	        List<String> details = homePage.getCredentialUrl();
	       
	        Assertions.assertEquals("example.gmail", details.get(0));
    	    Assertions.assertEquals("user1", details.get(1));
    		
				Assertions.assertEquals("user123", getDecryptedText(details.get(2)));
			
		
	}
	
	private String getDecryptedText(String encryptedPassword) throws Exception {
		User user = userService.getUser("mm");
		Credential credential = credentialsService.getUserCredentials(user.getUserId()).get(0);
		return encryptionService.decryptValue(encryptedPassword, credential.getKey());
	}
	
	
	
}
	
	



package federico_bechini.training.globant.com.tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.sourceforge.htmlunit.corejs.javascript.ast.ForInLoop;

import org.eclipse.jetty.util.log.Log;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import federico_bechini.training.globant.com.pages.HomePage;

public class Tests {

	WebDriver driver;

	@BeforeMethod
	@Parameters("browser") 
	public void before(String browser) throws MalformedURLException {

		if(browser.equals("RFF")){
			
			DesiredCapabilities capability = DesiredCapabilities.firefox();
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capability);
		}
		if(browser.equals("FF")){
			DesiredCapabilities capability = DesiredCapabilities.firefox();
			//capability.setCapability(FirefoxDriver.DEFAULT_ENABLE_NATIVE_EVENTS, true);
			/*
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile myprofile = profile.getProfile("default");
			myprofile.setAcceptUntrustedCertificates(true);
			myprofile.setAssumeUntrustedCertificateIssuer(true);
			 */
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

			driver = new FirefoxDriver(capabilities);
			driver.manage().deleteAllCookies();
			//driver = new FirefoxDriver(myprofile);
		}
		if(browser.equals("CH")){
			
			DesiredCapabilities capability = DesiredCapabilities.chrome();
			capability.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			System.setProperty("webdriver.chrome.driver","D:/Soft de la facultad/chromeDriver/chromedriver.exe");
			driver = new ChromeDriver(capability);
			driver.manage().deleteAllCookies();
		}
		if(browser.equals("IE")){
			
			DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
			capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			capability.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
			System.setProperty("webdriver.ie.driver","D:/Soft de la facultad/InternetExplorerDriver/IEDriverServer.exe");
			driver = new InternetExplorerDriver(capability);
		}
		driver.manage().window().maximize();
	}

	@AfterMethod
	public void after() {
		driver.quit();
	}

	@Test(description = "Loguearse y verificar que el usuario este logueado.")
	public void validateUserLoged() throws InterruptedException {

		if (login()) {
			Assert.assertTrue(true);
			Reporter.log("<br>Usuario logueado");
		} else {
			Assert.assertFalse(true, "ERROR: no me encuentro logeado ...");
			Reporter.log("<br>Usuario NO logueado");
		}
	}

	@Test(description = "Loguearse con usuario incorrecto y verificar el error.")
	public void validateUserLogedFailUsername() throws InterruptedException {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");

		homePage.pause(15);
		Reporter.log("<br>Entrado a la seccion Login.");

		homePage.goToSignPage();
		homePage.pause(10);

		Reporter.log("<br>Completando formulario de login.");
		driver.findElement(By.name("models['userName'].userName")).sendKeys("fedeg.testreeeee@gmail.com");
		driver.findElement(By.name("models['loginPasswordInput'].password")).sendKeys("4237345fj");
		driver.findElement(By.cssSelector("input[type='submit'][value='Sign in']")).click();

		Reporter.log("<br>Enviando formulario de login.");
		homePage.pause(10);
		
		Reporter.log("<br>Comprobando login incorrecto.");
		boolean existsSingOutLink = driver.findElements(By.linkText("Sign out")).size() != 0;

		String error = driver.findElement(By.className("signInForm")).findElement(By.tagName("p")).getText();
		boolean msgError = error.equals("The e-mail and password you have entered do not match. Please try again.");

		if (existsSingOutLink == false && msgError == true) {
			Assert.assertTrue(true);
			Reporter.log("<br>Se verifico el error: " + error);
		} else {
			Assert.assertFalse(true, "ERROR: algo paso ...");
			Reporter.log("<br>ERROR: algo paso ...");
		}

	}

	@Test( description = "Loguearse con password incorrecto y verificar el error.")
	public void validateUserLogedFailPassword() throws InterruptedException {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");

		homePage.pause(15);
		Reporter.log("<br>Entrado a la seccion Login.");

		homePage.goToSignPage();
		homePage.pause(10);

		Reporter.log("<br>Completando formulario de login.");
		driver.findElement(By.name("models['userName'].userName")).sendKeys("fedeg.test@gmail.com");
		driver.findElement(By.name("models['loginPasswordInput'].password")).sendKeys("4237345fjfj");
		driver.findElement(By.cssSelector("input[type='submit'][value='Sign in']")).click();

		Reporter.log("<br>Enviando formulario de login.");
		homePage.pause(10);
		
		Reporter.log("<br>Comprobando login incorrecto.");
		boolean existsSingOutLink = driver.findElements(By.linkText("Sign out")).size() != 0;

		String error = driver.findElement(By.className("signInForm")).findElement(By.tagName("p")).getText();
		boolean msgError = error.equals("The e-mail and password you have entered do not match. Please try again.");

		if (existsSingOutLink == false && msgError == true) {
			Assert.assertTrue(true);
			Reporter.log("<br>Se verifico el error: " + error);
		} else {
			Assert.assertFalse(true, "ERROR: algo paso ...");
			Reporter.log("<br>ERROR: algo paso ...");
		}
	}

	public boolean login() {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");
		homePage.pause(15);
		
		Reporter.log("<br>Entrado a la seccion Login.");
		homePage.goToSignPage();
		homePage.pause(10);
		
		Reporter.log("<br>Completando formulario de login.");
		driver.findElement(By.name("models['userName'].userName")).sendKeys("fedeg.test@gmail.com");
		driver.findElement(By.name("models['loginPasswordInput'].password")).sendKeys("4237345fj");
		driver.findElement(By.cssSelector("input[type='submit'][value='Sign in']")).click();

		Reporter.log("<br>Enviando formulario de login.");
		homePage.pause(10);
		
		Reporter.log("<br>Comprobando que estemos logeados.");
		boolean existsSingOutLink = driver.findElements(By.linkText("Sign out")).size() != 0;

		return existsSingOutLink;
	}

	@Test( description = "Ya estando logeados, cerrar sesion y verificar que no estamos logueados.")
	public void validateLogout() throws InterruptedException {

		/*if (!login()) Assert.assertFalse(true, "ERROR: no me encuentro logeado ...");*/
		login();
		Reporter.log("<br>Ya estamos logueados.");
		
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		driver.findElement(By.linkText("Sign out")).click();
		
		Reporter.log("<br>Cerrando sesion.");
		homePage.pause(10);

		Reporter.log("<br>Comprobando que no estamos logeados.");
		boolean existsSingInLink = driver.findElements(By.linkText("Sign in")).size() != 0;

		boolean existTitle = driver.findElement(By.id("preMain")).findElement(By.tagName("h1")).getText().equals("You are now signed out");
		
		if (existsSingInLink && existTitle) {
			Assert.assertTrue(true);
			Reporter.log("<br>No estamos logeados.");
		} else {
			Assert.assertFalse(true, "ERROR: algo paso ...");
			Reporter.log("<br>ERROR: algo paso ...");
		}
	}

	@Test( description = "Sin loguearse, buscar un vuelo y verificar pasos del mismo.")
	public void validateSearchSteps() throws InterruptedException {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");
		homePage.pause(15);
		searchAndSteps(false);
	}

	@Test( description = "Sin loguearse, buscar un vuelo y verificar error en el procedimiento mismo.")
	public void negativeTest1() throws InterruptedException {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");
		homePage.pause(15);

		Reporter.log("<br>Seleccionando opcion de 'Flight only'.");
		//driver.findElement(By.cssSelector("#products > div > fieldset > div.products > label:nth-child(1)")).click();
		driver.findElement(By.cssSelector("label[for='search.type.air']")).click();
		homePage.pause(2);

		driver.findElement(By.cssSelector("label[for='search.ar.type.code.oneWay']")).click();

		homePage.pause(2);
		Reporter.log("<br>Llenando campos de origen y destino.");
		driver.findElement(By.name("ar.ow.leaveSlice.orig.key")).sendKeys("LAS");

		Reporter.log("<br>Seleccionando 2 Adultos.");
		new Select(driver.findElement(By.name("ar.ow.numAdult"))).selectByVisibleText("2");
		homePage.pause(1);

		Reporter.log("<br>Calculando fechas dinamicas.");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yy");
		Calendar dateOrigin = Calendar.getInstance();
		dateOrigin.add(Calendar.DATE, 5);

		Reporter.log("<br>Llenando fechas dinamicas.");
		driver.findElement(By.name("ar.ow.leaveSlice.date")).sendKeys(format1.format(dateOrigin.getTime()));

		Reporter.log("<br>Enviando formulario.");
		driver.findElement(By.cssSelector("input[type='submit'][value='Search Flights'][name='search']")).click();

		Reporter.log("<br>Cargando pagina de resultados.");
		homePage.pause(10);

		boolean flag = false;
		List<WebElement> errors = driver.findElements(By.cssSelector("p[class^='error message']"));
		for (WebElement item : errors) {
			if (item.getText().equals("You have not told us where you are going. "
							+ "Please enter the name of a city or an airport code and try again.")) flag = true;
		}

		if (flag) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse(true,"ERROR: algo paso y no se encontro el error esperado ...");
		}
	}

	@Test( description = "Sin loguearse, buscar un vuelo , reservando y verificar que faltan ingresar datos.")
	public void negativeTest2() throws InterruptedException {

		search();
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);

		Reporter.log("<br>Clickeando el primer elemento de la lista en el boton select rojo.");
		driver.findElement(By.linkText("Select")).click();
		homePage.pause(20);

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 1.");
		driver.findElement(By.name("_eventId_checkout")).click();
		homePage.pause(10);

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 2.");
		driver.findElement(By.name("_eventId_continue")).click();
		homePage.pause(10);

		Reporter.log("<br>Por buscar y completar los campos a llenar.");
		Reporter.log("<br>Datos del primer pasajero.");
		
		Reporter.log("<br>firstName1.");
		driver.findElement(By.name("models['travelersInput'].travelers[0].name.firstName")).sendKeys("Federico");
		
		Reporter.log("<br>lastName1");
		driver.findElement(By.name("models['travelersInput'].travelers[0].name.lastName")).sendKeys("Bechini");
		
		Reporter.log("<br>phoneCountryCode1");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].phoneNumber.phoneCountryCode"))).selectByValue("AR");
		
		Reporter.log("<br>phoneNumber1");
		driver.findElement(By.name("models['travelersInput'].travelers[0].phoneNumber.phoneNumber")).sendKeys("4237345");
		
		Reporter.log("<br>gender1.");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.gender.gender"))).selectByValue("M");
		
		Reporter.log("<br>dateOfBirthMonth1.");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobMonth"))).selectByVisibleText("April");
		
		Reporter.log("<br>dateOfBirthDay1.");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobDay"))).selectByValue("10");
		
		Reporter.log("<br>dateOfBirthYear1");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobYear"))).selectByValue("1991");
		driver.findElement(By.name("models['bookingInput'].email.emailAddress")).sendKeys("fedeg.test@gmail.com");


		Reporter.log("<br>No completamos los campos del segundo pasajero.");

		List<WebElement> checkboxs = driver.findElements(By.className("primaryRadioMessage"));
		Reporter.log("<br>Por elegir los checkboxs.");
		for (WebElement item : checkboxs) {
			if (item.getText().equals("Do not send Flight Status Updates")) item.click();
			if (item.getText().equals("No, I choose not to protect my purchase.")) item.click();
		}

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 3.");
		driver.findElement(By.name("_eventId_submit")).click();
		homePage.pause(10);

		Reporter.log("<br>Comprobando los 5 errores esperados.");
		List<WebElement> labels = driver.findElements(By.cssSelector("p[class^='error message'] span:nth-child(2)"));
		int flagError = 0;
		for (WebElement item : labels) {
			if (item.getText().equals("An error has occurred while processing this page. Please see detail below."))
				flagError++;
			if (item.getText().equals("Please specify the first name."))
				flagError++;
			if (item.getText().equals("Please specify the last name."))
				flagError++;
			if (item.getText().equals("Please specify the traveler's gender."))
				flagError++;
			if (item.getText().equals("Please provide a birthdate."))
				flagError++;
		}

		if (flagError == 5) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse(true,"ERROR: algo paso y no se encontraron todos los errores esperados...");
		}
	}

	@Test( description = "Sin loguearse, buscar un vuelo.")
	public void positiveTest1() throws InterruptedException {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");
		homePage.pause(15);

		Reporter.log("<br>Seleccionando opcion de 'Flight only'.");
		//driver.findElement(By.cssSelector("#products > div > fieldset > div.products > label:nth-child(1)")).click();
		driver.findElement(By.cssSelector("label[for='search.type.air']")).click();
		homePage.pause(2);

		List<WebElement> checkboxs = driver.findElements(By.className("primaryRadioMessage"));
		Reporter.log("<br>Por elegir los checkboxs.");
		for (WebElement item : checkboxs) {
			if (item.getText().equals("One-way"))
				item.click();
		}

		homePage.pause(2);
		Reporter.log("<br>Llenando campos de origen y destino.");
		driver.findElement(By.name("ar.ow.leaveSlice.orig.key")).sendKeys("LAS");
		driver.findElement(By.name("ar.ow.leaveSlice.dest.key")).sendKeys("LAX");

		Reporter.log("<br>Seleccionando 2 Adultos.");
		new Select(driver.findElement(By.name("ar.ow.numAdult"))).selectByVisibleText("2");
		homePage.pause(1);

		Reporter.log("<br>Calculando fechas dinamicas.");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yy");
		Calendar dateOrigin = Calendar.getInstance();
		dateOrigin.add(Calendar.DATE, 5);

		Reporter.log("<br>Llenando fechas dinamicas.");
		driver.findElement(By.name("ar.ow.leaveSlice.date")).sendKeys(format1.format(dateOrigin.getTime()));

		Reporter.log("<br>Por elegir los labels.");
		List<WebElement> labels = driver.findElements(By.className("labelText"));
		for (WebElement item : labels) {
			if (item.getText().equals("I prefer non-stop flights"))
				item.click();
		}

		Reporter.log("<br>Enviando formulario.");
		driver.findElement(By.cssSelector("input[type='submit'][value='Search Flights'][name='search']")).click();
		
		Reporter.log("<br>Cargando pagina de resultados.");
		homePage.pause(15);
		
		Reporter.log("<br>Comprobando que estamos en la pagina de resultados.");
		Reporter.log("<br>Comprobando el elemento CHEAPEST PRICE.");
		boolean element = driver.findElement(By.className("airLowestPrice")).findElement(By.tagName("h2")).getText().equals("CHEAPEST PRICE");
		
		if (element) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse(true,"ERROR: No estamos en la pagina de resultados.");
		}
	}

	@Test( description = "Sin loguearse, buscar un vuelo y verificar pasos del mismo.")
	public void positiveTest2() throws InterruptedException {

		search();
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		// Reporter.log("<br>Cargando pagina de resultados.");

		Reporter.log("<br>Clickeando el primer elemento de la lista en el boton select rojo.");
		WebElement link = driver.findElement(By.linkText("Select"));
		link.click();
		homePage.pause(20);

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 1.");
		WebElement continueButton = driver.findElement(By
				.name("_eventId_checkout"));
		continueButton.click();
		homePage.pause(10);

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 2.");
		WebElement continue2Button = driver.findElement(By
				.name("_eventId_continue"));
		continue2Button.click();
		homePage.pause(10);

		Reporter.log("<br>Por buscar los campos a llenar.");
		Reporter.log("<br>Datos del primer pasajero.");
		Reporter.log("<br>firstName1.");
		WebElement firstName1 = driver.findElement(By
				.name("models['travelersInput'].travelers[0].name.firstName"));
		Reporter.log("<br>lastName1");
		WebElement lastName1 = driver.findElement(By
				.name("models['travelersInput'].travelers[0].name.lastName"));
		Reporter.log("<br>phoneCountryCode1");
		Select phoneCountryCode1 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[0].phoneNumber.phoneCountryCode")));
		Reporter.log("<br>phoneNumber1");
		WebElement phoneNumber1 = driver
				.findElement(By
						.name("models['travelersInput'].travelers[0].phoneNumber.phoneNumber"));
		Reporter.log("<br>gende1r.");
		Select gender1 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[0].tsaInfoInput.gender.gender")));
		Reporter.log("<br>dateOfBirthMonth1.");
		Select dateOfBirthMonth1 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobMonth")));
		Reporter.log("<br>dateOfBirthDay1.");
		Select dateOfBirthDay1 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobDay")));
		Reporter.log("<br>dateOfBirthYear1");
		Select dateOfBirthYear1 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobYear")));
		WebElement emailAddress1 = driver.findElement(By
				.name("models['bookingInput'].email.emailAddress"));

		Reporter.log("<br>Datos del segundo pasajero.");
		Reporter.log("<br>firstName2.");
		WebElement firstName2 = driver.findElement(By
				.name("models['travelersInput'].travelers[1].name.firstName"));
		Reporter.log("<br>lastName2");
		WebElement lastName2 = driver.findElement(By
				.name("models['travelersInput'].travelers[1].name.lastName"));
		Reporter.log("<br>phoneCountryCode2");
		Reporter.log("<br>gender2.");
		Select gender2 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[1].tsaInfoInput.gender.gender")));
		Reporter.log("<br>dateOfBirthMonth2.");
		Select dateOfBirthMonth2 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[1].tsaInfoInput.dateOfBirth.dobMonth")));
		Reporter.log("<br>dateOfBirthDay2.");
		Select dateOfBirthDay2 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[1].tsaInfoInput.dateOfBirth.dobDay")));
		Reporter.log("<br>dateOfBirthYear2");
		Select dateOfBirthYear2 = new Select(
				driver.findElement(By
						.name("models['travelersInput'].travelers[1].tsaInfoInput.dateOfBirth.dobYear")));

		Reporter.log("<br>Por completar los campos del primer pasajero.");
		firstName1.sendKeys("Federico");
		lastName1.sendKeys("Bechini");
		phoneCountryCode1.selectByValue("AR");
		phoneNumber1.sendKeys("4237345");
		gender1.selectByValue("M");
		dateOfBirthDay1.selectByValue("10");
		dateOfBirthMonth1.selectByVisibleText("April");
		dateOfBirthYear1.selectByValue("1991");
		emailAddress1.sendKeys("fedeg.test@gmail.com");

		Reporter.log("<br>Por completar los campos del segundo pasajero.");
		firstName2.sendKeys("Federicooo");
		lastName2.sendKeys("Bechiniii");
		gender2.selectByValue("M");
		dateOfBirthDay2.selectByValue("20");
		dateOfBirthMonth2.selectByVisibleText("April");
		dateOfBirthYear2.selectByValue("1992");

		List<WebElement> checkboxs = driver.findElements(By
				.className("primaryRadioMessage"));
		Reporter.log("<br>Por elegir los checkboxs.");
		for (WebElement item : checkboxs) {
			if (item.getText().equals("Do not send Flight Status Updates"))
				item.click();
			if (item.getText().equals(
					"No, I choose not to protect my purchase."))
				item.click();
		}

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 3.");
		WebElement continue3Button = driver.findElement(By
				.name("_eventId_submit"));
		continue3Button.click();
		homePage.pause(5);
/*
		Reporter.log("<br>Comprobando que estamos en el siguiente paso.");
		WebElement title = driver.findElement(By
				.cssSelector("h1 span[class='subtitle']"));
		boolean tmp = title.getText().equals("Review and book");

		if (tmp) {
			Assert.assertTrue(true);
		} else {
			Assert.assertFalse(false, "ERROR: No estamos en el siguiente paso");
		}
		*/
	}

	@Test( description = "Estando logueado, buscar un vuelo y verificar pasos del mismo.")
	public void validateSearchStepsLogedIn() {

		//if (!login()) Assert.assertFalse(true, "ERROR: no me encuentro logeado ...");
		login();
		Reporter.log("<br>Ya estamos logueados.");
		searchAndSteps(true);
	}

	@Test( description = "Sin loguearse, buscar un vuelo + hotel y verificar pasos del mismo.")
	public void vaidateSearchHotelSteps() {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");
		homePage.pause(30);

		Reporter.log("<br>Seleccionando opcion de 'Flight only + Hotel'.");
		driver.findElement(By.cssSelector("label[for='search.type.aph']")).click();
				
		homePage.pause(15);

		Reporter.log("<br>Llenando campos de origen y destino.");
		driver.findElement(By.name("aph.leaveSlice.orig.key")).sendKeys("LAS");
		driver.findElement(By.name("aph.leaveSlice.dest.key")).sendKeys("LAX");
		
		Reporter.log("<br>Seleccionando 1 Adulto.");
		new Select(driver.findElement(By.name("aph.rooms[0].adlts"))).selectByVisibleText("1");
		homePage.pause(1);
		
		Reporter.log("<br>Calculando fechas dinamicas.");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yy");
		Calendar dateOrigin = Calendar.getInstance();
		Calendar dateDestination = Calendar.getInstance();
		dateOrigin.add(Calendar.DATE, 5);
		dateDestination.add(Calendar.DATE, 15);

		Reporter.log("<br>Llenando fechas dinamicas.");
		driver.findElement(By.name("aph.leaveSlice.date")).sendKeys(format1.format(dateOrigin.getTime()));
		driver.findElement(By.name("aph.returnSlice.date")).sendKeys(format1.format(dateDestination.getTime()));

		Reporter.log("<br>Enviando formulario.");
		driver.findElement(By.cssSelector("div input[type='submit'][value='Search'][name='search'][data-agent^='{']")).click();

		Reporter.log("<br>Cargando pagina de resultados.");
		homePage.pause(15);

		Reporter.log("<br>Buscando los 5 elementos para comprobar.");
		Reporter.log("<br>Comprobando element1.");
		boolean element1 = driver.findElement(By.cssSelector("li[class='currentTab']")).getText().equals("Vacation Packages");
		
		Reporter.log("<br>Comprobando element2.");
		boolean element2 = driver.findElement(By.cssSelector(".pkgBreadcrumbMod  .headerContent h2")).getText().equals("Build your package:");

		Reporter.log("<br>Comprobando element3.");
		boolean element3 = driver.findElement(By.cssSelector(".productCrumb.hotelIcon.selected")).getText().equals("Refine Results");

		Reporter.log("<br>Comprobando element4.");
		boolean element4 = driver.findElement(By.cssSelector(".rating h3")).getText().equals("Star rating");

		Reporter.log("<br>Comprobando element5.");
		boolean element5 = driver.findElement(By.cssSelector(".location span")).getText().equals("Our most booked areas");

		Reporter.log("<br>Comprobando ...");
		if (!element1 && !element2 && !element3 && !element4 && !element5) {
			Assert.assertFalse(true,"ERROR: no se cunplieron los 5 validaciones en resultados.");
			Reporter.log("<br>ERROR: no se cunplieron los 5 validaciones en resultados.");
		}
		
		Reporter.log("<br>Haciendo click en Filtrado.");
		driver.findElement(By.cssSelector(".actionExpand")).click();
		homePage.pause(1);
		
		Reporter.log("<br>Eligiendo la opcion 'Reviewer Score' del filtrado.");
		driver.findElement(By.partialLinkText("Reviewer Score")).click();
		homePage.pause(15);
		
		Reporter.log("<br>Haciendo click en link '4-star hotels and up'.");
		driver.findElement(By.linkText("4-star hotels and up")).click();
		homePage.pause(5);
		
		Reporter.log("<br>Buscando elementos para verificar el filtrado elegido anteriormente.");
		List<WebElement> resultados = driver.findElements(By.cssSelector("div.pkgResultsCard.pkgResultsHotelCard"));
		int flagValid = 0;
		for (WebElement item : resultados) {
			WebElement r = item.findElement(By.cssSelector("p.hotelRatings"));
			String stars = r.findElement(By.tagName("img")).getAttribute("alt").replaceAll("[a-zA-Z]+", "").trim();
			String reviews = r.findElement(By.tagName("a")).getText().replaceAll("[a-zA-Z]+", "").trim();
			if(Integer.valueOf(stars) >= 4 && Integer.valueOf(reviews) >= 1) flagValid++;	
		}
		
		Reporter.log("<br>Comprobando que todos tenga estrellas 4-5 y score.");
		if (flagValid != resultados.size() ) {
			Assert.assertFalse(true,"ERROR: no se cumple la cantidad de items con la cantidad de extrellas y score esperadas.");
			Reporter.log("<br>ERROR: no se cumple la cantidad de items con la cantidad de extrellas y score esperadas.");
		}

		Reporter.log("<br>Clickeando el primer elemento de la lista en el boton select rojo que no sea sponsor.");
		resultados.get(0).findElement(By.linkText("Select")).click();
		homePage.pause(10);
		
		Reporter.log("<br>Clickeando el primer elemento de la lista en el boton select rojo de la seccion vuelos.");
		driver.findElement(By.linkText("Select")).click();
		homePage.pause(20);
		
		Reporter.log("<br>Buscando element1.");  
		element1 = driver.findElement(By.cssSelector("div .bookingBreadcrumb li[class='selected']")).getText().equals("Trip details");

		Reporter.log("<br>Buscando element2.");  
		element2 =driver.findElement(By.cssSelector("#preMain h1")).getText().contains("Trip Details: Flight + Hotel");

		Reporter.log("<br>Buscando element3 y element4."); 
		List<WebElement>  temp = driver.findElements(By.cssSelector(".tripDetailsComponent.dpTripDetailsComponent .accountBookingPanel .title .titleText"));
		element3 = temp.get(0).getText().equals("Flight");
		element4 = temp.get(1).getText().equals("Hotel");
		
		Reporter.log("<br>Buscando element5 y element6."); 
		temp = driver.findElements(By.cssSelector(".heading h3"));
		element5 = temp.get(0).getText().equals("Trip cost");
		boolean element6 = temp.get(1).getText().equals("Trip Information");

		Reporter.log("<br>Comprobando si estan todos los 5 elementos ...");
		if( !element1 || !element2 || !element3 || !element4 || !element5 || !element6){
			Assert.assertFalse(true,"ERROR: no se cunplieron las 6 validaciones en Trip Details.");
			Reporter.log("<br>ERROR: no se cunplieron los 5 validaciones en Trip Details."); 
		} 
		Reporter.log("<br>Estan los elementos en Trip Details.");
	}

	public void searchAndSteps(boolean loged){
		
		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		Reporter.log("<br>Seleccionando opcion de 'Flight only'.");
		//driver.findElement(By.cssSelector("#products > div > fieldset > div.products > label:nth-child(1)")).click();
		driver.findElement(By.cssSelector("label[for='search.type.air']")).click();
		homePage.pause(2);

		Reporter.log("<br>Llenando campos de origen y destino.");
		driver.findElement(By.name("ar.rt.leaveSlice.orig.key")).sendKeys("LAS");
		driver.findElement(By.name("ar.rt.leaveSlice.dest.key")).sendKeys("LAX");

		Reporter.log("<br>Seleccionando 1 Adulto.");
		new Select(driver.findElement(By.name("ar.rt.numAdult"))).selectByVisibleText("1");
		homePage.pause(1);
		
		Reporter.log("<br>Calculando fechas dinamicas.");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yy");
		Calendar dateOrigin = Calendar.getInstance();
		Calendar dateDestination = Calendar.getInstance();
		dateOrigin.add(Calendar.DATE, 5);
		dateDestination.add(Calendar.DATE, 15);

		Reporter.log("<br>Llenando fechas dinamicas.");
		driver.findElement(By.name("ar.rt.leaveSlice.date")).sendKeys(format1.format(dateOrigin.getTime()));
		driver.findElement(By.name("ar.rt.returnSlice.date")).sendKeys(format1.format(dateDestination.getTime()));

		Reporter.log("<br>Enviando formulario.");
		driver.findElement(By.cssSelector("input[type='submit'][value='Search Flights'][name='search']")).click();

		Reporter.log("<br>Cargando pagina de resultados.");
		homePage.pause(60);

		Reporter.log("<br>Buscando los 5 elementos para comprobar.");
		Reporter.log("<br>Comprobando element1.");
		// busca si esta el link 'Flights' en la seccion roja
		boolean element1 = driver.findElements(By.linkText("Flights")).size() != 0;

		Reporter.log("<br>Comprobando element2.");
		// busca el texto chepest price
		boolean element2 = driver.findElement(By.className("airLowestPrice")).findElement(By.tagName("h2")).getText().equals("CHEAPEST PRICE");

		Reporter.log("<br>Comprobando element3.");
		// busca el texto Refine Results
		boolean element3 = driver.findElement(By.className("airFiltersMod")).findElement(By.tagName("h2")).getText().equals("Refine Results");

		Reporter.log("<br>Comprobando element4.");
		// busca el texto Sort by:
		boolean element4 = driver.findElement(By.className("bidirectionalSort")).findElement(By.tagName("h2")).getText().equals("Sort by:");

		Reporter.log("<br>Comprobando element5.");
		// busca el texto Matching Results:
		boolean element5 = driver.findElement(By.className("matchingResults")).findElement(By.tagName("h2")).getText().contains("Matching Results:");

		Reporter.log("<br>Comprobando ...");
		if (!element1 && !element2 && !element3 && !element4 && !element5) {
			Assert.assertFalse(true,"ERROR: no se cunplieron los 5 validaciones en resultados.");
			Reporter.log("<br>ERROR: no se cumplieron las 5 validaciones en resultados.");
		}

		Reporter.log("<br>Haciendo click en Filtrado.");
		driver.findElement(By.cssSelector(".actionExpand")).click();

		Reporter.log("<br>Eligiendo la opcion 'Earliest Departure' del filtrado.");
		driver.findElement(By.partialLinkText("Earliest Departure")).click();
		homePage.pause(15);

		Reporter.log("<br>Clickeando el primer elemento de la lista en el boton select rojo.");
		driver.findElement(By.linkText("Select")).click();
		homePage.pause(20);

		Reporter.log("<br>Buscando element1."); 
		//busca el texto 'Trip details' en la barra del principio del home  
		element1 =driver.findElement(By.cssSelector("div .bookingBreadcrumb li[class='selected']")).getText().equals("Trip details");
		
		Reporter.log("<br>Buscando element2.");
		element2 =driver.findElement(By.cssSelector(".last td")).getText().contains("(Adult: 1)");
		
		Reporter.log("<br>Buscando element3."); //busca si estan los link'Continue' rojo tanto arriba como abajo 
		element3 =driver.findElements(By.name("_eventId_checkout") ).size() != 0;
		
		Reporter.log("<br>Buscando element4."); //busca el texto 'Trip cost'
		element4 =driver.findElement(By.cssSelector("h3[data-context='currentTripCostTitle']")).getText().equals("Trip cost");
		
		Reporter.log("<br>Buscando element5."); 
		element5 =driver.findElement(By.cssSelector(".descriptionItem")).getText().equals("Airline Ticket(1)");
		
		Reporter.log("<br>Comprobando si estan todos los 5 elementos ...");
		
		if( !element1 || !element2 || !element3 || !element4 || !element5){
			Assert.assertFalse(true,"ERROR: no se cunplieron los 5 validaciones en Trip Details.");
			Reporter.log("<br>ERROR: no se cunplieron los 5 validaciones en Trip Details."); 
		} 
		Reporter.log("<br>Estan los elementos en Trip Details.");
		
		Reporter.log("<br>Clickeando el boton 'Continue' Parte 1.");
		driver.findElement(By.name("_eventId_checkout")).click();
		homePage.pause(10);

		Reporter.log("<br>Verificando que la informacion del veulo es correcta ...");
		List<WebElement> datos = driver.findElements(By.className("flightOriginDestination"));
		boolean a1 = datos.get(0).getText().equals("Las Vegas (LAS) > Los Angeles (LAX)");
		boolean a2 = datos.get(1).getText().equals("Los Angeles (LAX) > Las Vegas (LAS)");
		
		if (!a1 && !a2 ) {
			Assert.assertFalse(true,"ERROR: no se cumplio la informacion del vuelo esperada.");
			Reporter.log("<br>ERROR: no se cumplio la informacion del vuelo esperada.");
		}
		
		Reporter.log("<br>Clickeando el boton 'Continue' Parte 2.");
		driver.findElement(By.name("_eventId_continue")).click();
		homePage.pause(10);
		
		Reporter.log("<br>Por buscar y completar los campos a llenar.");

		if(!loged){
			Reporter.log("<br>firstName.");
			driver.findElement(By.name("models['travelersInput'].travelers[0].name.firstName")).sendKeys("Federico");
			Reporter.log("<br>lastName");
			driver.findElement(By.name("models['travelersInput'].travelers[0].name.lastName")).sendKeys("Bechini");
		}
		Reporter.log("<br>phoneCountryCode");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].phoneNumber.phoneCountryCode"))).selectByValue("AR");
		
		Reporter.log("<br>phoneNumber");
		driver.findElement(By.name("models['travelersInput'].travelers[0].phoneNumber.phoneNumber")).sendKeys("4237345");
		
		Reporter.log("<br>gender.");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.gender.gender"))).selectByValue("M");
		
		Reporter.log("<br>dateOfBirthMonth.");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobMonth"))).selectByVisibleText("April");
		
		Reporter.log("<br>dateOfBirthDay.");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobDay"))).selectByValue("10");
		
		Reporter.log("<br>dateOfBirthYear");
		new Select(driver.findElement(By.name("models['travelersInput'].travelers[0].tsaInfoInput.dateOfBirth.dobYear"))).selectByValue("1991");
		if(!loged){
			driver.findElement(By.name("models['bookingInput'].email.emailAddress")).sendKeys("fedeg.test@gmail.com");
		}
		List<WebElement> checkboxs = driver.findElements(By.className("primaryRadioMessage"));
		Reporter.log("<br>Por elegir los checkboxs.");
		for (WebElement item : checkboxs) {
			if (item.getText().equals("Do not send Flight Status Updates")) item.click();
			if (item.getText().equals("No, I choose not to protect my purchase and understand "
							+ "I am responsible for all cancellation fees and delay expenses.")) item.click();
		}

		Reporter.log("<br>Clickeando el boton 'Continue' Parte 3.");
		driver.findElement(By.name("_eventId_submit")).click();
	}
	
	public void search() {

		HomePage homePage = PageFactory.initElements(driver, HomePage.class);
		homePage.go(driver);
		Reporter.log("<br>Entrado a cheaptickets.com");
		homePage.pause(15);
		
		Reporter.log("<br>Seleccionando opcion de 'Flight only'.");
		driver.findElement(By.cssSelector("label[for='search.type.air']")).click();
		homePage.pause(2);

		Reporter.log("<br>Por elegir los checkboxs.");
		driver.findElement(By.cssSelector("label[for='search.ar.type.code.oneWay']")).click();
		homePage.pause(2);

		Reporter.log("<br>Llenando campos de origen y destino.");
		driver.findElement(By.name("ar.ow.leaveSlice.orig.key")).sendKeys("LAS");
		driver.findElement(By.name("ar.ow.leaveSlice.dest.key")).sendKeys("LAX");

		Reporter.log("<br>Seleccionando 2 Adultos.");
		new Select(driver.findElement(By.name("ar.ow.numAdult"))).selectByVisibleText("2");
		homePage.pause(1);

		Reporter.log("<br>Calculando fechas dinamicas.");
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yy");
		Calendar dateOrigin = Calendar.getInstance();
		dateOrigin.add(Calendar.DATE, 5);

		Reporter.log("<br>Llenando fechas dinamicas.");
		driver.findElement(By.name("ar.ow.leaveSlice.date")).sendKeys(format1.format(dateOrigin.getTime()));

		Reporter.log("<br>Por elegir los labels.");
		driver.findElement(By.cssSelector(".airOptions .control.checkbox.custom")).click();
		
		Reporter.log("<br>Enviando formulario.");
		driver.findElement(By.cssSelector("input[type='submit'][value='Search Flights'][name='search']")).click();
		
		Reporter.log("<br>Cargando pagina.");
		homePage.pause(15);
	}
}

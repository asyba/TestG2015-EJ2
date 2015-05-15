package federico_bechini.training.globant.com.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class HomePage {


	@FindBy(linkText = "Sign in")
	private WebElement signInButton;
	
	/*
	@FindBy(tagName = "title")
	private WebElement titleTag;

	@FindBy(css = "form[name='f'] input[name='q']")
	private WebElement searchInput;
	
	@FindBy(css = "form[name='f'] input[type='submit']")
	private WebElement submitButtom;
	
*/
	public void go(WebDriver driver){
		driver.get("http://www.cheaptickets.com/");
		//String title = driver.getTitle();
	}
	
	
	public void goToSignPage(){
		signInButton.click();
	}
	public void pause(int s){
		
		try{
			Thread.sleep(s*1000);
		}catch(InterruptedException e){
			System.out.println(e);
		}
	}
}

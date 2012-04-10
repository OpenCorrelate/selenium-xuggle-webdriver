package org.opencorrelate.ci.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.WebDriver;

public interface ITestWebDriver extends WebDriver {
	
	public Keyboard getKeyboard();
	
	public void setDelegateDriver(WebDriver driver);
	
	public WebDriver getDelegateDriver();
	
	public boolean isElementPresent(By by);
	
	public boolean isTextPresent(String text);
	
	public void inputAndValidate(final By invalid, final By valid, String input);

}

package com.ig.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	
	@GetMapping("/")
	public String home() {
		return "truc nguyen";
	}

	@GetMapping("/ig-crawling/{short_code}")
	public List<String> IgCrawling(@PathVariable("short_code")String short_code) {
		
		String developResource = "/develop/chromedriver.exe";
		File file = new File(getClass().getResource(developResource).getFile());
		String path = file.getAbsolutePath();
		
		System.out.println(">> path: " + path);
		
//		open browser
		System.setProperty("webdriver.chrome.driver", path);
		WebDriver driver = new ChromeDriver();
		
//		get instagram page
		driver.get("https://www.instagram.com/");
		
//		login
		WebElement username = new WebDriverWait(driver, 10)
		        .until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='username']")));
		WebElement password = new WebDriverWait(driver, 10)
		        .until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='password']")));

		username.clear();
		password.clear();
		
		username.sendKeys("truc_react");
		password.sendKeys("18112001t");
		
		WebElement login_btn = new WebDriverWait(driver, 10)
		        .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
		login_btn.click();
		
//		ignore save info
		WebElement not_now = new WebDriverWait(driver, 10)
		        .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Not Now')]")));
		not_now.click();
		
		WebElement not_now1 = new WebDriverWait(driver, 10)
		        .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Not Now')]")));
		not_now1.click();
		
//		redirect to the post want to get
		driver.get("https://www.instagram.com/p/" + short_code + "/");
		
//		items count
		List<WebElement> items = driver.findElements(By.className("Yi5aA"));
		int items_count = items.size();
		
		List<String> images_filter = new ArrayList<String>();
		
		if(items_count > 0) {
			
//			click next button
			WebElement next_btn = new WebDriverWait(driver, 10)
			        .until(ExpectedConditions.elementToBeClickable(
			        		By.className("_6CZji")));
			for(int i=0; i<items_count-1; i++) {
				next_btn.click();
			}
			
//			get images
			List<WebElement> images = driver.findElements(By.className("FFVAD"));
			
			for(int i=0; i<items_count; i++) {
				images_filter.add(images.get(i).getAttribute("src"));
			}
			System.out.println("----images_filter len: " + images_filter.size());
		}
		else {
			WebElement image = driver.findElement(By.className("FFVAD"));
			images_filter.add(image.getAttribute("src"));
		}
		
		driver.quit();
		
		return images_filter;
	}
}








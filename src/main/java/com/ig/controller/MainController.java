package com.ig.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class MainController {
	
	@GetMapping("/")
	public String home() {
		return "truc nguyen";
	}
	
	private String os = System.getProperty("os.name").toLowerCase();
	
	private ObjectMapper om = new ObjectMapper();
	
	private JsonNode parse(String src) throws JsonMappingException, JsonProcessingException {
		return om.readTree(src);
	}

	@GetMapping("/ig-crawl/{short_code}")
	@ResponseBody
	public JsonNode IgCrawl(
			@PathVariable("short_code")String short_code) 
			throws JsonMappingException, JsonProcessingException {
		
		System.out.println(">> os: " + os);
		
		if(os.contains("windows"))
			System.setProperty("webdriver.chrome.driver", "develop/chrome/chromedriver.exe");
		else
			System.setProperty("webdriver.chrome.driver", "develop/linux/chromedriver.exe");
		
		
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
		
//		redirect to the post want to get
		driver.get("https://www.instagram.com/p/" + short_code + "/");
		
		// get data
		List<WebElement> scripts = driver.findElements(By.tagName("script"));
		int length = scripts.size();
		
		System.out.println(">> length: " + length);
		
		String data = scripts.get(length-2).getAttribute("innerHTML");
		int startDataIndex = data.indexOf("{");
		
		data = data.substring(startDataIndex, data.length()-2);
		JsonNode node = parse(data);
		
		driver.quit();
		
		return node;
	}
	
	
	@GetMapping("/ig-crawling/{short_code}")
	public List<String> IgCrawling(@PathVariable("short_code")String short_code) {
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\chromedriver.exe");
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
		List<String> videos_filter = new ArrayList<String>();
		
		if(items_count > 0) {
			
//		click next button
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








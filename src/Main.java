import org.openqa.selenium.By;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.border.TitledBorder;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.openqa.selenium.*;

public class Main {
	public static WebDriver driver;
	public static void main(String[] argv) throws IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "F:\\codes\\java\\selenium-jars\\chromedriver.exe");
		//System.setProperty("webdriver.firefox.bin", "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");  
		//DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		//capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		driver = new ChromeDriver();
		
		driver.get("http://tkkc.hfut.edu.cn");
		
		driver.findElement(By.id("logname")).sendKeys("ѧ��");
		driver.findElement(By.id("password")).sendKeys("����");
		
		System.out.println("��������֤��");
		Scanner scanner = new Scanner(System.in);
		String randomCode = scanner.next();
		driver.findElement(By.id("randomCode")).sendKeys(randomCode);
		driver.findElement(By.id("loginBtn")).click();
		
		
		
		System.out.println("������ok��excel�ļ���ſ�ʼ����");
		String state = scanner.next();
		int filNum = scanner.nextInt();
		while(state.equals("ok")) {
			
			
			System.out.println("ȷ���ѽ�������ҳ�棬���뿪ʼ���");
			int num = scanner.nextInt();
			java.util.List<WebElement> liEle = driver.findElement(By.className("quickChangeBtn")).findElements(By.tagName("li"));
			while(num <= liEle.size()) {
				
				try {
					WebElement next = driver.findElement(By.id("examStudentExerciseSerialBtn" + String.valueOf(num - 1)));	
					Actions actions = new Actions(driver);
					actions.moveToElement(next).click().perform();
					
					String title = "";	
					String anwser = null;
					WebElement choose = null;
					String exerciseType = driver.findElement(By.id("Exercise_Type")).getText();
					if(exerciseType.equals("��ѡ��")) {
						title = driver.findElement(By.id("D_title")).getText();
						title = title.replace("(", "");
						title = title.replace(")", "");
						title = title.replace("��", "");
						title = title.replace("��", "");
						title = title.replace(" ", "");
						anwser = getKey(title,0,0,filNum);
						
						if(anwser.equals("")){
							System.out.println(num + "δ�ҵ�");
						}else {
							choose = driver.findElement(By.id("D_" + anwser));
							choose.click();
						}
						
					}else if(exerciseType.equals("�ж���")){
						title = driver.findElement(By.id("P_title")).getText();
						title = title.replace(" ", "");
						//anwser = getKey(title,1,1,filNum);//�����
						anwser = getKey(title,1,2,filNum);//����������
						
						if (anwser.equals("")) {
							System.out.println(num + "δ�ҵ�");
						}else {
							if(anwser.equals("��ȷ")){
								choose = driver.findElement(By.id("P_A"));
								choose.click();
							}else if(anwser.equals("����")){
								choose = driver.findElement(By.id("P_B"));
								choose.click();
							}
						}
					}else if(exerciseType.equals("��ѡ��")){
						title = driver.findElement(By.id("Duo_title")).getText();
						title = title.replace("(", "");
						title = title.replace(")", "");
						title = title.replace("��", "");
						title = title.replace("��", "");
						title = title.replace(" ", "");
						anwser = getKey(title,2,1,filNum);
						
						
						if(anwser.equals("")){
							System.out.println(num + "δ�ҵ�");
						}else {
							String keyArray[] = anwser.split(",");
							for(int i = 0; i < keyArray.length;i++)
							{
								choose = driver.findElement(By.id("Duo_" + keyArray[i]));
								choose.click();
							}
							
						}
					}
					
					num++;
				} catch (Exception e) {
					System.out.println("������");
					System.out.println(e.toString());
					//num = num + 1;
					driver.navigate().refresh();
					java.util.concurrent.TimeUnit.MILLISECONDS.sleep(100);
				}
				
			}
			
		}
			
	}
	
	public static String getKey(String title, int exerciseType,int sheetNum,int fileNum) throws IOException
	{
		File file = new File("C:\\Users\\nanmu\\Desktop\\doc\\exercise" + String.valueOf(fileNum) + ".xls");
		FileInputStream is = null;
		HSSFSheet sheet = null;
		is = new FileInputStream(file);
		HSSFWorkbook wbs = new HSSFWorkbook(is);
        sheet = wbs.getSheetAt(sheetNum);
        
        for (int i = 0, num = sheet.getLastRowNum(); i < num; i++) {
        	HSSFRow row = sheet.getRow(i);
            String scanTitle = row.getCell(0).getStringCellValue(); 
            scanTitle = scanTitle.replace("(", "");
            scanTitle = scanTitle.replace(")", "");
            scanTitle = scanTitle.replace("��", "");
            scanTitle = scanTitle.replace("��", "");
            scanTitle = scanTitle.replace(" ", "");
            if(scanTitle.equals(title)) {
            	//System.out.println(row.getCell(7).getStringCellValue());
            	if(exerciseType == 0) {
            		return row.getCell(7).getStringCellValue(); 
            	}else if(exerciseType == 1){
					return row.getCell(2).getStringCellValue();
				}else {
					return row.getCell(7).getStringCellValue();
				}
            	
            }
        }
        
		return "";
	}
}

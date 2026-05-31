#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);

// --- 1. CẤU HÌNH WIFI VÀ SERVER (EM PHẢI SỬA PHẦN NÀY) ---
const char* ssid = "T";          // Thay bằng tên WiFi
const char* password = "0987654321";        // Thay bằng mật khẩu WiFi
const char* serverIP = "10.180.198.98";         // Thay bằng IPv4 của Laptop em vừa tìm được
const int serverPort = 8080;                   // Cổng của Tomcat/GlassFish (Thường là 8080)
// Thay "TenProjectCuaEm" bằng đúng tên đường dẫn trên NetBeans
const String apiPath = "/Group-4-Project-SpeedTrap/api/nhan-du-lieu"; 

// Khai báo chân (Giữ nguyên như phần cứng em đã làm)
const int startSensorPin = 14; 
const int endSensorPin = 12;   
const int greenLedPin = 16;    
const int redLedPin = 15;      
const int buzzerPin = 13;      

unsigned long startTime = 0;
unsigned long endTime = 0;
float distance = 0.2;     
float speedLimit = 5.0;   

void setup() {
  Serial.begin(115200);

  pinMode(startSensorPin, INPUT);
  pinMode(endSensorPin, INPUT);
  pinMode(greenLedPin, OUTPUT);
  pinMode(redLedPin, OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  digitalWrite(greenLedPin, LOW);
  digitalWrite(redLedPin, LOW);
  noTone(buzzerPin);

  lcd.init();
  lcd.backlight();
  
  // --- 2. KẾT NỐI WIFI ---
  lcd.setCursor(0, 0);
  lcd.print("Connecting WiFi");
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  // Chờ cho đến khi kết nối thành công
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println("\nWiFi connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("WiFi Connected!");
  delay(2000); 
  
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Waiting...      ");
}

void loop() {
  while (digitalRead(startSensorPin) == HIGH) { 
  }
  startTime = millis(); 

  while (digitalRead(endSensorPin) == HIGH) { 
  }
  endTime = millis(); 
  
  float duration = (endTime - startTime) / 1000.0; 
  if (duration <= 0.0) duration = 0.01; 
  
  float speed = (distance / duration) * 3.6; 
  
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Spd: ");
  lcd.print(speed, 1); 
  lcd.print(" km/h");
  lcd.setCursor(0, 1);
  
  if (speed <= speedLimit) {
    lcd.print("Normal Speed");
    digitalWrite(greenLedPin, HIGH); 
    digitalWrite(redLedPin, LOW);    
  } else {
    lcd.print("OVERSPEED!  ");
    digitalWrite(greenLedPin, LOW);  
    digitalWrite(redLedPin, HIGH);   
    tone(buzzerPin, 1000); 
  }
    
    // --- 3. BẮN DỮ LIỆU QUA WIFI (HTTP GET) ---
    // --- 3. BẮN DỮ LIỆU QUA WIFI CHO ESP8266 ---
    if(WiFi.status() == WL_CONNECTED) {
      WiFiClient client;  // <--- Khác ESP32 ở điểm phải tạo thêm biến này
      HTTPClient http;
      
      String serverPath = "http://" + String(serverIP) + ":" + String(serverPort) + apiPath + "?station=STATION_CE201665&speed=" + String(speed);
      Serial.println("Goi API: " + serverPath);
      
      // Mở kết nối, nhớ phải có biến client nhét vào hàm begin
      http.begin(client, serverPath); 
      
      int httpResponseCode = http.GET(); 
      if (httpResponseCode > 0) {
        Serial.print("HTTP Response code: ");
        Serial.println(httpResponseCode);
        String payload = http.getString();
        Serial.println("Server tra loi: " + payload); 
      } else {
        Serial.print("Loi goi API. Ma loi: ");
        Serial.println(httpResponseCode);
      }
      http.end(); 
    } else {
      Serial.println("Mat ket noi WiFi!");
    }
  
  delay(4000);
  
  digitalWrite(greenLedPin, LOW);
  digitalWrite(redLedPin, LOW);
  noTone(buzzerPin);
  
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Waiting...      ");
}
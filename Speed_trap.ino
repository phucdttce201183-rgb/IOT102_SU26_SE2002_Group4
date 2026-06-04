#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);

// --- 1. CẤU HÌNH WIFI VÀ SERVER ---
const char* ssid = "T";
const char* password = "0987654321";
const char* serverIP = "10.180.198.98";
const int serverPort = 8080;
const String apiPath = "/Group-4-Project-SpeedTrap/api/speedcheck";

const int startSensorPin = 14;
const int endSensorPin = 12;
const int greenLedPin = 16;
const int redLedPin = 15;
const int buzzerPin = 13;

unsigned long startTime = 0;
unsigned long endTime = 0;
float distance = 0.2;

void setup() {
  Serial.begin(115200);

  pinMode(startSensorPin, INPUT);
  pinMode(endSensorPin, INPUT);
  pinMode(greenLedPin, OUTPUT);
  pinMode(redLedPin, OUTPUT);
  pinMode(buzzerPin, OUTPUT);

  // Tắt toàn bộ đèn còi lúc khởi động
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

  // Hiển thị tốc độ đo được, chờ Server quyết định
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Spd: ");
  lcd.print(speed, 1);
  lcd.print(" km/h");
  lcd.setCursor(0, 1);
  lcd.print("Checking DB...");

  // --- 3. BẮN DỮ LIỆU QUA WIFI (HTTP POST) ---
  if(WiFi.status() == WL_CONNECTED) {
    WiFiClient client;
    HTTPClient http;

    String serverPath = "http://" + String(serverIP) + ":" + String(serverPort) + apiPath;

    http.begin(client, serverPath);
    http.addHeader("Content-Type", "text/plain");

    // Gửi ID đường = 1 và tốc độ
    String payload = "1|" + String(speed);
    Serial.println("Goi API POST: " + serverPath + " | Data: " + payload);

    int httpResponseCode = http.POST(payload);

    if (httpResponseCode > 0) {
      String responseText = http.getString();
      Serial.println("Server tra loi: " + responseText);

      // CHỈ CẢNH BÁO KHI SERVER TRẢ VỀ "WARNING"
      lcd.setCursor(0, 1);
      if (responseText == "WARNING") {
           lcd.print("! OVERSPEED !   ");
           digitalWrite(greenLedPin, LOW);
           digitalWrite(redLedPin, HIGH);
           tone(buzzerPin, 1000);
           delay(2000); // Kêu 2 giây
      } else if (responseText == "SAFE") {
           lcd.print("Normal Speed    ");
           digitalWrite(redLedPin, LOW);
           digitalWrite(greenLedPin, HIGH);
           noTone(buzzerPin);
      }
    } else {
      Serial.print("Loi goi API. Ma loi: ");
      Serial.println(httpResponseCode);
      lcd.setCursor(0, 1);
      lcd.print("API Error!      ");
    }
    http.end();
  } else {
    Serial.println("Mat ket noi WiFi!");
  }

  // Thời gian chờ để xe đi qua hẳn (tránh đo lặp)
  delay(3000);

  // Reset trạng thái về ban đầu
  digitalWrite(greenLedPin, LOW);
  digitalWrite(redLedPin, LOW);
  noTone(buzzerPin);

  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Waiting...      ");
}

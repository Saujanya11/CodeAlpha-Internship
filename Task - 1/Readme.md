# AI Language Translation Tool

## 📌 About the Project
This is a **Language Translation Tool** developed as part of the **Artificial Intelligence Internship at CodeAlpha**. The tool utilizes **Java** along with the **LibreTranslate API** (self-hosted via Docker) to translate text between multiple languages using neural machine translation.

## 🚀 Features
- Supports multiple languages (Hindi, English, Spanish, French, German, etc.)
- User-friendly **Command Line Interface (CLI)**
- Error handling for API failures and connection issues
- Uses **LibreTranslate API** for translations

## 🛠️ Tech Stack
- **Java** (Backend logic)
- **LibreTranslate API** (Self-hosted using Docker)
- **JSON** (For API response parsing)

---

## 🔧 Installation & Setup

### 1️⃣ Install Required Software
Before running the project, ensure you have the following installed:
- **Java (JDK 8 or higher)** → [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Docker** → [Download Docker](https://www.docker.com/get-started)

### 2️⃣ Clone the Repository
```sh
 git clone https://github.com/Saujanya11/CodeAlpha-Internship.git
 cd CodeAlpha-Internship
```

### 3️⃣ Run LibreTranslate API Using Docker
LibreTranslate requires a local server to function. Run the following command to start it in a Docker container:
```sh
docker run -d -p 5000:5000 --name libretranslate ghcr.io/uav4geo/libretranslate:latest
```
This will run LibreTranslate on `http://localhost:5000`.

### 4️⃣ Compile and Run the Java Program
```sh
javac -d . src/com/saujanya/translation/SimpleAITranslator.java
java com.saujanya.translation.SimpleAITranslator
```

---

## 💡 How to Use
1. Select the **source language** (e.g., English, Hindi, Spanish, etc.)
2. Select the **target language**
3. Enter the text you want to translate
4. View the translated text!

---

## ❗ Troubleshooting
### If LibreTranslate API is not working:
- Make sure Docker is running properly
- Restart the container using:
  ```sh
  docker restart libretranslate
  ```

### If Java program gives errors:
- Ensure JDK is properly installed
- Verify that LibreTranslate API is running (`http://localhost:5000` should be accessible)

---

## 📌 Project Repository
💻 **GitHub Repository:** [CodeAlpha-Internship](https://github.com/Saujanya11/CodeAlpha-Internship.git)

Feel free to contribute or raise issues! 🚀

---

## 📢 Connect With Me
🔗 **LinkedIn:** [Saujanya Mishrikotkar](https://www.linkedin.com/in/saujanya-mishrikotkar-663832259/)  
📧 **Email:** saujanyamishrikotkar65@gmail.com  
🔗 **GitHub:** [Profile Link](https://github.com/Saujanya11)

#ArtificialIntelligence #Java #LibreTranslate #Docker #CodeAlpha #MachineTranslation #Internship

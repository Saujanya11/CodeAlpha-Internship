# 🤖 FAQ Chatbot

## 🌟 Project Overview
Welcome to the **AI-powered FAQ Chatbot**, developed as part of **Task 2** for the **CodeAlpha Artificial Intelligence Internship**. This chatbot leverages **Natural Language Processing (NLP)** to understand and respond to user queries effectively.

## 🚀 Features
✅ **Stanford NLP Integration** for advanced text analysis  
✅ **Jaccard Similarity Algorithm** for question matching  
✅ **Customizable FAQ Database** using JSON  
✅ **Intelligent Greetings Detection** for a friendly interaction  
✅ **Robust Error Handling** for seamless conversations  

---

## 🛠️ Technologies Used
🔹 **Java** (Core Development)  
🔹 **Stanford NLP** (Natural Language Processing)  
🔹 **JSON** (FAQ Data Storage)  

---

## 📥 Installation & Setup
### 🔧 Prerequisites
Before running the chatbot, ensure you have:
✔ **Java (JDK 8 or later)** installed  
✔ **Stanford NLP Library** added to the classpath  

### 📌 Steps to Run
1️⃣ **Clone the repository**:
   ```sh
   git clone https://github.com/Saujanya11/faq-chatbot.git
   cd faq-chatbot
   ```
2️⃣ **Ensure the FAQ JSON file is structured properly (Example below).**
3️⃣ **Compile and Run the Chatbot**:
   ```sh
   javac -cp ".;stanford-corenlp-4.5.0.jar" com/saujanya/chatbot/FAQChatbot.java
   java -cp ".;stanford-corenlp-4.5.0.jar" com.saujanya.chatbot.FAQChatbot
   ```

---

## 📁 FAQ JSON Format
Your `faqs.json` should follow this structure:
```json
{
  "faqs": [
    {"question": "What is your name?", "answer": "I'm an FAQ Chatbot."},
    {"question": "How do you work?", "answer": "I analyze keywords to provide relevant answers."}
  ]
}
```

---

## 💬 How to Use
🟢 **Step 1**: Run the chatbot.  
🟢 **Step 2**: Ask a question from the FAQ database.  
🟢 **Step 3**: Get an accurate response based on keyword matching.  
🟢 **Step 4**: Type `exit` to close the chat.  

---

## ✨ Example Interaction
```
You: What is your name?
Bot: I'm an FAQ Chatbot.

You: How do you work?
Bot: I analyze keywords to provide relevant answers.
```

---

## 🤝 Contributing
Contributions are welcome! Feel free to **fork** the repository and submit **pull requests** with improvements.  

---

## 📜 License
📝 This project is **open-source** and available under the **MIT License**.  

💡 _Happy Coding!_ 🎯

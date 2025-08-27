# 💰 Finance Tracker AI App

A personal expense management mobile application developed in **Java** using **Android Studio**. The app helps users manage daily expenses, visualize spending, and predict future expenses using **AI (TensorFlow Lite)**.


## 🚀 Features
- Add, view, and delete expenses  
- Categorize expenses with description and date  
- Total and category-wise visualization using Pie Charts  
- Dark-themed UI with lime green highlights  
- AI module to predict **future spending trends**  
- Local **SQLite Database** for data persistence  

---

## 🧠 Technologies Used
- **Android Studio**  
- **Java**  
- **XML (Layouts & UI)**  
- **SQLite Database**  
- **MPAndroidChart** (for pie chart visualization)  
- **TensorFlow Lite** (for expense prediction)  

---

## 📂 App Structure
- `MainActivity` → Dashboard & Pie Chart visualization  
- `AddExpenseActivity` → Add new expenses  
- `ExpenseListActivity` → View & manage saved expenses  
- `PredictionActivity` → AI-based expense prediction  
- `DatabaseHelper` → Handles SQLite CRUD operations  
- `ExpenseAdapter` → Custom adapter for ListView  

---


## 🧾 How It Works
1. Users add expenses with details (amount, category, description, date).  
2. Expenses are stored locally in **SQLite**.  
3. Pie Chart provides a visual breakdown of spending by category.  
4. AI predictor estimates future expenses based on past trends.  

---

## 🛠️ Installation
1. Clone this repository  
2. Open project in **Android Studio**  
3. Build & run on an emulator or physical Android device  

---

## 📘 Learning Outcomes
- Activity Lifecycle & Navigation in Android  
- UI/UX Design with Material Theme  
- SQLite database integration  
- Using adapters with ListView  
- Integrating TensorFlow Lite models into Android apps  

---

## 📃 License
This project is for academic purposes (semester project).  

# ⏰ ClockMate

**ClockMate** is a full-stack employee attendance tracking system that allows users to **clock in/out using a PIN**, **view attendance sessions**, and **export logs to Excel**. It is built using **Spring Boot** for the backend and **React + Tailwind CSS** for the frontend.

---

## 🌐 Live Preview

> _Coming soon: host the frontend (e.g., Netlify) and backend (e.g., Render/Heroku)_

---

## 🛠️ Tech Stack

| Layer       | Tech Used                             |
|-------------|----------------------------------------|
| Frontend    | React, Tailwind CSS, Axios, React Router |
| Backend     | Spring Boot, Java, Maven               |
| Database    | PostgreSQL                             |
| Utilities   | Apache POI (Excel Export), Git         |

---

## 📁 Project Structure

```
ClockMate/
├── backend/                 # Spring Boot backend code
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   ├── service/
│   └── ...
├── frontend/                # React frontend code
│   ├── components/
│   ├── pages/
│   └── ...
├── README.md
└── ...
```

---

## 📦 Installation

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/huzefa897/ClockMate.git
cd ClockMate
```

### 2️⃣ Setup Backend

```bash
cd backend
# Make sure PostgreSQL is running and DB is configured in application.properties
./mvnw spring-boot:run
```

### 3️⃣ Setup Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## ✅ API Endpoints

### 🔒 POST - Clock In/Out

```
POST /attendance/record?pin=1234
```

> Clock in or out based on current status using the PIN.

---

### 📍 GET - Clock-In/Out Status

```
GET /attendance/1234/status
```

> Returns `Clocked In` or `Clocked Out`.

---

### 🧾 GET - Raw Timestamps

```
GET /attendance/1234/timestamps
```

> Fetches raw timestamps for the employee with that PIN.

---

### 📊 GET - Sessions Between Dates

```
GET /attendance/1234/sessions?from=2024-01-01&to=2024-12-31
```

> Retrieves paired clock-in/out sessions with duration breakdown.

---

### 📤 GET - Export Full Session Log

```
GET /attendance/export?pin=1234
```

> Downloads an Excel file containing all sessions.

---

### 🗓️ GET - Export Session Log by Date Range

```
GET /attendance/export-range?pin=1234&from=2024-01-01&to=2024-12-31
```

> Downloads an Excel file for the selected date range.

---

## 🧠 Features

- 🔐 PIN-based login (no password needed)
- 📊 Dynamic session calculation
- 📈 Excel export support via Apache POI
- 🖥️ Intuitive UI with React + Tailwind
- 💡 No need to remember user IDs — everything works via PIN

---

## 🎯 Future Improvements

- Session analytics (charts)
- Admin dashboard
- Export PDF reports
- OTP login / password-less auth
- Hosting + CI/CD pipeline

---

## 👤 Author

**Huzefa Taher Saleem**  
Feel free to contribute or fork the project.  
📧 [Email](mailto:huzefa897@gmail.com) | 🌐 [GitHub](https://github.com/huzefa897)

---

## 📄 License

This project is licensed under the MIT License.
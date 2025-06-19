⏰ ClockMate

ClockMate is a time-tracking web application designed to help businesses and employees manage clock-in/clock-out sessions efficiently. Built with a Spring Boot backend and a React + Tailwind CSS frontend, ClockMate supports session tracking, timesheet reporting, and employee management.

🚀 Features

Clock-in and clock-out system per user

View complete attendance history

Calculate total working hours and session duration

Export session data to Excel

Modern responsive UI with Tailwind CSS

Material UI integration for rich UI components

💠 Tech Stack

Backend

Java 21 + Spring Boot

Spring Web, Spring Data JPA

PostgreSQL (or H2 for local testing)

Maven

Frontend

React 19

Tailwind CSS

Material UI

Vite

🧪 Running the Project

Backend

cd backend
./mvnw spring-boot:run

Frontend

cd frontend
npm install
npm run dev

📋 Folder Structure

/clockmate
├── backend/
│   └── src/main/java/com/clockmate/...
├── frontend/
│   └── src/
│       └── components/
│       └── pages/

📋 API Endpoints

Method

Endpoint

Description

POST

/api/clock-in

Clock in a user

POST

/api/clock-out

Clock out a user

GET

/api/{userId}/sessions

Get session list for a user

GET

/api/{userId}/status

Check current clock-in status

GET

/api/export/excel

Export all sessions to Excel

📦 Deployment

You can deploy ClockMate using:

Render / Railway / Fly.io for backend

Vercel / Netlify for frontend

🙌 Contributing

Open to improvements and bug fixes! Create an issue or fork the repo and make a PR.

🌐 Additional Info

Environment Variables (Sample)

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/clockmate
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

License

MIT License — Feel free to use, modify, and share.


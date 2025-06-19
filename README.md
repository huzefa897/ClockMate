# ClockMate

ClockMate is a time-tracking web application designed to help individuals and businesses manage attendance efficiently. It offers features like clock in/out, session history, attendance logs, and data export to Excel.

## Features

- ✅ **User Clock In/Out**: Track start and end times of work sessions.
- 📅 **Session History**: View detailed session data with timestamps.
- ⏱️ **Duration Calculation**: Automatically calculates total hours and minutes per session.
- 📤 **Excel Export**: Export attendance records between specific dates.
- 🌐 **Responsive UI**: Built using React and Tailwind CSS for a modern, user-friendly experience.
- 🔐 **Login/Register (WIP)**: Planned authentication features for user access control.

## Tech Stack

### Frontend
- **React 19**
- **Tailwind CSS**
- **React Router DOM**
- **Material UI**

### Backend
- **Spring Boot 3**
- **Java 21**
- **JPA (Hibernate)**
- **H2 / PostgreSQL** (configurable)

## Setup Instructions

### Backend (Spring Boot)
```bash
# Navigate to backend directory
cd backend

# Build and run the app (e.g. via IntelliJ or CLI)
./mvnw spring-boot:run
```

### Frontend (React)
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

## API Endpoints (Sample)

- `POST /clockin/{userId}` – Clock in a user
- `POST /clockout/{userId}` – Clock out a user
- `GET /{userId}/sessions` – Get all sessions
- `GET /status/{userId}` – Get user's current clock-in/clock-out status
- `GET /export?from={date}&to={date}` – Export Excel for given date range

## Folder Structure

```
ClockMate/
├── backend/
│   ├── model/
│   ├── controller/
│   ├── repository/
│   └── service/
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── assets/
│   │   ├── App.jsx
│   │   └── index.css
```

## Credits
- Developed by [Huzefa Taher Saleem](https://github.com/huzefa897)

## License
This project is licensed under the MIT License.

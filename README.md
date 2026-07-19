# 🛡️ EduShield

**A full-stack student dropout-risk detection and scholarship-matching platform.**

EduShield helps educational institutions identify at-risk students early — before problems become irreversible — by combining attendance, academic performance, and fee-payment data into a weighted risk score. At-risk students are then automatically matched with scholarships they're eligible for, turning a warning sign into an actionable solution.

---

## 🚀 Live Demo

- **Frontend:** [edu-shield-six.vercel.app](https://edu-shield-six.vercel.app)
- **Backend API:** [edushield-backend-4yn6.onrender.com](https://edushield-backend-4yn6.onrender.com)

> ⚠️ **Note:** This project is deployed on free-tier hosting (Render + Aiven). The backend and database may take **30–60 seconds** to "wake up" if inactive. Please be patient on first load.

---

## ✨ Features

### 🎯 Risk Scoring Engine
- Weighted formula combining **attendance (40%)**, **academic marks (35%)**, and **fee payment status (25%)**
- Automatically classifies students as **LOW / MEDIUM / HIGH** risk
- Full historical tracking — every computation is stored, powering trend charts over time

### 🎓 Scholarship Matching
- Rule-based matching engine that cross-references a student's income, category, marks, and state against available scholarships
- At-risk students are automatically shown scholarships they're eligible for — closing the loop between "flagged" and "helped"

### 👥 Role-Based Dashboards
- **Admin** — manage scholarships, create user accounts, bulk-upload data via CSV
- **Mentor** — view assigned students with risk levels, drill into individual trend charts, log interventions
- **Student** — view own academic status, attendance, and matched scholarships in a supportive, non-alarming format

### 📊 Bulk CSV Data Tools
Admins can bulk-upload via CSV instead of manual entry, one-by-one:
- Students (with optional mentor assignment)
- Mentors
- Scholarships
- Attendance records
- Marks / assessments
- Fee status records

### 🔐 Authentication & Security
- JWT-based stateless authentication
- Role-based access control (Spring Security)
- BCrypt password hashing
- Admin-provisioned accounts (no public self-registration, by design — see [Design Decisions](#-design-decisions))

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Frontend** | React (Vite), Tailwind CSS, React Router, Axios, Recharts, Framer Motion, Lucide Icons |
| **Backend** | Java, Spring Boot, Spring Security, Spring Data JPA (Hibernate) |
| **Database** | MySQL (local dev), Aiven MySQL (cloud) |
| **Auth** | JWT (JJWT library) |
| **Deployment** | Vercel (frontend), Render + Docker (backend), Aiven (database) |

---

## 🏗️ Architecture

```
[ React Frontend ]
        |
        |  REST API (JWT Auth)
        v
[ Spring Boot Backend ]
        |
        |  JDBC
        v
[ MySQL Database (Aiven) ]
```

- **Frontend** communicates with the backend exclusively via REST API, authenticated with a JWT stored client-side
- **Backend** exposes role-protected REST endpoints, computes risk scores on-demand, and handles CSV parsing for bulk imports
- **Database** stores all persistent data — users, students, mentors, scholarships, attendance, marks, fees, risk history, and intervention logs

---

## 📁 Project Structure

```
edushield/
├── backend/                   # Spring Boot application
│   ├── src/main/java/com/edushield/backend/
│   │   ├── config/             # Security & CORS configuration
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                 # Request/response DTOs
│   │   ├── entity/               # JPA entities
│   │   ├── repository/          # Spring Data repositories
│   │   ├── security/             # JWT filter & utilities
│   │   └── service/               # Business logic
│   ├── src/main/resources/
│   │   └── application.properties
│   └── Dockerfile
│
├── frontend/                  # React application
│   ├── src/
│   │   ├── api/                  # Axios client
│   │   ├── components/          # Shared components (Navbar, ProtectedRoute)
│   │   ├── context/               # Auth context
│   │   └── pages/                  # Login, Landing, Admin/Mentor/Student dashboards
│   └── vercel.json
│
└── README.md
```

---

## ⚙️ Local Setup

### Prerequisites
- Java 21+ (JDK)
- Node.js 18+
- MySQL Server (local instance)
- Maven (or use the included `mvnw` wrapper)

### Backend Setup

```bash
cd backend

# Set required environment variable (local MySQL root password)
# Windows PowerShell:
$env:DB_PASSWORD="your_local_mysql_root_password"
# macOS/Linux:
export DB_PASSWORD="your_local_mysql_root_password"

./mvnw spring-boot:run
```

The backend will start on `http://localhost:8081` and auto-create the database schema (`edushield_db`) via Hibernate `ddl-auto=update`.

**Environment variables** (all have local fallback defaults except password):
| Variable | Default (local) | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/edushield_db?...` | Database connection URL |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | *(required, no default)* | Database password |

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173` and proxy API calls to `http://localhost:8081/api` by default (configurable via `VITE_API_URL` environment variable for production builds).

---

## 🔑 Default Test Accounts

After setup, register accounts via the Admin Dashboard's "Create User Account" or bulk CSV upload feature. Example roles: `ADMIN`, `MENTOR`, `STUDENT`.

---

## 📄 CSV Bulk Upload Formats

| Upload Type | Columns |
|---|---|
| **Students** | `username,password,name,rollNo,department,year,category,annualIncome,state,mentorUsername (optional)` |
| **Mentors** | `username,password,name,department` |
| **Scholarships** | `name,minIncome,maxIncome,category,minMarks,state,deadline,description` |
| **Attendance** | `rollNo,month,year,totalDays,presentDays` |
| **Marks** | `rollNo,subject,assessmentType,score,maxScore,assessmentDate` |
| **Fee Status** | `rollNo,semester,amountDue,dueDate,paid (true/false)` |

---

## 🧠 Risk Scoring Formula

```
riskScore = (0.40 × attendanceRiskFactor) 
          + (0.35 × marksRiskFactor) 
          + (0.25 × feeRiskFactor)

Where each factor is normalized 0–1 based on thresholds:
- Attendance risk triggers below 75%
- Marks risk triggers below 50%
- Fee risk is binary: 1 if overdue & unpaid, else 0

Final score (0–100 scale) is classified as:
- HIGH risk   ≥ 60
- MEDIUM risk ≥ 30
- LOW risk    < 30
```

New students with no attendance/marks data yet default to a "safe" baseline (100%) to avoid false-positive flagging before any real data exists.

---

## 🎨 Design Decisions

**Why admin-provisioned accounts instead of public self-registration?**
In a real institutional setting, sensitive data like income and category typically requires verification by the institution — not self-declaration. Admin-controlled account creation also prevents spam/fake accounts and keeps risk-scoring data trustworthy. Bulk CSV upload was added specifically to make this scale painlessly for large batches of students.

**Why is risk framed differently for students vs. mentors?**
Mentors see raw risk levels (LOW/MEDIUM/HIGH) to make informed intervention decisions. Students instead see supportive, encouraging messaging ("You're doing great, keep it up!") rather than a stark risk label — this avoids discouraging students who are already vulnerable.

---

## 🚧 Known Limitations / Future Improvements

- Free-tier hosting (Render + Aiven) may experience cold-start delays or occasional downtime
- No email notifications for high-risk flags yet
- No file/document upload for scholarship application proof
- Mentor-student assignment via CSV is one-directional (student → mentor); no drag-and-drop reassignment UI yet
- Password reset / forgot-password flow not yet implemented

---


## 📜 License

This project is for academic/educational purposes.

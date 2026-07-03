# Aetheris Banking & Accounting Core

A comprehensive, bank-grade Java + Spring Boot accounting and core ledger platform with role-based maker-checker approvals, automated AML compliance rules, immutable audit logging, credit facility scheduling, and a professional corporate banking light theme interface.

GitHub Repository: [https://github.com/AtharvaDhobale/aetheris-banking](https://github.com/AtharvaDhobale/aetheris-banking)

---

## 🏦 Key Features & Dual-Control Banking Protocols

- **Dual-Control Maker-Checker Approval System**: General clerks and employees queue up invoices, journal vouchers, and vendor payments into a pending approval stream. Designated administrators or Chartered Accountants (CAs) review, authorize, or reject them.
- **Anti-Money Laundering (AML) Compliance Engine**: Real-time screening flags high-value transactions (>= 500,000 INR) as `FLAGGED_HIGH_RISK`, instantly alerting compliance officers and locking the transaction pending manual override.
- **Immutable Audit Logging**: Every transaction, login, and authorization action automatically records security telemetry, capturing user authorities, client IP addresses, browser User-Agent details, and risk flags.
- **Credit Lines & Loan Amortization**: Sanction business loan limits and automatically generate reducing-balance amortization schedules (EMI, interest component, principal split, and outstanding principal).
- **Interactive Bank Reconciliation**: Match internal bank ledger entries with simulated external bank statements visually with reconciliation difference calculations.
- **Professional Light Banking Interface**: Re-themed with a modern white and blue banking layout, Outfit/Space Grotesk typography, responsive sidebar navigation, clean forms, and custom CSS layout transitions.
- **Tax Calendar & Reminders**: Track statutory GST filing, advance income tax, and compliance due dates with active countdowns and penalty estimations.
- **CA Knowledge Base**: Searchable resource repository containing standard banking tax rules, formulas, and accounting standard references.

---

## 💻 Tech Stack
- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA
- **Database**: H2 In-Memory Database (configured for zero-setup execution; no Docker, PostgreSQL, or MongoDB required to start)
- **Frontend**: HTML5, CSS3 (Custom Light Banking Theme), JavaScript, Thymeleaf
- **Build Tool**: Maven

---

## 🚀 Run Locally
1. Ensure you have **Java 17+** and **Maven** installed.
2. Clone or download the repository.
3. Open your terminal in the project directory and run:
   ```bash
   mvn spring-boot:run
   ```
4. Once started, access the application in your browser:
   - **Main Core Terminal**: `http://localhost:8080/dashboard`
   - **H2 Database Console**: `http://localhost:8080/h2-console`
     - *JDBC URL*: `jdbc:h2:mem:accounting_db`
     - *Username*: `sa`
     - *Password*: (leave empty)

### Secure Credentials
- **Administrator**: `admin` / `admin123`
- **Accountant / Clerk**: `employee` / `employee123`
- **CA / Auditor**: `causer` / `ca123`

---

## ☁️ Deployment Guidelines

Since this is a full-featured Java Spring Boot application (requiring a persistent JVM process and relational database context), it is not compatible with frontend-only hosting services like Vercel. 

### Render (Recommended & Free)
Render compiles and deploys Spring Boot applications using the repository's `Dockerfile`:
1. Log in to [Render](https://render.com).
2. Click **New +** and choose **Web Service**.
3. Connect your GitHub repository: `https://github.com/AtharvaDhobale/aetheris-banking.git`.
4. Render will build the container from the root `Dockerfile` and publish the app at a custom `.onrender.com` domain.

### Railway (Instant Git Deployment)
1. Log in to [Railway](https://railway.app).
2. Click **New Project** and select **Deploy from GitHub**.
3. Choose the `aetheris-banking` repository.
4. Railway will automatically detect Maven, build the project, and provision a public URL.

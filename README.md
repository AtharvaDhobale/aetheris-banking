# 🏦 Aetheris Banking Core — Secure Digital System for Enterprise Ledger Management

[![Live Demo](https://img.shields.io/badge/Live_Demo-View_App-success?style=for-the-badge&logo=vercel&logoColor=white&color=0052cc)](https://aetheris-banking.vercel.app/)
[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-HTML5-005F00?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![Database](https://img.shields.io/badge/Database-H2_Embedded-blue?style=for-the-badge&logo=databricks&logoColor=white)](https://www.h2database.com/)

---

### 🌐 Live Deployment
> [!IMPORTANT]
> **Experience the live deployment portal here:** **[https://aetheris-banking.vercel.app/](https://aetheris-banking.vercel.app/)**
> 
> *The static core gateway is deployed on **Vercel** with full platform feature specifications and links. The local server instance operates locally on port `8080` (with H2 database structures).*

---

A production-ready, bank-grade core accounting and ledger platform architected to manage enterprise-level transaction records, vault cashier operations, and financial auditing. Engineered with double-entry ledger bookkeeping, this system leverages **Spring Boot**, **Spring Security**, and an embedded **H2 Database** context to deliver a secure, zero-dependency banking infrastructure.

## 🚀 Project Overview

The financial sector demands zero-tolerance data integrity, rigorous audit telemetry, and dual-control security. **Aetheris Banking Core** addresses operational risks in corporate banking by enforcing maker-checker segregation, automated anti-money laundering (AML) checks, and cash denomination vault audits. 

As the **Project Lead**, I spearheaded the system design, RESTful API architecture, JPA schema seeding, and the premium white-blue corporate interface overhaul.

## 🛠️ Technology Stack & Architecture

- **Backend Architecture:** Java 17, Spring Boot 3.2, Spring Security (Stateless Authorization), Maven
- **Database & Persistence:** H2 In-Memory Relational Engine (Zero setup overhead)
- **Frontend Ecosystem:** Thymeleaf Server-Side Templates, Vanilla JavaScript (ES6+), FontAwesome Icons, Custom Light Theme CSS
- **Compliance & Controls:** Anti-Money Laundering (AML) Rules Engine, Immutable Telemetry Logging, Dual-Control Workflow
- **DevOps & Hosting:** Git/GitHub, Vercel (Frontend Static Gateway), Docker ready (with root `Dockerfile` and `docker-compose`)

## ✨ Technical Achievements & Key Features

- **Dual-Control Maker-Checker Workflow:** Clerks (Makers) record transaction drafts (Invoices, Payments, Journal Vouchers), while authorized Administrators or Chartered Accountants (Checkers) inspect and verify them before posting to the ledger.
- **Anti-Money Laundering (AML) Rules Engine:** Automated checks scan net values. Transactions $\ge 500,000\text{ INR}$ are flagged `FLAGGED_HIGH_RISK`, suspending execution and generating compliance warnings.
- **Teller Cash Vault Drawer:** Cashiers process vault deposits/withdrawals, calculate denomination totals (₹2000 down to ₹10 notes), and match cash counts against drawer records.
- **Fixed & Recurring Deposits Ledger:** Manage compound interest FD accounts, tenure terms, and trigger matured payouts.
- **Cheques Transit Clearing House:** Record deposited cheques, log routing codes (IFSC/MICR), and clear or bounce transit instruments.
- **Standing Orders & Auto-Debits:** Deploy recurring scheduled instructions for periodic vendor payouts and collections.
- **Credit Lines & reducing Loan Amortization:** Track corporate loan accounts and dynamically render interest schedules.

## 👥 Project Team

- **Project Lead:** Atharva Dhobale  
- **GitHub Account:** [AtharvaDhobale](https://github.com/AtharvaDhobale)

*This application was developed as a high-fidelity corporate banking system demonstrating security controls, double-entry trial balances, and cashier workflows.*

## ⚙️ Local Installation & Setup

### Prerequisites
- Java 17+ (JDK)
- Maven 3.8+

### 1. Build and Compile
```bash
git clone https://github.com/AtharvaDhobale/aetheris-banking.git
cd aetheris-banking
mvn clean install
```

### 2. Run the Application
```bash
mvn spring-boot:run
```
*The core banking server will initialize on `http://localhost:8080`*

### 3. Test Credentials
Access the console at `http://localhost:8080/dashboard` and authenticate with:
- **Administrator / Checker**: `admin` / `admin123`
- **Account Clerk / Maker**: `employee` / `employee123`
- **CA Auditor**: `causer` / `ca123`

## 🔒 Security & Auditing Standards

- **Stateless Role Access Control:** Compartmentalized authorizations restrict clerks from clearing their own created vouchers.
- **Immutable Log Telemetry:** Captures IP addresses, browser configurations, transaction values, and risk levels.
- **Double-Entry Safeguards:** Enforces balanced Debit and Credit validation rules before ledger commits.

---
*Built for absolute financial precision and secure transaction processing.*

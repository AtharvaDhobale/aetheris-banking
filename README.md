# Accounting System

A comprehensive Java + Spring Boot accounting application for CA, accountant, clerk, and banking workflows.
This project is intentionally designed without AI features and focuses on financial records, ledger entries, invoices, and reconciliation with multi-user role management and activity tracking.

## Key Features
- **Employee Management**: Employee lookup with autocomplete, bank account details, salary tracking
- **Employee Activity Tracking**: Track every operation performed by each employee
- **Admin Dashboard**: Real-time dashboard showing today's activities, employee reports, payroll summary
- **Accounting Operations**:
  - Chart of accounts and general ledger
  - Invoice management
  - Journal entries with dual debit/credit
  - Bank reconciliation
  - Payroll processing
  - GST return filing
  - Tax return management
  - Depreciation schedules
  - Vendor payment tracking
  - Client and customer ledgers
- **Search & Knowledge Base**: Search accounting operations and CA knowledge topics with formulas
- **Multi-Role Access**: Admin, Employee, CA with role-based permissions
- **Excel Import/Export**: Bulk import client data via Excel files
- **Large Dataset Support**: PostgreSQL database with support for millions of records

## Tech Stack
- **Backend**: Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA
- **Database**: PostgreSQL (primary), MongoDB (optional for logs)
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Excel**: Apache POI for import/export
- **Build**: Maven

## Database Setup

### PostgreSQL Installation
1. Download PostgreSQL from https://www.postgresql.org/download/
2. Install and start PostgreSQL service
3. Create database:
   ```bash
   createdb accounting_db
   ```
4. Update `application.yml` with correct credentials

### MongoDB Setup (Optional)
For activity logging, install MongoDB:
```bash
# Windows: Download from https://www.mongodb.com/try/download/community
# macOS: brew install mongodb-community
# Linux: follow docs.mongodb.com
```

## Run Locally
1. Install Java 17+
2. Install PostgreSQL and MongoDB
3. Build:
   ```bash
   mvn clean package
   ```
4. Run:
   ```bash
   mvn spring-boot:run
   ```
5. Access:
   - Dashboard: `http://localhost:8080/dashboard`
   - Login: `http://localhost:8080/login`

## Test Credentials
- **Admin**: admin / admin123
- **Employee**: employee / employee123
- **CA**: causer / ca123

## API Endpoints

### Employee Management
- `GET /api/employees/suggest?query=name` - Search employees with autocomplete
- `GET /api/employees/{id}/history` - Get employee bank details and history

### Admin Dashboard
- `GET /api/admin/dashboard/today` - Today's activities summary
- `GET /api/admin/dashboard/employee-reports` - All employee activity reports
- `GET /api/admin/dashboard/payroll-summary` - Payroll summary for today

### Accounting Operations
- `GET/POST /api/accounts` - Ledger accounts
- `GET/POST /api/invoices` - Customer invoices
- `GET/POST /api/journal-entries` - Journal entries with double entry
- `GET/POST /api/payroll` - Payroll entries
- `GET/POST /api/gst-returns` - GST return filing
- `GET/POST /api/tax-returns` - Income tax returns
- `GET/POST /api/vendor-payments` - Vendor payment tracking
- `GET/POST /api/depreciation` - Asset depreciation schedules

### Search & Knowledge
- `GET /api/search/suggestions?query=term` - Operation suggestions
- `GET /api/search/knowledge?query=term` - CA knowledge topics with formulas

### Utility
- `POST /api/excel/import/clients` - Bulk import clients from Excel

## Database Schema Highlights
- `users`: Application users with roles
- `employees`: Employee master data with bank details
- `employee_activities`: Activity audit trail for each employee
- `accounts`: Chart of accounts
- `invoices`: Customer invoices
- `journal_entries`: Double-entry bookkeeping
- `payroll_entries`: Employee salaries and deductions
- `gst_returns`: GST filing records
- `tax_returns`: Income tax returns
- `vendor_payments`: Vendor payment tracking
- `depreciation_schedules`: Fixed asset depreciation
- `transaction_entries`: General ledger transactions

## Features for Different Users

### Accountant/Clerk
- Invoice creation and tracking
- Journal entry recording
- Bank reconciliation
- Vendor payment tracking
- Employee payroll

### CA/Auditor
- Financial statement generation
- Tax return filing
- GST compliance tracking
- Audit trail review
- Depreciation calculations

### Admin
- View all employee activities for today
- Employee performance reports
- Payroll monitoring
- System configuration
- Audit logs and compliance

## Notes
- The project uses Spring Security for authentication and authorization
- Role-based access control restricts features by user role
- Activity logging tracks all employee operations for audit purposes
- Large dataset support via PostgreSQL with proper indexing
- Excel import uses Apache POI for XLS/XLSX files

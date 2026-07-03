package com.am.accounting.config;

import com.am.accounting.model.*;
import com.am.accounting.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DataInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final TenderRequestRepository tenderRequestRepository;
    private final KnowledgeTopicRepository knowledgeTopicRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeActivityRepository employeeActivityRepository;
    private final AccountRepository accountRepository;
    private final LoanFacilityRepository loanFacilityRepository;
    private final InvoiceRepository invoiceRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final VendorPaymentRepository vendorPaymentRepository;
    private final AuditLogRepository auditLogRepository;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ClientRepository clientRepository,
                           TenderRequestRepository tenderRequestRepository,
                           KnowledgeTopicRepository knowledgeTopicRepository,
                           EmployeeRepository employeeRepository,
                           EmployeeActivityRepository employeeActivityRepository,
                           AccountRepository accountRepository,
                           LoanFacilityRepository loanFacilityRepository,
                           InvoiceRepository invoiceRepository,
                           JournalEntryRepository journalEntryRepository,
                           VendorPaymentRepository vendorPaymentRepository,
                           AuditLogRepository auditLogRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.tenderRequestRepository = tenderRequestRepository;
        this.knowledgeTopicRepository = knowledgeTopicRepository;
        this.employeeRepository = employeeRepository;
        this.employeeActivityRepository = employeeActivityRepository;
        this.accountRepository = accountRepository;
        this.loanFacilityRepository = loanFacilityRepository;
        this.invoiceRepository = invoiceRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.vendorPaymentRepository = vendorPaymentRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (roleRepository.count() == 0) {
            Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
            Role employeeRole = roleRepository.save(new Role("ROLE_EMPLOYEE"));
            Role caRole = roleRepository.save(new Role("ROLE_CA"));

            if (userRepository.count() == 0) {
                AppUser admin = new AppUser("admin", passwordEncoder.encode("admin123"), "System Administrator", "admin@am.com");
                admin.setRoles(Set.of(adminRole));
                userRepository.save(admin);

                AppUser employee = new AppUser("employee", passwordEncoder.encode("employee123"), "Account Clerk", "employee@am.com");
                employee.setRoles(Set.of(employeeRole));
                userRepository.save(employee);

                AppUser ca = new AppUser("causer", passwordEncoder.encode("ca123"), "Chartered Accountant", "ca@am.com");
                ca.setRoles(Set.of(caRole));
                userRepository.save(ca);
            }

            clientRepository.save(new Client("Acme Corp", "+919876543210", "acme@example.com", "27AAAAA0000A1Z5", "Mumbai, India"));
            clientRepository.save(new Client("Bajaj Finance", "+919812345678", "bajaj@example.com", "27BBBBB0000B1Z6", "Pune, India"));

            tenderRequestRepository.save(new TenderRequest("Annual Audit Tender", "Tender for auditing and compliance services.", "Finance", LocalDate.now().plusDays(21)));
            tenderRequestRepository.save(new TenderRequest("Software License Procurement", "Tender for accounting software licensing.", "Procurement", LocalDate.now().plusDays(30)));

            if (employeeRepository.count() == 0) {
                Employee emp1 = employeeRepository.save(new Employee(
                        "EMP001", "Rajesh Kumar", "Senior Accountant", "Accounting",
                        "rajesh@company.com", "+919876543210", "9876543210123456",
                        "HDFC Bank", "HDFC0001001", 60000, "ABCDE1234F"
                ));

                Employee emp2 = employeeRepository.save(new Employee(
                        "EMP002", "Priya Sharma", "Junior Accountant", "Accounting",
                        "priya@company.com", "+919876543211", "9876543210123457",
                        "ICICI Bank", "ICIC0000001", 35000, "XYZAB5678G"
                ));

                Employee emp3 = employeeRepository.save(new Employee(
                        "EMP003", "Amit Patel", "Accounts Clerk", "Accounting",
                        "amit@company.com", "+919876543212", "9876543210123458",
                        "SBI Bank", "SBIN0001234", 25000, "PQRST9012H"
                ));

                employeeActivityRepository.save(new EmployeeActivity(emp1, "Invoice Entry", "Entered 5 customer invoices for XYZ Corp", LocalDate.now()));
                employeeActivityRepository.save(new EmployeeActivity(emp2, "Ledger Update", "Updated General Ledger with GST calculations", LocalDate.now()));
                employeeActivityRepository.save(new EmployeeActivity(emp3, "Bank Reconciliation", "Reconciled bank statement for May 2024", LocalDate.now()));
            }

            knowledgeTopicRepository.save(new KnowledgeTopic("Financial Accounting", "Double Entry System",
                    "Every transaction affects at least two accounts with debit and credit entries.",
                    "Assets = Liabilities + Equity"));
            knowledgeTopicRepository.save(new KnowledgeTopic("Taxation", "GST Formula",
                    "GST collected and GST paid are used to calculate net GST liability.",
                    "Net GST = GST Output - GST Input"));
            knowledgeTopicRepository.save(new KnowledgeTopic("Audit", "Internal Control",
                    "Controls ensure accuracy and completeness in accounting records.",
                    "Control Environment + Risk Assessment + Control Activities"));
            knowledgeTopicRepository.save(new KnowledgeTopic("Banking", "Bank Reconciliation",
                    "Process of matching bank statement with ledger entries to identify discrepancies.",
                    "Book Balance + Deposits in Transit - Outstanding Checks = Bank Balance"));

            // --- Seed Bank-friendly Accounts & Transactions ---
            Account bankAccount = accountRepository.save(new Account("1010", "Main Operating Account (HDFC)", "ASSET", new BigDecimal("1250000.00")));
            Account cashAccount = accountRepository.save(new Account("1020", "Petty Cash", "ASSET", new BigDecimal("15000.00")));
            Account payablesAccount = accountRepository.save(new Account("2010", "Accounts Payable", "LIABILITY", new BigDecimal("45000.00")));
            Account receivablesAccount = accountRepository.save(new Account("1200", "Accounts Receivable", "ASSET", new BigDecimal("85000.00")));
            Account equityAccount = accountRepository.save(new Account("3000", "Owner's Equity", "EQUITY", new BigDecimal("1000000.00")));
            Account revenueAccount = accountRepository.save(new Account("4000", "Sales Revenue", "INCOME", new BigDecimal("350000.00")));
            Account expenseAccount = accountRepository.save(new Account("5010", "Office Expense", "EXPENSE", new BigDecimal("20000.00")));

            // Credit Facility / Loans Seeding
            loanFacilityRepository.save(new LoanFacility(bankAccount, "Term Loan - Corporate Finance", new BigDecimal("5000000.00"), new BigDecimal("9.5"), 36, LocalDate.now().minusMonths(3)));
            loanFacilityRepository.save(new LoanFacility(bankAccount, "Working Capital Facility", new BigDecimal("2000000.00"), new BigDecimal("8.0"), 12, LocalDate.now()));

            // Seed Maker-Checker Pending Queue Invoices
            Invoice inv1 = new Invoice("INV-2026-001", "Tesla Motors Inc", LocalDate.now().minusDays(5), new BigDecimal("120000.00"));
            inv1.setStatus("PENDING_APPROVAL");
            invoiceRepository.save(inv1);

            Invoice inv2 = new Invoice("INV-2026-SUSP", "Unknown Shell Corp (Cayman)", LocalDate.now().minusDays(1), new BigDecimal("650000.00"));
            inv2.setStatus("FLAGGED_HIGH_RISK");
            invoiceRepository.save(inv2);

            // Seed Maker-Checker Pending Queue Payments
            VendorPayment vp1 = new VendorPayment("Office Space Rentals", "27BBBBB0000B1Z6", "RENT-MAY-26", LocalDate.now().minusDays(10), new BigDecimal("45000.00"), new BigDecimal("8100.00"), LocalDate.now(), "ACH");
            vp1.setStatus("PENDING_APPROVAL");
            vendorPaymentRepository.save(vp1);

            VendorPayment vp2 = new VendorPayment("Offshore Consulting Ltd", "27XXXXX0000X1Z9", "CONS-26-99", LocalDate.now().minusDays(2), new BigDecimal("800000.00"), new BigDecimal("144000.00"), LocalDate.now(), "WIRE");
            vp2.setStatus("FLAGGED_HIGH_RISK");
            vendorPaymentRepository.save(vp2);

            // Seed Maker-Checker Pending Journal Entries
            JournalEntry jv = new JournalEntry("JV-001", LocalDate.now(), officeExpenseAccount(expenseAccount), pettyCashAccount(cashAccount), new BigDecimal("2500.00"), "Replenish petty cash for stationery", "GENERAL");
            jv.setStatus("PENDING_APPROVAL");
            journalEntryRepository.save(jv);

            // Seed initial Audit Logs for compliance
            auditLogRepository.save(new AuditLog("admin", "[ROLE_ADMIN]", "Initialized clean H2 Database storage", "127.0.0.1", "System Engine", "LOW"));
            auditLogRepository.save(new AuditLog("employee", "[ROLE_EMPLOYEE]", "Drafted invoice INV-2026-001", "192.168.1.52", "Chrome Browser", "LOW"));
            auditLogRepository.save(new AuditLog("employee", "[ROLE_EMPLOYEE]", "Submitted large payment request for Offshore Consulting Ltd (Flagged by AML rules)", "192.168.1.52", "Chrome Browser", "HIGH"));
        }
    }

    private Account officeExpenseAccount(Account fallback) {
        return accountRepository.findAll().stream().filter(a -> "5010".equals(a.getCode())).findFirst().orElse(fallback);
    }

    private Account pettyCashAccount(Account fallback) {
        return accountRepository.findAll().stream().filter(a -> "1020".equals(a.getCode())).findFirst().orElse(fallback);
    }
}

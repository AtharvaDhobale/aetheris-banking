// AETHERIS Core Banking & Accounting Dashboard Script
// === GLOBAL UTILITIES ===

// --- Toast Notification System ---
function showToast(message, type = 'info', duration = 4000) {
    const container = document.getElementById('toast-container');
    if (!container) return;
    const icons = { success: 'fa-circle-check', error: 'fa-circle-xmark', warning: 'fa-triangle-exclamation', info: 'fa-circle-info' };
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `<i class="fa-solid ${icons[type] || icons.info}"></i><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => { toast.style.animation = 'toastIn 0.2s ease reverse'; setTimeout(() => toast.remove(), 200); }, duration);
}

// --- Status Pill Helper ---
function statusPill(status) {
    const map = {
        'PENDING_APPROVAL': ['pending', 'Pending Approval'],
        'APPROVED':         ['approved', 'Approved'],
        'REJECTED':         ['rejected', 'Rejected'],
        'FLAGGED_HIGH_RISK':['flagged', '⚠ High Risk'],
        'DRAFT':            ['draft', 'Draft'],
        'PROCESSED':        ['approved', 'Processed'],
    };
    const [cls, label] = map[status] || ['draft', status || 'Unknown'];
    return `<span class="status-pill status-${cls}">${label}</span>`;
}

// --- Switch Tab Programmatically ---
function switchTab(tabName) {
    const navItem = document.querySelector(`.nav-item[data-tab="${tabName}"]`);
    if (navItem) navItem.click();
}

// --- Live Activity Sparkline Chart ---
function renderActivityChart() {
    const canvas = document.getElementById('live-activity-chart');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    const W = canvas.parentElement.offsetWidth;
    const H = 110;
    canvas.width = W;
    canvas.height = H;

    // Simulated 7-day data
    const days = ['Mon','Tue','Wed','Thu','Fri','Sat','Sun'];
    const txVals = [18, 34, 27, 45, 38, 12, 52];
    const alertVals = [2, 1, 3, 5, 2, 0, 4];
    const maxVal = Math.max(...txVals) + 10;

    const padL = 36, padR = 20, padT = 16, padB = 28;
    const plotW = W - padL - padR;
    const plotH = H - padT - padB;
    const stepX = plotW / (days.length - 1);

    // Grid lines
    ctx.clearRect(0, 0, W, H);
    for (let i = 0; i <= 4; i++) {
        const y = padT + (plotH / 4) * i;
        ctx.beginPath();
        ctx.strokeStyle = 'rgba(30,111,255,0.07)';
        ctx.lineWidth = 1;
        ctx.moveTo(padL, y);
        ctx.lineTo(W - padR, y);
        ctx.stroke();
        ctx.fillStyle = 'rgba(136,153,196,0.5)';
        ctx.font = '9px Inter, sans-serif';
        ctx.textAlign = 'right';
        ctx.fillText(Math.round(maxVal - (maxVal / 4) * i), padL - 4, y + 3);
    }

    // Transaction area fill
    const txPoints = txVals.map((v, i) => ({ x: padL + i * stepX, y: padT + plotH - (v / maxVal) * plotH }));
    const grad = ctx.createLinearGradient(0, padT, 0, padT + plotH);
    grad.addColorStop(0, 'rgba(30,111,255,0.30)');
    grad.addColorStop(1, 'rgba(30,111,255,0.00)');

    ctx.beginPath();
    ctx.moveTo(txPoints[0].x, padT + plotH);
    txPoints.forEach(p => ctx.lineTo(p.x, p.y));
    ctx.lineTo(txPoints[txPoints.length-1].x, padT + plotH);
    ctx.closePath();
    ctx.fillStyle = grad;
    ctx.fill();

    // Transaction line
    ctx.beginPath();
    ctx.strokeStyle = '#4f93ff';
    ctx.lineWidth = 2;
    ctx.lineJoin = 'round';
    txPoints.forEach((p, i) => i === 0 ? ctx.moveTo(p.x, p.y) : ctx.lineTo(p.x, p.y));
    ctx.stroke();

    // Alert area fill
    const alertPoints = alertVals.map((v, i) => ({ x: padL + i * stepX, y: padT + plotH - (v / maxVal) * plotH }));
    const alertGrad = ctx.createLinearGradient(0, padT, 0, padT + plotH);
    alertGrad.addColorStop(0, 'rgba(255,61,110,0.18)');
    alertGrad.addColorStop(1, 'rgba(255,61,110,0.00)');

    ctx.beginPath();
    ctx.moveTo(alertPoints[0].x, padT + plotH);
    alertPoints.forEach(p => ctx.lineTo(p.x, p.y));
    ctx.lineTo(alertPoints[alertPoints.length-1].x, padT + plotH);
    ctx.closePath();
    ctx.fillStyle = alertGrad;
    ctx.fill();

    // Alert line
    ctx.beginPath();
    ctx.strokeStyle = '#ff3d6e';
    ctx.lineWidth = 1.5;
    ctx.setLineDash([4, 3]);
    alertPoints.forEach((p, i) => i === 0 ? ctx.moveTo(p.x, p.y) : ctx.lineTo(p.x, p.y));
    ctx.stroke();
    ctx.setLineDash([]);

    // Dots on transaction line
    txPoints.forEach((p, i) => {
        ctx.beginPath();
        ctx.arc(p.x, p.y, 3.5, 0, Math.PI * 2);
        ctx.fillStyle = '#4f93ff';
        ctx.fill();
        ctx.strokeStyle = '#040810';
        ctx.lineWidth = 1.5;
        ctx.stroke();
    });

    // Day labels
    ctx.fillStyle = 'rgba(136,153,196,0.7)';
    ctx.font = '9px Inter, sans-serif';
    ctx.textAlign = 'center';
    days.forEach((d, i) => ctx.fillText(d, padL + i * stepX, H - 6));

    // Legend
    ctx.font = '9.5px Inter, sans-serif';
    ctx.fillStyle = '#4f93ff';
    ctx.fillRect(W - padR - 90, 6, 8, 8);
    ctx.fillStyle = 'rgba(136,153,196,0.8)';
    ctx.textAlign = 'left';
    ctx.fillText('Transactions', W - padR - 79, 14);
    ctx.fillStyle = '#ff3d6e';
    ctx.fillRect(W - padR - 90, 19, 8, 8);
    ctx.fillStyle = 'rgba(136,153,196,0.8)';
    ctx.fillText('AML Alerts', W - padR - 79, 27);
}

document.addEventListener('DOMContentLoaded', () => {

    // --- State management ---
    let activeTab = 'overview';
    let accountsList = [];
    let exchangeRates = {
        INR: 1.0,
        USD: 83.5,
        EUR: 89.2,
        GBP: 105.7
    };

    // --- Tab Switching Logic ---
    const navItems = document.querySelectorAll('.nav-item');
    const tabPanes = document.querySelectorAll('.tab-pane');

    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const targetTab = item.getAttribute('data-tab');
            
            navItems.forEach(nav => nav.classList.remove('active'));
            tabPanes.forEach(pane => pane.classList.remove('active'));

            item.classList.add('active');
            document.getElementById(`tab-${targetTab}`).classList.add('active');
            
            activeTab = targetTab;
            loadTabData(targetTab);
        });
    });

    // --- Core Loading Hub ---
    function loadTabData(tabName) {
        switch (tabName) {
            case 'overview':
                loadOverviewData();
                break;
            case 'maker-checker':
                loadApprovalsQueue();
                break;
            case 'ledger':
                loadLedgerAccounts();
                break;
            case 'journal':
                loadJournalEntries();
                break;
            case 'reconciliation':
                loadReconciliationWorkspace();
                break;
            case 'loans':
                loadLoanFacilities();
                break;
            case 'invoices':
                loadInvoices();
                break;
            case 'payments':
                loadVendorPayments();
                break;
            case 'depreciation':
                loadDepreciationSchedules();
                break;
            case 'tax-gst':
                loadTaxGSTReturns();
                break;
            case 'tenders':
                loadTenderRequests();
                break;
            case 'compliance':
                loadComplianceLogs();
                break;
            case 'knowledge':
                loadKnowledgeBase();
                break;
            case 'teller-vault':
                loadTellerVault();
                break;
            case 'fd-rd':
                loadFixedDeposits();
                break;
            case 'cheques':
                loadCheques();
                break;
            case 'standing-instructions':
                loadStandingInstructions();
                break;
        }
    }

    // --- Tab 1: Overview & Executive Dashboard ---
    async function loadOverviewData() {
        // Render live chart first
        renderActivityChart();

        try {
            // Load Today's Activities
            const resActivities = await fetch('/api/admin/dashboard/today');
            const dataActivities = await resActivities.json();

            // Load Employee Reports
            const resEmployees = await fetch('/api/admin/dashboard/employee-reports');
            const dataEmployees = await resEmployees.json();

            // Update Timeline Activity Stream using activity-item classes
            const stream = document.getElementById('dashboard-content');
            if (dataActivities.activitiesByEmployee) {
                let html = '';
                Object.entries(dataActivities.activitiesByEmployee).forEach(([empName, list]) => {
                    list.forEach(act => {
                        const riskLevel = (act.activityType.toLowerCase().includes('suspicious') || act.description.toLowerCase().includes('suspicious')) ? 'HIGH' : 'LOW';
                        html += `
                            <div class="activity-item">
                                <div class="activity-dot ${riskLevel}"></div>
                                <div class="activity-content">
                                    <div class="activity-action">${act.activityType}</div>
                                    <div class="activity-meta">${act.description} &mdash; <strong style="color:var(--text-accent)">${empName}</strong> &bull; ${act.activityDate || 'Today'}</div>
                                </div>
                            </div>
                        `;
                    });
                });
                stream.innerHTML = html || '<div style="padding:20px;text-align:center;color:var(--text-muted);font-size:13px;">No activity logs recorded today.</div>';
            } else {
                stream.innerHTML = '<div style="padding:20px;text-align:center;color:var(--text-muted);font-size:13px;">No data available.</div>';
            }

            // Update Employee Performance Lists with avatars
            const perfList = document.getElementById('employee-reports-content');
            if (dataEmployees) {
                let html = '';
                Object.entries(dataEmployees).forEach(([empName, report]) => {
                    const initials = empName.split(' ').map(w => w[0]).join('').substring(0,2).toUpperCase();
                    html += `
                        <div class="perf-item">
                            <div class="perf-avatar">${initials}</div>
                            <div class="perf-info">
                                <div class="perf-name">${empName}</div>
                                <div class="perf-role">${report.role || 'Accounts Associate'}</div>
                            </div>
                            <span class="perf-count">${report.activitiesCount || 0}</span>
                        </div>
                    `;
                });
                perfList.innerHTML = html || '<div style="padding:20px;text-align:center;color:var(--text-muted);font-size:13px;">No reports available.</div>';
            }

            // Refresh key card metrics
            updateCardsMetrics();

        } catch (err) {
            console.error('Error fetching overview logs:', err);
            showToast('Failed to load dashboard data', 'error');
        }
    }

    // Refresh Card metrics dynamically
    async function updateCardsMetrics() {
        try {
            // Funds from ledger
            const resAcc = await fetch('/api/accounts');
            const accounts = await resAcc.json();
            accountsList = accounts; // Save locally
            let totalFunds = 0;
            accounts.forEach(a => {
                if (a.type === 'ASSET' && (a.name.toLowerCase().includes('bank') || a.name.toLowerCase().includes('cash'))) {
                    totalFunds += a.balance;
                }
            });
            document.getElementById('metric-funds').textContent = '₹' + totalFunds.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

            // Outstanding loans
            const resLoans = await fetch('/api/loans');
            const loans = await resLoans.json();
            let totalCredit = 0;
            loans.forEach(l => {
                totalCredit += l.outstandingBalance;
            });
            document.getElementById('metric-credit').textContent = '₹' + totalCredit.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

            // Pending Approvals count
            const resAppr = await fetch('/api/approvals/pending');
            const approvals = await resAppr.json();
            const pendingCount = (approvals.invoices?.length || 0) + (approvals.journalEntries?.length || 0) + (approvals.vendorPayments?.length || 0);
            
            document.getElementById('metric-pending').textContent = pendingCount;
            document.getElementById('pending-approvals-badge').textContent = pendingCount;

            // Compliance warnings count
            const resLogs = await fetch('/api/compliance/logs');
            const complianceLogs = await resLogs.json();
            let alertCount = 0;
            complianceLogs.forEach(log => {
                if (log.riskLevel === 'HIGH') alertCount++;
            });
            document.getElementById('metric-alerts').textContent = alertCount;
            document.getElementById('risk-alerts-badge').textContent = alertCount;

        } catch (err) {
            console.error('Error updating metrics:', err);
        }
    }

    document.getElementById('btn-refresh-dashboard').addEventListener('click', loadOverviewData);

    // --- Tab 2: Maker-Checker Queue ---
    async function loadApprovalsQueue() {
        try {
            const res = await fetch('/api/approvals/pending');
            const data = await res.json();

            // Render Invoices
            const invoiceTbody = document.getElementById('pending-invoices-tbody');
            if (data.invoices && data.invoices.length > 0) {
                invoiceTbody.innerHTML = data.invoices.map(i => `
                    <tr>
                        <td><strong style="font-family:var(--font-mono);color:var(--text-accent)">${i.referenceNumber}</strong></td>
                        <td>${i.customerName}</td>
                        <td>${i.invoiceDate}</td>
                        <td><strong style="font-family:var(--font-mono)">₹${i.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td>${statusPill(i.status)}</td>
                        <td>
                            <button class="btn-icon-approve btn-approve-invoice" data-id="${i.id}" title="Approve"><i class="fa-solid fa-check"></i></button>
                            <button class="btn-icon-reject btn-reject-invoice" data-id="${i.id}" title="Reject" style="margin-left:6px;"><i class="fa-solid fa-xmark"></i></button>
                        </td>
                    </tr>
                `).join('');
                bindApprovalButtons('invoices');
            } else {
                invoiceTbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted" style="padding:18px;">No pending invoices in queue</td></tr>';
            }

            // Render Payments
            const paymentTbody = document.getElementById('pending-payments-tbody');
            if (data.vendorPayments && data.vendorPayments.length > 0) {
                paymentTbody.innerHTML = data.vendorPayments.map(p => `
                    <tr>
                        <td><strong>${p.vendorName}</strong></td>
                        <td style="font-family:var(--font-mono);font-size:11px;">${p.invoiceNumber}</td>
                        <td><code style="font-family:var(--font-mono);font-size:11px;color:var(--text-accent);">${p.vendorGST}</code></td>
                        <td><strong style="font-family:var(--font-mono)">₹${p.totalAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td>${statusPill(p.status)}</td>
                        <td>
                            <button class="btn-icon-approve btn-approve-payment" data-id="${p.id}" title="Approve"><i class="fa-solid fa-check"></i></button>
                            <button class="btn-icon-reject btn-reject-payment" data-id="${p.id}" title="Reject" style="margin-left:6px;"><i class="fa-solid fa-xmark"></i></button>
                        </td>
                    </tr>
                `).join('');
                bindApprovalButtons('payments');
            } else {
                paymentTbody.innerHTML = '<tr><td colspan="6" class="text-center text-secondary">No pending payments needing review</td></tr>';
            }

            // Render Journals
            const journalTbody = document.getElementById('pending-journals-tbody');
            if (data.journalEntries && data.journalEntries.length > 0) {
                journalTbody.innerHTML = data.journalEntries.map(j => `
                    <tr>
                        <td><strong>${j.voucherNumber}</strong></td>
                        <td>${j.entryDate}</td>
                        <td>${j.debitAccount ? j.debitAccount.name : 'N/A'}</td>
                        <td>${j.creditAccount ? j.creditAccount.name : 'N/A'}</td>
                        <td>₹${j.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                        <td><span class="status-pill status-pending">${j.status}</span></td>
                        <td>
                            <button class="btn btn-success btn-sm btn-approve-journal" data-id="${j.id}"><i class="fa-solid fa-check"></i></button>
                            <button class="btn btn-danger btn-sm btn-reject-journal" data-id="${j.id}"><i class="fa-solid fa-xmark"></i></button>
                        </td>
                    </tr>
                `).join('');
                bindApprovalButtons('journals');
            } else {
                journalTbody.innerHTML = '<tr><td colspan="7" class="text-center text-secondary">No pending journal entries needing review</td></tr>';
            }

        } catch (err) {
            console.error('Error loading approvals queue:', err);
        }
    }

    function bindApprovalButtons(type) {
        if (type === 'invoices') {
            document.querySelectorAll('.btn-approve-invoice').forEach(btn => {
                btn.addEventListener('click', () => processApproval('invoices', btn.getAttribute('data-id'), 'approve'));
            });
            document.querySelectorAll('.btn-reject-invoice').forEach(btn => {
                btn.addEventListener('click', () => processApproval('invoices', btn.getAttribute('data-id'), 'reject'));
            });
        }
        if (type === 'payments') {
            document.querySelectorAll('.btn-approve-payment').forEach(btn => {
                btn.addEventListener('click', () => processApproval('vendor-payments', btn.getAttribute('data-id'), 'approve'));
            });
            document.querySelectorAll('.btn-reject-payment').forEach(btn => {
                btn.addEventListener('click', () => processApproval('vendor-payments', btn.getAttribute('data-id'), 'reject'));
            });
        }
        if (type === 'journals') {
            document.querySelectorAll('.btn-approve-journal').forEach(btn => {
                btn.addEventListener('click', () => processApproval('journal-entries', btn.getAttribute('data-id'), 'approve'));
            });
            document.querySelectorAll('.btn-reject-journal').forEach(btn => {
                btn.addEventListener('click', () => processApproval('journal-entries', btn.getAttribute('data-id'), 'reject'));
            });
        }
    }

    async function processApproval(apiPath, id, action) {
        try {
            const res = await fetch(`/api/approvals/${apiPath}/${id}/${action}`, { method: 'POST' });
            if (res.ok) {
                loadApprovalsQueue();
                updateCardsMetrics();
            } else {
                alert('Authorization failed. Check your role permissions.');
            }
        } catch (err) {
            console.error(err);
        }
    }

    // --- Tab 3: General Ledger Accounts ---
    async function loadLedgerAccounts() {
        try {
            const res = await fetch('/api/accounts');
            const data = await res.json();
            accountsList = data;

            // Render table
            const tbody = document.getElementById('ledger-accounts-tbody');
            tbody.innerHTML = data.map(a => `
                <tr>
                    <td><strong>${a.code}</strong></td>
                    <td>${a.name}</td>
                    <td><span class="status-pill status-approved">${a.type}</span></td>
                    <td>₹${a.balance.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>${a.createdAt?.substring(0, 10) || 'N/A'}</td>
                </tr>
            `).join('');

            // Populate Journal dropdowns
            const debitSelect = document.getElementById('jv-debit');
            const creditSelect = document.getElementById('jv-credit');
            const loanAccSelect = document.getElementById('loan-account');
            const reconSelect = document.getElementById('reconcile-account-select');

            const dropdownHtml = data.map(a => `<option value="${a.id}">${a.code} - ${a.name} (₹${a.balance})</option>`).join('');
            debitSelect.innerHTML = dropdownHtml;
            creditSelect.innerHTML = dropdownHtml;
            loanAccSelect.innerHTML = dropdownHtml;

            // Reconciliation only shows Asset accounts
            const assetDropdownHtml = data.filter(a => a.type === 'ASSET')
                .map(a => `<option value="${a.id}">${a.code} - ${a.name}</option>`).join('');
            reconSelect.innerHTML = assetDropdownHtml || '<option>No Asset Accounts Found</option>';

        } catch (err) {
            console.error('Error loading ledger accounts:', err);
        }
    }

    document.getElementById('form-create-account').addEventListener('submit', async (e) => {
        e.preventDefault();
        const code = document.getElementById('acc-code').value;
        const name = document.getElementById('acc-name').value;
        const type = document.getElementById('acc-type').value;
        const balance = parseFloat(document.getElementById('acc-balance').value);

        try {
            const res = await fetch('/api/accounts', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ code, name, type, balance })
            });

            if (res.ok) {
                document.getElementById('form-create-account').reset();
                loadLedgerAccounts();
                updateCardsMetrics();
            } else {
                alert('Failed to initialize account code. Ensure it is unique.');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 4: Double Entry Journal ---
    async function loadJournalEntries() {
        try {
            const res = await fetch('/api/journal-entries');
            const data = await res.json();

            const tbody = document.getElementById('journal-entries-tbody');
            tbody.innerHTML = data.map(j => `
                <tr>
                    <td><strong>${j.voucherNumber}</strong></td>
                    <td>${j.entryDate}</td>
                    <td>${j.debitAccount ? j.debitAccount.name : 'N/A'}</td>
                    <td>${j.creditAccount ? j.creditAccount.name : 'N/A'}</td>
                    <td>₹${j.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td><span class="status-pill ${j.status === 'APPROVED' ? 'status-approved' : 'status-pending'}">${j.status}</span></td>
                    <td>${j.description}</td>
                </tr>
            `).join('');

        } catch (err) {
            console.error('Error loading journal entries:', err);
        }
    }

    // Live balancing checking display
    const jvDebit = document.getElementById('jv-debit');
    const jvCredit = document.getElementById('jv-credit');
    const balanceAlert = document.getElementById('journal-balance-alert');

    function checkDoubleEntryBalance() {
        if (jvDebit.value === jvCredit.value) {
            balanceAlert.className = 'compliance-box info-box mb-3';
            balanceAlert.innerHTML = '<i class="fa-solid fa-scale-unbalanced text-warning"></i> <span>Double Entry Error: Debit and Credit accounts cannot be the same!</span>';
            return false;
        } else {
            balanceAlert.className = 'compliance-box success-box mb-3';
            balanceAlert.innerHTML = '<i class="fa-solid fa-check-double text-success"></i> <span>Double Entry accounts balanced. Ready for maker submission.</span>';
            return true;
        }
    }

    jvDebit.addEventListener('change', checkDoubleEntryBalance);
    jvCredit.addEventListener('change', checkDoubleEntryBalance);

    document.getElementById('form-create-journal').addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!checkDoubleEntryBalance()) {
            alert('Resolve double-entry errors before submitting.');
            return;
        }

        const voucherNumber = document.getElementById('jv-voucher').value;
        const entryDate = document.getElementById('jv-date').value;
        const debitAccountId = parseInt(jvDebit.value);
        const creditAccountId = parseInt(jvCredit.value);
        const amount = parseFloat(document.getElementById('jv-amount').value);
        const description = document.getElementById('jv-desc').value;
        const entryType = document.getElementById('jv-type').value;

        try {
            const res = await fetch('/api/journal-entries', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    voucherNumber,
                    entryDate,
                    debitAccount: { id: debitAccountId },
                    creditAccount: { id: creditAccountId },
                    amount,
                    description,
                    entryType
                })
            });

            if (res.ok) {
                document.getElementById('form-create-journal').reset();
                loadJournalEntries();
                updateCardsMetrics();
            } else {
                alert('Submission failed.');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 5: Bank Reconciliation Workspace ---
    let reconAccountId = null;
    let mockStatementBalance = 0;

    function loadReconciliationWorkspace() {
        // Just displays selectors
        document.getElementById('reconcile-workspace').style.display = 'none';
        document.getElementById('recon-summary-panel').style.display = 'none';
    }

    document.getElementById('btn-load-reconciliation').addEventListener('click', async () => {
        const select = document.getElementById('reconcile-account-select');
        reconAccountId = select.value;
        if (!reconAccountId) return;

        try {
            const res = await fetch(`/api/reconciliation/accounts/${reconAccountId}`);
            const data = await res.json();

            document.getElementById('reconcile-workspace').style.display = 'grid';
            document.getElementById('recon-summary-panel').style.display = 'flex';

            const internalBal = data.balance || 0;
            document.getElementById('recon-ledger-bal').textContent = '₹' + internalBal.toLocaleString('en-IN', { minimumFractionDigits: 2 });
            mockStatementBalance = internalBal + 50000; // Simulated offset
            document.getElementById('recon-diff-bal').textContent = '₹' + (mockStatementBalance - internalBal).toLocaleString('en-IN', { minimumFractionDigits: 2 });

            // Display ledger transactions to select
            const ledgerTbody = document.getElementById('recon-ledger-tbody');
            if (data.transactions && data.transactions.length > 0) {
                ledgerTbody.innerHTML = data.transactions.map(t => `
                    <tr>
                        <td><input type="checkbox" class="chk-recon-item" data-amount="${t.amount}" /></td>
                        <td><strong>VOUCHER-${t.id}</strong></td>
                        <td>₹${t.amount.toLocaleString('en-IN')}</td>
                        <td>${t.description}</td>
                    </tr>
                `).join('');
                bindReconCheckboxes(internalBal);
            } else {
                // Seed some dummy items if none returned
                ledgerTbody.innerHTML = `
                    <tr>
                        <td><input type="checkbox" class="chk-recon-item" data-amount="25000.00" /></td>
                        <td><strong>VOUCHER-042</strong></td>
                        <td>₹25,000.00</td>
                        <td>Vendor Settlement</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" class="chk-recon-item" data-amount="15000.00" /></td>
                        <td><strong>VOUCHER-088</strong></td>
                        <td>₹15,000.00</td>
                        <td>Petty Cash transfer</td>
                    </tr>
                `;
                bindReconCheckboxes(internalBal);
            }

            // Clear statement table
            document.getElementById('recon-statement-tbody').innerHTML = '<tr><td colspan="5" class="text-center text-secondary">Upload bank statement or click Simulation below</td></tr>';

        } catch (err) {
            console.error(err);
        }
    });

    function bindReconCheckboxes(internalBal) {
        document.querySelectorAll('.chk-recon-item').forEach(chk => {
            chk.addEventListener('change', () => {
                let matchedSum = 0;
                document.querySelectorAll('.chk-recon-item:checked').forEach(c => {
                    matchedSum += parseFloat(c.getAttribute('data-amount'));
                });
                const remainingDiff = mockStatementBalance - internalBal - matchedSum;
                const diffEl = document.getElementById('recon-diff-bal');
                diffEl.textContent = '₹' + remainingDiff.toLocaleString('en-IN', { minimumFractionDigits: 2 });
                if (Math.abs(remainingDiff) < 0.01) {
                    diffEl.className = 'text-success';
                } else {
                    diffEl.className = 'text-warning';
                }
            });
        });
    }

    document.getElementById('btn-parse-mock-statement').addEventListener('click', () => {
        // Feed mock statement rows
        const statementTbody = document.getElementById('recon-statement-tbody');
        statementTbody.innerHTML = `
            <tr>
                <td><button class="btn btn-secondary btn-sm btn-match-statement">Link Entry</button></td>
                <td>2026-06-15</td>
                <td>HDFC BANK ACH DEBIT</td>
                <td>₹25,000.00</td>
                <td><span class="status-pill status-approved">UNMATCHED</span></td>
            </tr>
            <tr>
                <td><button class="btn btn-secondary btn-sm btn-match-statement">Link Entry</button></td>
                <td>2026-06-20</td>
                <td>PETTY CASH WITHDRAWAL</td>
                <td>₹15,000.00</td>
                <td><span class="status-pill status-approved">UNMATCHED</span></td>
            </tr>
        `;
        document.querySelectorAll('.btn-match-statement').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const tr = e.target.closest('tr');
                tr.querySelector('.status-pill').textContent = 'MATCHED';
                tr.querySelector('.status-pill').className = 'status-pill status-approved';
                btn.disabled = true;
                btn.textContent = 'Linked';
            });
        });
    });

    document.getElementById('btn-finalize-reconciliation').addEventListener('click', () => {
        alert('Bank statement reconciliation cleared and locked successfully!');
        loadReconciliationWorkspace();
    });

    // --- Tab 6: Credit Lines & Loan Facilities ---
    async function loadLoanFacilities() {
        try {
            const res = await fetch('/api/loans');
            const data = await res.json();

            const tbody = document.getElementById('loans-tbody');
            tbody.innerHTML = data.map(l => `
                <tr>
                    <td><strong>${l.facilityName}</strong></td>
                    <td>${l.account ? l.account.name : 'N/A'}</td>
                    <td>₹${l.creditLimit.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>${l.interestRate}%</td>
                    <td>${l.termMonths} Months</td>
                    <td>₹${l.outstandingBalance.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>
                        <button class="btn btn-primary btn-sm btn-view-schedule" data-id="${l.id}">Schedule</button>
                    </td>
                </tr>
            `).join('');

            document.querySelectorAll('.btn-view-schedule').forEach(btn => {
                btn.addEventListener('click', () => loadAmortizationSchedule(btn.getAttribute('data-id')));
            });

        } catch (err) {
            console.error('Error loading loan list:', err);
        }
    }

    async function loadAmortizationSchedule(loanId) {
        try {
            const res = await fetch(`/api/loans/${loanId}/schedule`);
            const data = await res.json();

            document.getElementById('loan-schedule-card').style.display = 'block';
            document.getElementById('loan-schedule-title').textContent = `${data.facilityName} - Amortization Ledger`;
            document.getElementById('loan-schedule-emi').textContent = '₹' + data.monthlyEmi.toLocaleString('en-IN', { minimumFractionDigits: 2 });
            document.getElementById('loan-schedule-interest').textContent = '₹' + data.totalInterestPaid.toLocaleString('en-IN', { minimumFractionDigits: 2 });

            const tbody = document.getElementById('loan-schedule-tbody');
            tbody.innerHTML = data.schedule.map(row => `
                <tr>
                    <td>Month ${row.month}</td>
                    <td>₹${row.emi.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>₹${row.principal.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>₹${row.interest.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>₹${row.remainingBalance.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                </tr>
            `).join('');

        } catch (err) {
            console.error('Error calculating schedule:', err);
        }
    }

    document.getElementById('form-create-loan').addEventListener('submit', async (e) => {
        e.preventDefault();
        const accountId = parseInt(document.getElementById('loan-account').value);
        const facilityName = document.getElementById('loan-name').value;
        const creditLimit = parseFloat(document.getElementById('loan-limit').value);
        const interestRate = parseFloat(document.getElementById('loan-rate').value);
        const termMonths = parseInt(document.getElementById('loan-term').value);

        try {
            const res = await fetch('/api/loans', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ accountId, facilityName, creditLimit, interestRate, termMonths })
            });

            if (res.ok) {
                document.getElementById('form-create-loan').reset();
                loadLoanFacilities();
                updateCardsMetrics();
            } else {
                alert('Failed to sanction term credit limit.');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 7: Invoices & Currencies ---
    async function loadInvoices() {
        try {
            const res = await fetch('/api/invoices');
            const data = await res.json();

            const tbody = document.getElementById('invoices-tbody');
            tbody.innerHTML = data.map(i => `
                <tr>
                    <td><strong>${i.referenceNumber}</strong></td>
                    <td>${i.customerName}</td>
                    <td>${i.invoiceDate}</td>
                    <td>₹${i.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td><span class="status-pill ${
                        i.status === 'APPROVED' ? 'status-approved' : 
                        i.status === 'FLAGGED_HIGH_RISK' ? 'status-high-risk' : 
                        'status-pending'
                    }">${i.status}</span></td>
                </tr>
            `).join('');

        } catch (err) {
            console.error('Error loading invoices:', err);
        }
    }

    // Currency exchange rates visual converter
    const invAmount = document.getElementById('inv-amount');
    const invCurrency = document.getElementById('inv-currency');
    const displayRate = document.getElementById('converted-rate-display');

    function updateConvertedDisplay() {
        const val = parseFloat(invAmount.value) || 0;
        const rate = exchangeRates[invCurrency.value] || 1.0;
        const inrEquivalent = val * rate;
        displayRate.textContent = '₹' + inrEquivalent.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    invAmount.addEventListener('input', updateConvertedDisplay);
    invCurrency.addEventListener('change', updateConvertedDisplay);

    document.getElementById('form-create-invoice').addEventListener('submit', async (e) => {
        e.preventDefault();
        const referenceNumber = document.getElementById('inv-ref').value;
        const customerName = document.getElementById('inv-customer').value;
        const invoiceDate = document.getElementById('inv-date').value;
        
        // Convert to INR base amount for bookkeeping standard
        const val = parseFloat(invAmount.value);
        const rate = exchangeRates[invCurrency.value] || 1.0;
        const baseAmount = val * rate;

        try {
            const res = await fetch('/api/invoices', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ referenceNumber, customerName, invoiceDate, amount: baseAmount })
            });

            if (res.ok) {
                document.getElementById('form-create-invoice').reset();
                displayRate.textContent = '₹0.00';
                loadInvoices();
                updateCardsMetrics();
            } else {
                alert('Submit failed. Ensure Reference Code is unique.');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 8: Vendor Payments ---
    async function loadVendorPayments() {
        try {
            const res = await fetch('/api/vendor-payments');
            const data = await res.json();

            const tbody = document.getElementById('payments-tbody');
            tbody.innerHTML = data.map(p => `
                <tr>
                    <td><strong>${p.vendorName}</strong></td>
                    <td><code>${p.vendorGST}</code></td>
                    <td>${p.invoiceNumber}</td>
                    <td>₹${p.taxAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>₹${p.totalAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                    <td>${p.paymentMode}</td>
                    <td><span class="status-pill ${
                        p.status === 'APPROVED' ? 'status-approved' : 
                        p.status === 'FLAGGED_HIGH_RISK' ? 'status-high-risk' : 
                        'status-pending'
                    }">${p.status}</span></td>
                </tr>
            `).join('');

        } catch (err) {
            console.error('Error loading vendor payments:', err);
        }
    }

    // Auto-calculate 18% GST link
    document.getElementById('lnk-autocalc-gst').addEventListener('click', (e) => {
        e.preventDefault();
        const baseAmount = parseFloat(document.getElementById('vp-amount').value) || 0;
        const gstTax = baseAmount * 0.18;
        document.getElementById('vp-tax').value = gstTax.toFixed(2);
    });

    document.getElementById('form-create-payment').addEventListener('submit', async (e) => {
        e.preventDefault();
        const vendorName = document.getElementById('vp-vendor').value;
        const vendorGST = document.getElementById('vp-gst').value;
        const invoiceNumber = document.getElementById('vp-invoice').value;
        const invoiceDate = document.getElementById('vp-date').value;
        const invoiceAmount = parseFloat(document.getElementById('vp-amount').value);
        const taxAmount = parseFloat(document.getElementById('vp-tax').value) || 0.0;
        const paymentMode = document.getElementById('vp-paymode').value;

        try {
            const res = await fetch('/api/vendor-payments', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    vendorName, vendorGST, invoiceNumber, invoiceDate,
                    invoiceAmount, taxAmount, paymentDate: invoiceDate, paymentMode
                })
            });

            if (res.ok) {
                document.getElementById('form-create-payment').reset();
                loadVendorPayments();
                updateCardsMetrics();
            } else {
                alert('Submit failed.');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 9: Asset Depreciation Schedules ---
    async function loadDepreciationSchedules() {
        try {
            const res = await fetch('/api/depreciation');
            const data = await res.json();

            const tbody = document.getElementById('depreciation-tbody');
            tbody.innerHTML = data.map(d => `
                <tr>
                    <td><strong>${d.assetName}</strong></td>
                    <td>₹${d.purchaseCost.toLocaleString('en-IN')}</td>
                    <td>₹${d.salvageValue.toLocaleString('en-IN')}</td>
                    <td>${d.depreciationRate}%</td>
                    <td>${d.usefulLifeYears} Years</td>
                    <td>${d.depreciationMethod}</td>
                    <td>₹${d.yearlyDepreciationAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</td>
                </tr>
            `).join('');

        } catch (err) {
            console.error('Error loading depreciation records:', err);
        }
    }

    document.getElementById('form-create-depreciation').addEventListener('submit', async (e) => {
        e.preventDefault();
        const assetName = document.getElementById('dep-name').value;
        const purchaseCost = parseFloat(document.getElementById('dep-cost').value);
        const salvageValue = parseFloat(document.getElementById('dep-salvage').value);
        const usefulLifeYears = parseInt(document.getElementById('dep-life').value);
        const depreciationMethod = document.getElementById('dep-method').value;

        try {
            const res = await fetch('/api/depreciation', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ assetName, purchaseCost, salvageValue, usefulLifeYears, depreciationMethod })
            });

            if (res.ok) {
                document.getElementById('form-create-depreciation').reset();
                loadDepreciationSchedules();
            } else {
                alert('Error creating depreciation schedules.');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 10: GST & Tax Filing ---
    async function loadTaxGSTReturns() {
        try {
            const resGst = await fetch('/api/gst-returns');
            const gstList = await resGst.json();

            const resTax = await fetch('/api/tax-returns');
            const taxList = await resTax.json();

            const tbody = document.getElementById('tax-gst-tbody');
            let html = '';

            gstList.forEach(g => {
                html += `
                    <tr>
                        <td><strong>${g.returnPeriod}</strong></td>
                        <td>GST Supply (GSTR-3B)</td>
                        <td>₹${g.grossTurnover.toLocaleString('en-IN')}</td>
                        <td>₹${(g.taxCollected - g.taxPaid).toLocaleString('en-IN')}</td>
                        <td><span class="status-pill status-approved">FILED</span></td>
                    </tr>
                `;
            });

            taxList.forEach(t => {
                html += `
                    <tr>
                        <td><strong>AY ${t.assessmentYear}</strong></td>
                        <td>Corporate Income Tax (ITR-6)</td>
                        <td>₹${t.totalRevenue.toLocaleString('en-IN')}</td>
                        <td>₹${t.taxLiability.toLocaleString('en-IN')}</td>
                        <td><span class="status-pill status-approved">FILED</span></td>
                    </tr>
                `;
            });

            tbody.innerHTML = html || '<tr><td colspan="5" class="text-center text-secondary">No files submitted yet</td></tr>';

        } catch (err) {
            console.error('Error loading tax list:', err);
        }
    }

    document.getElementById('form-create-tax').addEventListener('submit', async (e) => {
        e.preventDefault();
        const type = document.getElementById('tax-type').value;
        const period = document.getElementById('tax-period').value;
        const turnover = parseFloat(document.getElementById('tax-turnover').value);
        const due = parseFloat(document.getElementById('tax-due').value);

        try {
            let res;
            if (type.includes('GST')) {
                res = await fetch('/api/gst-returns', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        returnPeriod: period,
                        grossTurnover: turnover,
                        taxCollected: due,
                        taxPaid: 0.0
                    })
                });
            } else {
                res = await fetch('/api/tax-returns', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        assessmentYear: period,
                        totalRevenue: turnover,
                        taxLiability: due,
                        deductions: 0.0
                    })
                });
            }

            if (res.ok) {
                document.getElementById('form-create-tax').reset();
                loadTaxGSTReturns();
            } else {
                alert('Tax filing transmission failed.');
            }

        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab 11: Procurement Tenders ---
    async function loadTenderRequests() {
        try {
            const res = await fetch('/api/tenders');
            const data = await res.json();

            const tbody = document.getElementById('tenders-tbody');
            tbody.innerHTML = data.map(t => `
                <tr>
                    <td><strong>${t.title}</strong></td>
                    <td>${t.department}</td>
                    <td>${t.description}</td>
                    <td>${t.dueDate}</td>
                    <td><span class="status-pill ${t.status === 'CLOSED' ? 'status-rejected' : 'status-approved'}">${t.status}</span></td>
                    <td>
                        ${t.status === 'OPEN' 
                            ? `<button class="btn btn-danger btn-sm btn-close-tender" data-id="${t.id}">Close Tender</button>` 
                            : 'Closed'}
                    </td>
                </tr>
            `).join('');

            document.querySelectorAll('.btn-close-tender').forEach(btn => {
                btn.addEventListener('click', async () => {
                    const resClose = await fetch(`/api/tenders/${btn.getAttribute('data-id')}/close`, { method: 'POST' });
                    if (resClose.ok) loadTenderRequests();
                });
            });

        } catch (err) {
            console.error('Error loading tenders:', err);
        }
    }

    // --- Tab 12: Compliance Logs ---
    async function loadComplianceLogs() {
        try {
            const res = await fetch('/api/compliance/logs');
            const data = await res.json();

            const tbody = document.getElementById('compliance-logs-tbody');
            tbody.innerHTML = data.map(log => {
                const isHigh = log.riskLevel === 'HIGH';
                return `
                    <tr class="${isHigh ? 'text-danger' : ''}">
                        <td><code>${log.timestamp?.substring(11, 19) || 'N/A'}</code></td>
                        <td><strong>${log.username}</strong></td>
                        <td>${log.role}</td>
                        <td>${log.action}</td>
                        <td><code>${log.ipAddress}</code></td>
                        <td><small>${log.userAgent?.substring(0, 30) || 'Direct Service'}...</small></td>
                        <td><span class="status-pill ${isHigh ? 'status-high-risk' : 'status-approved'}">${log.riskLevel}</span></td>
                    </tr>
                `;
            }).join('');

        } catch (err) {
            console.error('Error loading compliance log records:', err);
        }
    }

    document.getElementById('btn-refresh-logs').addEventListener('click', loadComplianceLogs);

    // --- Tab 13: CA Knowledge Base ---
    let knowledgeTopics = [];
    async function loadKnowledgeBase() {
        try {
            const res = await fetch('/api/search/knowledge?query=');
            const data = await res.json();
            knowledgeTopics = data;

            const list = document.getElementById('knowledge-topics-list');
            list.innerHTML = data.map((t, idx) => `
                <div class="list-group-item" data-index="${idx}">
                    <h4>${t.topicName}</h4>
                    <p>${t.subTopic}</p>
                </div>
            `).join('');

            document.querySelectorAll('#knowledge-topics-list .list-group-item').forEach(item => {
                item.addEventListener('click', () => {
                    const idx = parseInt(item.getAttribute('data-index'));
                    displayTopicDetail(knowledgeTopics[idx]);
                });
            });

        } catch (err) {
            console.error('Error loading knowledge topics:', err);
        }
    }

    function displayTopicDetail(topic) {
        const details = document.getElementById('knowledge-topic-details');
        details.innerHTML = `
            <h2>${topic.topicName} - ${topic.subTopic}</h2>
            <div class="compliance-box info-box mt-3 mb-3">
                <i class="fa-solid fa-graduation-cap"></i>
                <span>Standard Accounting Practice Reference</span>
            </div>
            <div class="mt-3">
                <strong>Accounting Standards Reference Guidelines:</strong>
                <p class="mt-2 text-secondary">${topic.description}</p>
            </div>
            <div class="mt-4 p-3" style="background: rgba(0,0,0,0.2); border-radius: 8px; border-left: 3px solid var(--color-info);">
                <strong>Formula / Regulation Rule:</strong>
                <p class="mt-2 font-mono text-info">${topic.formula}</p>
            </div>
        `;
    }

    // --- Header Autocomplete Global Search Suggestions ---
    const globalSearchInput = document.getElementById('global-search-input');
    const globalSuggestionsDropdown = document.getElementById('global-suggestions');

    globalSearchInput.addEventListener('input', async () => {
        const query = globalSearchInput.value.trim();
        if (query.length < 2) {
            globalSuggestionsDropdown.style.display = 'none';
            return;
        }

        try {
            // Fetch autocomplete suggestions
            const res = await fetch(`/api/search/suggestions?query=${encodeURIComponent(query)}`);
            const suggestions = await res.json();

            if (suggestions && suggestions.length > 0) {
                globalSuggestionsDropdown.style.display = 'block';
                globalSuggestionsDropdown.innerHTML = suggestions.map(s => `
                    <li class="suggestion-item">${s}</li>
                `).join('');

                document.querySelectorAll('.suggestion-item').forEach(li => {
                    li.addEventListener('click', () => {
                        globalSearchInput.value = li.textContent;
                        globalSuggestionsDropdown.style.display = 'none';
                        triggerSearchAction(li.textContent);
                    });
                });
            } else {
                globalSuggestionsDropdown.style.display = 'none';
            }
        } catch (err) {
            console.error(err);
        }
    });

    document.addEventListener('click', (e) => {
        if (!e.target.closest('.header-search')) {
            globalSuggestionsDropdown.style.display = 'none';
        }
    });

    function triggerSearchAction(searchTerm) {
        // Quick tab navigation matching search
        const term = searchTerm.toLowerCase();
        if (term.includes('audit') || term.includes('log') || term.includes('compliance')) {
            document.querySelector('[data-tab="compliance"]').click();
        } else if (term.includes('recon') || term.includes('bank')) {
            document.querySelector('[data-tab="reconciliation"]').click();
        } else if (term.includes('loan') || term.includes('interest') || term.includes('credit')) {
            document.querySelector('[data-tab="loans"]').click();
        } else if (term.includes('invoice') || term.includes('customer')) {
            document.querySelector('[data-tab="invoices"]').click();
        } else if (term.includes('payment') || term.includes('vendor')) {
            document.querySelector('[data-tab="payments"]').click();
        } else if (term.includes('depreciation') || term.includes('asset')) {
            document.querySelector('[data-tab="depreciation"]').click();
        } else if (term.includes('tax') || term.includes('gst')) {
            document.querySelector('[data-tab="tax-gst"]').click();
        } else {
            // Fallback to Knowledge Base search
            document.querySelector('[data-tab="knowledge"]').click();
            displayTopicBySearchTerm(searchTerm);
        }
    }

    async function displayTopicBySearchTerm(term) {
        try {
            const res = await fetch(`/api/search/knowledge?query=${encodeURIComponent(term)}`);
            const data = await res.json();
            if (data && data.length > 0) {
                displayTopicDetail(data[0]);
            }
        } catch (err) {
            console.error(err);
        }
    }

    // --- Tab: Teller Cash Vault Desk ---
    async function loadTellerVault() {
        try {
            const res = await fetch('/api/banking/vault');
            const data = await res.json();

            // Display vault balance
            const balance = data.balance || 0;
            document.getElementById('denom-calculated-total').textContent = '₹' + balance.toLocaleString('en-IN', { minimumFractionDigits: 2 });

            // Render transactions
            const tbody = document.getElementById('vault-tx-tbody');
            if (data.transactions && data.transactions.length > 0) {
                tbody.innerHTML = data.transactions.map(t => `
                    <tr>
                        <td><code>${t.tellerId}</code></td>
                        <td>${t.transactionType === 'DEPOSIT' ? '<span class="status-pill status-approved">Deposit</span>' : '<span class="status-pill status-rejected">Dispense</span>'}</td>
                        <td><strong style="font-family:var(--font-mono)">₹${t.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td><small>${t.timestamp?.substring(11, 19) || 'N/A'}</small></td>
                        <td><small style="color:var(--text-muted);">${t.denominationSummary || 'Standard transfer'}</small></td>
                    </tr>
                `).reverse().join('');
            } else {
                tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted" style="padding:15px;">No cash transactions registered today</td></tr>';
            }

        } catch (err) {
            console.error('Error loading vault details:', err);
            showToast('Failed to load vault desk ledger', 'error');
        }
    }

    // Tally denomination calculator
    const denomBtn = document.getElementById('btn-calc-denom-total');
    if (denomBtn) {
        denomBtn.addEventListener('click', (e) => {
            e.preventDefault();
            let total = 0;
            document.querySelectorAll('.denom-input').forEach(input => {
                const count = parseInt(input.value) || 0;
                const value = parseInt(input.getAttribute('data-value')) || 0;
                total += count * value;
            });
            document.getElementById('denom-calculated-total').textContent = '₹' + total.toLocaleString('en-IN', { minimumFractionDigits: 2 });
            showToast('Denominations calculated and tallied', 'success');
        });
    }

    document.getElementById('form-vault-tx').addEventListener('submit', async (e) => {
        e.preventDefault();
        const tellerId = document.getElementById('vault-teller-id').value;
        const transactionType = document.getElementById('vault-type').value;
        const amount = parseFloat(document.getElementById('vault-amount').value);

        // Gather denoms summary
        const denomObj = {};
        document.querySelectorAll('.denom-input').forEach(input => {
            const count = parseInt(input.value) || 0;
            if (count > 0) {
                denomObj[input.getAttribute('data-value')] = count;
            }
        });
        const denominationSummary = JSON.stringify(denomObj);

        try {
            const res = await fetch('/api/banking/vault', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ tellerId, transactionType, amount, denominationSummary })
            });

            if (res.ok) {
                showToast(`Cash ${transactionType.toLowerCase()} posted to vault`, 'success');
                document.getElementById('form-vault-tx').reset();
                document.querySelectorAll('.denom-input').forEach(inp => inp.value = 0);
                loadTellerVault();
                updateCardsMetrics();
            } else {
                showToast('Failed to post vault transaction', 'error');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab: Fixed Deposits & RD ---
    async function loadFixedDeposits() {
        try {
            const res = await fetch('/api/banking/fds');
            const data = await res.json();

            const tbody = document.getElementById('fds-tbody');
            if (data && data.length > 0) {
                tbody.innerHTML = data.map(fd => `
                    <tr>
                        <td><strong>${fd.customerName}</strong></td>
                        <td><strong style="font-family:var(--font-mono)">₹${fd.principalAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td>${fd.interestRate}%</td>
                        <td>${fd.termMonths} Months</td>
                        <td><strong style="font-family:var(--font-mono);color:var(--brand-blue)">₹${fd.maturityAmount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td>${statusPill(fd.status)}</td>
                        <td>
                            ${fd.status === 'ACTIVE' 
                                ? `<button class="btn btn-success btn-xs btn-mature-fd" data-id="${fd.id}"><i class="fa-solid fa-circle-check"></i> Mature</button>` 
                                : '<span class="text-muted">Settled</span>'}
                        </td>
                    </tr>
                `).join('');

                document.querySelectorAll('.btn-mature-fd').forEach(btn => {
                    btn.addEventListener('click', async () => {
                        const resMature = await fetch(`/api/banking/fds/${btn.getAttribute('data-id')}/mature`, { method: 'POST' });
                        if (resMature.ok) {
                            showToast('Fixed deposit interest accrued & matured', 'success');
                            loadFixedDeposits();
                            updateCardsMetrics();
                        }
                    });
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted" style="padding:15px;">No active term deposits</td></tr>';
            }

        } catch (err) {
            console.error('Error loading FDs:', err);
        }
    }

    document.getElementById('form-create-fd').addEventListener('submit', async (e) => {
        e.preventDefault();
        const customerName = document.getElementById('fd-customer').value;
        const principalAmount = parseFloat(document.getElementById('fd-principal').value);
        const interestRate = parseFloat(document.getElementById('fd-rate').value);
        const termMonths = parseInt(document.getElementById('fd-term').value);

        try {
            const res = await fetch('/api/banking/fds', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ customerName, principalAmount, interestRate, termMonths })
            });

            if (res.ok) {
                showToast('Fixed deposit created successfully', 'success');
                document.getElementById('form-create-fd').reset();
                loadFixedDeposits();
                updateCardsMetrics();
            } else {
                showToast('Failed to create Fixed Deposit', 'error');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab: Cheques ---
    async function loadCheques() {
        try {
            const res = await fetch('/api/banking/cheques');
            const data = await res.json();

            const tbody = document.getElementById('cheques-tbody');
            if (data && data.length > 0) {
                tbody.innerHTML = data.map(c => `
                    <tr>
                        <td><strong style="font-family:var(--font-mono)">${c.chequeNumber}</strong></td>
                        <td>${c.payName}</td>
                        <td>${c.bankName}</td>
                        <td><code style="font-family:var(--font-mono);font-size:11px;">${c.transitCode}</code></td>
                        <td><strong style="font-family:var(--font-mono)">₹${c.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td>${statusPill(c.status)}</td>
                        <td>
                            ${c.status === 'IN_TRANSIT' 
                                ? `<button class="btn btn-success btn-xs btn-clear-cheque" data-id="${c.id}"><i class="fa-solid fa-check"></i> Clear</button>
                                   <button class="btn btn-danger btn-xs btn-bounce-cheque" data-id="${c.id}" style="margin-left:4px;"><i class="fa-solid fa-xmark"></i> Bounce</button>` 
                                : '<span class="text-muted">Completed</span>'}
                        </td>
                    </tr>
                `).join('');

                document.querySelectorAll('.btn-clear-cheque').forEach(btn => {
                    btn.addEventListener('click', async () => {
                        const resClear = await fetch(`/api/banking/cheques/${btn.getAttribute('data-id')}/clear`, { method: 'POST' });
                        if (resClear.ok) {
                            showToast('Cheque cleared and funds transferred', 'success');
                            loadCheques();
                            updateCardsMetrics();
                        }
                    });
                });

                document.querySelectorAll('.btn-bounce-cheque').forEach(btn => {
                    btn.addEventListener('click', async () => {
                        const resBounce = await fetch(`/api/banking/cheques/${btn.getAttribute('data-id')}/bounce`, { method: 'POST' });
                        if (resBounce.ok) {
                            showToast('Cheque bounced. Compliance log triggered', 'warning');
                            loadCheques();
                            updateCardsMetrics();
                        }
                    });
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="7" class="text-center text-muted" style="padding:15px;">No cheques in transit</td></tr>';
            }

        } catch (err) {
            console.error('Error loading cheques:', err);
        }
    }

    // Interactive cheque visual binder
    const chNumInput = document.getElementById('ch-number');
    const chPayInput = document.getElementById('ch-payee');
    const chBankInput = document.getElementById('ch-bank');
    const chIfscInput = document.getElementById('ch-ifsc');
    const chAmtInput = document.getElementById('ch-amount');

    function updateChequeVisual() {
        document.getElementById('visual-cheque-bank').textContent = chBankInput.value || 'Aetheris Clearing Bank';
        const amt = parseFloat(chAmtInput.value) || 0;
        document.getElementById('visual-cheque-amount').textContent = '₹' + amt.toLocaleString('en-IN', { minimumFractionDigits: 2 });
        document.getElementById('visual-cheque-no').textContent = 'CQ-' + (chNumInput.value || 'XXXXXX');
        document.getElementById('visual-cheque-ifsc').textContent = 'IFSC: ' + (chIfscInput.value || 'XXXXXXX');
    }

    if (chNumInput) {
        [chNumInput, chPayInput, chBankInput, chIfscInput, chAmtInput].forEach(inp => {
            inp.addEventListener('input', updateChequeVisual);
        });
    }

    document.getElementById('form-create-cheque').addEventListener('submit', async (e) => {
        e.preventDefault();
        const chequeNumber = chNumInput.value;
        const payName = chPayInput.value;
        const bankName = chBankInput.value;
        const transitCode = chIfscInput.value;
        const amount = parseFloat(chAmtInput.value);

        try {
            const res = await fetch('/api/banking/cheques', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ chequeNumber, payName, bankName, transitCode, amount })
            });

            if (res.ok) {
                showToast('Cheque submitted to clearing house', 'success');
                document.getElementById('form-create-cheque').reset();
                updateChequeVisual();
                loadCheques();
                updateCardsMetrics();
            } else {
                showToast('Failed to submit cheque', 'error');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Tab: Standing Instructions ---
    async function loadStandingInstructions() {
        try {
            const res = await fetch('/api/banking/instructions');
            const data = await res.json();

            // Populate accounts in dropdown
            const select = document.getElementById('si-source');
            if (select && accountsList.length > 0) {
                select.innerHTML = accountsList.map(a => `<option value="${a.name}">${a.name} (Code: ${a.code})</option>`).join('');
            }

            const tbody = document.getElementById('si-tbody');
            if (data && data.length > 0) {
                tbody.innerHTML = data.map(si => `
                    <tr>
                        <td>${si.sourceAccount}</td>
                        <td><strong>${si.beneficiaryName}</strong></td>
                        <td><code>${si.destinationAccount}</code></td>
                        <td><strong style="font-family:var(--font-mono)">₹${si.amount.toLocaleString('en-IN', { minimumFractionDigits: 2 })}</strong></td>
                        <td><span class="badge badge-blue">${si.frequency}</span></td>
                        <td>${si.startDate}</td>
                        <td>${statusPill(si.status)}</td>
                        <td>
                            <button class="btn btn-secondary btn-xs btn-toggle-si" data-id="${si.id}">
                                <i class="fa-solid fa-repeat"></i> Toggle
                            </button>
                        </td>
                    </tr>
                `).join('');

                document.querySelectorAll('.btn-toggle-si').forEach(btn => {
                    btn.addEventListener('click', async () => {
                        const resToggle = await fetch(`/api/banking/instructions/${btn.getAttribute('data-id')}/toggle`, { method: 'POST' });
                        if (resToggle.ok) {
                            showToast('Standing order status toggled', 'info');
                            loadStandingInstructions();
                        }
                    });
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="8" class="text-center text-muted" style="padding:15px;">No active standing instructions configured</td></tr>';
            }

        } catch (err) {
            console.error('Error loading SI:', err);
        }
    }

    document.getElementById('form-create-si').addEventListener('submit', async (e) => {
        e.preventDefault();
        const sourceAccount = document.getElementById('si-source').value;
        const beneficiaryName = document.getElementById('si-beneficiary').value;
        const destinationAccount = document.getElementById('si-dest').value;
        const amount = parseFloat(document.getElementById('si-amount').value);
        const frequency = document.getElementById('si-freq').value;

        try {
            const res = await fetch('/api/banking/instructions', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ sourceAccount, beneficiaryName, destinationAccount, amount, frequency })
            });

            if (res.ok) {
                showToast('Auto-debit standing order established', 'success');
                document.getElementById('form-create-si').reset();
                loadStandingInstructions();
                updateCardsMetrics();
            } else {
                showToast('Failed to deploy standing instruction', 'error');
            }
        } catch (err) {
            console.error(err);
        }
    });

    // --- Init call ---
    loadOverviewData();
});


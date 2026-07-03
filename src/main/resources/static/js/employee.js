const employeeSearch = document.getElementById('employee-search');
const employeeSuggestions = document.getElementById('employee-suggestions');
const employeeDetails = document.getElementById('employee-details');

async function searchEmployees(query) {
    const url = '/api/employees/suggest' + (query ? `?query=${encodeURIComponent(query)}` : '');
    const response = await fetch(url);
    return response.json();
}

async function getEmployeeHistory(employeeId) {
    const response = await fetch(`/api/employees/${employeeId}/history`);
    return response.json();
}

function renderEmployeeSuggestions(employees) {
    if (!employees || employees.length === 0) {
        employeeSuggestions.innerHTML = '<p>No employees found</p>';
        return;
    }
    employeeSuggestions.innerHTML = employees.map(emp => `
        <div class="employee-item" onclick="selectEmployee(${emp.id})">
            <strong>${emp.name}</strong> (${emp.employeeId}) - ${emp.designation}
        </div>
    `).join('');
}

async function selectEmployee(employeeId) {
    const history = await getEmployeeHistory(employeeId);
    employeeDetails.innerHTML = `
        <h3>${history.employeeName}</h3>
        <p><strong>Employee ID:</strong> ${history.employeeId}</p>
        <div class="bank-details">
            ${history.bankDetails.map(detail => `<p>${detail}</p>`).join('')}
        </div>
    `;
    employeeSuggestions.innerHTML = '';
    employeeSearch.value = history.employeeName;
}

employeeSearch.addEventListener('input', async () => {
    const employees = await searchEmployees(employeeSearch.value);
    renderEmployeeSuggestions(employees);
});

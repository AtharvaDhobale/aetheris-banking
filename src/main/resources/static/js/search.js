const input = document.getElementById('search-input');
const button = document.getElementById('search-button');
const suggestions = document.getElementById('suggestions');
const results = document.getElementById('knowledge-results');

async function fetchSuggestions(query) {
    const url = '/api/search/suggestions' + (query ? `?query=${encodeURIComponent(query)}` : '');
    const response = await fetch(url);
    return response.json();
}

async function fetchKnowledge(query) {
    const url = '/api/search/knowledge' + (query ? `?query=${encodeURIComponent(query)}` : '');
    const response = await fetch(url);
    return response.json();
}

function renderSuggestions(items) {
    suggestions.innerHTML = items.map(item => `<li>${item}</li>`).join('');
}

function renderKnowledge(items) {
    if (!items || items.length === 0) {
        results.innerHTML = '<p>No matching knowledge topics found.</p>';
        return;
    }
    results.innerHTML = `
        <h2>Knowledge Results</h2>
        ${items.map(item => `
            <div class="section-card">
                <h3>${item.title}</h3>
                <p><strong>Category:</strong> ${item.category}</p>
                <p>${item.summary}</p>
                <p><strong>Formula:</strong> ${item.formula || 'N/A'}</p>
            </div>
        `).join('')}
    `;
}

input.addEventListener('input', async () => {
    const items = await fetchSuggestions(input.value);
    renderSuggestions(items);
});

button.addEventListener('click', async () => {
    const query = input.value;
    const items = await fetchKnowledge(query);
    renderKnowledge(items);
});

window.addEventListener('load', async () => {
    renderSuggestions(await fetchSuggestions(''));
    renderKnowledge(await fetchKnowledge(''));
});

document.addEventListener('DOMContentLoaded', function() {
    fetch('/languages')
        .then(response => response.json())
        .then(languages => {
            const fromSelect = document.getElementById('from');
            const toSelect = document.getElementById('to');
            for (const [code, name] of Object.entries(languages)) {
                const optionFrom = document.createElement('option');
                optionFrom.value = code;
                optionFrom.text = name;
                fromSelect.appendChild(optionFrom);

                const optionTo = document.createElement('option');
                optionTo.value = code;
                optionTo.text = name;
                toSelect.appendChild(optionTo);
            }
        })
        .catch(error => console.error('Error fetching languages:', error));
});

function translateText() {
    var text = document.getElementById('text').value;
    var from = document.getElementById('from').value;
    var to = document.getElementById('to').value;

    var requestBody = JSON.stringify({
        from: from,
        to: to,
        text: text
    });

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/submit', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function () {
        if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            document.getElementById('output').innerText = response.translatedText;
        } else {
            var response = JSON.parse(xhr.responseText);
            document.getElementById('output').innerText = response.translatedText || 'Error translating text.';
        }
    };
    xhr.send(requestBody);
}

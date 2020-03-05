function openSignUpPage() {
    window.location = "/QuizHubApplication/SignUpPage.html";
}

function openLoginPage() {
    window.location = "/QuizHubApplication/LoginPage.html";
}

function addUser() {

    var details, statusCode;
    var url = "http://localhost:8080/api/user/signup";
    var response = makeAjaxRequest(url, {
        method: 'POST',
        request: {
            userId: document.getElementById('userId').value,
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            company: document.getElementById('company').value
        },
        async: true
    }, function(details, statusCode) {
        if (statusCode === 200) {
            loginUser(email, password);
        }
    });

}


function loginUser(emailNew, passwordNew) {

    var details, statusCode;
    var url = "http://localhost:8080/api/user/login";
    var response = makeAjaxRequest(url, {
        method: 'POST',
        request: {
            //userId: document.getElementById('userTd').value,
            // firstName: document.getElementById('firstName').value,
            // lastName: document.getElementById('lastName').value,
            email: emailNew,
            password: passwordNew
                // company: document.getElementById('company').value
        },
        async: true
    }, function(details, statusCode) {
        if (statusCode === 200) {
            location.replace("http://localhost:8080/api/user/dashboard");
        }
    });

}


function makeAjaxRequest(url, payload, callback) {

    if (!url)
        throw new Error("Invalid url to make ajax request");

    if (!payload)
        throw new Error("Invalid details");
    var http = new XMLHttpRequest();
    method = payload.method;
    if (typeof(payload.request) == "string") {
        url += payload.request;
    }

    if (method == "GET") {
        http.open(method, url, payload.async);
        http.send();
    } else {
        http.open("POST", url, payload.async);
        http.send(JSON.stringify(payload.request));
    }
    http.onreadystatechange = function() { // Call a function when the
        // state changes.
        if (http.readyState == 4 && http.status == 200) {
            callback(this.responseText, http.status);
        }

        if (http.readyState == 4 && http.status == 409) {
            callback(JSON.parse(this.responseText));
        }


    }
}
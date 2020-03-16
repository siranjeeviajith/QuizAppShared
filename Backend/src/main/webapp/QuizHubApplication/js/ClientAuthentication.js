function openSignUpPage() {
    window.location = "/QuizHubApplication/html/SignUpPage.html";
}

function openLoginPage() {
    window.location = "/QuizHubApplication/html/LoginPage.html";
}


function addClient() {


    var url = "/api/client/clientSignup";
    makeAjaxCall(url, {
        method: 'POST',
        request: {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            company: document.getElementById('company').value
        },
        async: true
    }).then((signUpResponse) => {
        console.log("sign up successful", signUpResponse);
        loginClient(document.getElementById('email').value, document.getElementById('password').value);
    }).catch(error => { console.log("error", error); });

}


function loginClient(emailNew, passwordNew) {

    var password, email;
    if (arguments.length == 2) {
        email = emailNew;
        password = passwordNew;
    } else {
        email = document.getElementById('email').value;
        password = document.getElementById('password').value;
    }
    var url = "/api/client/clientLogin";
    makeAjaxCall(url, {
        method: 'POST',
        request: {
            email: email,
            password: password
        },
        async: true
    }).then((LoginResponse) => {
        console.log("Log in successful", LoginResponse);
        window.location.replace("/QuizHubApplication/html/Dashboard.html")
    }).catch(error => { console.log("error", error); });

}

function logOutClient() {
    var url = "/api/logout";
    makeAjaxCall(url, {
        method: 'GET',
        async: true
    }).then((LogOutResponse) => {
        console.log("Logged out successful", LogOutResponse);
        window.location.replace("/");
    }).catch(error => { console.log("error", error); });


}
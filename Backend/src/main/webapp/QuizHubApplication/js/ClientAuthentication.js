function openSignUpPage() {
    window.location = "/QuizHubApplication/html/SignUpPage.html";
}

function openLoginPage() {
    window.location = "/QuizHubApplication/html/LoginPage.html";
}

function checkEmptyLogIn() {
    if (document.getElementById('email').value == "" || document.getElementById('password').value == "") {
        document.getElementById('error').innerHTML = "Please fill in all the fields";
        document.getElementById('error').style.color = "red";
        return false;
    }
    var emailValid = checkValidEmail();
    if (emailValid == false) {
        document.getElementById('error').innerHTML = "Please enter a valid email id";
        document.getElementById('error').style.color = "red";
        return false;
    }
    document.getElementById('error').innerHTML = "";
    return true;
}

function checkEmptySignUp() {
    if (document.getElementById('firstName').value == "" || document.getElementById('lastName').value == "" || document.getElementById('email').value == "" || document.getElementById('password').value == "" || document.getElementById('company').value == "") {
        document.getElementById('error').innerHTML = "Please fill in all the fields";
        document.getElementById('error').style.color = "red";
        return false;
    }
    var emailValid = checkValidEmail();
    if (emailValid == false) {
        document.getElementById('error').innerHTML = "Please enter a valid email id";
        document.getElementById('error').style.color = "red";
        return false;
    }
    document.getElementById('error').innerHTML = "";
    return true;
}

function checkValidEmail() {
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    var userEmail = document.getElementById("email").value;
    if (reg.test(userEmail) == false) {
        return false;
    }
    return true;
}

function addClient() {
    var validDetails = checkEmptySignUp();
    if (validDetails == false) {
        return false;
    }

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
    var validDetails = checkEmptyLogIn();
    if (validDetails == false) {
        return false;
    }
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
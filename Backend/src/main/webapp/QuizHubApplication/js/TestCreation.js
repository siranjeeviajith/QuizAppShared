function openCreateTestForm() {
    document.getElementById('validateEmailForm').style.display = "block";
}

function cancelTestForm() {
    document.getElementById('validateEmailForm').style.display = "none";
}

function checkEmail() {
    var url = "http://localhost:8080/api/user/checkEmail";
    makeAjaxCall(url, {
        method: 'GET',
        request: "?email=" + document.getElementById("userEmail").value,
        async: true
    }).then((checkEmailResponse) => {
        if (status == 200) {
            window.location.replace("/QuizHubApplication/html/CreateTest.html");

        }


    }).catch(error => {

        if (status == 302) {

            document.getElementById("userEmailID").innerHTML = document.getElementById("userEmail").value;
            openUserSignupForm();
        }

        console.log("error", error);

    });
}
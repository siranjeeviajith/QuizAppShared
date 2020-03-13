function openUserSignUpForm() {
    cancelTestForm();
    document.getElementById('userSignUpForm').style.display = "block";
}

function cancelAddQuestionForm() {
    document.getElementById('userSignUpForm').style.display = "none";
}

function checkEmptyUserSignUpForm() {


    if (document.getElementById('userPassword').value == "" || document.getElementById('firstNameUser').value == "" || document.getElementById('lastNameUser').value == "") {
        var errorMsg = "Please fill out the fields";
        document.getElementById("errorMsg").innerHTML = errorMsg;
        return false;
    } else {
        addUser();

    }

}

function addUser()

{
    var url = "http://localhost:8080/api/client/userSignup";
    makeAjaxCall(url, {
        method: 'POST',
        request: {
            firstName: document.getElementById('firstNameUser').value,
            lastName: document.getElementById('lastNameUser').value,
            email: document.getElementById('userEmailID').value,
            password: document.getElementById('userPassword').value
        },
        async: true
    }).then((signUpResponse) => {
        console.log("sign up successful", signUpResponse);
        window.location.replace("/QuizHubApplication/html/CreateTest.html");
    }).catch(error => { console.log("error", error); });



}
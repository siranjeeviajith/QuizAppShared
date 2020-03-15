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
    localStorage.setItem("userEmail", document.getElementById('userEmailID').textContent);
    makeAjaxCall(url, {
        method: 'POST',
        request: {
            firstName: document.getElementById('firstNameUser').value,
            lastName: document.getElementById('lastNameUser').value,
            email: document.getElementById('userEmailID').textContent,
            password: document.getElementById('userPassword').value
        },
        async: true
    }).then((signUpResponse) => {
        console.log("sign up successful", signUpResponse);
                   let emailUser=document.getElementById("userEmailID").textContent;
//                document.getElementById("userEmailCreateTest").srcdoc= emailUser;
        // document.getElementById("userEmailCreateTest").innerHTML = emailUser;
        window.location.replace("/QuizHubApplication/html/CreateTest.html");
    }).catch(error => { console.log("error", error); });



}
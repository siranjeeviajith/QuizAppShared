function openCreateTestForm() {
    document.getElementById('validateEmailForm').style.display = "block";
}

function cancelTestForm() {
    document.getElementById('validateEmailForm').style.display = "none";
}

function checkValidEmail() {
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    var userEmail = document.getElementById("userEmail").value;
    if (reg.test(userEmail) == false) {
        document.getElementById("demo").style.color = "red";
        document.getElementById("demo").innerHTML = "Please enter a valid email";
        return false;
    } else {
        checkEmailExists(userEmail);
    }
}

function checkEmailExists(userEmail) {
    var url = "http://localhost:8080/api/user/checkEmail";
    makeAjaxCall(url, {
        method: 'GET',
        request: "?email=" + userEmail,
        async: true
    }).then((checkEmailResponse) => {
                 localStorage.setItem("userEmail", document.getElementById("userEmail").value);
                         window.location.replace("/QuizHubApplication/html/CreateTest.html");
                     }).catch(error => {
                         document.getElementById("userEmailID").innerHTML = document.getElementById("userEmail").value;
                         openUserSignUpForm();
                     });
}

function getQuestionsFromTag() {
    var tag = document.getElementById("tag");
    var selectedTag = tag.options[tag.selectedIndex].value;
    var url = "http://localhost:8080/api/question/getQuestion/" + selectedTag;
    makeAjaxCall(url, {
        method: 'GET',
        async: true
    }).then((getQuestionsResponse) => {
        var questions = getQuestionsResponse.data.questions;
        displayQuestions(questions);
        console.log(getQuestionsResponse);
    }).catch(error => {
        console.log("error", error);
    });

}


// var questions = [{
//         "id": "1fdbdfvsdxcv",
//         "modifiedAt": 1584165884859,
//         "createdAt": 1584165884859,
//         "tag": "html",
//         "status": "ACTIVE",
//         "description": "what is html",
//         "option": { "A": "language", "B": "nothinh", "C": "you", "D": "blabls" },
//         "correctAns": "A"
//     },
//     {
//         "id": "2dsgdfhbgfn",
//         "modifiedAt": 1584165884859,
//         "createdAt": 1584165884859,
//         "tag": "html",
//         "status": "ACTIVE",
//         "description": "what is html2nd",
//         "option": { "A": "pee", "B": "panni", "C": "eruma", "D": "katteruma" },
//         "correctAns": "A"
//     }
// ];

function displayQuestions(questions) {
    //    var i = 0,
    //         quest = [],

    //         que = document.getElementById('questionsList');
     document.getElementById("quesListForm").innerHTML = "";
    for (var i = 0; i < questions.length; i++) {
        var para = document.createElement("table");
        para.innerHTML = `${i+1}. <input type="checkbox" name="selectedQuestions" id=${questions[i].id} value=${questions[i].id}> ${questions[i].description}
              <td id="option"> A:   ${questions[i].option.A}   B:   ${questions[i].option.B}   C:   ${questions[i].option.C}   D:   ${questions[i].option.D}</td> `
        document.body.appendChild(para);
        document.getElementById("quesListForm").appendChild(para);
    }
}
// quest = questions.map(question => {

//     // document.getElementById("description").innerHTML += question.description;
//     // document.getElementById("option").innerHTML = "A. " + question.option.A + " B. " + question.option.B + " C. " + question.option.C + " D. " + question.option.D;
//     // document.getElementById("optionA").innerText += question.option.A;
//     // document.getElementById("optionB").innerText += question.option.B;
//     // document.getElementById("optionC").innerText += question.option.C;
//     // document.getElementById("optionD").innerText += question.option.D;
//     return `
//     <input type="checkbox" id=${question.id}>
//      <p id="description"> ${question.description}</p>
//        <p id="option"> A:   ${question.option.A}   B:   ${question.option.B}   C:   ${question.option.C}   D:   ${question.option.D}</p>
//      `

// });
// que.innerHTML = quest;
// document.getElementById("questionsList").appendChild(que);
//  <p id="optionB">${q}</p>
// <p id="optionC">optionC</p>
// <p id="optionD">optionD</p>

//}


function sendSelectedQuestions() {
    let items = document.getElementsByName('selectedQuestions');
    let selectedQuestions = [];
    for (var i = 0; i < items.length; i++) {
        if (items[i].type == 'checkbox' && items[i].checked == true)
            selectedQuestions[i] = items[i].value;
    }
    return selectedQuestions;
}

function createTest() {

    let selectedQuestions = sendSelectedQuestions();
    if (selectedQuestions.length <= 1) {
        alert("Select atleast one question");
        return false;
    } else {

        var url = "http://localhost:8080/api/test/generateTestLink";
        makeAjaxCall(url, {
            method: 'POST',
            request: {
                userEmail: document.getElementById("userEmailCreateTest").textContent,
                questionIds: selectedQuestions
            },
            async: true
        }).then((createTestResponse) => {
            alert("Test Created");
            window.location.replace("/QuizHubApplication/html/Dashboard.html");

        }).catch(error => {
            alert("Failed to create test. Try again");
        });

    }


}
function openDisplayQuestions() {
    document.getElementById('DisplayQuestions').style.display = "block";
    getAllQuestions();

}
//Function to Hide Popup
function cancelDisplayQuestions() {
    //  document.getElementById('popupFormDisplayQuestions').reset();
    // localStorage.clickcount = 0;
    // localStorage.clear();
    document.getElementById('DisplayQuestions').style.display = "none";
}


function getAllQuestions() {

    var url = "/api/question/getAllQuestions/";
    makeAjaxCall(url, {
        method: 'GET',
        async: true
    }).then((getAllQuestionsResponse) => {
        var questions = getAllQuestionsResponse.data.questions;
        displayAllQuestions(questions);
        console.log(getQuestionsResponse);
    }).catch(error => {
        document.getElementById('popupFormDisplayQuestions').innerHTML = "";
        document.getElementById('popupFormDisplayQuestions').innerHTML = "no questions to display";
        console.log("error", error);
    });

}



function displayAllQuestions(questions) {

    document.getElementById('popupFormDisplayQuestions').innerHTML = "";
    for (let i = 0; i < questions.length; i++) {
        let para = document.createElement("table");
        para.innerHTML = ` <td name="question" id=${questions[i].id} value=${questions[i].id}>${i+1}. ${questions[i].description}
              <tr id="option"> A:   ${questions[i].option.A}   B:   ${questions[i].option.B}   C:   ${questions[i].option.C}   D:   ${questions[i].option.D}</td> `
        document.getElementById('popupFormDisplayQuestions').appendChild(para);
    }
}
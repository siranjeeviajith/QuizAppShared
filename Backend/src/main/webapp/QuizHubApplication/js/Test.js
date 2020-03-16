function startTest() {
    var currentURL = window.location.href;
    var url = currentURL.replace(/testStart/, 'doTest');
    makeAjaxCall(url, {
        method: 'GET',
        async: true
    }).then((startTestResponse) => {
        var currentURL = window.location.href;
        var newUrl = currentURL.replace(/testStart/, 'doTest');
        location.replace(newUrl);
        //   console.log(startTestResponse);
    }).catch(error => {
        console.log("error", error);
    });

}

function displayQuestionsForTest(questions) {
    for (var i = 0; i < questions.length; i++) {
        var para = document.createElement("table");
        para.innerHTML = `<td id=${questions[i].id} value=${questions[i].id}>${i+1}.${questions[i].description}</td>
              A.<input type="radio" id="A${questions[i].id}" value="A" name=${questions[i].id}> ${questions[i].option.A}  
              B.<input type="radio" id="B${questions[i].id}" value="B" name=${questions[i].id}> ${questions[i].option.B} 
              C.<input type="radio" id="C${questions[i].id}" value="C" name=${questions[i].id}> ${questions[i].option.C} 
              D.<input type="radio" id="D${questions[i].id}" value="D" name=${questions[i].id}> ${questions[i].option.D}  `
            //   document.body.appendChild(para);
        document.getElementById("questions").appendChild(para);
    }
}

function submitTest(questions) {
    var j = 0,
        k = 0,
        queList = [],
        selectedOption,
        id;
    for (var i = 0; i < questions.length; i++) {
        var qid = questions[i].id;
        var optionA = "A" + qid;
        var optionB = "B" + qid;
        var optionC = "C" + qid;
        var optionD = "D" + qid;
        if (document.getElementById(optionA).checked || document.getElementById(optionB).checked || document.getElementById(optionC).checked || document.getElementById(optionD).checked) {
            if (document.getElementById(optionA).checked) {
                queList[j++] = { selectedOption: "A", id: qid };
                // selectedOption[j++] = "A";
                // answeredQue[k++] = qid;
            } else if (document.getElementById(optionB).checked) {
                queList[j++] = { selectedOption: "B", id: qid };
                // selectedOption[j++] = "B";
                // answeredQue[k++] = qid;
            } else if (document.getElementById(optionC).checked) {
                queList[j++] = { selectedOption: "C", id: qid };
                // selectedOption[j++] = "C";
                // answeredQue[k++] = qid;
            } else if (document.getElementById(optionD).checked) {
                queList[j++] = { selectedOption: "D", id: qid };
                // selectedOption[j++] = "D";
                // answeredQue[k++] = qid;
            }

        } //end of big if


        console.log(selectedOption);
        console.log(answeredQue);

    } // end of for
    document.getElementById("questions").innerHTML = "";
    document.write("your test is over");
    fetchResults(JSON.stringify(queList));

}

function fetchResults(queList) {
    var currentURL = window.location.href;
    var url = currentURL.replace(/doTest/, 'submitTest');
    makeAjaxCall(url, {
        method: 'POST',
        request: queList,
        async: true
    }).then((fetchResultResponse) => {
        var currentURL = window.location.href;
        var url = currentURL.replace(/doTest/, 'result');
        makeAjaxCall(url, {
            method: 'GET',
            async: true
        }).then((resultResponse) => {
            var currentURL = window.location.href;
            var newUrl = currentURL.replace(/doTest/, 'result');
            location.replace(newUrl);
        }).catch(error => {
            console.log("error", error);
        });

    }).catch(error => {
        console.log("error", error);
    });

}
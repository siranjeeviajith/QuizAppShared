function startTest() {
    var currentURL = 'http://localhost:8080/api/test/b8d835db-522d-4242-b786-0edae9574167/testStart';
    var url = currentURL.replace(/testStart/, 'doTest');
    makeAjaxCall(url, {
        method: 'GET',
        async: true
    }).then((startTestResponse) => {
        console.log(startTestResponse);
    }).catch(error => {
        console.log("error", error);
    });

}
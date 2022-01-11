const votingUrl = "voting/";
const menuUrl = "menu/voting/";

// https://stackoverflow.com/a/5064235/548473
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });


    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    let id = document.querySelector('input[name="vote"]:checked').value;
    document.getElementById("votedId").value = id;
    enableVoteButton();
});

function castVote() {
    let id = document.querySelector('input[name="vote"]:checked').value;
    var d = new Date();
    $.ajax({
        type: "POST",
        url: votingUrl + id +"?voteDateTime=" + d.toLocaleDateString('en-CA') + "T" + d.toLocaleTimeString('ru'),
        contentType: "application/json; charset=utf-8",
        data: ""
    }).done(function () {
        document.getElementById("votedId").value = id;
        document.getElementById("voteBtn").disabled = true;
        successNoty("voting.saved");
    });
}

function enableVoteButton() {
    let id = document.querySelector('input[name="vote"]:checked').value;
    document.getElementById("voteBtn").disabled = document.getElementById("votedId").value === id;
}

function showMenu(id) {
    var d = new Date();
    $.ajax({
        type: "GET",
        url: menuUrl + id + "?date=" + d.toLocaleDateString('en-CA'),
        dataType: "json",
    }).done(function (objects) {
        let table = document.getElementById("menuTable");
        table.innerHTML = "";

        if (objects.length > 0) {
            for (let i = 0; i < objects.length; i++) {
                let tr = document.createElement("tr");
                let th = document.createElement("th");
                th.setAttribute("scope", "row");
                th.appendChild(document.createTextNode(String(i + 1)));
                tr.appendChild(th);

                let td1 = document.createElement("td");
                td1.appendChild(document.createTextNode(objects[i].name));
                tr.appendChild(td1);

                let td2 = document.createElement("td");
                td2.appendChild(document.createTextNode(objects[i].weight));
                tr.appendChild(td2);

                let td3 = document.createElement("td");
                td3.appendChild(document.createTextNode(objects[i].price));
                tr.appendChild(td3);

                table.appendChild(tr);
            }
            $("#showMenu").modal("show");
        } else {
            $("#showNoMenu").modal("show");
        }

    });

}

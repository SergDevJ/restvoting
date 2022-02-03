const profileAjaxUrl = "profile";

const ctx = {
    ajaxUrl: profileAjaxUrl + "/voting-history",
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: profileAjaxUrl + "/voting-history",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

$(function () {
    init();
});

function init() {
    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/a/4303862/548473
    $.ajaxSetup({cache: false});

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var register = document.getElementById("register").value;
    if (register !== "true") {
        readProfile();
        makeEditable({
            "columns": [
                {
                    "data": "voteDate"
                },
                {
                    "data": "restaurantName"
                },
                {
                    "data": "restaurantId",
                    "visible": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        });
    } else {
        let votingHistoryForm = $('#votingHistoryForm');
        votingHistoryForm.hide();
    }
}

function readProfile() {
    let form = $('#profileForm');
    form.find(":input").val("");
    $.get(profileAjaxUrl + "/personal-data", function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
        });
    });
}

function save() {
    var obj = {};
    obj["id"] = document.getElementById("id").value;
    obj["name"] = document.getElementById("name").value;
    obj["email"] = document.getElementById("email").value;
    obj["password"] = document.getElementById("password").value;
    var jsonData = JSON.stringify(obj);

    var requestType;
    var url;
    if (obj["id"] === "") {
        requestType = "POST";
        url = profileAjaxUrl + "/register";
    } else {
        requestType = "PUT";
        url = profileAjaxUrl;
    }

    $.ajax({
        type: requestType,
        url: url,
        contentType: "application/json; charset=utf-8",
        data: jsonData
    }).done(function () {
        successNoty("common.saved");
        if (obj["id"] === "") window.location.href = "login?message=app.registered&username=" + obj["name"];
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(profileAjaxUrl + "/voting-history", updateTableByData);
}

const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, updateTableByData);
    }
}

function save() {
    var id = document.getElementById("id").value;
    var obj = {};
    obj["id"] = document.getElementById("id").value;
    obj["name"] = document.getElementById("name").value;
    obj["email"] = document.getElementById("email").value.toLowerCase();
    obj["password"] = document.getElementById("password").value;
    var jsonData = JSON.stringify(obj);

    var saveType;
    var saveUrl;
    if (obj["id"] === "") {
        saveType = "POST";
        saveUrl = userAjaxUrl;
    } else {
        saveType = "PUT";
        saveUrl = userAjaxUrl + id;
    }

    $.ajax({
        type: saveType,
        url: saveUrl,
        contentType: "application/json; charset=utf-8",
        data: jsonData
    }).done(function () {
        $("#editRow").modal("hide");
        ctx.updateTable();
        successNoty("common.saved");
    });
}


$(function () {
    makeEditable({
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<a href='mailto:" + data + "'>" + data + "</a>";
                    }
                    return data;
                }
            },
            {
                "data": "roles"
            },
             {
                "orderable": false,
                "defaultContent": "",
                "render": renderEditBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
});
const DishAjaxUrl = "admin/dishes/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: DishAjaxUrl,
    updateTable: function () {
        $.get(DishAjaxUrl, updateTableByData);
    }
}


function save() {
    var id = document.getElementById("id").value;
    var obj = {};
    obj["id"] = document.getElementById("id").value;
    obj["name"] = document.getElementById("name").value;
    obj["weight"] = document.getElementById("weight").value;
    var jsonData = JSON.stringify(obj);

    var saveType;
    var saveUrl;
    if (obj["id"] === "") {
        saveType = "POST";
        saveUrl = DishAjaxUrl;
    } else {
        saveType = "PUT";
        saveUrl = DishAjaxUrl + id;
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
                "data": "weight"
            },
            {
                "render": renderEditBtn,
                "defaultContent": "",
                "orderable": false
            },
            {
                "render": renderDeleteBtn,
                "defaultContent": "",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    })
});
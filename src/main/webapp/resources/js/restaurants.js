const RestaurantAdminAjaxUrl = "admin/restaurants/";
const RestaurantAjaxUrl = "restaurants/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: RestaurantAdminAjaxUrl,
    updateTable: function () {
        $.get(RestaurantAjaxUrl, updateTableByData);
    }
}

function save() {
    var id = document.getElementById("id").value;
    var obj = {};
    obj["id"] = document.getElementById("id").value;
    obj["name"] = document.getElementById("name").value;
    obj["email"] = document.getElementById("email").value;
    obj["address"] = document.getElementById("address").value;
    var jsonData = JSON.stringify(obj);

    var saveType;
    var saveUrl;
    if (obj["id"] === "") {
        saveType = "POST";
        saveUrl = RestaurantAdminAjaxUrl;
    } else {
        saveType = "PUT";
        saveUrl = RestaurantAdminAjaxUrl + id;
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
                "data": "email"
            },
            {
                "data": "address"
            },

            {
                "render": renderMenuBtn,
                "defaultContent": "",
                "orderable": false
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
            },
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });

});

function renderMenuBtn(data, type, row) {
    if (type === "display") {
       return "<a href=admin/restaurant-menu/" + row.id + "><span class='fa fa-bars'></span></a>";
    }
}

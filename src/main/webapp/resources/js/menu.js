const date = new Date();
const RestaurantMenuAjaxUrl = "restaurant-menu/" + document.getElementById("restaurantId").value;
const MenuAjaxUrl = "admin/menu/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: RestaurantMenuAjaxUrl,
    updateTable: function () {
        $.get(RestaurantMenuAjaxUrl, updateTableByData);
    }
}

function addMenuItem() {
    var form = $('#detailsForm');
    form.find(":input").val("");
    $("#editRow").modal();
}

function saveMenuItem() {
    var id = document.getElementById("id").value;
    var obj = {};
    obj["id"] = document.getElementById("id").value;
    obj["restaurantId"] = document.getElementById("restaurantId").value;
    obj["date"] = document.getElementById("date").value;
    obj["dishId"] = document.getElementById("dishList").value;
    obj["price"] = document.getElementById("price").value;
    var jsonData = JSON.stringify(obj);

    var saveType;
    var saveUrl;
    if (obj["id"] === "") {
        saveType = "POST";
        saveUrl = MenuAjaxUrl;
    } else {
        saveType = "PUT";
        saveUrl = MenuAjaxUrl + id;
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
                "data": "price"
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
            {
                "data": "date",
                "visible": false
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

function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateRow(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteRow(" + row.id + ");'><span class='fa fa-remove'></span></a>";
    }
}

function updateRow(id) {
    form.find(":input").val("");
    $.get(MenuAjaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
        });

        var list = document.getElementById("dishList");
        var dishId = form.find("input[name='dishId']").val();
        for (var i = 0; i < list.options.length; i++)
        {
            if (list.options[i].value === dishId) list.options[i].selected = true;
        }
        if (list.options.selectedIndex === -1 )
        {
            list.options.selectedIndex = 0;
        }
        $('#editRow').modal();
    });
}

function deleteRow(id) {
    if (confirm('Are you sure?')) {
        $.ajax({
            url: MenuAjaxUrl + id,
            type: "DELETE"
        }).done(function () {
            ctx.updateTable();
            successNoty("common.deleted");
        });
    }
}

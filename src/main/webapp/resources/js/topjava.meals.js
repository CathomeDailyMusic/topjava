let filter;
// $(document).ready(function () {
$(function () {
    filter = $("#filterForm");
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function updateTable() {
    if (typeof filter !== "undefined" && $("#filterFlag").prop("checked") === true) {
        applyFilter();
    } else {
        $.get(context.ajaxUrl, function (data) {
            context.datatableApi.clear().rows.add(data).draw();
        });
    }
}

function applyFilter() {
    var empty = true;
    filter.find('.form-control').each(function () {
        if ($(this).val() !== "") {
            empty = false;
            return false;
        }
    });
    if (!empty) {
        $("#filterFlag").prop("checked", true);
        $.get(context.ajaxUrl + 'filter', filter.serialize(), function (data) {
            context.datatableApi.clear().rows.add(data).draw();
            successNoty("Filter applied");
        });
    }
}

function clearFilter() {
    filter.find('.form-control').val('');
    $("#filterFlag").prop("checked", false);
    updateTable();
}
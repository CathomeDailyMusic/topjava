// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );

    $(".enabled").change(function () {
        switchEnabled($(this).parents('tr').attr("id"), this);
    });
});

function updateTable() {
    tableRefresh(context.ajaxUrl);
}

function switchEnabled(id, checkbox) {
    enabled = checkbox.checked;
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + id + "/status",
        data: "enabled=" + enabled,
        success: function () {
            $(checkbox).parents('tr').attr('data-userEnabled', enabled);
            const status = enabled ? "enabled" : "disabled";
            successNoty("User " + status);
        },
        error: function () {
            checkbox.checked = !enabled;
        }
    });
}
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
        switchEnabled($(this).parents('tr').attr("id"), this.checked);
    });
});

function switchEnabled(id, enabled) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl + id + "/status",
        data: "enabled=" + enabled
    }).done(function () {
        $.get(context.ajaxUrl + id, function (data) {
            context.datatableApi.row($("tr#" + id)).data(data).draw();
        });
        const status = enabled ? "enabled" : "disabled";
        successNoty("User " + status);
    });
}
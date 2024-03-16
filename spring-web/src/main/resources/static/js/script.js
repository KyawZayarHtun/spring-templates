// Check Url has id params
function hasQueryParam(paramName) {
    const url = new URL(window.location.href)
    const params = new URLSearchParams(url.search)
    return params.has(paramName);
}

function postSoftDelete(url, id) {
    let deleteToast = $("#deleteToast");
    let deletedStatusText = $("#deletedStatusText")

    $.post(url, {id: id}).done(data => {
        if (data === true) {
            $("#deleteToast > .toast-body").addClass("toast-success")
            deletedStatusText.html("Change status Successfully!")
            bootstrap.Toast.getOrCreateInstance(deleteToast, {
                delay: 3000
            }).show();
        } else {
            $("#deleteToast > .toast-body").addClass("toast-error")
            deletedStatusText.text("Something wrong try again!")
            bootstrap.Toast.getOrCreateInstance(deleteToast, {
                delay: 3000
            }).show();
        }
        $('#deleteModal').modal('toggle');
    });
    setTimeout(
        function () {
            data.ajax.reload();
        }, 500);
}
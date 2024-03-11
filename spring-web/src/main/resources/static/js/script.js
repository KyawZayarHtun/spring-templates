// Check Url has id params
function hasQueryParam(paramName) {
    const url = new URL(window.location.href)
    const params = new URLSearchParams(url.search)
    return params.has(paramName);
}

// For Datatable
function checkStringNullOrRenderStringForDataTable(data) {
    if (data == null || data.length <= 0 || data === 0) {
        return `<div class="chip chip-empty">Empty</div>`
    } else {
        return data;
    }
}

function checkListNullOrRenderListForDataTable(data, list) {
    if (data.length > 0) {
        let html = `<div class="d-flex gap-1">`;
        list.forEach(l => {
            html += `<div class="chip secondary-color">${l}</div>`
        });
        html += `</div>`
        return html;
    } else {
        return `<div class="chip chip-empty">Empty</div>`;
    }
}

function checkStatusForDataTable(data) {
    if (data === "ACTIVE") {
        return `<div class="chip success-color">${data}</div>`
    } else {
        return `<div class="chip danger-color">${data}</div>`
    }
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

function checkLinkNullOrRenderStringForDataTable(data) {
    if (data == null || data.length <= 0 || data === 0) {
        return `<div class="chip chip-empty">Empty</div>`
    } else {
        return `<a href="https://${data}" target="_blank">${data}</a>`;
    }
}

$(document).ready(function () {

    $(".input-date").on("click", function (e) {
        e.target.showPicker();
    })
})

// for current Date
function getCurrentDate() {
    const dt = new Date();
    const padL = (nr) => `${nr}`.padStart(2, `0`);
    return `${
        padL(dt.getDate())}-${
        padL(dt.getMonth() + 1)}-${
        dt.getFullYear()} ${
        padL(dt.getHours())}:${
        padL(dt.getMinutes())}:${
        padL(dt.getSeconds())}`;
}

// for refresh btn for datatable
/*let refreshBtnAndExcelExport = (tableId, needExport, exportId) => {
    let parentDivForRefreshBtn = $(`#${tableId}_length`);
    let refreshDataTableBtn = `<button class="primary-btn d-flex gap-2" onclick="location.reload()">
                                        <img src="/images/icon/refresh.svg" class="btn-icon" alt="">
                                        <span>Refresh</span>
                                    </button>`;
    parentDivForRefreshBtn.append(refreshDataTableBtn)
}*/


let refreshBtnAndExcelExport = (tableId, needExport, exportId) => {
    let parentDiv = $(`#${tableId}_length`);

    const div = document.createElement("div");
    div.className = "d-flex gap-2"
    let refreshDataTableBtn = `<button class="border-btn d-flex gap-2" onclick="location.reload()">
                                          <img src="/images/icon/refresh.svg" class="btn-icon" alt="">
                                          <span>Refresh</span>
                                      </button>`;
    if (needExport) {
        let exportBtn = `<button class="success-btn d-flex gap-2" id="${exportId}">
                                    <img src="/images/icon/document-arrow-down.svg" class="btn-icon" alt="">
                                    <span>Export</span>
                                </button>`;
        div.insertAdjacentHTML("beforeend", exportBtn)
    }

    div.insertAdjacentHTML("beforeend", refreshDataTableBtn);
    parentDiv.append(div);
}

function createExportBtn(tableId, exportId) {
    let parentDiv = $(`#${tableId}_length`);
    console.log(parentDiv)

    const div = document.createElement("div");
    div.className = "d-flex gap-2"
    let exportBtn = `<button class="success-btn d-flex gap-2" id="${exportId}">
                                    <img src="/images/icon/document-arrow-down.svg" class="btn-icon" alt="">
                                    <span>Export</span>
                                </button>`;
    div.insertAdjacentHTML("beforeend", exportBtn);
    parentDiv.append(div);
}

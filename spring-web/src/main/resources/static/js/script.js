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

// For Fetch Data for Table
const fetchDataForTable = async (url, searchPayload) => {

    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(searchPayload)
    })

    if (!response.ok) throw new Error("Something Wrong!")

    return await response.json();
}

const createDataTable = (searchPayload, addTableBody, tableId, selectId, footerId) => {
    // For sorting header
    addSortingHeader(searchPayload, addTableBody, tableId)

    // For list size change
    changeBaseOnListSize(searchPayload, addTableBody, selectId, "table-footer")

    // For Pagination
    tablePagination(searchPayload, addTableBody, footerId)
}

const addSortingHeader = (searchPayload, addTableBody, tableId) => {
    const tableHeaders = document.querySelectorAll(`#${tableId} thead  th`)
    tableHeaders.forEach(header => {
        header.addEventListener('click', () => {

            // for default ascending for new selected column
            if (searchPayload.sortColumnName === header.dataset.columnName) {
                searchPayload.sortDir = searchPayload.sortDir === 'desc' ? 'asc' : 'desc'
            } else {
                searchPayload.sortDir = 'asc'
            }

            // set column Name
            searchPayload.sortColumnName = header.dataset.columnName

            // show and hide sort icon based on direction
            let icon = header.querySelector("span > img");
            if (searchPayload.sortDir === 'asc') {
                icon.classList.remove("d-none")
                icon.src = "images/icon/sort-up.svg"
            } else {
                icon.classList.remove("d-none")
                icon.src = "images/icon/sort-down.svg"
            }

            // for removing sort icon for unselected column
            tableHeaders.forEach(header => {
                if (searchPayload.sortColumnName !== header.dataset.columnName) {
                    let icon = header.querySelector("span > img");
                    if (icon != null) {
                        icon.classList.add("d-none")
                    }
                }
            })

            // Adding content
            addTableBody();
        });

    })
}

const changeBaseOnListSize = (searchPayload, addTableBody, selectId, footerId) => {
    let listSize = document.getElementById(selectId);
    listSize.addEventListener('change', () => {
        searchPayload.size = listSize.value;
        tablePagination(searchPayload, addTableBody, footerId)
    })
}

const tablePagination = (searchPayload, addTableBody, footerId) => {
    addTableBody().then(({totalPages, totalCount, filterCount, contents}) => {
        const tableFooter = document.getElementById(footerId);
        const infoDiv = tableFooter.children.item(0)
        const paginationDiv = tableFooter.children.item(1)

        paginationInfo(searchPayload, totalCount, filterCount, contents, infoDiv)
        pagination(totalPages)
    });
}

const paginationInfo = (searchPayload, totalCount, filterCount, contents, infoDiv) => {
    const startItem = searchPayload.pageNo === 1
        ? 1
        : searchPayload.pageNo * searchPayload.size - searchPayload.size + 1
    const endItem = filterCount < searchPayload.size
        ? filterCount
        : contents.length < searchPayload.size
            ? startItem + contents.length - 1
            : searchPayload.pageNo * searchPayload.size
    const isFiltered = totalCount !== filterCount
        ? `(filtered from ${totalCount} entries)`
        : ``
    infoDiv.innerHTML = `Showing ${startItem} to ${endItem} of ${filterCount} entries ${isFiltered}`
}

const pagination = (totalPage) => {
    console.log(totalPage)
}
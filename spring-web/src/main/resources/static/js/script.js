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

const getApiResponse = data => {
    return {
        totalCount: data.totalCount,
        filterCount: data.filterCount,
        totalPages: data.totalPages,
        contents: data.contents
    }
}

const getDummyTableNumber = (searchPayload, index) => {
    return `${(searchPayload.pageNo - 1) * searchPayload.size + (index + 1)}`
}

const createDataTable = (searchPayload, addTableBody, tableId, selectId, footerId) => {

    const essentialData = {
        searchPayload,
        addTableBody,
        tableId,
        selectId,
        footerId
    }

    // For sorting header
    addSortingHeader(essentialData)

    // For list size change
    changeBaseOnListSize(essentialData)

    // For Pagination
    tablePagination(essentialData)
}

const addSortingHeader = ({searchPayload, addTableBody, tableId}) => {
    const tableHeaders = document.querySelectorAll(`#${tableId} thead  th`)
    tableHeaders.forEach(header => {
        header.addEventListener('click', () => {

            let columnName = header.dataset.columnName;
            if (columnName == null) {
                return;
            }

            // for default ascending for new selected column
            if (searchPayload.sortColumnName === header.dataset.columnName) {
                searchPayload.sortDir = searchPayload.sortDir === 'desc' ? 'asc' : 'desc'
            } else {
                searchPayload.sortDir = 'asc'
            }

            // set column Name
            searchPayload.sortColumnName = columnName

            // show and hide sort icon based on direction
            let icon = header.querySelector("span > span");
            if (icon == null) {
                const iconSpan = document.createElement("span")
                icon = header.querySelector("span").appendChild(iconSpan)
            }

            if (searchPayload.sortDir === 'asc') {
                icon.outerHTML = `<span class="ascending-icon d-flex">
                        <!-- arrow up -->
                        <svg xmlns='http://www.w3.org/2000/svg' stroke="" viewBox='0 0 24 24'
                             fill='#331D2C'
                             width='14'
                             height='14'>
                            <path d="M3 19h18a1.002 1.002 0 0 0 .823-1.569l-9-13c-.373-.539-1.271-.539-1.645 0l-9 13A.999.999 0 0 0 3 19z"></path>
                        </svg>
                        <!-- arrow down-->
                        <svg xmlns='http://www.w3.org/2000/svg' stroke="#000" viewBox='0 0 24 24'
                             fill='transparent'
                             width='14'
                             height='14'>
                            <path d="M11.178 19.569a.998.998 0 0 0 1.644 0l9-13A.999.999 0 0 0 21 5H3a1.002 1.002 0 0 0-.822 1.569l9 13z"></path>
                        </svg>
                    </span>`
            } else {
                icon.outerHTML = `<span class="descending-icon d-flex">
                        <!-- arrow up -->
                        <svg xmlns='http://www.w3.org/2000/svg' stroke="#000" viewBox='0 0 24 24'
                             fill='transparent'
                             width='14'
                             height='14'>
                            <path d="M3 19h18a1.002 1.002 0 0 0 .823-1.569l-9-13c-.373-.539-1.271-.539-1.645 0l-9 13A.999.999 0 0 0 3 19z"></path>
                        </svg>
                        <!-- arrow down-->
                        <svg xmlns='http://www.w3.org/2000/svg' stroke="" viewBox='0 0 24 24'
                             fill='#331D2C'
                             width='14'
                             height='14'>
                            <path d="M11.178 19.569a.998.998 0 0 0 1.644 0l9-13A.999.999 0 0 0 21 5H3a1.002 1.002 0 0 0-.822 1.569l9 13z"></path>
                        </svg>
                    </span>`
            }


            // for removing sort icon for unselected column
            tableHeaders.forEach(header => {
                if (searchPayload.sortColumnName !== header.dataset.columnName) {
                    let icon = header.querySelector("span > span");
                    if (icon != null) {
                        icon.remove()
                    }
                }
            })

            // Adding content
            addTableBody();
        });

    })
}

const changeBaseOnListSize = (essentialData) => {
    let listSize = document.getElementById(essentialData.selectId);
    listSize.addEventListener('change', () => {
        essentialData.searchPayload.size = listSize.value;
        essentialData.searchPayload.pageNo = 1
        tablePagination(essentialData)
    })
}

const tablePagination = (essentialData) => {
    essentialData.addTableBody().then(({totalPages, totalCount, filterCount, contents}) => {
        const tableFooter = document.getElementById(essentialData.footerId);
        const infoDiv = tableFooter.children.item(0)
        const paginationDiv = tableFooter.children.item(1)

        paginationInfo(essentialData.searchPayload, totalCount, filterCount, contents, infoDiv)
        // pagination(paginationDiv, filterCount, essentialData.searchPayload, addTableBody, infoDiv)
        pagination(essentialData, filterCount, paginationDiv, infoDiv)
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

const pagination = ({searchPayload, addTableBody}, filterCount, paginationDiv, infoDiv) => {
    let pagination = new tui.Pagination(paginationDiv, {
        totalItems: filterCount,
        itemsPerPage: searchPayload.size,
        visiblePages: 5,
        centerAlign: true,
        template: {
            page: '<a href="#" class="pagination-btn">{{page}}</a>',
            currentPage: '<span class="pagination-btn current-page">{{page}}</span>',
            moveButton: `<a href="#" class="pagination-btn {{type}}-page">{{type}}</a>`,
            disabledMoveButton: `<span class="pagination-btn page-disabled {{type}}-page">{{type}}</span>`,
            moreButton: `<a href="#" class="pagination-btn">...</a>`
        }
    });
    pagination.on('beforeMove', function (eventData) {
        searchPayload.pageNo = eventData.page;
        addTableBody().then(({totalPages, totalCount, filterCount, contents}) => {
            paginationInfo(searchPayload, totalCount, filterCount, contents, infoDiv)
        })
    });

}
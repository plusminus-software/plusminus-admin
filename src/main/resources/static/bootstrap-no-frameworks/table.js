$('a.nav-link').click(function(data) {
    let tableContainerId = data.target.getAttribute('href');
    loadPage($('.tab-content ' + tableContainerId), 0);
});

function clickPage(element, pageNumber) {
    let tableContainer = element.closest('.table-container');
    loadPage($(tableContainer), pageNumber);
}

function loadPage(tableContainer, pageNumber) {
    let table = tableContainer.find('table');
    let pagination = tableContainer.find('ul');
    let refreshButton = tableContainer.find('button.refresh');
    let typeName = table.attr('type-name');
    let url = table.attr('url');

    $.get(url + '?page=' + pageNumber, function(page) {
        refreshTable(table, typeName, page.content);
        refreshPagination(pagination, page);
        refreshButton.attr('page', page.number);
    });
}

function refreshTable(table, typeName, elements) {
    let columns = table.find('th').map(function (index, element) {
        return element.innerHTML;
    }).get();
    let body = table.find('tbody');
    body.empty();
    for (let i = 0; i < elements.length; i++) {
        let object = elements[i];
        body.append(getRow(table, typeName, columns, object));
    }
}

function refreshPagination(pagination, page) {
    pagination.empty();
    for (let pageNumber = 0; pageNumber < page.totalPages; pageNumber++) {
        let pageElement = $('<li class="page-item"></li>');
        if (pageNumber === page.number) {
            pageElement.addClass('active');
        }
        pageElement.append(
            $('<a class="page-link" href="#"></a>')
                .attr('onClick', `clickPage(this, ${pageNumber})`)
                .html(pageNumber + 1));
        pagination.append(pageElement);
    }
}

function getRow(table, typeName, columns, object) {
    let rowElement = $('<tr/>');
    for (let columnIndex = 0; columnIndex < columns.length; columnIndex++) {
        if (columns[columnIndex] === '') {
            if (table.attr('inside-modal')) {
                rowElement.append($('<td/>')
                    .append(getChooseButton(object)));
            }
            else {
                rowElement.append($('<td/>')
                    .append(getUpdateButton(typeName, object))
                    .append(getCloneButton(typeName, object))
                    .append(getDeleteButton(typeName, object)));
            }
            continue;
        }
        let cellValue = object[columns[columnIndex]];
        if (cellValue == null) {
            cellValue = "";
        }
        rowElement.append($('<td/>').html(cellValue));
    }
    return rowElement;
}

function getUpdateButton(typeName, object) {
    let updateModal = $('#modal-' + typeName + "-update");
    let updateForm = updateModal.find('form');

    let updateButton = $('<button></button>');
    //updateButton.addType('button');
    updateButton.addClass('btn btn-sm btn-outline-info mr-1');
    updateButton.html('<i class="fa fa-pencil" aria-hidden="true"></i>');
    updateButton.click(createModalClickFunction(updateModal, updateForm, object));

    return updateButton;
}

function getCloneButton(typeName, object) {
    let cloneModal = $('#modal-' + typeName + "-clone");
    let cloneForm = cloneModal.find('form');

    let cloneButton = $('<button></button>');
    //cloneButton.addType('button');
    cloneButton.addClass('btn btn-sm btn-outline-warning mr-1');
    cloneButton.html('<i class="fa fa-clone" aria-hidden="true"></i>');
    let clonnedObject = JSON.parse(JSON.stringify(object));
    delete clonnedObject['id'];
    cloneButton.click(createModalClickFunction(cloneModal, cloneForm, clonnedObject));

    return cloneButton;
}

function getDeleteButton(typeName, object) {
    let deleteModal = $('#modal-' + typeName + "-delete");
    let deleteForm = deleteModal.find('form');

    let deleteButton = $('<button></button>');
    //deleteButton.addType('button');
    deleteButton.addClass('btn btn-sm btn-outline-danger mr-1');
    deleteButton.html('<i class="fa fa-ban" aria-hidden="true"></i>');
    deleteButton.click(createModalClickFunction(deleteModal, deleteForm, object));

    return deleteButton;
}

function getChooseButton(object) {
    let chooseButton = $('<button></button>');
    chooseButton.addClass('btn btn-sm btn-primary mr-1');
    chooseButton.html('Choose');
    chooseButton.click(createChooseClickFunction(chooseButton, object));

    return chooseButton;
}

function createModalClickFunction(modal, form, object) {
    return function() {
        clearForm(form);
        form.values(object);
        modal.modal();
    }
}

function createChooseClickFunction(button, object) {
    return function() {
        console.log('hello');
    }
}
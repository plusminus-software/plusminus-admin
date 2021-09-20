function onCreate(element) {
    write(element, 'POST');
}
function onUpdate(element) {
    write(element, 'PATCH');
}
function onClone(element) {
    write(element, 'POST');
}
function onDelete(element) {
    write(element, 'DELETE');
}
function write(element, httpMethod) {
    let url = element.getAttribute('url');
    let modal = $(element).closest('.modal');
    let form = modal.find('form');
    form.find('[dynamic]').find(':input').attr('disabled', 'disabled');
    let object = form.serializeObject();
    form.find('[dynamic]').find(':input').removeAttr('disabled');
    $.ajax({
        url: url,
        contentType: "application/json",
        data: JSON.stringify(object),
        method: httpMethod
    }).done(function() {
        form.find('[name]').val('');
        modal.modal('hide');
        $(`table[url=${url}]`).closest('.table-container').find('button.refresh')
            .click();
    }).fail(function(jqXHR, textStatus, error) {
        alert("Error");
    });
}
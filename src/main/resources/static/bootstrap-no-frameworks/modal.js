function modalNew(id) {
    var modal = $(id);
    var form = modal.find('form');
    clearForm(form);
    modal.modal();
}
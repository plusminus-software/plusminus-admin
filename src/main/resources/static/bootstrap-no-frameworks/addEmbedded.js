function addEmbedded(button) {
    let formGroup = $(button).closest('.form-group');
    let embedded = formGroup.find('[dynamic]').find(':input').serializeObject();
    formGroup.find('[tags]').tagsinput('add', embedded);
}
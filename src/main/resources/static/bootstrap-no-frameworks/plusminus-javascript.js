function removeElement(element) {
    $(element).parent().remove();
}

function clearForm(form) {
    form.find(':input')
        .not(':button, :submit, :reset')
        .val('')
        .prop('checked', false)
        .prop('selected', false)
        .change();
    form.find(":input[type='checkbox']")
        .val('on');
}
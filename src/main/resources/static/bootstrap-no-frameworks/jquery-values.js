/* jQuery.values: get or set all of the name/value pairs from child input controls
 * @argument data {array} If included, will populate all child controls.
 * @returns element if data was provided, or array of values if not
*/

$.fn.values = function(data) {
    var els = this.find(':input').get();

    if(arguments.length === 0) {
        // return all data
        data = {};

        $.each(els, function() {
            if (this.name && !this.disabled && (this.checked
                || /select|textarea/i.test(this.nodeName)
                || /text|hidden|password/i.test(this.type))) {
                if(data[this.name] == undefined){
                    data[this.name] = [];
                }
                data[this.name].push($(this).val());
            }
        });
        return data;
    } else {
        $.each(els, function() {
            if (!this.name) {
                return;
            }
            var $this = $(this);
            var value = getByPath(data, this.name);
            if (value === undefined || value === null) {
                if ($this.attr('tags')) {
                    $this.tagsinput('removeAll');
                }
                return;
            }
            if (this.type == 'checkbox' && value === true) {
                $this.prop('checked', true);
            } else if (this.type == 'radio') {
                var val = $this.val();
                var found = false;
                for (var i = 0; i < value.length; i++) {
                    if(value[i] == val){
                        found = true;
                        break;
                    }
                }
                $this.attr("checked", found);
            } else if ($this.attr('tags')) {
                $this.tagsinput('removeAll');
                if ($this.attr('single')) {
                    $this.tagsinput('add', value);
                } else {
                    value.forEach(function(e) {
                        $this.tagsinput('add', e);
                    });
                }
            } else {
                $this.val(value);
                $this.change();
            }
        });
        return this;
    }
};
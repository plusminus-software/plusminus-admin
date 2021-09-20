function createModalComponent(name) {
    return Vue.component(name, {
        props: {
            title: String,
            typeName: String,
            action: String,
            url: String,
            color: String
        },
        data: function() {
            return {
                object: null,
                files: [],
                active: false,
                methodName: null,
                isLoading: false
            }
        },
        methods: {
            open(object) {
                if (object) {
                    this.object = object;
                } else {
                    this.object = {};
                    this.object.class = this.typeName;
                }
                this.active = true;
            },
            close() {
                this.object = null;
                this.active = false;
            },
            refreshTable() {
                this.$parent.$parent.$parent.$refs[this.typeName + 'Table'].refresh();
            },
            write() {
                this.isLoading = true;
                let body = this.object;
                if (this.methodName === 'delete') {
                   body = {body: body};
                }
                this.$http[this.methodName](this.url, body).then(response => {
                    this.close();
                    this.refreshTable();
                    this.isLoading = false;
                }, error => {
                    console.log("Error on AJAX request");
                    this.isLoading = false;
                });
            },
            arraymove(arr, fromIndex, toIndex) {
                const element = arr[fromIndex];
                arr.splice(fromIndex, 1);
                arr.splice(toIndex, 0, element);
            },
            copyToClipboard(text) {
                var body = document.getElementsByTagName('body')[0];
                var tempInput = document.createElement('INPUT');
                body.appendChild(tempInput);
                tempInput.setAttribute('value', text)
                tempInput.select();
                document.execCommand('copy');
                body.removeChild(tempInput);
            },
            replaceField(sourceObject, fieldName) {
                this.object[fieldName] = sourceObject[fieldName];
                this.$forceUpdate();
            },
            uploadFromClipboard(url) {
                return navigator.clipboard.readText()
                  .then(text => {
                    if (!text) {
                        return Promise.resolve();
                    }
                    return this.upload(text, url);
                  })
                  .catch(err => {
                    console.error('Failed to read clipboard contents: ', err);
                  });
            },
            uploadFiles(files, url) {
                if (!files) {
                    Promise.resolve();
                }
                const formData = new FormData();
                files.forEach(file => {
                    formData.append('files', file, file.name);
                });
                return this.upload(formData, url);
            },
            upload(body, url) {
                return new Promise(function (resolve, reject) {

                    const xhr = new XMLHttpRequest();
                    self = this; // TODO change this global variable
                    xhr.onload = function () {
                      if (this.status >= 200 && this.status < 300) {
                        resolve(xhr.response);
                      } else {
                        reject({
                          status: this.status,
                          statusText: xhr.statusText
                        });
                      }
                    };
                    xhr.onerror = function () {
                      reject({
                        status: this.status,
                        statusText: xhr.statusText
                      });
                    };
                    xhr.open("POST", url);
                    xhr.send(body);
                });
            },
            simplifyObject(object) {
                let fieldSize = Object.keys(object).length;
                if (fieldSize <= 3) {
                    return object;
                }
                let result = {
                    id: object.id
                }
                if (object.name) {
                    result.name = object.name;
                }
                return result;
            }
        },
        filters: {
          capitalize: function (value) {
            if (!value) return ''
            value = value.toString()
            return value.charAt(0).toUpperCase() + value.slice(1)
          }
        },
        mounted: function() {
            if (this.action === 'Create') {
                this.methodName = "post";
            } else if (this.action === 'Update') {
                this.methodName = "patch";
            } else if (this.action === 'Clone') {
                this.methodName = "post";
            } else if (this.action === 'Delete') {
                this.methodName = "delete";
            }
        },
        template: `
        <b-modal :active.sync="active"
                         full-screen
                         has-modal-card
                         trap-focus
                         aria-role="dialog"
                         aria-modal>
            <b-loading :active.sync="isLoading"></b-loading>
            <div class="modal-card" style="width: auto">
                <header class="modal-card-head">
                    <p class="modal-card-title">{{title}}</p>
                </header>
                <section class="modal-card-body">
                    <slot v-if="object" v-bind:object="object" v-bind:files="files" v-bind:uploadFiles="uploadFiles" v-bind:replaceField="replaceField" v-bind:arraymove="arraymove" v-bind:copyToClipboard="copyToClipboard" v-bind:uploadFromClipboard="uploadFromClipboard" v-bind:simplifyObject="simplifyObject"></slot>
                    <div v-else>
                        Error has been occurred. Please contact your administrator.
                    </div>
                </section>
                <footer class="modal-card-foot">
                    <button class="button" type="button" @click="close()">Close</button>
                    <button class="button" v-bind:class="color ? 'is-' + color : ''" @click="write()">{{ action | capitalize }}</button>
                </footer>
            </div>
        </b-modal>
        `
    });
}
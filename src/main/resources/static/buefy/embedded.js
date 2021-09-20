function createEmbeddedComponent(name) {
    return Vue.component(name, {
        props: {
            object: Object,
            fieldName: String
        },
        data: function() {
            return {
                embedded: null
            }
        },
        mounted: function () {
            this.embedded = this.object[this.fieldName];
        },
        methods: {
            click() {
                if (this.embedded) {
                    this.embedded = null;
                    this.object[this.fieldName] = null;
                } else {
                    this.embedded = {};
                    this.object[this.fieldName] = this.embedded;
                }
            }
        },
        template: `
        <div>
            <b-field>
                <a @click="click()" class="is-size-7">
                    <b-icon :icon="embedded ? 'minus' : 'plus'" size="is-small"></b-icon>
                    {{ embedded ? 'Set null' : 'Create' }}
                </a>
            </b-field>
            <slot v-bind:object="embedded" v-if="embedded"></slot>
        </div>
        `
    });
}
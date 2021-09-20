function createTaglistEmbeddedComponent(name) {
    return Vue.component(name, {
        props: {
            object: Object,
            fieldName: String
        },
        data: function() {
            return {
                show: false,
                embedded: {}
            }
        },
        methods: {
            add() {
                if (!this.object[this.fieldName]) {
                    this.$set(this.object, this.fieldName, []);
                }
                this.object[this.fieldName].push(this.embedded);
                this.embedded = {};
            }
        },
        template: `
        <div>
            <b-field>
                <a @click="show = !show" class="is-size-7">
                    <b-icon :icon="show ? 'menu-up' : 'menu-down'" size="is-small"></b-icon>
                    {{ show ? 'Hide embedded' : 'Show embedded' }}
                </a>
            </b-field>
            <div v-if="show">
                <slot v-bind:object="embedded"></slot>
                <b-button type="is-primary" expanded @click="add()">Add</b-button>
            </div>
        </div>
        `
    });
}
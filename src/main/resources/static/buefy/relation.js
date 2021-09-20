function createRelationComponent(name) {
    return Vue.component(name, {
        props: {
            object: Object,
            fieldName: String
        },
        data: function() {
            return {
                show: false
            }
        },
        methods: {
            choose(item) {
                if (!this.object[this.fieldName]) {
                    this.$set(this.object, this.fieldName, item);
                } else {
                    this.object[this.fieldName] = item;
                }
            }
        },
        template: `
        <div>
            <b-field>
                <a @click="show = !show" class="is-size-7">
                    <b-icon :icon="show ? 'menu-up' : 'menu-down'" size="is-small"></b-icon>
                    {{ show ? 'Hide table' : 'Show table' }}
                </a>
            </b-field>
            <slot v-bind:choose="choose" v-if="show"></slot>
        </div>
        `
    });
}
function createTableComponent(name) {
  return Vue.component(name, {
    props: {
        typeName: String,
        url: String
    },
    data: function() {
        return {
            data: [],
            total: 0,
            loading: false,
            defaultSortOrder: 'asc',
            page: 1,
            perPage: 20
        }
    },
    methods: {
        /*
        * Load async data
        */
        loadAsyncData() {
            let paramsArray = [
                `page=${this.page - 1}`,
                `pageSize=${this.perPage}`,
                `sort=id,desc`
            ]
            if (this.sortField) {
                paramsArray.push(`sort=${this.sortField},${this.sortOrder}`);
            }
            const params = paramsArray.join('&')

            this.loading = true
            this.$http.get(`${this.url}?${params}`)
                .then(({ data }) => {
                    const linked = JSOG.parse(JSON.stringify(data.content))
                    this.data = linked.map(l => this.unlink(l))
                    this.total = data.totalElements
                    this.loading = false
                })
                .catch((error) => {
                    this.data = []
                    this.total = 0
                    this.loading = false
                    throw error
                })
        },
        /*
        * Handle page-change event
        */
        onPageChange(page) {
            this.page = page
            this.loadAsyncData()
        },
        /*
        * Handle sort event
        */
        onSort(field, order) {
            this.sortField = field
            this.sortOrder = order
            this.loadAsyncData()
        },
        /*
        * Type style in relation to the value
        * TODO
        */
        tagType(value) {
            const number = parseFloat(value)
            if (number < 6) {
                return 'is-danger'
            } else if (number >= 6 && number < 8) {
                return 'is-warning'
            } else if (number >= 8) {
                return 'is-success'
            }
        },
        create() {
            this.$parent.$parent.$parent.$refs[this.typeName + 'CreateModal'].open();
        },
        edit(object) {
            const cloned = JSON.parse(JSON.stringify(object));
            this.$parent.$parent.$parent.$refs[this.typeName + 'UpdateModal'].open(cloned);
        },
        clone(object) {
            const cloned = JSON.parse(JSON.stringify(object));
            delete cloned.id;
            delete cloned.version;
            cloned.class = this.typeName;
            this.$parent.$parent.$parent.$refs[this.typeName + 'CloneModal'].open(cloned);
        },
        delete(object) {
            const cloned = JSON.parse(JSON.stringify(object));
            this.$parent.$parent.$parent.$refs[this.typeName + 'DeleteModal'].open(cloned);
        },
        refresh() {
            this.loadAsyncData();
        },
        unlink(model) {
            const json = JSON.stringify(
                model, (k, v) => {
                    if (k && v && (v['id'] || v['uuid'])) {
                        return {
                            class: v.class,
                            id: v.id,
                            uuid: v.uuid,
                            version: v.version,
                            name: v.name,
                            title: v.title
                        };
                    }
                    return v;
                });
            return JSON.parse(json);
        }
    },
    filters: {
        /**
        * Filter to truncate string, accepts a length parameter
        */
        truncate(value, length) {
            return value.length > length
                ? value.substr(0, length) + '...'
                : value
        }
    },
    mounted() {
        this.loadAsyncData()
    },
    template: `
        <section>
            <b-table
                :data="data"
                :loading="loading"

                paginated
                backend-pagination
                :total="total"
                :per-page="perPage"
                @page-change="onPageChange"
                aria-next-label="Next page"
                aria-previous-label="Previous page"
                aria-page-label="Page"
                aria-current-label="Current page"

                backend-sorting
                :default-sort-direction="defaultSortOrder"
                @sort="onSort">

                <template slot-scope="props">
                    <slot :props="props"></slot>
                </template>

            </b-table>
        </section>
    `
  });
}
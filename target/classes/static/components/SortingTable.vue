<template>
    <table>
        <thead>
            <tr>
                <th @click="sort(h.value)" v-for="h in headers" :key="h.text">
                    {{h.text}}
                </th>
            </tr>
            <tr v-for="(item, index) in sorted" :key="index">
                <td v-for="(h, hi) in headers" :key="hi">
                    {{item[h.value]}}
                </td>
            </tr>
        </thead>
    </table>
</template>

<script>
module.exports = {
    data() {
        return {
            currentSort: this.headers[0].value,
            currentSortDir: 'asc',
        }
    },
  props: ["items", "headers"],
  methods: {
        sort: function(s) {
            if(s == this.currentSort) {
                this.currentSortDir = this.currentSortDir == 'asc' ? 'desc' : 'asc';
            }
            this.currentSort = s;
        },
  },  
  computed: {
        sorted: function() {
            return this.items.sort((a, b) => {
                let modifier = 1;
                if(this.currentSortDir == 'desc') {
                    modifier = -1;
                }
                if(a[this.currentSort] < b[this.currentSort]) {
                    return -1 * modifier;
                }
                if(a[this.currentSort] > b[this.currentSort]) {
                    return modifier;
                }
                return 0;
            });
        },
  },
};
</script>

<style scoped>

    th {
        cursor: pointer;
    }

    table, tr, th, td {
        border: solid 1px #eee;
    }
</style>
<template>
    <div>
        <main-navigation></main-navigation>
        <div id="search-main">
            <div id="search-container">
                <input type="text" placeholder="Name" @keyup.enter="search('')" v-model="name">
                <div id="sort-container">
                    <p @click="sort('name')">Name <span v-if="sortBy == 'name'" v-html="sortSymbol"></span></p>
                    <p @click="sort('address.city')">City <span v-if="sortBy == 'address.city'" v-html="sortSymbol"></span></p>
                    <p @click="sort('address.state')">State <span v-if="sortBy == 'address.state'" v-html="sortSymbol"></span></p>
                    <p @click="sort('avgScore')">Avg. score <span v-if="sortBy == 'avgScore'" v-html="sortSymbol"></span></p>
                </div>
                <div id="filters-container">
                    <div v-for="(filter, index) in selectedFilters" :key="filter.text" class="filter-tag">
                        <input v-if="filter.edit && filter.editable"
                        :placeholder="filter.text"
                        :type="filter.type ? filter.type : 'text'"
                        v-model="filter.value"
                        @blur="filter.edit=false; $emit('update')"
                        @keyup.enter="filter.edit=false; $emit('update')"/>
                        <div v-else class="filter-tag-inner" @click="filter.edit=true">
                            <div>{{filter.text}} {{filter.value}}</div>
                            <div class="spacer"></div>
                            <span @click="removeFilter(index)">x</span>
                        </div>
                    </div>
                    <select v-model="selectedFilter" @change="selectFilter">
                        <option value="" disabled>Add a filter</option>
                        <option v-for="filter in filters" :key="filter.text" :value="filter">{{filter.text}}</option>
                    </select>
                </div>
            </div>
            <div id="results-container">
                <div @click="$router.push('/restaurant/' + result.name)" v-for="result in sortedResults" :key="result.name" class="result">
                    <div class="result-header">
                        <h3>{{result.name}}</h3>
                        <b :class="result.status=='OPEN' ? 'open' : 'closed'">{{result.status}}</b>
                        <p>{{result.restType}}</p>
                        <p>{{result.avgScore}}/5.0</p>
                        <div class="result-location">
                            <p>{{result.address.street}} {{result.address.streetNo}}</p>
                            <p>{{result.address.city}}, {{result.address.state}}</p>
                        </div>
                    </div>
                    <div class="spacer"></div>
                    <div class="result-profile">
                        <img :src="'http://localhost:8080/image/' + result.logoFilepath" alt="Logo">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        name: '',
        sortBy: 'name',
        sortDirection: 'asc',
        results: [
            {
                name: 'Ciao',
                type: 'Pizzeria',
                status: 'Open',
                avgScore: 3.0,
                address: {
                    state: 'Serbia',
                    city: 'Novi Sad',
                    street: 'Bul. oslobodjenja',
                    streetNo: '23a',
                },
            },
            {
                name: 'HAO',
                type: 'Chinese',
                status: 'Open',
                avgScore: 5.0,
                address: {
                    state: 'Serbia',
                    city: 'Beograd',
                    street: 'Bul. Kralja Aleksandra',
                    streetNo: '212',
                },
            },
            {
                name: 'Borsalino',
                type: 'Pizzeria',
                status: 'Closed',
                avgScore: 2.4,
                address: {
                    state: 'Serbia',
                    city: 'Beograd',
                    street: 'Jurija Gagarina',
                    streetNo: '12',
                },
            },
            {
                name: 'Ni Hao',
                type: 'Chinese',
                status: 'Open',
                avgScore: 2.0,
                address: {
                    state: 'Serbia',
                    city: 'Novi Sad',
                    street: 'Sekspirova',
                    streetNo: '23',
                },
            },
        ],

        selectedFilter: '',
        filters: [
            { text: "Type", value: '', edit: false, editable: true, },
            { text: "City", value: '', edit: false, editable: true, },
            { text: "State", value: '', edit: false, editable: true, },
            { text: "Avg. score", value: 0.0, edit: false, editable: true, type: 'number' },
            { text: "Only open", value: '', edit: false, },
        ],
        selectedFilters: [],
    }),
    methods: {
        search: function(routeName) {
            this.results = [];
            let type = '';
            let city = '';
            let state ='';
            let avgScore = 0.0;
            this.selectedFilters.forEach(f => {
                if(f.text == 'Type') {
                    type = f.value;
                }
                if(f.text == 'City') {
                    city = f.value;
                }
                if(f.text == 'State') {
                    state = f.value;
                }
                if(f.text == 'Avg. score') {
                    avgScore = f.value;
                }
            });
            if(routeName) {
                this.name = routeName;
            }
            let query = '?name=' + this.name + '&type=' + type + '&state=' + state + '&city=' + city + '&score=' + avgScore;
            axios.get('/restaurant' + query)
                .then(r => this.results = r.data)
                .catch(r => console.log(r));
        },

        sort: function(s) {
            if(this.sortBy == s) {
                this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
            }
            this.sortBy = s;
        },

        openCompare: function(a, b) {
            if(a.status!='Open' && b.status=='Open') {
                return 1;
            }
            return 0;
        },

        removeFilter: function(i) {
            this.filters.push(this.selectedFilters.splice(i, 1)[0]);
        },

        selectFilter: function() {
            // let value = e.target.value;
            this.selectedFilters.push(this.selectedFilter);
            this.filters.splice(this.filters.indexOf(this.selectedFilter), 1);
            this.selectedFilter = '';
        },
    },

    computed: {
        sortedResults: function() {
            let res = [...this.results];
            res.sort((a, b) => {
                let aval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, a);
                let bval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, b);
                let modifier = 1;
                if(this.sortDirection == 'desc') {
                    modifier = -1;
                }
                if(aval < bval) {
                    return -1 * modifier;
                }
                if(aval > bval) {
                    return modifier;
                }
                return 0;
            });
            res.sort(this.openCompare);
            return res;
        },

        sortSymbol: function() {
            return this.sortDirection=='asc' ? '&#x25B2;' : '&#x25BC;'
        },
    },
    mounted() {
        this.search(this.$route.params.name);
    },
};
</script>

<style scoped>
    #search-main {
        min-height: 100vh;
        background: #fafafa;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    #sort-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    #sort-container p {
        cursor: pointer;
    }

    #search-container {
        background: #fff;
        padding: 20px;
        border: solid 1px #eee;
        width: 400px;
        text-align: center;
    }

    #filters-container {
        display: flex;
        flex-direction: column;
    }

    .filter-tag, .filter-tag-inner {
        display: flex;
        flex-direction: row;
        cursor: pointer;
        height: 40px;
        float: left;
        /* margin-right: 10px; */
        background-color: #eee;
        line-height: 40px;
        padding: 0 10px;
        border-radius: 5px;
        margin: 2px 0px;
    }

    .filter-tag-inner {
        width: 100%;
        margin: 0;
    }

    .filter-tag span {
        cursor: pointer;
    }

    #results-container {
        margin: 20px;
        display: grid;
        gap: 10px;
        grid-template-columns: repeat(3, 1fr);
    }

    .result {
        cursor: pointer;
        display: flex;
        flex-direction: row;
        background: #fff;
        padding: 10px;
        width: 400px;
        border: solid 1px #eee;
    }

    .result-header b {
        text-transform: uppercase;
    }

    .result-header p {
        margin: 0;
    }

    .result-location {
        display: flex;
        flex-direction: column;
        padding: 10px;
    }

    .result-location p {
        margin: 0;
    }

    .result-profile img {
        width: 128px;
        height: 128px;
    }

    .open {
        color: #2ecc71;
    }

    .closed {
        color: #e74c3c;
    }
</style>
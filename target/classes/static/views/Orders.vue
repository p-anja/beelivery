<template>
    <div>
        <main-navigation>
            <router-link to="/">Home</router-link>
        </main-navigation>
        <div id="cart-main">
            <div id="search-container">
                <input type="text" placeholder="Restaurant">
                <div id="sort-container">
                    <p @click="sort('restaurant.name')">Restaurant<span v-if="sortBy == 'restaurant.name'" v-html="sortSymbol"></span></p>
                    <p @click="sort('date')">Date<span v-if="sortBy == 'date'" v-html="sortSymbol"></span></p>
                    <p @click="sort('price')">Price<span v-if="sortBy == 'price'" v-html="sortSymbol"></span></p>
                </div>
                <div id="filters-container">
                    <input type="number" min="0" v-model="fromPrice" placeholder="Min price">
                    <input type="number" min="0" v-model="toPrice" placeholder="Max price">
                    <div class="filter-item">
                        <label>From date:</label>
                        <input type="date" v-model="fromDate">
                    </div>
                    <div class="filter-item">
                        <label>To date:</label>
                        <input type="date" v-model="toDate">
                    </div>
                    <select v-model="selectedType">
                        <option value="">Select type</option>
                        <option v-for="type in types" :key="type" :value="type">{{type}}</option>
                    </select>
                    <select v-model="selectedStatus" >
                        <option value="">Select status</option>
                        <option v-for="status in statuses" :key="status" :value="status">{{status}}</option>
                    </select>
                    <div class="filter-item">
                        <label>Undelivered: </label>
                        <input type="checkbox" v-model="undelivered" />
                    </div>
                </div>
            </div>
            <div id="results-container">
                <div v-for="result in sortedResults" :key="result.id" class="result">
                    <div class="result-header">
                        <h3>{{result.id}}</h3>
                        <p>{{result.date}}</p>
                        <b>{{result.status}}</b>
                        <p>{{result.restaurant.name}} ({{result.restaurant.type}})</p>
                        <p>{{result.address.street}} {{result.address.streetNo}}</p>
                        <p>{{result.address.city}}, {{result.address.state}}</p>
                    </div>
                    <div class="result-action">
                        <b>{{result.price}} &#8364;</b>
                        <div class="spacer"></div>
                        <button class="button-cancel" v-if="user.role=='USER' && result.status=='Pending'">Cancel</button>
                        <button class="button-primary" v-else-if="user.role=='DELIVERY' && result.status=='Waiting delivery'">Request</button>
                        <button class="button-delivered" v-else-if="user.role=='DELIVERY' && result.status=='In transit'">Delivered</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        user: {
            role: "DELIVERY",
        },

        sortBy: 'restaurant.name',
        sortDirection: 'asc',
        results: [
            {
                id: '1234567890',
                date: '2021-07-01',
                status: 'Pending',
                restaurant: {
                    name: 'Ciao',
                    type: 'Pizzeria',
                },
                price: 12.0,
                address: {
                    state: 'Serbia',
                    city: 'Novi Sad',
                    street: 'Balzakova',
                    streetNo: '32a',
                },
            },
            {
                id: '0987654321',
                date: '2021-06-23',
                status: 'Delivered',
                restaurant: {
                    name: 'HAO',
                    type: 'Chinese',
                },
                price: 12.5,
                address: {
                    state: 'Serbia',
                    city: 'Novi Sad',
                    street: 'Sekspirova',
                    streetNo: '3a',
                },
            },
            {
                id: '1239874560',
                date: '2021-06-25',
                status: 'Waiting delivery',
                restaurant: {
                    name: 'Ni Hao',
                    type: 'Chinese',
                },
                price: 21.3,
                address: {
                    state: 'Serbia',
                    city: 'Novi Sad',
                    street: 'Rumenacka',
                    streetNo: '32a',
                },
            },
            {
                id: '1239874560',
                date: '2021-07-01',
                status: 'In transit',
                restaurant: {
                    name: 'Balans',
                    type: 'Crepes',
                },
                price: 2.0,
                address: {
                    state: 'Serbia',
                    city: 'Novi Sad',
                    street: 'Rumenacka',
                    streetNo: '32a',
                },
            },
        ],
        types: ['Chinese', 'Pizzeria', 'Italian',],
        statuses: ['Pending', 'Preparation', 'Waiting delivery', 'In transit', 'Delivered', 'Canceled'],

        undelivered: false,
        selectedStatus: '',
        selectedType: '',
        fromDate: '',
        toDate: '',
        fromPrice: '',
        toPrice: '',
    }),

    methods: {
        sort: function(s) {
            if(this.sortBy == s) {
                this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
            }
            this.sortBy = s;
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
            if(this.selectedType) {
                res = res.filter(r => r.restaurant.type == this.selectedType);
            }
            if(this.selectedStatus) {
                res = res.filter(r => r.status == this.selectedStatus);
            }
            if(this.undelivered) {
                res = res.filter(r => r.status != 'Delivered');
            }
            return res;
        },
        sortSymbol: function() {
            return this.sortDirection=='asc' ? '&#x25B2;' : '&#x25BC;'
        },
    },
};
</script>

<style scoped>
    #cart-main {
        min-height: 100vh;
        background: #fafafa;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    #sort-container {
        display: flex;
        flex-direction: row;
        justify-content: space-evenly;
    }

    #sort-container p {
        cursor: pointer;
    }

    #search-container {
        background: #fff;
        padding: 20px;
        border: solid 1px #eee;
        width: 800px;
        text-align: center;
    }

    #filters-container {
        display: grid;
        grid-template-columns: 1fr 1fr;
    }

    .filter-item {
        display: flex;
        flex-direction: row;
        justify-content: space-evenly;
    }

    #results-container {
        margin: 20px;
        display: grid;
        gap: 10px;
        grid-template-columns: repeat(3, 1fr);
    }

    .result {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        padding: 10px;
        width: 400px;
        background: #fff;
        border: solid 1px #eee;
    }

    .result-header h3 {
        font-size: 2.5rem;
    }

    .result-header b {
        text-transform: uppercase;
        color: #666;
    }

    .result-header p {
        margin: 0;
    }

    .result-action {
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        text-align: right;
    }

    .button-cancel {
        color: #fff;
        background-color: #e74c3c;
    }

    .button-delivered {
        color: #fff;
        background-color: #2ecc71;
    }
</style>
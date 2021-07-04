<template>
    <div>
        <main-navigation>
        </main-navigation>
        <div id="cart-main">
            <div id="search-container">
                <input v-if="role != 'MANAGER'" type="text" placeholder="Restaurant" v-model="restaurantName" @keydown.enter="getOrders">
                <div id="sort-container">
                    <p @click="sort('restaurant.name')">Restaurant<span v-if="sortBy == 'restaurant.name'" v-html="sortSymbol"></span></p>
                    <p @click="sort('date')">Date<span v-if="sortBy == 'date'" v-html="sortSymbol"></span></p>
                    <p @click="sort('price')">Price<span v-if="sortBy == 'price'" v-html="sortSymbol"></span></p>
                </div>
                <div id="filters-container">
                    <input type="number" min="0" v-model="fromPrice" placeholder="Min price" @keydown.enter="getOrders">
                    <input type="number" min="0" v-model="toPrice" placeholder="Max price" @keydown.enter="getOrders">
                    <div class="filter-item">
                        <label>From date:</label>
                        <input type="date" v-model="fromDate" @keydown.enter="getOrders">
                    </div>
                    <div class="filter-item">
                        <label>To date:</label>
                        <input type="date" v-model="toDate" @keydown.enter="getOrders">
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
                        <p>{{result.restaurant.name}} ({{result.restaurant.restType}})</p>
                        <p>{{result.restaurant.address.street}} {{result.restaurant.address.streetNo}}</p>
                        <p>{{result.restaurant.address.city}}, {{result.restaurant.address.state}}</p>
                    </div>
                    <div class="result-action">
                        <b>{{result.price}} &#8364;</b>
                        <div class="spacer"></div>
                        <button class="button-cancel" v-if="role=='USER' && result.status=='PENDING'" @click="cancelOrder(result.id)">Cancel</button>
                        <button class="button-primary" v-if="role=='MANAGER' && result.status=='PENDING'" @click="waitOrder(result.id)">Wait</button>
                        <button class="button-primary" v-else-if="role=='DELIVERY' && result.status=='WAITING'" @click="requestOrder(result.id)">Request</button>
                        <button class="button-delivered" v-else-if="role=='DELIVERY' && result.status=='TRANSPORT'" @click="deliverOrder(result.id)">Delivered</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        role: 'DELIVERY',

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
        types: ['ITALIAN', 'CHINESE', 'BARBEQUE', 'PIZZERIA',],
        statuses: ['PENDING', 'PREPARING', 'WAITING', 'TRANSPORT', 'DELIVERED', 'CANCELED',],

        undelivered: false,
        selectedStatus: '',
        selectedType: '',
        fromDate: '',
        toDate: '',
        fromPrice: '',
        toPrice: '',
        restaurantName: '',
    }),

    methods: {
        requestOrder: function(id) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.post('/delivery/request', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getOrders();
                })
                .catch(r => console.log(r));
        },

        waitOrder: function(id) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.put('/manager/order/wait', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getOrders();
                })
                .catch(r => console.log(r));
        },

        deliverOrder: function(id) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.put('/delivery/delivered', id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getOrders();
                })
                .catch(r => console.log(r));
        },

        cancelOrder: function(id) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.delete('/users/order' + id, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getOrders();
                })
                .catch(r => console.log(r));
        },

        getRole: function() {
            if(!localStorage.jws) {
                return;
            }
            axios.get('/user/role', {headers:{'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => this.role = r.data);
        },

        getOrders: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            let priceFrom = this.fromPrice ? '&pricefrom=' + this.fromPrice : '';
            let priceTo = this.toPrice ? '&priceto=' + this.toPrice : '';
            let dateFrom = this.fromDate ? '&datefrom=' + this.fromDate : '';
            let dateTo = this.toDate ? '&dateto=' + this.toDate : '';

            let query = '?rest=' + this.restaurantName + priceFrom + priceTo + dateFrom + dateTo;

            axios.get('/user/order' + query, {headers:{'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => {
                    console.log(r.data);
                    this.results = r.data;
                })
                .catch(r => console.log(r));
        },

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
                res = res.filter(r => r.restaurant.restType == this.selectedType);
            }
            if(this.selectedStatus) {
                res = res.filter(r => r.status == this.selectedStatus);
            }
            if(this.undelivered) {
                res = res.filter(r => r.status != 'DELIVERED');
            }
            return res;
        },
        sortSymbol: function() {
            return this.sortDirection=='asc' ? '&#x25B2;' : '&#x25BC;'
        },
    },

    mounted() {
        this.getRole();
        this.getOrders();
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
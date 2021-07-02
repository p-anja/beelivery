<template>
    <div id="rest-main">
        <div v-if="!restaurant" id="rest-container">
            <h2>You don't currently own a restaurant</h2>
        </div>
        <div v-else id="rest-container">
            <div id="rest-profile-and-info">
                <div id="rest-profile">
                    <img :src="'http://localhost:8080/image/' + restaurant.logoFilepath" alt="">
                    <div id="rest-header">
                        <h2>{{restaurant.name}}</h2>
                        <b :class="restaurant.status == 'OPEN' ? 'open' : 'closed'">{{restaurant.status}}</b>
                        <router-link :to="'/restaurant/' + restaurant.name">Go to restaurant page &#62;</router-link>
                    </div>
                </div>
                <div id="rest-info">
                    <h3>Location</h3>
                    <div class="info-container">
                        <div class="info">
                            <b>City</b>
                            <p>{{restaurant.address.city}}</p>
                        </div>
                        <div class="info">
                            <b>Street</b>
                            <p>{{restaurant.address.street}}</p>
                        </div>
                        <div class="info">
                            <b>Street No.</b>
                            <p>{{restaurant.address.streetNo}}</p>
                        </div>
                    </div>

                    <h3>General info</h3>
                    <div class="info-container">
                        <div class="info">
                            <b>Type of restaurant</b>
                            <b id="type">{{restaurant.restType}}</b>
                        </div>
                        <div class="info">
                            <b>No. of customers</b>
                            <router-link to="manager/customers">{{restaurant.customerCount}}</router-link>
                        </div>
                        <div class="info">
                            <b>No. of orders</b>
                            <p>{{restaurant.orderCount}}</p>
                        </div>
                        <div class="info">
                            <b>No. of comments</b>
                            <p>{{restaurant.commentCount}}</p>
                        </div>
                    </div>
            </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        restaurant: null,
    }),
    methods: {
        getRestaurant: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.get('/manager/restaurant', {headers: {'Authorization': 'Bearer ' + localStorage.jws }})
                .then(r => {
                    this.restaurant = r.data;
                })
                .catch(r => {
                    console.log(r);
                    this.$router.push('/');
                });
        },
    },

    mounted() {
        this.getRestaurant();
    },
};
</script>

<style scoped>
    #rest-main {
        padding: 10px;
        background: #fff;
    }

    #rest-container {
        width: 40vw;
        margin: 0 auto;
    }
    
    #rest-header {
        display: flex;
        flex-direction: column;
    }

    #rest-header b {
        text-transform: uppercase;
    }

    #rest-header a {
        color: #333;
        text-decoration: none;
    }

    .open {
        color: #2ecc71;
    }

    .closed {
        color: #e74c3c;
    }

    #rest-profile-and-info {
        display: flex;
        flex-direction: column;
    }

    #rest-profile {
        display: grid;
        grid-template-columns: 1fr 1fr;
    }

    #rest-profile img {
        width: 128px;
        height: 128px;
    }

    #rest-info {
        display: flex;
        flex-direction: column;
    }

    #rest-info h3 {
        font-size: 3rem;
        border-bottom: solid 1px #eee;
        padding: 10px;
    }

    .info-container {
        display: grid;
        grid-template-columns: 1fr 1fr;
    }

    .info {
        display: flex;
        flex-direction: column;
        padding: 10px;
    }

    #type {
        color: #aaa;
        text-transform: uppercase;
    }

</style>
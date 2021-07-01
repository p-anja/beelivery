<template>
    <div id="rest-main">
        <div v-if="!restaurant" id="rest-container">
            <h2>You don't currently own a restaurant</h2>
        </div>
        <div v-else id="rest-container">
            <div id="rest-profile-and-info">
                <div id="rest-profile">
                    <img src="img/profile_placeholder.png" alt="">
                    <div id="rest-header">
                        <h2>{{restaurant.name}}</h2>
                        <b :class="restaurant.status == 'Open' ? 'open' : 'closed'">{{restaurant.status}}</b>
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
                            <b id="type">{{restaurant.type}}</b>
                        </div>
                        <div class="info">
                            <b>No. of customers</b>
                            <router-link to="manager/customers">{{restaurant.customerCount}}</router-link>
                        </div>
                        <div class="info">
                            <b>No. of orders</b>
                            <router-link to="manager/orders">{{restaurant.orderCount}}</router-link>
                        </div>
                        <div class="info">
                            <b>No. of comments</b>
                            <router-link to="manager/comments">{{restaurant.commentCount}}</router-link>
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
        restaurant: {
            name: 'HAO',
            type: 'Chinese',
            status: 'Open',
            customerCount: 3,
            orderCount: 12,
            commentCount: 2,
            address: {
                lat: 1.0,
                lon: 1.0,
                city: 'Novi Sad',
                street: 'Temerinska',
                streetNo: '43',
            },
        },
    })
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
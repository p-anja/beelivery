<template>
    <div id="customers-main">
        <div id="customers-container">
            <h2>Customers</h2>
            <div id="customers">
                <div v-for="customer in customers" :key="customer.username" class="customer">
                    <div class="customer-info">
                        <h3>{{customer.username}}</h3>
                        <p>{{customer.firstName}} {{customer.lastName}}</p>
                        <b v-if="customer.memberType" :class="customer.memberType.toLowerCase()">{{customer.memberType}}</b>
                        <b>Points: {{customer.points}}</b>
                    </div>
                    <div class="spacer"></div>
                    <div class="customer-profile">
                        <img v-if="customer.profileImg" :src="'http://localhost:8080/image/' + customer.profileImg" alt="profile">
                        <img v-else src="img/profile_placeholder.png" alt="profile">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        customers: [
            {
                username: 'dparko',
                firstName: '',
                lastName: '',
                profileImg: '',
                memberType: '',
                points: '',
            },
            {
                orderId: 'adfasdasdd',
                username: 'ddarko',
            },
        ],
    }),

    methods: {
        getUsers: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.get('/manager/customer', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => this.customers = r.data)
                .catch(r => console.log(r));
        },
    },

    mounted() {
        this.getUsers();
    },
};
</script>

<style scoped>
    #customers-main {
        display: grid;
        place-items: center;
        padding: 10px;
    }

    #customers-container {
        display: flex;
        flex-direction: column;
        width: 400px;
        background: #fff;
        padding: 10px;
        border-radius: 5px;
    }

    #customers-container h2 {
        border-bottom: solid 1px #eee;
    }

    #customers {
        display: flex;
        flex-direction: column;
        height: 300px;
        overflow: auto;
        padding: 10px;
    }

    .customer {
        display: flex;
        flex-direction: row;
        width: 100%;
        border-bottom: solid 1px #eee;
    }

    .customer-info {
        display: flex;
        flex-direction: column;
    }

    .customer-info h3 {
        font-size: 2.5rem;
    }
    
    .customer-profile img {
        width: 128px;
        height: 128px;
    }

    .bronze {
        color: #cd7f32;
        text-transform: uppercase;
    }

    .silver {
        color: #c0c0c0;
        text-transform: uppercase;
    }

    .gold {
        color: #ffd700;
        text-transform: uppercase;
    }
</style>
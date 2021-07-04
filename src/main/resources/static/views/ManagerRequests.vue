<template>
    <div id="reqs-main">
        <div id="reqs-container">
            <h2>Delivery requests</h2>
            <div id="reqs">
                <div v-for="request in requests" :key="request.orderId" class="request">
                    <div class="request-info">
                        <h3>{{request.orderId}}</h3>
                        <b>{{request.username}}</b>
                    </div>
                    <div class="request-actions">
                        <button @click="declineRequest(request)">Decline</button>
                        <div class="spacer"></div>
                        <button class="button-primary" @click="acceptRequest(request)">Accept</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        requests: [
            {
                orderId: 'asdasdasdd',
                username: 'ddarko',
            },
            {
                orderId: 'adfasdasdd',
                username: 'ddarko',
            },
        ],
    }),

    methods: {
        acceptRequest: function(req) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.post('/manager/requests', req, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.getRequests())
                .catch(r => console.log(r));
        },

        declineRequest: function(req) {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.put('/manager/requests', req, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.getRequests())
                .catch(r => console.log(r));
        },

        getRequests: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            axios.get('/manager/requests', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => this.requests = r.data)
                .catch(r => console.log(r));
        },
    },

    mounted() {
        this.getRequests();
    },
};
</script>

<style scoped>
    #reqs-main {
        display: grid;
        place-items: center;
        padding: 10px;
    }

    #reqs-container {
        display: flex;
        flex-direction: column;
        width: 400px;
        background: #fff;
        padding: 10px;
        border-radius: 5px;
    }

    #reqs-container h2 {
        border-bottom: solid 1px #eee;
    }

    #reqs {
        display: flex;
        flex-direction: column;
        height: 300px;
        overflow: auto;
        padding: 10px;
    }

    .request {
        display: flex;
        flex-direction: column;
        /* width: 300px; */
        border-bottom: solid 1px #eee;
    }

    .request-info {
        display: flex;
        flex-direction: column;
    }

    .request-info h3 {
        font-size: 2.5rem;
    }

    .request-info b {
        color: #666;
        font-weight: 500;
    }

    .request-actions {
        display: flex;
        flex-direction: row;
    }
</style>
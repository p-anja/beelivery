<template>
    <div id="overview-main">
        <div id="overview-container">
            <h2>Admin users overview</h2>
            <div id="user-cards">
                <div v-for="user in users" :key="user.username" class="user-card">
                    <div class="user-header">
                        <h3>{{user.username}}</h3>
                        <p>{{user.firstName}} {{user.lastName}}</p>
                        <b>{{user.type}}</b>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            users: [
                {
                    username: "ppetrovic",
                    type: "manager",
                    firstName: "Petar",
                    lastName: "Petrovic",
                },
                {
                    username: "mmarkovic",
                    type: "regular",
                    firstName: "Marko",
                    lastName: "Markovic",
                },
                {
                    username: "pperic",
                    type: "regular",
                    firstName: "Pera",
                    lastName: "Peric",
                },
            ],
        }
    },
    methods: {
    },
    computed: {
    },
};
</script>

<style scoped>
    #overview-main {
        padding: 10px;
        background: #fff;
    }

    #overview-container h2 {
        border-bottom: solid 1px #eee;
    }

    #user-cards {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 10px;
        padding: 10px;
    }

    .user-card {
        display: flex;
        flex-direction: column;
        padding: 10px;
        border: solid 1px #eee;
    }

    .user-header h3 {
        font-size: 2rem;
    }

    .user-header p {
        color: #aaa;
    }

    .user-header b {
        color: #aaa;
        text-transform: uppercase;
    }
    
</style>
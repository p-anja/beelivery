<template>
    <div id="overview-main">
        <div id="overview-container">
            <h2>Admin overview</h2>
            <div class="overview-section">
                <p class="section-title">Users</p>
                <div class="section-body">
                    <sorting-table :items="users" :headers="userHeaders"></sorting-table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            userHeaders: [
                { text: 'Username', value: 'username' },
                { text: 'type', value: 'type' },
            ],
            users: [
                {
                    username: "ppetrovic",
                    type: "manager",
                },
                {
                    username: "mmarkovic",
                    type: "regular",
                },
                {
                    username: "pperic",
                    type: "regular",
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

    .overview-section {
        padding: 10px;
    }

    .section-title {
        font-size: 2rem;
    }
    
</style>
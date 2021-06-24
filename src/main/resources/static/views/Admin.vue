<template>
    <div>
        <main-navigation>
            <router-link to="/">Home</router-link>
        </main-navigation>
        <div id="admin-main">
            <div id="admin-container">
                <sidebar>
                    <router-link to="/admin/">Overview</router-link>
                    <router-link>Restaurants</router-link>
                    <router-link>Users</router-link>
                </sidebar>
                <div id="admin-view">
                    <router-view></router-view>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    #admin-main {
        background: #fafafa;
        margin: 10px;
        height: 87vh;
    }
    #admin-container {
        display: flex;
        flex-direction: row;
        padding: 10px;
        height: 100%;
    }
    #admin-view {
        flex-grow: 1;
        margin-left: 10px;
    }
</style>
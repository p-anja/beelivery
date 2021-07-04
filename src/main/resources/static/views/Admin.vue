<template>
    <div>
        <main-navigation>
        </main-navigation>
        <div id="admin-main">
            <div id="admin-container">
                <sidebar title="Admin panel">
                    <router-link to="/admin">Users</router-link>
                    <router-link to="/admin/newrestaurant">New restaurant</router-link>
                    <router-link to="/admin/newuser">New user</router-link>
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
        background: var(--secondary);
        margin: 10px;
        height: 87vh;
    }
    #admin-container {
        display: flex;
        flex-direction: row;
        padding: 10px;
        height: 97%;
    }
    #admin-view {
        flex-grow: 1;
        margin-left: 10px;
    }
</style>
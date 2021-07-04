<template>
    <div>
        <main-navigation>
        </main-navigation>
        <div id="manager-main">
            <div id="manager-container">
                <sidebar title="Manager panel">
                    <router-link to="/manager">Restaurant</router-link>
                    <router-link to="/manager/requests">Requests</router-link>
                    <router-link to="/manager/customers">Customers</router-link>
                </sidebar>
                <div id="manager-view">
                    <router-view />
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
    #manager-main {
        background: var(--secondary);
        margin: 10px;
        height: 87vh;
    }

    #manager-container {
        display: flex;
        flex-direction: row;
        padding: 10px;
        height: 97%;
    }

    #manager-view {
        flex-grow: 1;
        margin-left: 10px;
    }
</style>
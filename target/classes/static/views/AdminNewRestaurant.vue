<template>
    <div id="newrest-main">
        <div id="newrest-container">
            <h3>Add new restaurant</h3>
            <div id="newrest-and-manager">
                <form id="newrest-form" @submit="addRestaurant">
                    <label>Select manager:</label>
                    <select v-model="selectedManager">
                        <option value="New">New</option>
                        <option v-for="manager in managers" :key="manager.username" :value="manager.username">{{manager.username}}</option>
                    </select>
                    <input type="text" placeholder="Name" v-model="name">
                    <input type="text">
                    <label>Restaurant type</label>
                    <select v-model="types">
                        <option v-for="type in types" :key="type">{{type}}</option>
                    </select>
                    <input type="text" placeholder="Location">
                    <label>Select logo:</label>
                    <input type="file">
                    <div class="spacer"></div>
                    <input class="button-primary" type="submit" value="Add">
                </form>
                <form id="newmanager-form" @submit="addManager" :class="selectedManager != 'New' ? 'disabled' : ''">
                    <h4>New manager</h4>
                    <b class="error">{{errors.username}}</b>
                    <input v-model="username" type="text" placeholder="Username" :disabled="selectedManager != 'New'">
                    <b class="error">{{errors.password}}</b>
                    <input v-model="password" type="password" placeholder="Password" :disabled="selectedManager != 'New'">
                    <b class="error">{{errors.firstName}}</b>
                    <input v-model="firstName" type="text" placeholder="First name" :disabled="selectedManager != 'New'">
                    <b class="error">{{errors.lastName}}</b>
                    <input v-model="lastName" type="text" placeholder="Last name" :disabled="selectedManager != 'New'">
                    <select v-model="sex"  :disabled="selectedManager != 'New'">
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                        <option value="other">Other</option>
                    </select>
                    <div class="spacer"></div>
                    <input type="submit"
                    :class="selectedManager != 'New' ? 'disabled-btn' : 'button-primary'"
                    value="Add manager" :disabled="selectedManager != 'New'">
                </form>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            types: [],
            selectedType: null,
            managers: [],
            selectedManager: null,
            //
            username: '',
            password: '',
            firstName: '',
            lastName: '',
            sex: 'male',
            errors: {
                username: '',
                password: '',
                firstName: '',
                lastName: '',
            },
        }
    },
    methods: {
        addManager: function(e) {
            e.preventDefault();
        },
        
        addRestaurant: function(e) {
            e.preventDefault();
        }
    },
};
</script>

<style scoped>
    #newrest-main {
        background: #fff;
        padding: 10px;
    }

    #newrest-container {
        display: flex;
        flex-direction: column;
    }

    #newrest-and-manager {
        display: grid;
        grid-template-columns: 1fr 1fr;
    }

    #newrest-form {
        display: flex;
        flex-direction: column;
        margin-bottom: 0;
    }

    #newmanager-form {
        display: flex;
        flex-direction: column;
        margin-bottom: 0;
        margin-left: auto;
        width: 20vw;
    }

    .disabled {
        color: #aaa;
    }

    .disabled-btn {
        background: #aaa;
    }
</style>
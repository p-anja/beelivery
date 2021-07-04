<template>
    <div id="newuser-main">
            <h2>New user</h2>
        <div id="newuser-container">
            <form id="newuser-form" @submit.prevent>
                <select v-model="role">
                    <option value="MANAGER">Manager</option>
                    <option value="DELIVERY">Delivery</option>
                </select>
                <b class="error">{{errors.username}}</b>
                <input v-model="username" type="text" placeholder="Username">
                <b class="error">{{errors.password}}</b>
                <input v-model="password" type="password" placeholder="Password">
                <b class="error">{{errors.firstName}}</b>
                <input v-model="firstName" type="text" placeholder="First name">
                <b class="error">{{errors.lastName}}</b>
                <input v-model="lastName" type="text" placeholder="Last name">
                <select v-model="sex">
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                </select>
                <b class="error">{{errors.birthDate}}</b>
                <label>Birthdate</label>
                <input type="date" v-model="birthDate" :max="new Date()"/>
                <div class="spacer"></div>
                <b class="error">{{errors.register}}</b>
                <b class="success">{{successMsg}}</b>
                <button
                class="button-primary"
                @click="registerUser">New user</button>
            </form>
        </div>
    </div>
</template>

<script>
module.exports = {
    data: () => ({
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        birthDate: '',
        sex: 'MALE',
        role: 'MANAGER',
        errors: {
            username: '',
            password: '',
            firstName: '',
            lastName: '',
            birthDate: '',
            register: '',
        },
        successMsg: '',
    }),

    methods: {
        validateInput: function() {
            let usernameValid = !!this.username;
            let passwordValid = !!this.password && this.password.length >= 8;
            let firstNameValid = !!this.firstName;
            let lastNameValid = !!this.lastName;
            let birthDateValid = !!this.birthDate && new Date(Date.parse(this.birthDate)) < new Date();
            
            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.password = passwordValid ? '' : 'Password must be 8+ characters';
            this.errors.firstName = firstNameValid ? '' : 'First name required';
            this.errors.lastName = lastNameValid ? '' : 'Last name required';
            this.errors.birthDate = birthDateValid ? '' : 'Date before today required';
            this.errors.register = '';
            this.successMsg = '';
            return usernameValid && passwordValid
                && firstNameValid && lastNameValid
                && birthDateValid;
        },

        registerUser: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            if(!this.validateInput()) {
                return;
            }

            let request = {
                username: this.username,
                firstName: this.firstName,
                lastName: this.lastName,
                password: this.password,
                sex: this.sex,
                birthDate: this.birthDate,
            };

            let url = this.role == 'MANAGER' ? 'http://localhost:8080/admin/manager' : 'http://localhost:8080/admin/delivery';
            axios.post(url, request, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.successMsg = 'User registered')
                .catch(r => {
                    console.log(r);
                    this.errors.register = 'Failed to register user';
                });
        },
    },
};
</script>

<style scoped>
    #newuser-main {
        background: #fff;
        padding: 10px;
        width: 300px;
        margin: 0 auto;
        border-radius: 5px;
    }

    #newuser-main h2 {
        border-bottom: solid 1px #eee;
    }

    #newuser-container {
        display: flex;
        flex-direction: column;
        padding: 10px;
    }

    #newuser-form {
        display: flex;
        flex-direction: column;
    }
</style>
<template>
    <div id="signup-main">
        <div id="signup-container">
            <h2>Join Beelivery</h2>
            <p>Start ordering today</p>
            <form @submit="signup" id="signup-form">
                <b class="error">{{errors.username}}</b>
                <input v-model="username" type="text" placeholder="Username">
                <b class="error">{{errors.password}}</b>
                <input v-model="password" type="password" placeholder="Password">
                <b class="error">{{errors.firstName}}</b>
                <input v-model="firstName" type="text" placeholder="First name">
                <b class="error">{{errors.lastName}}</b>
                <input v-model="lastName" type="text" placeholder="Last name">
                <select v-model="sex">
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                    <option value="other">Other</option>
                </select>
                <div class="spacer"></div>
                <input type="submit" class="button-primary" value="Sign up">
                <router-link to="/signin">Have an account? Sign in.</router-link>
            </form>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
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
        validateInputs: function() {
            this.errors.username = '';
            this.errors.password = '';
            this.errors.firstName = '';
            this.errors.lastName = '';

            let usernameValid = !!this.username;
            let passwordValid = !!this.password && this.password.length >= 8;
            let firstNameValid = !!this.firstName;
            let lastNameValid = !!this.lastName;

            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.password = passwordValid ? '' : 'Minimum 8 characters';
            this.errors.firstName = firstNameValid ? '' : 'First name required';
            this.errors.lastName = lastNameValid ? '' : 'Last name required';

            return usernameValid && passwordValid
                && firstNameValid && lastNameValid;
        },

        signup: function(e) {
            e.preventDefault();
            if(!this.validateInputs()) {
                return;
            }

            axios.post()
                .then(() => this.$router.push('/'))
                .catch(r => console.log(r));
        },
    },
};
</script>

<style scoped>
    #signup-main {
        display: grid;
        place-items: center;
        height: 100vh;
        background: #fafafa;
    }

    #signup-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        text-align: center;
        background: #fff;
        padding: 20px;
        border: solid 1px #eee;
        border-radius: 10px;
    }

    #signup-form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        min-height: 40vh;
        margin-bottom: 0;
    }

    #signup-form button {
        margin-top: auto;
    }
</style>
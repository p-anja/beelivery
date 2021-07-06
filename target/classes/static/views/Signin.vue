<template>
    <div id="signin-main">
        <div id="signin-container">
            <h2>Sign in to Beelivery</h2>
            <p>Enter your account info</p>
            <form id="signin-form" @submit="signin">
                <b class="error">{{errors.signin}}</b>
                <b class="error">{{errors.username}}</b>
                <input type="text" v-model="username" placeholder="Username">
                <b class="error">{{errors.password}}</b>
                <input type="password" v-model="password" placeholder="Password">
                <div class="spacer"></div>
                <input class="button-primary" type="submit" value="Sign in">
                <router-link to="/signup">Don't have an account? Signup now!</router-link>
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
            errors: {
                username: '',
                password: '',
                signin: '',
            },
        }
    },
    methods: {
        validateInputs: function() {
            this.errors.signin = '';
            let usernameValid = !!this.username;
            let passwordValid = !!this.password;
            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.password = passwordValid ? '' : 'Password required';
            return usernameValid && passwordValid;
        },

        signin: function(e) {
            e.preventDefault();
            if(!this.validateInputs()) {
                return;
            }

            let data = {
                username: this.username,
                password: this.password,
            };
            axios.post('/user/login', data)
                .then(r => {
                    localStorage.jws = r.data;
                    this.$router.push('/');
                })
                .catch(r => {
                    if(r.data == 'BLOCKED') {
                        this.errors.signin = 'User blocked';
                    } else {
                        this.errors.signin = 'Invalid username or password'
                    }
                });
        }
    },
};
</script>

<style scoped>
#signin-main {
    display: grid;
    place-items: center;
    height: 100vh;
    background: #fafafa;
}

#signin-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 20px;
    border: solid 1px #eee;
    background: #fff;
    border-radius: 10px;
    text-align: center;
}

#signin-form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-bottom: 0;
    min-height: 25vh;
}
</style>
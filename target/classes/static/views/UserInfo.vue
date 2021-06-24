<template>
    <div>
        <main-navigation>
            <router-link to="/">Home</router-link>
        </main-navigation>
        <div id="userinfo-main">
            <div id="userinfo-container">
                <div id="userinfo-profile">
                    <img src="img/profile_placeholder.png" alt="profile">
                </div>
                <div id="userinfo">
                    <h2>{{user.lastName}} {{user.firstName}}</h2>
                    <form @submit="updateInfo" id="userinfo-form">
                        <div id="userinfo-form-container">
                            <div class="form-field">
                                <label>First name</label>
                                <b class="error">{{errors.firstName}}</b>
                                <input type="text" v-model="user.firstName">
                            </div>
                            <div class="form-field">
                                <label>Last name</label>
                                <b class="error">{{errors.lastName}}</b>
                                <input type="text" v-model="user.lastName">
                            </div>
                            <div class="form-field">
                                <label>Old password</label>
                                <b class="error">{{errors.oldPassword}}</b>
                                <input type="password" v-model="oldPassword">
                            </div>
                            <div class="form-field">
                                <label>New Password</label>
                                <b class="error">{{errors.newPassword}}</b>
                                <input type="password" v-model="newPassword">
                            </div>
                        </div>
                        <div class="spacer"></div>
                        <div class="form-field">
                            <b class="error">{{errors.update}}</b>
                            <input class="button-primary" type="submit" value="Update info">
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            user: {
                username: 'ppetrovic',
                password: 'ppetrovic',
                firstName: 'Petar',
                lastName: 'Petrovic',
            },
            oldPassword: 'ppetrovic',
            newPassword: '',
            errors: {
                username: '',
                oldPassword: '',
                newPassword: '',
                firstName: '',
                lastName: '',
                update: '',
            },
        }
    },

    methods: {
        validateInputs: function() {
            let usernameValid = !!this.user.username;
            let oldPasswordValid = !!this.oldPassword && this.oldPassword.length >= 8;
            let newPasswordValid = !!this.newPassword && this.newPassword.length >= 8;
            let passwordsMatch = this.oldPassword == this.newPassword;
            let firstNameValid = !!this.user.firstName;
            let lastNameValid = !!this.user.lastName;
            this.errors.update = '';
            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.firstName = firstNameValid ? '' : 'First name required';
            this.errors.lastName = lastNameValid ? '' : 'Last name required';
            if (!passwordsMatch) {
                this.errors.oldPassword = "Passwords must match";
                this.errors.newPassword = "Passwords must match";
            }
            this.errors.oldPassword = oldPasswordValid ? '' : "Minimum 8 characters";
            this.errors.newPassword = newPasswordValid ? '' : "Minimum 8 characters";

            return usernameValid && oldPasswordValid && newPasswordValid
                && passwordsMatch && firstNameValid && lastNameValid;
        },

        updateInfo: function(e) {
            e.preventDefault();
            if(!this.validateInputs()) {
                return;
            }

            let data = {
                username: this.user.username,
                password: this.newPassword,
                firstName: this.user.firstName,
                lastName: this.user.lastName,
            };

            axios.post('user/update', data, {headers: {'Authorization': 'Bearer ' + localstorage.jws}})
                .then(() => this.$router.push('/'))
                .catch(() => this.errors.update = 'Could not update user info!');
        },
    },
};  
</script>

<style scoped>
    #userinfo-main {
        display: grid;
        place-items: center;
        height: 87vh;
        background: #fafafa;
    }

    #userinfo-container {
        display: grid;
        grid-template-columns: 1fr auto;
        background: #fff;
        border: solid 1px #eee;
        padding: 20px;
        min-height: 70vh;
        gap: 20px;
    }

    #userinfo-profile img {
        width: 128px;
        height: 128px;
    }

    #userinfo {
        display: flex;
        flex-direction: column;
    }

    #userinfo-form {
        display: flex;
        flex-direction: column;
        height: 100%;
    }

    #userinfo-form-container {
        display: grid;
        grid-template-columns: 1fr 1fr;
    }

    .form-field {
        display: flex;
        flex-direction: column;
    }
</style>
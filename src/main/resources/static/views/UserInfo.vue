<template>
    <div>
        <main-navigation>
        </main-navigation>
        <div id="userinfo-main">
            <div id="userinfo-container">
                <div id="userinfo-profile">
                    <input type="file" ref="file" @change="updateProfile" style="display: none">
                    <div v-if="!user.profileImg" class="profile-placeholder" @click="$refs.file.click()">
                        +
                    </div>
                    <img @click="$refs.file.click()" v-else :src="'http://localhost:8080/image/' + user.profileImg" alt="profile">
                    <b v-if="user.memberType">{{user.memberType}}</b>
                    <b v-if="user.points != null">Points: {{user.points}}</b>
                </div>
                <div id="userinfo">
                    <h2>{{user.username}}</h2>
                    <form id="userinfo-form" @submit.prevent>
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
                            <div class="form-field">
                                <label>Sex</label>
                                <select v-model="user.sex">
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                    <option value="OTHER">Other</option>
                                </select>
                            </div>
                            <div class="form-field">
                                <label>Birthday</label>
                                <b class="error">{{errors.birthDate}}</b>
                                <input type="date" v-model="user.birthDate" :max="new Date()" />
                            </div>
                        </div>
                        <div class="spacer"></div>
                        <div class="form-field">
                            <b class="error">{{errors.update}}</b>
                            <button class="button-primary" @click="updateInfo">Update info</button>
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
                sex: 'MALE',
                birthDate: '',
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
                birthDate: '',
            },
        }
    },

    methods: {
        updateProfile: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            let file = this.$refs.file.files[0];
            let data = new FormData();
            data.append('file', file);
            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': 'Bearer ' + localStorage.jws,
                }
            };
            axios.post('/user/profile', data, config)
                .then(() => this.$router.go(0))
                .catch(r => console.log(r));
        },
        getUser: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
            }
            axios.get('/user', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => {
                    this.user = r.data;
                    this.oldPassword = this.user.password;
                })
                .catch(r => {
                    console.log(r);
                    this.$router.push('/');
                });
        },

        validateInputs: function() {
            let usernameValid = !!this.user.username;
            let oldPasswordValid = !!this.oldPassword && this.oldPassword.length >= 8;
            let newPasswordValid = !!this.newPassword && this.newPassword.length >= 8;
            let passwordsMatch = this.oldPassword == this.newPassword;
            let firstNameValid = !!this.user.firstName;
            let lastNameValid = !!this.user.lastName;
            let birthDateValid = !!this.user.birthDate || new Date(Date.parse(this.user.birthDate)) > new Date();
            this.errors.update = '';
            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.firstName = firstNameValid ? '' : 'First name required';
            this.errors.lastName = lastNameValid ? '' : 'Last name required';
            this.errors.birthDate = birthDateValid ? '' : 'Must be before today';
            if (!passwordsMatch) {
                this.errors.oldPassword = "Passwords must match";
                this.errors.newPassword = "Passwords must match";
            }
            this.errors.oldPassword = oldPasswordValid ? '' : "Minimum 8 characters";
            this.errors.newPassword = newPasswordValid ? '' : "Minimum 8 characters";

            return usernameValid && oldPasswordValid && newPasswordValid
                && passwordsMatch && firstNameValid && lastNameValid
                && birthDateValid;
        },

        updateInfo: function() {
            if(!this.validateInputs()) {
                return;
            }

            let data = {
                username: this.user.username,
                password: this.newPassword,
                firstName: this.user.firstName,
                lastName: this.user.lastName,
                birthDate: this.user.birthDate,
                sex: this.user.sex,
            };

            axios.post('/user/update', data, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => this.$router.go(0))
                .catch(() => this.errors.update = 'Could not update user info!');
        },
    },
    mounted() {
        this.getUser();
    },
};  
</script>

<style scoped>
    #userinfo-main {
        display: grid;
        place-items: center;
        height: 92vh;
        background: var(--secondary);
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

    #userinfo-profile {
        display: flex;
        flex-direction: column;
    }

    #userinfo-profile img {
        cursor: pointer;
        width: 128px;
        height: 128px;
        margin-bottom: 20px;
    }

    #userinfo {
        display: flex;
        flex-direction: column;
    }

    #userinfo-form {
        display: flex;
        flex-direction: column;
        height: 100%;
        padding: 10px;
    }

    #userinfo-form-container {
        display: grid;
        gap: 10px;
        grid-template-columns: 1fr 1fr;
    }

    .form-field {
        display: flex;
        flex-direction: column;
    }

    .profile-placeholder {
        display: grid;
        place-items: center;
        font-weight: 500;
        font-size: 5rem;
        width: 128px;
        height: 128px;
        cursor: pointer;
        background: #eee;
    }
</style>
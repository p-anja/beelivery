<template>
    <div id="signup-main">
        <div id="signup-container">
            <h2>Join Beelivery</h2>
            <p>Start ordering today</p>
            <!-- <div v-if="step == 1" id="signup-form">
                <geosearch @selected="selectLocation"></geosearch>
                <div v-if="selected" id="location-container">
                    <label>State</label>
                    <input placeholder="State" type="text" v-model="address.state" disabled>
                    <label>City</label>
                    <input placeholder="City" type="text" v-model="address.city" disabled>
                    <label>Street</label>
                    <input placeholder="Street" type="text" v-model="address.street" disabled>
                    <label>Street No.</label>
                    <input placeholder="Street No." type="text" v-model="address.streetNo" disabled>
                </div>
                <div class="spacer"></div>
                <button :disabled="!selected" @click="++step">Next</button>
                <router-link to="/signin">Have an account? Sign in.</router-link>
            </div> -->
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
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                </select>
                <label>Birth date</label>
                <b class="error">{{errors.birthDate}}</b>
                <input type="date" v-model="birthDate" :max="new Date()" />
                <div class="spacer"></div>
                <input type="submit" class="button-primary" value="Sign up">
                <button @click="--step">Back</button>
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
            birthDate: '',
            errors: {
                username: '',
                password: '',
                firstName: '',
                lastName: '',
                birthDate: '',
            },
            // address: {
            //     state: '',
            //     city: '',
            //     street: '',
            //     streetNo: '',
            //     lat: '',
            //     lon: '',
            // },
            selected: false,
        };
    },

    methods: {
        // selectLocation: function(loc) {
        //     this.selected = true;
        //     this.address.state = loc.address.country;
        //     this.address.city = loc.address.city;
        //     this.address.street = loc.address.road;
        //     this.address.streetNo = loc.address.house_number;
        //     this.address.lat = loc.lat;
        //     this.address.lon = loc.lon;
        // },

        validateInputs: function() {
            this.errors.username = '';
            this.errors.password = '';
            this.errors.firstName = '';
            this.errors.lastName = '';
            this.errors.birthDate = '';

            let usernameValid = !!this.username;
            let passwordValid = !!this.password && this.password.length >= 8;
            let firstNameValid = !!this.firstName;
            let lastNameValid = !!this.lastName;
            let birthDateValid = !!this.birthDate && new Date(Date.parse(this.birthDate) < new Date());

            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.password = passwordValid ? '' : 'Minimum 8 characters';
            this.errors.firstName = firstNameValid ? '' : 'First name required';
            this.errors.lastName = lastNameValid ? '' : 'Last name required';
            this.errors.birthDate = birthDateValid ? '' : 'Select date before today';

            return usernameValid && passwordValid
                && firstNameValid && lastNameValid
                && birthDateValid;
        },

        signup: function(e) {
            e.preventDefault();
            if(!this.validateInputs()) {
                return;
            }
            let registerRequest = {
                username: this.username,
                firstName: this.firstName,
                lastName: this.lastName,
                password: this.password,
                sex: this.sex,
                birthDate: this.birthDate,
            };
            axios.post('/user/register', registerRequest)
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
        justify-content: flex-start;
        min-height: 40vh;
        margin-bottom: 0;
        text-align: left;
    }

    #signup-form button {
        margin-top: auto;
    }

    #signup-form button:disabled {
        background: #ddd;
    }

    #location-container {
        display: flex;
        flex-direction: column;
        margin-top: 10px;
        text-align: left;
    }

    #location-container input[type="text"]:disabled {
        background: #ddd;
    }
</style>
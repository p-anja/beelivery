<template>
    <div id="newrest-main">
        <div id="newrest-container">
            <h3>Add new restaurant</h3>
            <div id="newrest-and-manager">
                <form id="newrest-form" @submit.prevent>
                    <div id="newrest-form-container">
                        <div id="newrest">
                            <b class="error">{{errors.selectManager}}</b>
                            <select v-model="selectedManager">
                                <option value="" disabled>Select manager</option>
                                <option value="New">New</option>
                                <option v-for="manager in managers" :key="manager.username" :value="manager.username">{{manager.username}}</option>
                            </select>
                            <b class="error">{{errors.restaurantName}}</b>
                            <input type="text" placeholder="Restaurant name" v-model="restaurantName">
                            <label>Restaurant type</label>
                            <b class="error">{{errors.type}}</b>
                            <select v-model="selectedType">
                                <option value="" disabled>Select type</option>
                                <option v-for="type in types" :key="type" :value="type">{{type}}</option>
                            </select>
                            <b class="error">{{errors.location}}</b>
                            <geosearch @selected="selectLocation"></geosearch>
                            <div v-if="selectedLocation" id="rest-location">
                                <div class="location-field">
                                    <label>State</label>
                                    <input type="text" v-model="state" disabled>
                                </div>
                                <div class="location-field">
                                    <label>City</label>
                                    <input type="text" v-model="city" disabled>
                                </div>
                                <div class="location-field">
                                    <label>Street</label>
                                    <input type="text" v-model="street" disabled>
                                </div>
                                <div class="location-field">
                                    <label>Street No</label>
                                    <input type="text" v-model="streetNo" disabled>
                                </div>
                            </div>
                        </div>
                        <div id="newrest-profile">
                            <b class="error">{{errors.picture}}</b>
                            <input style="display: none" type="file" ref="file" @change="selectFile">
                            <div v-if="!file" id="add-thumbnail" @click="$refs.file.click()">
                                +
                            </div>
                            <img v-else :src="fileUrl" alt="rest img"/>
                        </div>
                    </div>
                    <div class="spacer"></div>
                    <b class="error">{{errors.restaurant}}</b>
                    <button class="button-primary" @click="addRestaurant">Add restaurant</button>
                </form>
                <div v-if="registeredManager" id="newmanager-form-success">
                    <h4>Registered!</h4>
                    <p><i class="fa fa-check-circle-o" aria-hidden="true"></i></p>
                </div>
                <form v-else id="newmanager-form" @submit.prevent :class="selectedManager != 'New' ? 'disabled' : ''">
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
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                        <option value="OTHER">Other</option>
                    </select>
                    <b class="error">{{errors.birthDate}}</b>
                    <label>Birthdate</label>
                    <input type="date" v-model="birthDate" :max="new Date()"/>
                    <div class="spacer"></div>
                    <b class="error">{{errors.registerManager}}</b>
                    <button 
                    :class="selectedManager != 'New' ? 'disabled-btn' : 'button-primary'"
                    :disabled="selectedManager != 'New'"
                    @click="registerManager">Add manager</button>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            types: ['ITALIAN', 'CHINESE', 'BARBEQUE', 'PIZZERIA'],
            selectedType: '',
            managers: [],
            selectedManager: '',
            registeredManager: false,
            selectedLocation: false,
            state: '',
            city: '',
            street: '',
            streetNo: '',
            lat: '',
            lon: '',
            file: '',
            fileUrl: '',
            //
            username: '',
            password: '',
            firstName: '',
            lastName: '',
            sex: 'MALE',
            birthDate: '',
            errors: {
                username: '',
                password: '',
                firstName: '',
                lastName: '',
                birthDate: '',
                registerManager: '',
                selectManager: '',
                restaurantName: '',
                type: '',
                location: '',
                picture: '',
                restaurant: '',
            },
            name: '',
        }
    },
    methods: {
        selectFile: function() {
            let file = this.$refs.file.files[0];
            this.fileUrl = URL.createObjectURL(file);
            this.file = file;
        },
        selectLocation: function(l) {
            this.state = l.address.country;
            this.city = l.address.city;
            this.street = l.address.road;
            this.streetNo = l.address.house_number;
            this.lat = l.lat;
            this.lon = l.lon;
            this.selectedLocation = true;
        },

        getManagers: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            axios.get('/admin/managers', {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => this.managers = r.data)
                .catch(r => console.log(r));
        },

        validateManagerInputs: function() {
            let usernameValid = !!this.username;
            let passwordValid = !!this.password && this.password.length >= 8;
            let firstNameValid = !!this.firstName;
            let lastNameValid  = !!this.lastName;
            let birthDateValid = !!this.birthDate && new Date(Date.parse(this.birthDate)) < new Date();
            
            this.errors.username = usernameValid ? '' : 'Username required';
            this.errors.password = passwordValid ? '' : 'Minimum 8 characters';
            this.errors.firstName = firstNameValid ? '' : 'First name required';
            this.errors.lastName = lastNameValid ? '' : 'Last name required';
            this.errors.birthDate = birthDateValid ? '' : 'Select date before today';
            this.errors.registerManager = '';

            return usernameValid && passwordValid
                && firstNameValid && lastNameValid
                && birthDateValid;
        },

        validateInputsRestaurant: function() {
            let managerValid = !!this.selectedManager && this.selectedManager != 'New';
            let nameValid = !!this.restaurantName;
            let typeValid = !!this.selectedType;
            let locationValid = !!this.selectedLocation;
            let pictureValid = !!this.file;


            this.errors.selectManager = managerValid ? '' : 'Select manager';
            this.errors.restaurantName = nameValid ? '' : 'Name required';
            this.errors.type = typeValid ? '' : 'Select type';
            this.errors.location = locationValid ? '' : 'Select location';
            this.errors.picture = pictureValid ? '' : 'Select picture';
            this.errors.restaurant = '';

            return managerValid && nameValid
                && typeValid && locationValid
                && pictureValid;
        },

        addRestaurant: function() {
            if(!this.validateInputsRestaurant()) {
                return;
            }
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }

            let data = new FormData();
            data.append('name', this.restaurantName);
            data.append('type', this.selectedType);
            data.append('manager', this.selectedManager);
            data.append('lat', this.lat);
            data.append('lon', this.lon);
            data.append('state', this.state);
            data.append('city', this.city);
            data.append('street', this.street);
            data.append('streetNo', this.streetNo);
            data.append('file', this.file);
            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': 'Bearer ' + localStorage.jws,
                },
            };

            axios.post('/admin/restaurant', data, config)
                .then(() => this.router.go(0))
                .catch(r => console.log(r));
        },

        registerManager: function() {
            this.registeredManager = false;
            if(!this.validateManagerInputs()) {
                return;
            }
            if(!localStorage.jws) {
                this.$router.push('/');
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

            axios.post('/admin/manager', registerRequest, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(() => {
                    this.getManagers();
                    this.registeredManager = true;
                })
                .catch(() => this.errors.registerManager = 'Failed to register manager');
        },
    },

    mounted() {
        this.getManagers();
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

    #newrest-form-container {
        display: grid;
        grid-template-columns: 1fr auto;
        gap: 40px;
    }

    #newrest {
        display: flex;
        flex-direction: column;
    }

    #newrest-profile {
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
    }

    #newrest-profile img {
        width: 256px;
        height: 256px;
    }

    #add-thumbnail {
        cursor: pointer;
        user-select: none;
        width: 128px;
        height: 128px;
        background: #eee;
        color: #000;
        display: grid;
        place-items: center;
        font-size: 2.5rem;
    }

    #rest-location {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 10px;
    }

    #newmanager-form {
        display: flex;
        flex-direction: column;
        margin-bottom: 0;
        margin-left: auto;
        width: 20vw;
        text-align: left;
    }

    #newmanager-form-success {
        display: grid;
        place-items: center;
        width: 20vw;
        margin-left: auto;
    }
    
    #newmanager-form-success p {
        font-size: 10rem;
        color: #2ecc71;
    }

    .disabled {
        color: #aaa;
    }

    .disabled-btn {
        background: #aaa;
    }
</style>
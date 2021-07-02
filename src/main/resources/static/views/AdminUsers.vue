<template>
    <div id="overview-main">
        <div id="overview-container">
            <h2>Admin users overview</h2>
            <div id="search">
                <div class="search-container">
                    <input type="text" placeholder="Username" v-model="username" @keyup="search">
                    <input type="text" placeholder="First name" v-model="fname"  @keyup="search">
                    <input type="text" placeholder="Last name" v-model="lname"   @keyup="search">
                </div>
                <div id="sort-container">
                    <p @click="sort('username')">Username<span v-if="sortBy == 'username'" v-html="sortSymbol"></span></p>
                    <p @click="sort('firstName')">First name<span v-if="sortBy == 'firstName'" v-html="sortSymbol"></span></p>
                    <p @click="sort('lastName')">Last name<span v-if="sortBy == 'lastName'" v-html="sortSymbol"></span></p>
                    <p @click="sort('points')">Points<span v-if="sortBy == 'points'" v-html="sortSymbol"></span></p>
                </div>
                <div class="filter-container">
                    <select v-model="selectedType">
                        <option value="">Select type</option>
                        <option v-for="type in types" :key="type" :value="type">{{type}}</option>
                    </select>
                    <select v-model="selectedRole">
                        <option value="">Select role</option>
                        <option v-for="role in roles" :key="role" :value="role">{{role}}</option>
                    </select>
                </div>

            </div>
            <div id="user-cards">
                <div v-for="user in sortedResults" :key="user.username" class="user-card">
                    <div class="user-header">
                        <h3>{{user.username}}</h3>
                        <p>{{user.firstName}} {{user.lastName}}</p>
                        <b class="role">{{user.role}}</b>
                        <b :class="user.memberType.toLowerCase()">{{user.memberType}}</b>
                        <span>Points: {{user.points}}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
module.exports = {
    data() {
        return {
            sortBy: 'username',
            sortDirection: 'asc',
            lname: '',
            fname: '',
            username: '',
            roles: ["ADMIN", "MANAGER", "REGULAR"],
            types: ["BRONZE", "SILVER", "GOLD"],
            selectedType: '',
            selectedRole: '',

            users: [
                {
                    username: "ppetrovic",
                    role: "MANAGER",
                    firstName: "Petar",
                    lastName: "Petrovic",
                    points: 12.0,
                },
                {
                    username: "mmarkovic",
                    type: "GOLD",
                    role: "USER",
                    firstName: "Marko",
                    lastName: "Markovic",
                    points: 2.0,
                },
                {
                    username: "pperic",
                    role: "USER",
                    type: "BRONZE",
                    firstName: "Pera",
                    lastName: "Peric",
                    points: 14.4,
                },
            ],
        }
    },

    methods: {
        search: function() {
            clearTimeout(this.timeout);
            this.results = [];
            let self = this;
            this.timeout = setTimeout(function() {
                self.getUsers();
            }, 1000);
        },

        getUsers: function() {
            if(!localStorage.jws) {
                this.$router.push('/');
                return;
            }
            this.users = [];
            let query = '?username=' + this.username + '&fname=' + this.fname + '&lname=' + this.lname;
            axios.get('/admin/users' + query, {headers: {'Authorization': 'Bearer ' + localStorage.jws}})
                .then(r => {
                    let users = r.data;
                    users.forEach(u => {
                        if(!u.points) {
                            u.points = 0.0;
                        }
                        if(!u.memberType) {
                            u.memberType = 'NONE';
                        }
                    });
                    this.users = [...users];
                })
                .catch(r => console.log(r));
        },

        sort: function(s) {
            if(this.sortBy == s) {
                this.sortDirection = this.sortDirection == 'asc' ? 'desc' : 'asc';
            }
            this.sortBy = s;
        },
    },
    computed: {
        sortedResults: function() {
            let res = [...this.users];
            res.sort((a, b) => {
                let aval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, a);
                let bval = this.sortBy.split('.').reduce(function(p,prop) { return p[prop]; }, b);
                let modifier = 1;
                if(this.sortDirection == 'desc') {
                    modifier = -1;
                }
                if(aval < bval) {
                    return -1 * modifier;
                }
                if(aval > bval) {
                    return modifier;
                }
                return 0;
            });
            if(this.selectedType) {
                res = res.filter(u => u.memberType == this.selectedType);
            }
            if(this.selectedRole) {
                res = res.filter(u => u.role == this.selectedRole);
            }
            return res;
        },

        sortSymbol: function() {
            return this.sortDirection=='asc' ? '&#x25B2;' : '&#x25BC;'
        },
    },

    mounted() {
        this.getUsers();
    },
};
</script>

<style scoped>
    #overview-main {
        padding: 10px;
        background: #fff;
    }

    #overview-container h2 {
        border-bottom: solid 1px #eee;
    }

    #user-cards {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 10px;
        padding: 10px;
    }

    .user-card {
        display: flex;
        flex-direction: column;
        padding: 10px;
        border: solid 1px #eee;
    }

    .user-header {
        display: flex;
        flex-direction: column;
    }

    .user-header h3 {
        font-size: 2rem;
    }

    .user-header p {
        color: #aaa;
    }

    .user-header b {
        text-transform: uppercase;
    }

    .role {
        color: #aaa;
    }

    #search {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 10px;
    }

    #search-container {
        display: flex;
        flex-direction: row;
    }

    #sort-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    #sort-container p {
        cursor: pointer;
        padding: 10px;
        user-select: none;
    }

    .bronze {
        color: #cd7f32;
    }

    .silver {
        color: #c0c0c0;
    }

    .gold {
        color: #ffd700;
    }
    
</style>